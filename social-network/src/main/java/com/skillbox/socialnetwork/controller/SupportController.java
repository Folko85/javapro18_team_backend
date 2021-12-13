package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.SupportRequestDto;
import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.service.PusherService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "Запросы в техническую поддержку")
public class SupportController {

    private final PusherService pusherService;

    public SupportController(PusherService pusherService) {
        this.pusherService = pusherService;
    }

    @PostMapping("api/v1/support")
    public AccountResponse create(@RequestBody SupportRequestDto requestDto) {
        log.info("a message has been received in support");
        return pusherService.createAndSendMessage(requestDto);
    }

}
