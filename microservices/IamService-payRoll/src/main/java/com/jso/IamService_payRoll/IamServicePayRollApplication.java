package com.jso.IamService_payRoll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
public class IamServicePayRollApplication {

	public static void main(String[] args) {
		SpringApplication.run(IamServicePayRollApplication.class, args);
	}

}
