package com.info_hub.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.info_hub.dtos.responses.auth.LoginErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * handle 403 to 401 when the user is not logged in but tries to access the api.
     * If not handle this, spring security will return 403 ( dont have permission )
     * instead 401 ( unauthorize )
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        OutputStream responseStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        LoginErrorResponse error = new LoginErrorResponse("Invalid usasdasdername or password");
        mapper.writeValue(responseStream, error);
    }
}
