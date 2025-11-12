package com.project.thymeleafboard.security;

import com.project.thymeleafboard.common.CommonUtil;
import com.project.thymeleafboard.entity.SiteUser;
import com.project.thymeleafboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import java.util.*;

import static com.project.thymeleafboard.common.GlobalConst.EMAIL_ALREADY_REGISTERED;
import static com.project.thymeleafboard.common.GlobalConst.oauth2UsernameSuffix;

@Component
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        if (userRepository.findByEmail(email).isPresent()) {
            SiteUser siteUser = userRepository.findByEmail(email).get();
            if (siteUser.getSignUpProviderType().name().equals("LOCAL")) {
                throw new OAuth2AuthenticationException(new OAuth2Error("email_exists"), EMAIL_ALREADY_REGISTERED);
            } else {
                return new CustomUser(siteUser);
            }
        } else {
            String username = "g" + CommonUtil.makeRandomNumber(oauth2UsernameSuffix);
            while (userRepository.findByUsername(username).isPresent()) {
                String suffix = username.substring(1);
                username = "g" + shuffle(suffix);
            }
            String password = passwordEncoder.encode(CommonUtil.makeRandomPassword());
            SiteUser siteUser = SiteUser.createOauth2User(username, password, email, SignUpProviderType.GOOGLE);
            userRepository.save(siteUser);
            return new CustomUser(siteUser);
        }

    }

    private String shuffle(String string) {
        char[] chArr = string.toCharArray();
        for (int i = chArr.length - 1; i > 0; i--) {
            int index = (int) (Math.random() * (i + 1));
            char tmp = chArr[i];
            chArr[i] = chArr[index];
            chArr[index] = tmp;
        }
        return new String(chArr);
    }
}
