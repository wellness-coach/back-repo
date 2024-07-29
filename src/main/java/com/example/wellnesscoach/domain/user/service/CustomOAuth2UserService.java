package com.example.wellnesscoach.domain.user.service;

import com.example.wellnesscoach.domain.user.User;
import com.example.wellnesscoach.global.oauth2.dto.GoogleResponse;
import com.example.wellnesscoach.global.oauth2.dto.NaverResponse;
import com.example.wellnesscoach.global.oauth2.dto.OAuth2Response;
import com.example.wellnesscoach.domain.user.dto.CustomOAuth2User;
import com.example.wellnesscoach.domain.user.dto.UserDTO;
import com.example.wellnesscoach.domain.user.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {
            return null;
        }

        //로그인 완료 후 로직은 추후 작성
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬

        User existData = userRepository.findByUsername(username);

        if (existData == null) { //한번도 로그인 X (유저 데이터가 존재하지 않음)

            User user = new User();
            user.setUsername(username);
            user.setEmail(oAuth2Response.getEmail());
            user.setName(oAuth2Response.getName());
            user.setRole("ROLE_USER");

            userRepository.save(user);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userDTO);
        }
        else {

            existData.setUsername(username);
            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());

            userRepository.save(existData);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(existData.getUsername());
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole(existData.getRole());

            return new CustomOAuth2User(userDTO);
        }
    }
}
