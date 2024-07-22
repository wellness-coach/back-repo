package com.example.wellnesscoach.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestConrtoller {

    @GetMapping(value = "/api/test")
    public String home() {
        return "index";
    }
}
