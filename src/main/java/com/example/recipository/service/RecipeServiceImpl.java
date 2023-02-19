package com.example.recipository.service;

import com.example.recipository.domain.*;
import com.example.recipository.dto.CommentDto;
import com.example.recipository.dto.PageDto;
import com.example.recipository.dto.RecipeDto;
import com.example.recipository.repository.CommentRepository;
import com.example.recipository.repository.LinkRepository;
import com.example.recipository.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import java.io.File;
import java.util.*;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final LinkRepository linkRepository;
    private final CommentRepository commentRepository;

    @Value("${file.directory}")
    private String savePath;

    public RecipeServiceImpl(RecipeRepository recipeRepository, LinkRepository linkRepository, CommentRepository commentRepository) {
        this.recipeRepository = recipeRepository;
        this.linkRepository = linkRepository;
        this.commentRepository = commentRepository;
    }

    // index page 게시글 목록 조회
    @Override
    public Map<String, Object> getRecipeList(int pageNum) {
        int pageIndex = pageNum - 1;
        // 한 페이지에 몇 개
        int groupSize = 2;
        // 페이징을 몇 개로
        int pageCounts = 2;

        // Repository method에 전달할 Pageable 객체 (index 부터 size 개수만큼)
        PageRequest pageable = PageRequest.of(pageIndex, groupSize);

        // Data를 조회하여 response 할 RecipeDto로 변환
        Page<Recipe> recipeList = recipeRepository.getAllWithPagination(pageable);
        List<RecipeDto> recipeDtoList = new ArrayList<>();
        recipeList.forEach(tmp -> {
            recipeDtoList.add(tmp.toDto());
        });

        // 게시글 data 전체의 size
        int totalPageNum = recipeList.getTotalPages();

        // pagination에서 보이는 시작 page number와 끝 page number
        int startPageNum = ((pageNum - 1) / pageCounts) * pageCounts + 1;
        int endPageNum = ((pageNum - 1) / pageCounts) * pageCounts + pageCounts;

        // Page의 전체 수가 끝 page number에 해당하지 않으면 끝 page number를 대체
        if(totalPageNum < endPageNum){
            endPageNum = totalPageNum;
        }

        // Pagination을 위해 response 할 data를 담는 PageDto
        PageDto pageDto = PageDto.builder()
                .startPageNum(startPageNum)
                .pageNum(pageNum)
                .endPageNum(endPageNum)
                .totalPageNum(totalPageNum)
                .build();

        // Map에 담아 Controller로
        Map<String, Object> map = new HashMap<>();
        map.put("recipeDtoList", recipeDtoList);
        map.put("pageDto", pageDto);

        return map;
    }

    // 게시글을 작성하는 service logic
    @Transactional
    @Override
    public boolean write(RecipeDto recipeDto,
                         MultipartFile imageFile,
                         Member member) {
        try {
            if (!imageFile.isEmpty()) {
                // application.properties 에 작성한 save path
                // 각 운영체제에 맞는 separator
                String savePath = this.savePath + File.separator;
                File file = new File(savePath);
                // 해당 경로에 directory가 없을 시 만듦
                if (!file.exists()) {
                    file.mkdir();
                }

                // 넘겨받은 file name
                String originFileName = imageFile.getOriginalFilename();
                // save file name에 사용할 UUID String
                String uuid = UUID.randomUUID().toString();
                // save file name
                String saveFileName = uuid + originFileName;

                // directory에 upload file save
                imageFile.transferTo(new File(savePath + saveFileName));
                // RecipeDto에 image save path setting
                recipeDto.setImagePath(savePath + saveFileName);
            }

            // RecipeDto에 조회수, 좋아요 0으로 setting
            recipeDto.setViewCount(0L);
            recipeDto.setLikeCount(0L);

            // RecipeDto를 Entity로 전환하고
            Recipe recipe = recipeDto.toEntity(member);
            // Entity의 List<Link> 의 각 Link에 Recipe setting
            recipe.setRecipeAtLink();

            // 각각 save
            recipeRepository.save(recipe);
//            linkRepository.saveAll(recipe.getLink());

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 게시글의 data를 가져오는 service logic
    @Transactional
    @Override
    public Map<String, Object> getRecipe(Long contentId, Cookie[] cookieList) {
        // 게시글 data를 가져옴
        Recipe recipe = recipeRepository.getRecipeByContentId(contentId);

        // 게시글에 대한 댓글 data를 가져옴
        List<CommentDto.CommentResponseDto> commentDtoList = new ArrayList<>();
        List<Comment> commentList = commentRepository.getCommentByRecipe(recipe);
        commentList.forEach(tmp -> {
            commentDtoList.add(tmp.toDto());
        });

        // Cookie의 조회수 중복 방지를 위한 작업
        // return을 위한 Cookie variable
        Cookie visitCookie = null;
        // Cookie 존재 여부
        boolean cookieExists = false;
        // Request로부터 넘겨받은 Cookie 중
        for(Cookie cookie : cookieList){
            // "visit" 이라는 이름이 있으면
            if(cookie.getName().equals("visit")){
                cookieExists = true;
                // 그 안에 해당 contentId가 포함되어 있는지 여부 확인해서 없으면
                if(!cookie.getValue().contains(contentId.toString())) {
                    // 값으로 추가
                    cookie.setValue(cookie.getValue() + "_" + contentId);
                    visitCookie = cookie;
                    // 조회수 1 증가 및 repository save(update)
                    recipe.addViewCount();
                    recipe.setRecipeAtLink();
                    recipeRepository.save(recipe);
                    break;
                // 확인해서 있으면 그대로 return
                } else {
                    visitCookie = cookie;
                    break;
                }
            }
        }
        // 아예 "visit" 이라는 이름의 Cookie가 없으면
        if(!cookieExists){
            // 새로 만들어서 return
            visitCookie = new Cookie("visit", contentId.toString());
            visitCookie.setDomain("localhost");
            visitCookie.setPath("/contents");
            visitCookie.setMaxAge(60*60*24);
            visitCookie.setSecure(true);
            // 조회수 1 증가 및 repository save(update)
            recipe.addViewCount();
            recipe.setRecipeAtLink();
            recipeRepository.save(recipe);
        }

        RecipeDto recipeDto = recipe.toDtoWithAll();

        // Map에 담아 controller로 return
        Map<String, Object> map = new HashMap<>();
        map.put("visitCookie", visitCookie);
        map.put("recipeDto", recipeDto);
        map.put("commentDtoList", commentDtoList);

        return map;
    }

    @Override
    public RecipeDto getRecipeOnly(Long contentId) {
        // repository로부터 게시글 data를 가져옴
        Recipe recipe = recipeRepository.getRecipeByContentId(contentId);
        RecipeDto recipeDto = recipe.toDtoWithAll();

        return recipeDto;
    }

    // 게시글을 수정하는 service logic
    @Transactional
    @Override
    public boolean update(Long contentId, RecipeDto recipeDto, MultipartFile imageFile) {
        try {
            // 기존 DB의 data에 대해
            Recipe recipe = recipeRepository.getRecipeByContentId(contentId);

            // 넘겨받은 file이 있다면 수정 정보가 담긴 recipeDto에 setting 하고
            if(imageFile != null){
                String savePath = this.savePath + File.separator;

                // 넘겨받은 file name
                String originFileName = imageFile.getOriginalFilename();
                // save file name에 사용할 UUID String
                String uuid = UUID.randomUUID().toString();
                // save file name
                String saveFileName = uuid + originFileName;

                // directory에 upload file save
                imageFile.transferTo(new File(savePath + saveFileName));
                // RecipeDto에 image save path setting
                recipeDto.setImagePath(savePath + saveFileName);
            }

            // 기존 DB의 Link data 와 새로 변경할 Link data에 대해
            List<Link> linkList = recipe.getLink();
            List<String> newLinkList = recipeDto.getLink();

            int dbLength = linkList.size();
            int newLength = newLinkList.size();

            // link 수가 줄어든 경우
            if(dbLength > newLength){
                // 줄어든 만듬 List에서 제거하면 link 수가 동일한 것과 같음
                linkList.subList(newLength, dbLength).clear();
            // link 수가 늘어난 index 일 경우
            } else if(dbLength < newLength){
                linkList.add(Link.builder()
                        .recipe(recipe)
                        .build());
            }
            // 앞선 if문 분기로 기존 List와 새 List의 size는 같아진다.
            for(int i = 0; i < newLength; i++){
                // data를 update
                linkList.get(i).updateLink(newLinkList.get(i));
            }

            // 기존 Entity에 수정한 data의 값들을 전달하여 변경한 뒤 save
            recipe.updateRecipe(recipeDto.getTitle(), recipeDto.getContent(),
                                recipeDto.getImagePath(), recipeDto.isBePublic(),
                                linkList);
            recipeRepository.save(recipe);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 게시글을 삭제하는 service logic
    @Transactional
    @Override
    public boolean delete(Long contentId) {
        try{
            Recipe recipe = recipeRepository.getRecipeByContentId(contentId);

            commentRepository.deleteAllByRecipe(recipe);
            linkRepository.deleteAllByRecipe(recipe);
            recipeRepository.deleteRecipeById(contentId);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    @Override
    public boolean deleteList(List<Long> ids) {
        try {
            List<Recipe> recipeList = recipeRepository.getRecipeByContentIds(ids);

            commentRepository.deleteAllByRecipes(recipeList);
            linkRepository.deleteAllByRecipes(recipeList);
            recipeRepository.deleteAllByIds(ids);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<RecipeDto> getMyRecipeList(Member member) {
        List<Recipe> recipeList = recipeRepository.getAllByMember(member);
        List<RecipeDto> recipeDtoList = new ArrayList<>();
        recipeList.forEach(tmp -> {
            recipeDtoList.add(tmp.toDto());
        });

        return recipeDtoList;
    }
}