package com.expensetrack.expensetrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing 
public class ExpensetrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpensetrackApplication.class, args);
	}

}
