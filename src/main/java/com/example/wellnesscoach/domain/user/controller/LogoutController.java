package com.example.wellnesscoach.domain.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // 세션 무효화
        request.getSession().invalidate();

        // Authorization 쿠키 삭제
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        // 응답 상태 코드 설정
        response.setStatus(HttpServletResponse.SC_OK);
    }
}