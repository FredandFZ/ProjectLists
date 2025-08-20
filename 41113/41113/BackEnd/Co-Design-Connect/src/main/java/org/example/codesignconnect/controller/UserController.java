package org.example.codesignconnect.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.example.codesignconnect.dto.SignupRequest;
import org.example.codesignconnect.service.EmailService;
import org.example.codesignconnect.service.OAuthService;
import org.example.codesignconnect.service.UserService;
import org.example.codesignconnect.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public Result login(String email, String password, HttpSession session){
        log.info("/login: {}, {}", email, password);
        return userService.login(email, password, session);
    }

    @PostMapping("/register")
    public Result signup(SignupRequest request){
        log.info("/register: {}", request);
        return userService.signup(request);
    }

    @PostMapping("/password/resetCode")
    public Result sendVerificationCode(String email){
        log.info("/password/resetCode: {}", email);
        return emailService.sendVerificationCode(email);
    }

    @PostMapping("/password/update")
    public Result resetPassword(String email, String verificationCode, String newPassword){
        log.info("/password/update: {}, {}, {}", email, verificationCode, newPassword);
        return userService.resetPassword(email, verificationCode, newPassword);
    }

    @GetMapping("/user/info")
    public Result getUserInfo(Integer userId){
        log.info("/user/info: {}", userId);
        return userService.getUserInfo(userId);
    }
}
