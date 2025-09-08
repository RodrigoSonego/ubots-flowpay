package com.ubots.flowpay;

import com.ubots.flowpay.services.AttendantService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseStarter {

    @Bean
    CommandLineRunner init(AttendantService attendantService) {
        return args -> {
            attendantService.createAttendant(new Attendant("João", Team.CARD));
            attendantService.createAttendant(new Attendant("Maria", Team.LOAN));
        };
    }
}
