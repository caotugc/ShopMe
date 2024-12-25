package com.shopme.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/images/**", "/js/**", "/webjars/**").permitAll()
						//.requestMatchers("/account_details").authenticated()
						.anyRequest().permitAll()
				)
				.csrf(csrf -> csrf.disable()) // Vô hiệu hóa CSRF nếu không cần
				.formLogin(form -> form.disable()); // Vô hiệu hóa form đăng nhập
		return http.build();
	}
}
