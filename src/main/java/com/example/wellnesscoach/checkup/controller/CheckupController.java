package com.example.wellnesscoach.checkup.controller;

import com.example.wellnesscoach.checkup.Checkup;
import com.example.wellnesscoach.checkup.controller.request.SaveCheckupRequest;
import com.example.wellnesscoach.checkup.service.CheckupService;
import com.example.wellnesscoach.checkup.service.request.MenuItemCommand;
import com.example.wellnesscoach.checkup.service.request.SaveCheckupCommand;
import com.example.wellnesscoach.checkup.service.response.SaveCheckupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/checkup")
public class CheckupController {

    private final CheckupService checkupService;

    public CheckupController(CheckupService checkupService) {
        this.checkupService = checkupService;
    }


    @ResponseBody
    @PostMapping("/save")
    public ResponseEntity<SaveCheckupResponse> saveCheckup(@RequestBody SaveCheckupRequest saveCheckupRequest) {

        List<MenuItemCommand> menuItemCommands = saveCheckupRequest.menuItems().stream()
                .map(menuItemRequest -> MenuItemCommand.of(menuItemRequest.type(), menuItemRequest.name()))
                .collect(Collectors.toList());

        SaveCheckupCommand saveCheckupCommand = SaveCheckupCommand.of(
                saveCheckupRequest.checkupId(),
                saveCheckupRequest.userId(),
                saveCheckupRequest.date(),
                menuItemCommands,
                saveCheckupRequest.memo()
        );

        Checkup savedCheckup =
                checkupService.saveCheckup(saveCheckupCommand);
        SaveCheckupResponse response = SaveCheckupResponse.of(savedCheckup);
        return ResponseEntity.ok().body(response);
    }
}