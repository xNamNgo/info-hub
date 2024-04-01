package com.info_hub.controllers.auth;

import com.info_hub.components.CustomMailSender;
import com.info_hub.dtos.auth.*;
import com.info_hub.dtos.responses.auth.LoginResponse;
import com.info_hub.dtos.responses.MessageResponse;
import com.info_hub.services.auth.AuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final CustomMailSender senderService;

    @Value("${api.url-mail}")
    private String urlMail;

    // register
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        MessageResponse response = authService.register(userRegisterDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDTO request) {
        LoginResponse response = authService.login(request.getEmail(), request.getPassword());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // request rftoken to get atoken
    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenDTO request) {
        LoginResponse response = authService.refreshToken(request.getRefreshToken());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> resetPassword(@RequestBody ForgotPasswordDTO request) throws MessagingException {
        String mail = request.getEmail();
        String resetPasswordToken = authService.forgotPassword(mail);
        String url = urlMail + resetPasswordToken;

        senderService.sendMail(mail, url); // send url to exsist email.

        MessageResponse response = new MessageResponse("We have sent a reset password link to your email. Please check.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPass(@RequestParam(value = "token") String resetToken,
                                                     @RequestBody ResetPasswordDTO newPassword) {
        authService.resetPassword(resetToken, newPassword.getPassword());
        MessageResponse response = new MessageResponse("Your password successfully updated!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
