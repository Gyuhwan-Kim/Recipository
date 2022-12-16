package com.example.recipository.service;

import com.example.recipository.domain.Recipe;
import com.example.recipository.dto.RecipeDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.Map;

public interface RecipeService {
    List<Recipe> getRecipeList();
    boolean write(RecipeDto recipeDto, MultipartFile multipartFile, String username);
    Map<String, Object> getRecipe(Long contentId, Cookie[] cookieList);
}
