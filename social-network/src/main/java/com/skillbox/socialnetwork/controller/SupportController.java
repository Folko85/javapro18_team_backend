package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.service.PusherService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@Slf4j
@RestController
@Tag(name = "Запросы в техническую поддержку")
public class SupportController {

    private final PusherService pusherService;

    public SupportController(PusherService pusherService) {
        this.pusherService = pusherService;
    }

    @PostMapping("api/v1/support")
    public AccountResponse create(@RequestParam(name = "first_name") String fName,
                                  @RequestParam(name = "last_name") String lName,
                                  @RequestParam(name = "e_mail") String email,
                                  @RequestParam(name = "message") String text) {
        log.info("a message has been received in support");
        pusherService.setParam(fName, lName, email, text);
        pusherService.createAndSendMessage();
        AccountResponse response = new AccountResponse();
        response.setData(Map.of("message", "ok"));
        response.setTimestamp(Instant.now());
        return response;
    }

}
