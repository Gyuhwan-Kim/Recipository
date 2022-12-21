package com.example.recipository.service;

import com.example.recipository.domain.Comment;
import com.example.recipository.domain.Link;
import com.example.recipository.domain.Recipe;
import com.example.recipository.dto.CommentDto;
import com.example.recipository.dto.CommentResponseDto;
import com.example.recipository.dto.LinkDto;
import com.example.recipository.dto.RecipeDto;
import com.example.recipository.repository.CommentRepository;
import com.example.recipository.repository.LinkRepository;
import com.example.recipository.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;

    @Value("${file.directory}")
    private String savePath;

    public RecipeServiceImpl(RecipeRepository recipeRepository, LinkRepository linkRepository, CommentRepository commentRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public List<Recipe> getRecipeList() {

        return recipeRepository.findAll();
    }

    // 게시글을 작성하는 service logic
    @Transactional
    @Override
    public boolean write(RecipeDto recipeDto,
                         MultipartFile imageFile,
                         String username) {
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

            // RecipeDto에 SpUser의 username setting
            recipeDto.setWriter(username);
            // RecipeDto에 조회수, 좋아요 0으로 setting
            recipeDto.setViewCount(0L);
            recipeDto.setLikeCount(0L);

            // RecipeDto를 Entity로 전환하고
            Recipe recipe = recipeDto.toEntity();
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
    @Override
    public Map<String, Object> getRecipe(Long contentId, Cookie[] cookieList) {
        // link repository로부터 data를 가져올 contentId를 담은 dummy entity
        Recipe recipe = recipeRepository.getRecipeByContentId(contentId);
        RecipeDto recipeDto = recipe.toDto();

        List<CommentDto.CommentResponseDto> commentDtoList = recipe.getCommentDtoList();

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
                    recipeDto.setViewCount(recipeDto.getViewCount() + 1);
                    Recipe newRecipe = recipeDto.toEntity();
                    newRecipe.setRecipeAtLink();
                    recipeRepository.save(newRecipe);
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
            visitCookie.setPath("/content");
            visitCookie.setMaxAge(60*60*24);
            visitCookie.setSecure(true);
            // 조회수 1 증가 및 repository save(update)
            recipeDto.setViewCount(recipeDto.getViewCount() + 1);
            Recipe newRecipe = recipeDto.toEntity();
            newRecipe.setRecipeAtLink();
            recipeRepository.save(newRecipe);
        }

        // Map에 담아 controller로 return
        Map<String, Object> map = new HashMap<>();
        map.put("visitCookie", visitCookie);
        map.put("recipeDto", recipeDto);
        map.put("commentDtoList", commentDtoList);

        return map;
    }

    @Override
    public RecipeDto getRecipeOnly(Long contentId) {
        // link repository로부터 data를 가져올 contentId를 담은 dummy entity
        Recipe recipe = recipeRepository.getRecipeByContentId(contentId);
        RecipeDto recipeDto = recipe.toDto();

        return recipeDto;
    }

    // 게시글 수정
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
}