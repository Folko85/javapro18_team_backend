package com.skillbox.microservice.controller;

import com.skillbox.microservice.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class SupportController {

    private final MessageService messageService;

    @RequestMapping("/support/messages")
    public String index(Model model) {
        return messageService.messageOutput(model);
    }
}
