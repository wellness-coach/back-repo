package com.example.wellnesscoach.controller;

import com.example.wellnesscoach.dto.ApiResponseDto;
import com.example.wellnesscoach.entity.Checkup;
import com.example.wellnesscoach.service.CheckupService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/meal/report")
public class CheckupController {

    private final CheckupService checkupService;

    public CheckupController(CheckupService checkupService) {
        this.checkupService = checkupService;
    }

    /*
    @ResponseBody
    @PostMapping("/upload")
    public ApiResponseDto<Checkup> tempUploadMeal(@Requestbody) {
        return checkupService.save()
    }*/
}