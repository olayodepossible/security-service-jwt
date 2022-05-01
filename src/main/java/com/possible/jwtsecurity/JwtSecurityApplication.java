package com.possible.jwtsecurity;

import com.possible.jwtsecurity.model.AppUser;
import com.possible.jwtsecurity.model.Role;
import com.possible.jwtsecurity.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class JwtSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtSecurityApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}



	@Bean
	CommandLineRunner run(UserService userService){
		return args -> {
			userService.saveRole(Role.builder().id(null).name("ROLE_USER").build());
			userService.saveRole(Role.builder().id(null).name("ROLE_MANAGER").build());
			userService.saveRole(Role.builder().id(null).name("ROLE_ADMIN").build());
			userService.saveRole(Role.builder().id(null).name("ROLE_SUPER_ADMIN").build());

			userService.saveUser(AppUser.builder().id(null).name("John Travolta").username("john@mail.com").password("1234").roles(new ArrayList<>()).build());
			userService.saveUser(AppUser.builder().id(null).name("Jane Arnold").username("jane@mail.com").password("1234").roles(new ArrayList<>()).build());
			userService.saveUser(AppUser.builder().id(null).name("Tom Smith").username("tom@mail.com").password("1234").roles(new ArrayList<>()).build());
			userService.saveUser(AppUser.builder().id(null).name("Perfect Mike").username("mike@mail.com").password("1234").roles(new ArrayList<>()).build());

			userService.addRoleToUser("john@mail.com", "ROLE_USER");
			userService.addRoleToUser("jane@mail.com", "ROLE_MANAGER");
			userService.addRoleToUser("tom@mail.com", "ROLE_ADMIN");
			userService.addRoleToUser("mike@mail.com", "ROLE_USER");
			userService.addRoleToUser("mike@mail.com", "ROLE_SUPER_ADMIN");
			userService.addRoleToUser("mike@mail.com", "ROLE_ADMIN");
		};
	}
}
