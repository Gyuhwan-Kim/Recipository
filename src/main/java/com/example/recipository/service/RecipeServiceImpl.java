package com.example.recipository.service;

import com.example.recipository.domain.Comment;
import com.example.recipository.domain.Link;
import com.example.recipository.domain.Recipe;
import com.example.recipository.dto.CommentDto;
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
    private final LinkRepository linkRepository;
    private final CommentRepository commentRepository;

    @Value("${file.directory}")
    private String savePath;

    public RecipeServiceImpl(RecipeRepository recipeRepository, LinkRepository linkRepository, CommentRepository commentRepository) {
        this.recipeRepository = recipeRepository;
        this.linkRepository = linkRepository;
        this.commentRepository = commentRepository;
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

                try {
                    // directory에 upload file save
                    imageFile.transferTo(new File(savePath + saveFileName));
                    // RecipeDto에 image save path setting
                    recipeDto.setImagePath(savePath + saveFileName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
            linkRepository.saveAll(recipe.getLink());

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 게시글의 data를 가져오는 service logic
    @Override
    public Map<String, Object> getRecipe(Long contentId, Cookie[] cookieList) {
        // link repository로부터 data를 가져올 contentId를 담은 dummy entity
        Recipe recipe = Recipe.builder()
                .contentId(contentId)
                .build();
        // link repository로부터 가져온 List<Link>
        List<Link> linkList = linkRepository.findAllByRecipe(recipe);

        // List<Link> 의 한 Link로부터의 온전한 Recipe entity를 Dto로
        recipe = linkList.get(0).getRecipe();
        RecipeDto recipeDto = recipe.toDto();

        List<CommentDto> commentDtoList = recipe.getCommentDtoList();
//        List<Comment> commentList = commentRepository.findAllByRecipeOrderByGroupIdAscCommentIdAsc(recipe);
//        List<CommentDto> commentDtoList = new ArrayList<>();
//        commentList.forEach(tmp -> {
//            commentDtoList.add(tmp.toDto());
//        });

        // List<Link> 에서 String link로 List<String> 을 만들어 Dto에 setting
        List<String> strLinkList = new ArrayList<String>();
        linkList.forEach(link -> {
            strLinkList.add(link.getLink());
        });
        recipeDto.setLink(strLinkList);

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
                    recipeRepository.save(recipeDto.toEntity());
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
            recipeRepository.save(recipeDto.toEntity());
        }

        // Map에 담아 controller로 return
        Map<String, Object> map = new HashMap<>();
        map.put("visitCookie", visitCookie);
        map.put("recipeDto", recipeDto);
        map.put("commentDtoList", commentDtoList);

        return map;
    }
}