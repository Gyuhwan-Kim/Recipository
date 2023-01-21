package com.example.recipository.controller;

import com.example.recipository.domain.Member;
import com.example.recipository.domain.SpUser;
import com.example.recipository.service.RecipeServiceImpl;
import com.example.recipository.service.UserServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class PageController {
    private final UserServiceImpl userService;
    private final RecipeServiceImpl recipeService;

    public PageController(UserServiceImpl userService, RecipeServiceImpl recipeService) {
        this.userService = userService;
        this.recipeService = recipeService;
    }

    // index page + 출력할 게시글 목록
    @GetMapping(value = {"/"})
    public ModelAndView main(){
        ModelAndView mView = new ModelAndView();
        mView.addObject("recipeList", recipeService.getRecipeList());
        mView.setViewName("index");
        return mView;
    }

    // 로그인 page
    @GetMapping("/login-form")
    public String goLogin(){
        return "pages/loginform";
    }

    // 회원 가입 page
    @GetMapping("/signin-form")
    public String goSignin(){
        return "pages/signinform";
    }

    // 로그인 실패 page
    @GetMapping("/login-failure")
    public String goLoginFailure(){
        return "pages/login-failure";
    }

    // 게시글 작성 page
    @GetMapping("/user/contents-form")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String goWrite(){
        return "pages/contentform";
    }

    // 게시글 page
    @GetMapping("/contents/{contentId}")
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

    // 게시글 수정 page
    @GetMapping("/user/contents/update-form/{contentId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
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

    // 유저 정보 page
    @GetMapping("/user/my-page")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ModelAndView goMyPage(@AuthenticationPrincipal SpUser spUser){
        ModelAndView mView = new ModelAndView();

        Member member = spUser.toMember();
        mView.addObject("recipeList", recipeService.getMyRecipeList(member));
        mView.setViewName("pages/my-page");

        return mView;
    }

    @GetMapping("/user/my-profile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String goMyProfile(Model model,
                              @AuthenticationPrincipal SpUser spUser){

        String email = spUser.getEmail();
        model.addAttribute("profileData", userService.getProfile(email));

        return "fragments/profile-forms :: myProfile";
    }

    @GetMapping("/user/profile-form")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String goProfileForm(Model model,
                              @AuthenticationPrincipal SpUser spUser){

        String email = spUser.getEmail();
        model.addAttribute("profileData", userService.getProfile(email));

        return "fragments/profile-forms :: profileForm";
    }

    @GetMapping("/user/password-form")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String goPwdForm(){

        return "fragments/profile-forms :: pwdForm";
    }
}
