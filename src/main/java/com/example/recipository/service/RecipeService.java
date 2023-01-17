package com.example.recipository.service;

import com.example.recipository.domain.Recipe;
import com.example.recipository.domain.SpUser;
import com.example.recipository.dto.RecipeDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.Map;

public interface RecipeService {
    List<RecipeDto> getRecipeList();
    boolean write(RecipeDto recipeDto, MultipartFile multipartFile, SpUser spUser);
    Map<String, Object> getRecipe(Long contentId, Cookie[] cookieList);
    RecipeDto getRecipeOnly(Long contentId);
    boolean update(Long contentId, RecipeDto recipeDto, MultipartFile multipartFile);

    boolean delete(Long contentId);

    List<RecipeDto> getMyRecipeList(SpUser spUser);
}
