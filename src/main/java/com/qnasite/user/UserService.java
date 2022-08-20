package com.qnasite.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public SiteUser create(String username, String password, String email) {
        SiteUser siteUser = new SiteUser();
        siteUser.setUsername(username);
        siteUser.setPassword(password);
        siteUser.setEmail(email);

        userRepository.save(siteUser);

        return siteUser;
    }
}
