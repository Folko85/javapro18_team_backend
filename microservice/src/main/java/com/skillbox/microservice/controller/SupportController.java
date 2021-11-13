package com.skillbox.microservice.controller;

import com.skillbox.microservice.entity.Message;
import com.skillbox.microservice.repository.MessageRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
public class SupportController {

    private final MessageRepository messageRepository;

    public SupportController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @RequestMapping("/support/messages")
    public String index(Model model) {
        Iterable<Message> messageIterable = messageRepository.findAll();
        ArrayList<Message> messageList = new ArrayList<>();
        for (Message message : messageIterable) {
            messageList.add(message);
        }
        model.addAttribute("messageList", messageList);
        model.addAttribute("messagesCount", messageList.size());
        return "result";
    }
}
