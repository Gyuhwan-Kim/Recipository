package com.example.recipository.service;

import com.example.recipository.domain.Comment;
import com.example.recipository.domain.Link;
import com.example.recipository.domain.Recipe;
import com.example.recipository.dto.CommentDto;
import com.example.recipository.dto.RecipeDto;
import com.example.recipository.repository.CommentRepository;
import com.example.recipository.repository.LinkRepository;
import com.example.recipository.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

        recipeService.write(recipeDto, null, null);
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
        Comment comment = commentDto.toEntity(id, writer);

        commentRepository.save(comment);
    }
}
