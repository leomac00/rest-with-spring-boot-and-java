package com.leomac00.reststudy;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class Startup {

    public static void main(String[] args) {
        SpringApplication.run(Startup.class, args);
    }
    
}
