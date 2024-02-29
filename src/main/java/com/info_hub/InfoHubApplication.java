package com.info_hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
// enable @CreateBy, @LastModifierBy,...
@EnableJpaAuditing(auditorAwareRef = "auditorAware") // bean name at SecurityConfig class.
public class InfoHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(InfoHubApplication.class, args);
    }
}
