package com.ubots.flowpay;

import com.ubots.flowpay.repositories.AttendantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseStarter {

    @Bean
    CommandLineRunner init(AttendantRepository attendantRepository) {
        return args -> {
            attendantRepository.save(new Attendant("João", Team.CARD));
            attendantRepository.save(new Attendant("Maria", Team.LOAN));
        };
    }
}
