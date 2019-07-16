package com.umbookings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
/**
 * @author Shrikar Kalagi
 *
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
public class UmBookingsApplication {
	public static void main(String[] args) {
		SpringApplication.run(UmBookingsApplication.class, args);
	}
}
