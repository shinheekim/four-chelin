package com.example.fourchelin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
public class FourChelinApplication {

    public static void main(String[] args) {
        SpringApplication.run(FourChelinApplication.class, args);
    }

}
