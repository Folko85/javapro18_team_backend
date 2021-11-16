package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.service.PusherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class SupportController {

    private final PusherService pusherService;

    public SupportController(PusherService pusherService) {
        this.pusherService = pusherService;
    }

    @GetMapping("/support")
    public String support() {
        return "support";
    }

    @PostMapping("/support")
    public String create(@RequestParam(name = "first_name") String fName,
                         @RequestParam(name = "last_name") String lName,
                         @RequestParam(name = "e_mail") String email,
                         @RequestParam(name = "message") String text) {
        log.info("a message has been received in support");
        pusherService.setParam(fName, lName, email, text);
        pusherService.createAndSendMessage();
        return "support";
    }

//    @GetMapping("/support")
//    public String index(Model model) {
//        model.addAttribute("requestObject", new MessageOfTechnicalSupportClient());
//        return "support";
//    }
//
//
//    @PostMapping("/support")
//    public ResponseEntity<?> sendSupportRequest(MessageOfTechnicalSupportClient message) {
//        log.info("New request {}", message);
//        pusherService.setParam(message);
//        pusherService.createAndSendMessage();
//
//        return ResponseEntity.status(HttpStatus.OK).body(null);
//    }
}
