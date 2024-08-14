package com.example.asm2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = "com.example.asm2.repository")
//@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
public class Asm2Application {

	public static void main(String[] args) {
		SpringApplication.run(Asm2Application.class, args);
	}

}
