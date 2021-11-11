package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.model.SupportRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/support")
public class SupportController {
    @GetMapping
    public String index(Model model) {
        model.addAttribute("requestObject", new SupportRequestDto());
        return "support";
    }

    @PostMapping
    public String sendSupportRequest(SupportRequestDto dto) {
        log.info("New request {}", dto);
        return "redirect:/";
    }
}
