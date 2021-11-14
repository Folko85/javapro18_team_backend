package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.technicalSupportDto.MessageOfTechnicalSupportClient;
import com.skillbox.socialnetwork.service.PusherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
//@RestController
@Controller
@Tag(name = "Техническая поддержка", description = "Обращение в техническую поддержку")
public class TechnicalSupportController {

    private final PusherService pusherService;

    public TechnicalSupportController(PusherService pusherService) {
        this.pusherService = pusherService;
    }

//    @RequestMapping("/api/v1/support")
//    public String create(@RequestParam(name = "first_name") String fName,
//                         @RequestParam(name = "last_name") String lName,
//                         @RequestParam(name = "e_mail") String email,
//                         @RequestParam(name = "message") String text) {
//        log.info("a message has been received in support");
//
//        pusherService.setParam(fName, lName, email, text);
//        pusherService.createAndSendMessage();
//        return "sending";
//    }
//    @RequestMapping("/api/v1/support")
//    public String index(Model model) {
//        model.addAttribute("requestObject", new MessageOfTechnicalSupportClient());
//        return "support";
//    }

//    @Operation(summary = "Отправка сообщения в техническую поддержку")
//    @PostMapping("/api/v1/support")
//    public ResponseEntity<?> sendMessage(@RequestBody MessageOfTechnicalSupportClient message) {
//        log.info("a message has been received in support");
//        pusherService.setParam(message);
//        pusherService.createAndSendMessage();
//
//        return ResponseEntity.status(HttpStatus.OK).body(null);
//    }


//    @Operation(summary = "Отправка сообщения в техническую поддержку")
//    @PostMapping("/api/v1/support")
//    public ResponseEntity<HttpStatus> sendMessage(@ModelAttribute MessageOfTechnicalSupportClient requestObject, Model model) {
//        log.info("a message has been received in support");
//
//        model.addAttribute(requestObject);
//        pusherService.setParam(requestObject);
//        pusherService.createAndSendMessage();
//        return ResponseEntity.status(HttpStatus.OK).body(null);
//    }
}
