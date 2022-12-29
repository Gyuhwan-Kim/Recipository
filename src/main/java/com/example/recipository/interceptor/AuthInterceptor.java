package com.example.recipository.interceptor;

import com.example.recipository.domain.Comment;
import com.example.recipository.domain.Recipe;
import com.example.recipository.domain.SpUser;
import com.example.recipository.repository.CommentRepository;
import com.example.recipository.repository.RecipeRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final RecipeRepository recipeRepository;
    private final CommentRepository commentRepository;

    public AuthInterceptor(RecipeRepository recipeRepository, CommentRepository commentRepository) {
        this.recipeRepository = recipeRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        // SecurityContextHolder로부터 Principal 을 불러옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SpUser spUser = (SpUser)authentication.getPrincipal();

        // URI의 path variable을 Map으로 불러와서 그 중에 contentId 의 value를 가져옴
        Map<?,?> pathVariables =
                (Map<?,?>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        // 초기값
        Long id = null;
        boolean beMatched = false;

        // 게시글인 경우의 key와 댓글인 경우의 key를 분기
        if(pathVariables.containsKey("contentId")){
            id = Long.parseLong((String)pathVariables.get("contentId"));

            // DB의 data와 비교해서 아닌 경우 banned page로 이동하게 하고 false를 return
            // 같은 경우는 true를 return하고 정상적으로 동작이 수행되도록 함
            Recipe recipe = recipeRepository.getRecipeByContentId(id);
            beMatched = spUser.getUsername().equals(recipe.getWriter());

        } else if(pathVariables.containsKey("commentId")){
            id = Long.parseLong((String)pathVariables.get("commentId"));

            Comment comment = commentRepository.getCommentByCommentId(id);
            beMatched = spUser.getUsername().equals(comment.getWriter());
        }

        if(!beMatched){
            response.sendRedirect(request.getContextPath() + "/banned");
        }

        return beMatched;
    }
}
