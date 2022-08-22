package com.qnasite.user;

import com.qnasite.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String username, String password, String email) {
        SiteUser siteUser = new SiteUser();
        siteUser.setUsername(username);
        siteUser.setEmail(email);
        siteUser.setPassword(passwordEncoder.encode(password));

        userRepository.save(siteUser);

        return siteUser;
    }
    public SiteUser getUser(String username) {
//        Optional<SiteUser> optionalSiteUser = userRepository.findByUsername(username);
//        if(optionalSiteUser.isPresent()) {
//            return optionalSiteUser.get();
//        } else {
//            throw new DataNotFoundException("siteuser not found");
//        }
        return userRepository.findByUsername(username).orElseThrow(() -> new DataNotFoundException("siteuser not found"));
    }

}
