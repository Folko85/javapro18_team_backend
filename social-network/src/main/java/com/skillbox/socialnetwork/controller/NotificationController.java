package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.notificationdto.NotificationData;
import com.skillbox.socialnetwork.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
@Tag(name = "Контроллер для получения уведомлений")
@RequestMapping("/api/v1")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PutMapping("/api/v1/notifications")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<ListResponse<NotificationData>> notifications(@RequestParam(name = "offset", defaultValue = "0") int offset,
                                                                        @RequestParam(name = "itemPerPage", defaultValue = "20") int itemPerPage,
                                                                        Principal principal) {
        return new ResponseEntity<>(notificationService.getNotification(offset, itemPerPage, principal), HttpStatus.OK);
    }
}
