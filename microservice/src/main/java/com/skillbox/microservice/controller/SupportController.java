//package com.skillbox.microservice.controller;
//
//import com.skillbox.microservice.entity.Message;
//import com.skillbox.microservice.service.MessageService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.util.ArrayList;
//
//@Controller
//@RequiredArgsConstructor
//public class SupportController {
//
//    private final MessageService messageService;
//
//    @RequestMapping("/support/messages")
//    public String index(Model model) {
//        ArrayList<Message> messageList = messageService.messageOutput();
//        model.addAttribute("messageList", messageList);
//        model.addAttribute("messagesCount", messageList.size());
//        return "result";
//
//    }
//}
