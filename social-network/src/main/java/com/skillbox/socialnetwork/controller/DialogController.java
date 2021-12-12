package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.DialogRequest;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.dialogdto.DialogData;
import com.skillbox.socialnetwork.service.DialogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/v1/")
@Tag(name = "Контроллер диалогов")
public class DialogController {
    private final DialogService dialogService;

    public DialogController(DialogService dialogService) {
        this.dialogService = dialogService;
    }

    @GetMapping("/dialogs")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Получить диалоги", security = @SecurityRequirement(name = "jwt"))
    public ResponseEntity<ListResponse<DialogData>> getDialogs(@RequestParam(name = "name", defaultValue = "") String name,
                                                               @RequestParam(name = "offset", defaultValue = "0") int offset,
                                                               @RequestParam(name = "itemPerPage", defaultValue = "1000") int itemPerPage,
                                                               Principal principal) {
        return new ResponseEntity<>(dialogService.getDialogs(name, offset, itemPerPage, principal), HttpStatus.OK);
    }

    @PostMapping("/dialogs")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Создать диалог", security = @SecurityRequirement(name = "jwt"))
    public ResponseEntity<DataResponse<DialogData>> postDialog(@RequestBody DialogRequest dialogRequest, Principal principal) {
        return new ResponseEntity<>(dialogService.postDialog(dialogRequest, principal), HttpStatus.OK);
    }
}
