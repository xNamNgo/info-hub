package com.info_hub.services.auth;

import com.info_hub.dtos.auth.TokenDTO;
import com.info_hub.exceptions.TokenException;
import com.info_hub.models.Token;
import com.info_hub.models.User;
import com.info_hub.repositories.TokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {
    private static final int MAX_TOKENS = 1;

    private final TokenRepository tokenRepository;
    private final ModelMapper mapper;

    public void addToken(TokenDTO token, User user) {
        List<Token> userTokens = tokenRepository.findByUser(user);

        int tokenCount = userTokens.size();

        // if access token more than 3, delete first token in list then insert next token.
        if (tokenCount >= MAX_TOKENS) {
            Token tokenToDelete = userTokens.get(0);
            tokenRepository.delete(tokenToDelete);
        }

        Token newToken = Token.builder()
                .user(user)
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .isRevoked(false)
                .isExpired(false)
                .tokenType("Bearer")
                .accessExpirationDate(token.getExpirationAccessToken())
                .refreshExpirationDate(token.getExpirationRefreshToken())
                .build();

        tokenRepository.save(newToken);
    }

    // update token when call refresh token.
    public void updateToken(Token existingToken,TokenDTO newToken) {
        // no change refresh token when refresh
        newToken.setRefreshToken(existingToken.getRefreshToken());
        newToken.setExpirationRefreshToken(existingToken.getRefreshExpirationDate());

        mapper.map(newToken,existingToken); // set new token into existing token
        tokenRepository.save(existingToken);
    }


    public Token findByRefreshToken(String refreshToken) {
        return  tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new TokenException("Invalid refresh token!"));
    }

    // when rftoken expired then delete all tokens.
    public void deleteTokenExpired(Token existingToken) {
        tokenRepository.delete(existingToken);
    }

    // when user reset password.
    // native query must use @Transactional
    @Transactional
    public void deleteTokenByUserId(Integer userId) {
        tokenRepository.deleteByUserId(userId);
    }
}
