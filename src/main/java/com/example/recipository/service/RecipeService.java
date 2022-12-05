package com.example.recipository.service;

import com.example.recipository.domain.RecipeDto;
import org.springframework.web.multipart.MultipartFile;

public interface RecipeService {
    public boolean write(RecipeDto recipeDto, MultipartFile multipartFile, String username);
}
