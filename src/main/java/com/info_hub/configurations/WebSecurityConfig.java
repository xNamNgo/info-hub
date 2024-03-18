package com.info_hub.configurations;

import com.info_hub.components.JwtAuthenticationEntryPoint;
import com.info_hub.filters.JwtTokenFilter;
import com.info_hub.services.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity(debug = false) // SecurityFilterChain,..
@EnableMethodSecurity // @PreAuthorize, @PostAuthorize, @PreFilter ...
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final LogoutHandler logoutHandler;  // AuthService implementation

    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // filter per request JwtTokenFilter class.
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> {
                    request
                            .requestMatchers(HttpMethod.GET, String.format("%s/**", apiPrefix)).permitAll()
                            .requestMatchers("/api/auth/**").permitAll()
                            .requestMatchers(HttpMethod.GET,"/api/media/**").permitAll()
                            .anyRequest().authenticated();

                })
                // indicating no session will be created for authenticated users
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))

                /*
                  in SecurityConfig class, khi không khai báo AuthProvier thì spring tự biết.
                 Nên nếu không khai @Bean AuthProvider thì có thể dùng cái này để spring nó tự config
                 .authenticationProvider(authenticationProvider)
                 */

                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)) // return  401 instead 403
                .logout(logout ->
                        logout.logoutUrl("/api/auth/logout") // default url: "/logout"
                                .addLogoutHandler(logoutHandler) // LogoutService class.
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );

        return http.build();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring()
//                .requestMatchers("/**");
//    }

}
