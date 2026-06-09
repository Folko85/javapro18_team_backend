package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.notificationdto.NotificationData;
import com.skillbox.socialnetwork.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Контроллер для получения уведомлений.
 */
@Slf4j
@RestController
@Tag(name = "Контроллер для получения уведомлений")
@RequestMapping("/api/v1")
@AllArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Получить уведомления.
     *
     * @param offset
     * @param itemPerPage
     * @param principal
     * @return
     */
    @GetMapping("/notifications")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Получить уведомления", security = @SecurityRequirement(name = "jwt"))
    public ListResponse<NotificationData> notifications(@RequestParam(name = "offset", defaultValue = "0") int offset,
                                                        @RequestParam(name = "itemPerPage", defaultValue = "20") int itemPerPage,
                                                        Principal principal) {
        return notificationService.getNotification(offset, itemPerPage, principal);
    }

    /**
     * Прочитать уведомление(я).
     *
     * @param offset
     * @param itemPerPage
     * @param id
     * @param all
     * @param principal
     * @return
     */
    @PutMapping("/notifications")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Прочитать уведомление(я)", security = @SecurityRequirement(name = "jwt"))
    public ListResponse<NotificationData> notifications(@RequestParam(name = "offset", defaultValue = "0") int offset,
                                                        @RequestParam(name = "itemPerPage", defaultValue = "20") int itemPerPage,
                                                        @RequestParam(name = "id", defaultValue = "0") int id,
                                                        @RequestParam(name = "all", defaultValue = "false") boolean all,
                                                        Principal principal) {
        return notificationService.putNotification(offset, itemPerPage, principal, id, all);
    }
}
