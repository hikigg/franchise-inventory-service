package com.danrod.backend.franchiseinventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

@EnableR2dbcAuditing
@SpringBootApplication
public class FranchiseInventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FranchiseInventoryServiceApplication.class, args);
	}

}
