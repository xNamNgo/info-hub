package com.info_hub.filters;

import com.info_hub.components.JwtTokenUtil;
import com.info_hub.constant.JwtConstant;
import com.info_hub.exceptions.TokenException;
import com.info_hub.models.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


// Every time it sends a request,
// it will enter this class (because extends OncePerRequest.. )
// before go to WebSecurityConfig class
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${api.prefix}")
    private String apiPrefix;
    private final UserDetailsService userDetailsService; //  User entity implements UserDetails
    private final JwtTokenUtil jwtTokenUtil;

    // when client send a request
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // request without token
        if (isBypassToken(request)) {
            filterChain.doFilter(request, response); // next step at WebSecurityConfig class
            return;
        }

        // request with token
        String authHeader = request.getHeader(JwtConstant.HEADER_STRING); // Bearer eyJhbGciOiJIUzM4NCJ9E0j5...

        if (authHeader == null || !authHeader.startsWith(JwtConstant.TOKEN_PREFIX)) {
            throw new TokenException("Invalid access token");
        }

        final String token = authHeader.substring(7); // eyJhbGciOiJIUzM4NCJ9E0j5...
        final String username = jwtTokenUtil.extractUsername(token); // get username by token

        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            // Ép sang kiểu User enitty của mình (không phải của Spring)
            // mục đích để getAuthorities(), vì th này implements UserDetails.
            User userDetails = (User) userDetailsService.loadUserByUsername(username);

            // validate token.
            if (jwtTokenUtil.validateToken(token, userDetails)) {
                // this class implements the Authentication interface.
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, // represents the user details retrieved from the database
                        null, // because JWTs are typically self-contained and don't require a separate password for each request.
                        userDetails.getAuthorities()
                );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // The SecurityContextHolder is where Spring Security stores the details of who is authenticated.
                // https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html#servlet-authentication-securitycontextholder
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                throw new TokenException("Invalid access token");
            }
        }
        // ( next step sang WebSecurityConfig class) - enable bypass
        filterChain.doFilter(request, response);

    }

    // list requests no need token
    private boolean isBypassToken(@NonNull HttpServletRequest request) {
        final List<Pair<String, String>> byPassTokens = Arrays.asList(
                Pair.of(String.format("%s/", apiPrefix), "GET"),
                Pair.of("/api/auth/", "POST"),
                Pair.of("/api/media/","GET")
        );
        for (Pair<String, String> bypassToken : byPassTokens) {
            if (request.getServletPath().contains(bypassToken.getLeft()) &&
                    request.getMethod().equals(bypassToken.getRight())) {
                return true;
            }
        }
        return false;
    }
}
