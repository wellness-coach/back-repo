package com.example.wellnesscoach.global.oauth2;

import com.example.wellnesscoach.domain.user.dto.CustomOAuth2User;
import com.example.wellnesscoach.global.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    public CustomSuccessHandler(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();
        Long userId = customUserDetails.getUserId();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        System.out.println("Creating JWT for userId: " + userId);

        String token = jwtUtil.createJwt(username, role, userId, 60*60*24*7L);

        response.addCookie(createCookie("Authorization", token));
        //System.out.println("Authentication Success: Redirecting to /main with JWT Token");
        //response.sendRedirect("http://localhost:3000/callback");
        //response.sendRedirect("https://www.wellnesscoach.store/main");
        response.sendRedirect("http://localhost:5173/main:" + userId);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60); //쿠키 살아있을 시간
        cookie.setSecure(true); //https에서만 허용
        cookie.setPath("/"); //쿠키는 전역에서 볼 수 있음
        cookie.setHttpOnly(true); //자바스크립트가 해당 쿠키를 가져가지 못함

        return cookie;
    }
}
