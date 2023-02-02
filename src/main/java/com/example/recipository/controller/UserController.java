package com.example.recipository.controller;

import com.example.recipository.domain.Member;
import com.example.recipository.domain.SpUser;
import com.example.recipository.dto.UserDto;
import com.example.recipository.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    // 중복 확인
    @PostMapping("/duplcheck")
    public Map<String, Object> nameCheck(@RequestBody UserDto userDto){
        Map<String, Object> map = new HashMap<>();
        if(userDto.getEmail() != null){
            map.put("isChecked", userService.duplCheck(userDto));
        } else if(userDto.getName() != null) {
            map.put("isChecked", userService.duplCheck(userDto));
        }

        return map;
    }

    // 회원 가입
    @PostMapping(value = "/signin")
    public ResponseEntity<Map<String, Object>> signin(@Valid @RequestBody UserDto userDto,
                                                      BindingResult bindingResult){
        Map<String, Object> map = new HashMap<>();
        if(bindingResult.hasErrors()){
            map.put("beSuccess", false);

            Map<String, Object> errorList = new HashMap<>();

            StringBuffer sb = new StringBuffer();
            bindingResult.getAllErrors().forEach(error -> {
                FieldError fieldError = (FieldError)error;
                String field = fieldError.getField();
                String errorMessage = error.getDefaultMessage();

                errorList.put(field, errorMessage);
                map.put("errorMessage", errorList);
            });

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }

        map.put("beSuccess", userService.signin(userDto));

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

//    @PostMapping("/login")
//    public Map<String, Object> login(@RequestBody User user, HttpSession session){
//        Map<String, Object> map = new HashMap<>();
//        map.put("isSuccess", userService.login(user));
//
//        if((boolean)map.get("isSuccess")){
//            session.setAttribute("user", user);
//        }
//
//        return map;
//    }

    // 마이 페이지에서 닉네임 변경
    @PostMapping("/user/profile/nickname")
    public ResponseEntity<Map<String, Object>> updateNickname(@Valid UserDto userDto,
                                                             BindingResult bindingResult,
                                                             @AuthenticationPrincipal SpUser spUser){
        Map<String, Object> map = new HashMap<>();

        // Validation Error가 발생했을 때 error message list return
        if(bindingResult.hasErrors()){
            map.put("beUpdated", false);

            Map<String, Object> errorList = new HashMap<>();

            StringBuffer sb = new StringBuffer();
            bindingResult.getAllErrors().forEach(error -> {
                FieldError fieldError = (FieldError)error;
                String field = fieldError.getField();
                String errorMessage = error.getDefaultMessage();

                errorList.put(field, errorMessage);
                map.put("errorMessage", errorList);
            });

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }

        // Authentication Principal 이 가진 email data를
        String email = spUser.getEmail();
        // nickname이 있는 UserDto에 setting
        userDto.setEmail(email);

        // Profile data update 성공 여부에 따라
        boolean beUpdated = userService.updateProfile(userDto);
        // 성공 시 Authentication Principal 의 nickname data를 수정해주고
        if(beUpdated){
            spUser.updateName(userDto.getName());
        }

        // 성공 여부 return
        map.put("beUpdated", beUpdated);

        return ResponseEntity.ok(map);
    }

    // 마이 페이지에서 비밀번호 변경
    @PostMapping("/user/profile/password")
    public ResponseEntity<Map<String, Object>> updatePassword(@Valid UserDto userDto,
                                                             BindingResult bindingResult,
                                                             @AuthenticationPrincipal SpUser spUser){
        Map<String, Object> map = new HashMap<>();

        // Validation Error가 발생했을 때 error message list return
        if(bindingResult.hasErrors()){
            map.put("beUpdated", false);

            Map<String, Object> errorList = new HashMap<>();

            StringBuffer sb = new StringBuffer();
            bindingResult.getAllErrors().forEach(error -> {
                FieldError fieldError = (FieldError)error;
                String field = fieldError.getField();
                String errorMessage = error.getDefaultMessage();

                errorList.put(field, errorMessage);
                map.put("errorMessage", errorList);
            });

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }

        // Authentication Principal 이 가진 email data를
        String email = spUser.getEmail();
        // nickname이 있는 UserDto에 setting
        userDto.setEmail(email);

        // Profile data update 성공 여부에 따라
        boolean beUpdated = userService.updatePassword(userDto);
        // 성공 시 Authentication 을 authenticated false로 하여 로그아웃 처리
        if(beUpdated){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            authentication.setAuthenticated(false);
        }

        // 성공 여부 return
        map.put("beUpdated", beUpdated);

        return ResponseEntity.ok(map);
    }

    // 회원 탈퇴
    @DeleteMapping("/user/exit")
    public ResponseEntity<Object> deleteUser(@AuthenticationPrincipal SpUser spUser){

        Member member = spUser.toMember();

        Map<String, Object> map = new HashMap<>();

        boolean beDeleted = userService.exit(member);
        // 성공 시 Authentication 을 authenticated false로 하여 로그아웃 처리
        if(beDeleted){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            authentication.setAuthenticated(false);
        }
        map.put("beDeleted", beDeleted);

        return ResponseEntity.ok().body(map);
    }
}
