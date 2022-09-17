package com.example.recipository.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PageController {
    @GetMapping(value = {"/"})
    public ModelAndView main(){
        ModelAndView mView = new ModelAndView();
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        mView.addObject("dummylist", list);
        mView.setViewName("index");
        return mView;
    }

    @GetMapping("/loginform")
    public String goLogin(){
        return "pages/loginform";
    }
}
