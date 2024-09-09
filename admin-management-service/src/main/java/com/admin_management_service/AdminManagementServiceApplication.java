package com.admin_management_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AdminManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminManagementServiceApplication.class, args);
	}

}
