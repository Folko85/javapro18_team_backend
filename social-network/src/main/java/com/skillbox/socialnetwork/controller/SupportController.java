package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.model.SupportRequestDto;
import com.skillbox.socialnetwork.api.request.technicalSupportDto.MessageOfTechnicalSupportClient;
import com.skillbox.socialnetwork.service.PusherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SupportController {

    private final PusherService pusherService;

    public SupportController(PusherService pusherService) {
        this.pusherService = pusherService;
    }

    @GetMapping("/support")
    public String index(Model model) {
        model.addAttribute("requestObject", new SupportRequestDto());
        return "support";
    }

    @PostMapping("/support")
    public ResponseEntity<?> sendSupportRequest(MessageOfTechnicalSupportClient message) {
        log.info("New request {}", message);
        pusherService.setParam(message);
        pusherService.createAndSendMessage();

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

//    @PostMapping
//    public String sendSupportRequest(SupportRequestDto dto) {
//        log.info("New request {}", dto);
//        return "redirect:/";
//    }
}
