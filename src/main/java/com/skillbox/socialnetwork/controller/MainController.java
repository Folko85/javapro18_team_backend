//package com.skillbox.socialnetwork.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//@Controller
//public class MainController {
//
//    @GetMapping("/")
//    public String getFrontend() {
//        return "index";
//    }
//
//    @RequestMapping(method = {RequestMethod.OPTIONS, RequestMethod.GET}, value = "/**/{path:[^\\.]*}")
//    public String all() {
//        return "forward:/";
//    }
//}