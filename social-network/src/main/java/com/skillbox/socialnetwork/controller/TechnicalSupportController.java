package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.model.SupportRequestDto;
import com.skillbox.socialnetwork.api.request.technicalSupportDto.MessageOfTechnicalSupportClient;
import com.skillbox.socialnetwork.service.PusherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "Техническая поддержка", description = "Обращение в техническую поддержку")
public class TechnicalSupportController {

    private final PusherService pusherService;

    public TechnicalSupportController(PusherService pusherService) {
        this.pusherService = pusherService;
    }

    @GetMapping("/api/v1/support")
    public String index(Model model) {
        model.addAttribute("requestObject", new SupportRequestDto());
        return "support";
    }

    @Operation(summary = "Отправка сообщения в техническую поддержку")
    @PostMapping("/api/v1/support")
    public ResponseEntity<?> sendMessage(@RequestBody MessageOfTechnicalSupportClient message) {
        log.info("a message has been received in support");
        pusherService.setParam(message);
        pusherService.createAndSendMessage();

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
