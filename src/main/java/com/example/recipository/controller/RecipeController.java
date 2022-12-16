package com.example.recipository.controller;

import com.example.recipository.dto.RecipeDto;
import com.example.recipository.domain.SpUser;
import com.example.recipository.service.RecipeServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RecipeController {
    private final RecipeServiceImpl recipeService;

    public RecipeController(RecipeServiceImpl recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/user/write")
    public ResponseEntity<Object> write(@ModelAttribute RecipeDto recipeDto,
                                        @RequestPart MultipartFile imageFile,
                                        @AuthenticationPrincipal SpUser spUser){

        // 로그인 한 사용자의 username 정보
        String username = spUser.getUsername();

        // 게시글을 작성하는 service logic
        boolean beSaved = recipeService.write(recipeDto, imageFile, username);
        Map<String, Object> map = new HashMap<>();
        map.put("beSaved", beSaved);

        return ResponseEntity.ok().body(map);
    }
}
