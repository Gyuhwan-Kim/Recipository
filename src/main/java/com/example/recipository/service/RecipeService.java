package com.example.recipository.service;

import com.example.recipository.domain.Member;
import com.example.recipository.domain.Recipe;
import com.example.recipository.domain.SpUser;
import com.example.recipository.dto.CommentDto;
import com.example.recipository.dto.RecipeDto;
import com.example.recipository.dto.SearchDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.Map;

public interface RecipeService {
    Map<String, Object> getRecipeList(int pageNum, SearchDto searchDto);
    boolean write(RecipeDto recipeDto, MultipartFile multipartFile, Member member);
    Map<String, Object> getRecipe(Long contentId, Cookie[] cookieList);
    List<CommentDto.CommentResponseDto> getCommentWithPagination(Long contentId, int pageNum);
    RecipeDto getRecipeOnly(Long contentId);
    boolean update(Long contentId, RecipeDto recipeDto, MultipartFile multipartFile);

    boolean delete(Long contentId);

    boolean deleteList(List<Long> ids);

    Map<String, Object> getMyRecipeList(Member member, int pageNum);
}
