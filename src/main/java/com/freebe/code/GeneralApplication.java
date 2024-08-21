package com.freebe.code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = "*")
@EnableJpaAuditing
@EnableTransactionManagement
public class GeneralApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(GeneralApplication.class, args);
	}
	
	
}
