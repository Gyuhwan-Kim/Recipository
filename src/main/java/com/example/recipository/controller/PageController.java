package com.example.recipository.controller;

import com.example.recipository.service.RecipeServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class PageController {
    private final RecipeServiceImpl recipeService;

    public PageController(RecipeServiceImpl recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping(value = {"/"})
    public ModelAndView main(){
        ModelAndView mView = new ModelAndView();
        mView.addObject("recipeList", recipeService.getRecipeList());
        mView.setViewName("index");
        return mView;
    }

    @GetMapping("/loginform")
    public String goLogin(){
        return "pages/loginform";
    }

    @GetMapping("/signinform")
    public String goSignin(){
        return "pages/signinform";
    }

    @GetMapping("/login-failure")
    public String goLoginFailure(){
        return "pages/login-failure";
    }

    @GetMapping("/user/contentform")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String goWrite(){
        return "pages/contentform";
    }

    @GetMapping("/content/{contentId}")
    public ModelAndView goContent(@PathVariable Long contentId,
                                  HttpServletRequest request,
                                  HttpServletResponse response){

        // Request로부터 Cookie array를 받아서 service에 전달
        Cookie[] cookieList = request.getCookies();
        Map<String, Object> map = recipeService.getRecipe(contentId, cookieList);
        // service에서 생성 혹은 수정한 Cookie를 response에 담음
        response.addCookie((Cookie) map.get("visitCookie"));

        ModelAndView mView = new ModelAndView();
        // service에서 data를 가져와서 ModelAndView 객체에 담음
        mView.addObject("recipe", map.get("recipeDto"));
        mView.addObject("commentList", map.get("commentDtoList"));
        // 이동하고자 하는 페이지 정보와 함께 return
        mView.setViewName("pages/content");

        return mView;
    }

    @GetMapping("/user/content/updateform/{contentId}")
    public ModelAndView updateForm(@PathVariable Long contentId){

        ModelAndView mView = new ModelAndView();

        mView.addObject("recipe", recipeService.getRecipeOnly(contentId));
        mView.setViewName("pages/content_updateform");

        return mView;
    }

    // 게시글 수정 및 삭제, 댓글 삭제에 대해 권한이 없는 경우
    // ban page로 이동
    @GetMapping("/banned")
    public String banned(){
        return "pages/banned";
    }
}
