package com.example.recipository.service;

import com.example.recipository.domain.Comment;
import com.example.recipository.domain.Link;
import com.example.recipository.domain.Recipe;
import com.example.recipository.dto.CommentDto;
import com.example.recipository.dto.LinkDto;
import com.example.recipository.dto.RecipeDto;
import com.example.recipository.repository.CommentRepository;
import com.example.recipository.repository.LinkRepository;
import com.example.recipository.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class RecipeServiceTest {

    @Autowired
    public RecipeServiceImpl recipeService;
    @Autowired
    public RecipeRepository recipeRepository;
    @Autowired
    public LinkRepository linkRepository;
    @Autowired
    public CommentRepository commentRepository;

    //save
    @Test
    public void test(){
        List<String> linkList = new ArrayList<>();
        linkList.add("aaa");
        linkList.add("bbb");

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setWriter("test1");
        recipeDto.setLink(linkList);

//        recipeService.write(recipeDto, null, null);
    }

    //get
    @Transactional
    @Test
    public void test2(){
//        Recipe recipe = recipeService.getRecipe(1L);
        Recipe recipe = Recipe.builder()
                .contentId(1L)
                .build();
        List<Link> linkList = linkRepository.findAllByRecipe(recipe);
        List<String> strLinkList = new ArrayList<String>();
        linkList.forEach(link -> {
            strLinkList.add(link.getLink());
        });
        recipe = linkList.get(0).getRecipe();
        RecipeDto recipeDto = recipe.toDto();
        recipeDto.setLink(strLinkList);
        System.out.println(recipeDto);

//        recipe = recipeRepository.getRecipeByContentId(1L);
        recipe.getCommentList().forEach(tmp -> {
            System.out.println(tmp.toDto());

        });
    }

    // Date formatter test
    @Test
    public void test3(){
        String str = "2021-11-05 13:47:13";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        System.out.println("dateTime : " + dateTime.toString());
    }

    // comment test
    @Test
    @Transactional
    @Rollback(false)
    public void test4() {
        Long id = commentRepository.getSequenceValue();
        CommentDto.CommentRequestDto commentDto = CommentDto.CommentRequestDto.builder()
                .comment("content test1")
                .targetId(3L)
                .build();
        String writer = "test@test.com";

        commentDto.setGroupId(id);
        System.out.println(commentDto);
//        Comment comment = commentDto.toEntity(id, writer);
//
//        commentRepository.save(comment);
    }

    // comment delete test
    @Test
    public void test5(){
        Comment comment = Comment.builder()
                    .commentId(58L)
                    .build();
        commentRepository.delete(comment);
    }

    // recipe update test
    @Test
    @Transactional
    @Rollback(false)
    public void test6(){
        Long contentId = 7L;

        Recipe recipe = recipeRepository.getRecipeByContentId(contentId);

        RecipeDto recipeDto = recipe.toDto();
        System.out.println(recipeDto);

        List<LinkDto> linkDtoList = new ArrayList<>();
        recipe.getLink().forEach(tmp -> {
//            linkDtoList.add(tmp.toDto());
        });
        System.out.println(linkDtoList);

        RecipeDto newRecipeDto = new RecipeDto();
        List<String> newLinkList = new ArrayList<>();
        newLinkList.add("ccc");
//        newLinkList.add("bbb");
        newRecipeDto.setLink(newLinkList);

        var dbLength = linkDtoList.size();
        var newLength = newLinkList.size();

        // link 수가 줄어든 경우
        if(dbLength > newLength){
            if(newLength == 0){
                linkRepository.deleteAll(recipe.getLink());
            } else {
                for(int i = dbLength - 1; i >= 0; i--){
                    if(i > newLength -1){
                        linkRepository.deleteById(linkDtoList.get(i).getId());
                        linkDtoList.remove(i);
                    } else {
                        linkDtoList.get(i).setLink(newLinkList.get(i));
                    }
                }
            }
        } else {
            for(int i = 0; i < newLength; i++){
                // link 수가 늘어난 경우
                if(newLength > dbLength && i > dbLength - 1){
                    linkDtoList.add(new LinkDto());
                }
                linkDtoList.get(i).setLink(newLinkList.get(i));
                // link 수가 그대로인 경우
                if(i == dbLength - 1 && dbLength == newLength){
                    break;
                }
            }
        }

        linkDtoList.forEach(tmp -> {
            if(newLength == 0){
                return;
            }
//            Link newLink = tmp.toEntity(recipe);
//            linkRepository.save(newLink);
        });
    }

    // recipe update test2
    @Test
    @Transactional
    @Rollback(false)
    public void test7(){
        Long contentId = 11L;

        Recipe recipe = recipeRepository.getRecipeByContentId(contentId);

        RecipeDto recipeDto = recipe.toDto();
        System.out.println(recipeDto);

        List<LinkDto> linkDtoList = new ArrayList<>();
        recipe.getLink().forEach(tmp -> {
//            linkDtoList.add(tmp.toDto());
        });
        System.out.println(linkDtoList);

        RecipeDto newRecipeDto = new RecipeDto();
        List<String> newLinkList = new ArrayList<>();
        newLinkList.add("ccc");
//        newLinkList.add("bbb");
        newRecipeDto.setLink(newLinkList);

        var dbLength = linkDtoList.size();
        var newLength = newLinkList.size();

        if(dbLength > newLength){
            recipe.getLink().subList(newLength, dbLength).clear();
        }
    }

    @Test
    @Transactional
    public void test8(){
        int pageNum = 6;

        int pageIndex = pageNum - 1;
        int groupSize = 2;
        int pageCounts = 2;

        PageRequest pageable = PageRequest.of(pageIndex, groupSize);

        Page<Recipe> recipeList = recipeRepository.findAll(pageable);
        List<RecipeDto> recipeDtoList = new ArrayList<>();
        recipeList.forEach(tmp -> {
            System.out.println(tmp.toDto());
            recipeDtoList.add(tmp.toDto());
        });

        int startPageNum = ((pageNum - 1) / pageCounts) * pageCounts + 1;
        int endPageNum = 0;

        System.out.println("startPageNum: " + startPageNum);
    }
}
