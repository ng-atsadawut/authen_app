package com.tjtech.authen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.tjtech.authen")
public class AuthenApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenApplication.class, args);
	}

}
