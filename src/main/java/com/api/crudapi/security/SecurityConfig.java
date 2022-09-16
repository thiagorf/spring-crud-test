package com.api.crudapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.api.crudapi.auth.AuthUserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

	@Autowired
	AuthUserService authUserService;
	
	@Bean
	public void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(authUserService);
	}
	
	
	@Bean
	public UserDetailsService userDetailsService() {
		var manager = new InMemoryUserDetailsManager();
		
		var user = User
				.withUsername("John")
				.password("1234")
				.authorities("read")
				.build();
		
		manager.createUser(user);
		
		return manager;
	}
	
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();	}
	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests()
			.antMatchers("/vehicles").authenticated()
			.antMatchers("/parking-spot").permitAll();
		return http.build();
	}
}
