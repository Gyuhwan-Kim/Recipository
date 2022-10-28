package com.example.recipository.controller;

import com.example.recipository.model.entity.User;
import com.example.recipository.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/duplcheck")
    public Map<String, Object> nameCheck(@RequestBody User user){
        Map<String, Object> map = new HashMap<>();
        if(user.getEmail() != null){
            map.put("isChecked", userService.duplCheck(user));
        } else if(user.getName() != null) {
            map.put("isChecked", userService.duplCheck(user));
        }

        return map;
    }

    @PostMapping(value = "/signin")
    public Map<String, Object> signin(@RequestBody User user){
        Map<String, Object> map = new HashMap<>();
        map.put("beSuccess", userService.signin(user));

        return map;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User user, HttpSession session){
        Map<String, Object> map = new HashMap<>();
        map.put("isSuccess", userService.login(user));

        if((boolean)map.get("isSuccess")){
            session.setAttribute("user", user);
        }

        return map;
    }
}
