package com.fusioncrew.aikiosk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@org.springframework.data.jpa.repository.config.EnableJpaAuditing
public class AiKioskApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiKioskApplication.class, args);
    }
}
