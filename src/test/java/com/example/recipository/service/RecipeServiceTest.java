package com.example.recipository.service;

import com.example.recipository.domain.RecipeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class RecipeServiceTest {

    @Autowired
    public RecipeServiceImpl recipeService;

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
}
