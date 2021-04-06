package com.example.insuranceapi;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class InsuranceapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(InsuranceapiApplication.class, args);
    }

}
