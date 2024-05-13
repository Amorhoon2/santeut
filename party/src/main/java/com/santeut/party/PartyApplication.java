package com.santeut.party;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
@EnableCaching
public class PartyApplication {

    public static void main(String[] args) {
        SpringApplication.run(PartyApplication.class, args);
    }

}
