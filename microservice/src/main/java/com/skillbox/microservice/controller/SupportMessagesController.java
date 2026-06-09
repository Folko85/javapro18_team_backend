package com.skillbox.microservice.controller;

import com.mailjet.client.errors.MailjetException;
import com.skillbox.microservice.dto.MessageDto;
import com.skillbox.microservice.entity.Message;
import com.skillbox.microservice.service.SendMailService;
import com.skillbox.microservice.service.SomeService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Контроллер для работы с сообщениями.
 */
@Slf4j
@Controller
@RequestMapping("/support/messages")
public class SupportMessagesController {

    private final SomeService someService;
    private final SendMailService mailService;

    public SupportMessagesController(final SomeService someService, final SendMailService mailService) {
        this.someService = someService;
        this.mailService = mailService;
    }

    /**
     * Получение списка сообщений.
     *
     * @param model - модель
     * @return страница с сообщениями
     */
    @GetMapping
    public String listMessages(final Model model) {
        final List<Message> messages = someService.getAllMessages();
        model.addAttribute("messages", messages);
        return "messages";
    }

    /**
     * Получение записи по ИД.
     *
     * @param id - ИД
     * @return - запись
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<MessageDto> getMessage(@PathVariable final Integer id) {
        final Message message = someService.getMessageById(id);
        if (message == null) {
            return ResponseEntity.notFound().build();
        }
        final MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setClientEmail(message.getClient().getEmail());
        dto.setMessageText(message.getMessage());
        return ResponseEntity.ok(dto);
    }

    /**
     * Метод отправки ответа.
     *
     * @param id      - ИД
     * @param payload - тело ответа
     * @return - ответ
     */
    @PostMapping("/{id}/reply")
    @ResponseBody
    public ResponseEntity<?> replyToMessage(@PathVariable final Integer id,
                                            @RequestBody final Map<String, String> payload) {
        final String responseText = payload.get("response");
        if (!StringUtils.hasText(responseText)) {
            return ResponseEntity.badRequest().body("Текст ответа не может быть пустым");
        }
        final Message message = someService.getMessageById(id);
        if (message == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            mailService.send(message.getClient().getEmail(), responseText);
            return ResponseEntity.ok().build();
        } catch (MailjetException | JSONException e) {
            log.error("Ошибка при отправке письма ", e);
            return ResponseEntity.internalServerError().body("Ошибка при отправке письма");
        }
    }

    /**
     * Удалить сообщение по id.
     *
     * @param id - ИД
     * @return ответ
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteMessage(@PathVariable final Integer id) {
        final Message message = someService.getMessageById(id);
        if (message == null) {
            return ResponseEntity.notFound().build();
        }
        someService.deleteMessage(message);
        return ResponseEntity.ok().build();
    }
}
