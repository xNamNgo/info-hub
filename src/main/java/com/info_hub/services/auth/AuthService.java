package com.info_hub.services.auth;

import com.info_hub.components.JwtTokenUtil;
import com.info_hub.constant.JwtConstant;
import com.info_hub.dtos.auth.TokenDTO;
import com.info_hub.dtos.auth.UserRegisterDTO;
import com.info_hub.exceptions.BadRequestException;
import com.info_hub.exceptions.ResetTokenException;
import com.info_hub.exceptions.TokenException;
import com.info_hub.models.Image;
import com.info_hub.models.Role;
import com.info_hub.models.Token;
import com.info_hub.models.User;
import com.info_hub.repositories.ImageRepository;
import com.info_hub.repositories.RoleRepository;
import com.info_hub.repositories.TokenRepository;
import com.info_hub.repositories.user.UserRepository;
import com.info_hub.dtos.responses.auth.LoginResponse;
import com.info_hub.dtos.responses.MessageResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService implements LogoutHandler {
    @Value("${spring.mail.exp-reset-token}")
    private int expResetToken;

    @Value("${api.url-image}")
    private String ipImage;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final TokenRepository tokenRepository;
    private final ImageRepository imageRepository;

    public MessageResponse register(UserRegisterDTO registerDTO) {
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new BadRequestException("Email is already exists!");
        }

        Role role = roleRepository.findByCode("ROLE_USER");
        String password = registerDTO.getPassword();
        User newUser = User.builder()
                .email(registerDTO.getEmail())
                .role(role)
                .password(passwordEncoder.encode(password))
                .fullName(registerDTO.getFullName())
                .isEnabled(true)
                .build();
        userRepository.save(newUser);

        return new MessageResponse("Register successfully!");
    }

    public LoginResponse login(String email, String password) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        // invalid username
        if (optionalUser.isEmpty()) {
            throw new BadRequestException("Invalid username or password!");
        }

        User existingUser = optionalUser.get();

        // check password
        if (!passwordEncoder.matches(password, existingUser.getPassword())) {
            throw new BadRequestException("Invalid password!");
        }

        if (!existingUser.isEnabled()) {
            throw new BadRequestException("The user is blocked!");
        }

        // generate token.
        TokenDTO token = jwtTokenUtil.generateToken(existingUser);
        tokenService.addToken(token, existingUser);

        LoginResponse response = LoginResponse.builder()
                .id(existingUser.getId())
                .email(email)
                .fullName(existingUser.getFullName())
                .role(existingUser.getRole().getCode())
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .message("Login successfully!")
                .build();

        Image avatar = imageRepository.findByUsers_Id(existingUser.getId());
        // user doesn't have avatar
        if(avatar == null) {
            // default avatar
            response.setImage(ipImage + "default-avatar.png");
        } else {
            response.setImage(avatar.getUrl());
        }

        return response;
    }

    public LoginResponse refreshToken(String refreshToken) {
        Token existingToken = tokenService.findByRefreshToken(refreshToken);
        User existingUser = existingToken.getUser();

        if (existingToken.getRefreshExpirationDate().isBefore(LocalDateTime.now())) {
            tokenService.deleteTokenExpired(existingToken);
            throw new TokenException("Refresh token is expired");
        }

        // else create new access token from refresh token.
        TokenDTO newToken = jwtTokenUtil.generateToken(existingUser);
        tokenService.updateToken(existingToken, newToken);

        return LoginResponse.builder()
                .email(existingUser.getUsername())
                .fullName(existingUser.getFullName())
                .role(existingUser.getRole().getCode())
                .accessToken(newToken.getAccessToken())
                .refreshToken(existingToken.getRefreshToken())
                .build();
    }

    /**
     * when click forgot password, insert "reset token" into existing user.
     * @param email
     * @return reset token string (random UID string)
     */
    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Email address does not exist!"));

        String resetPasswordToken = jwtTokenUtil.generateResetToken();
        user.setResetToken(resetPasswordToken);
        user.setExpResetToken(LocalDateTime.now().plusMinutes(expResetToken));

        userRepository.save(user);

        return user.getResetToken();
    }

    /**
     * Client send reset token then validate, if true -> reset password and delete exist token
     * @param resetToken - random UID string
     * @param password
     * @throws TokenException - when reset token expired or reset token is null
     */
    public void resetPassword(String resetToken, String password) {

        if (resetToken == null) {
            throw new ResetTokenException("Invalid token!");
        }

        // does user have reset token ?
        User user = userRepository.findByResetToken(resetToken)
                .orElseThrow(() -> new ResetTokenException("Invalid token!"));

        boolean isExpired = user.getExpResetToken().isBefore(LocalDateTime.now());
        if (isExpired) {
            throw new ResetTokenException("Token expired!");
        }

        String newPassword = passwordEncoder.encode(password);
        user.setPassword(newPassword);
        user.setResetToken(null);
        user.setExpResetToken(null);

        userRepository.save(user);

        // delete exist token (access token, refresh token) after change password
        tokenService.deleteTokenByUserId(user.getId());
    }

    /**
     * Custom logout and inject at WebSecurityConfig ( LogoutHandler class )
     * @param request
     * @param response
     * @param authentication
     */
    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        final String authHeader = request.getHeader(JwtConstant.HEADER_STRING);

        if (authHeader == null || !authHeader.startsWith(JwtConstant.TOKEN_PREFIX)) {
            return;
        }
        final String accessToken = authHeader.substring(7);
        Token storedToken = tokenRepository.findByAccessToken(accessToken);

        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
        }
    }
}
