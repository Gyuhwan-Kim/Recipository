package com.example.recipository.controller;

import com.example.recipository.dto.UserDto;
import com.example.recipository.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
