package com.example.recipository.controller;

import com.example.recipository.domain.Member;
import com.example.recipository.dto.RecipeDto;
import com.example.recipository.domain.SpUser;
import com.example.recipository.service.RecipeServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RecipeController {
    private final RecipeServiceImpl recipeService;

    public RecipeController(RecipeServiceImpl recipeService) {
        this.recipeService = recipeService;
    }

    // 게시글을 작성하는 controller method
    @PostMapping("/user/contents")
    public ResponseEntity<Object> addContent(@ModelAttribute RecipeDto recipeDto,
                                        @RequestPart MultipartFile imageFile,
                                        @AuthenticationPrincipal SpUser spUser){

        // 로그인 한 사용자의 username 정보
        Member member = spUser.toMember();

        // 게시글을 작성하는 service logic
        boolean beSaved = recipeService.write(recipeDto, imageFile, member);
        Map<String, Object> map = new HashMap<>();
        map.put("beSaved", beSaved);

        return ResponseEntity.ok().body(map);
    }

    // 게시글을 수정하는 controller method
    @PutMapping("/user/contents/{contentId}")
    public ResponseEntity<Object> updateContent(@PathVariable Long contentId,
                                         RecipeDto recipeDto,
                                         MultipartFile imageFile){

        Map<String, Object> map = new HashMap<>();
        map.put("beUpdated", recipeService.update(contentId, recipeDto, imageFile));

        return ResponseEntity.ok().body(map);
    }

    // 게시글을 삭제하는 controller method
    @DeleteMapping("/user/contents/{contentId}")
    public ResponseEntity<Object> deleteContent(@PathVariable Long contentId){

        Map<String, Object> map = new HashMap<>();
        map.put("beDeleted", recipeService.delete(contentId));

        return ResponseEntity.ok().body(map);
    }

    @DeleteMapping("/user/contents")
    public ResponseEntity<Object> deleteContents(@RequestParam List<Long> ids){

        Map<String, Object> map = new HashMap<>();
        map.put("beDeleted", recipeService.deleteList(ids));

        return ResponseEntity.ok().body(map);
    }
}
