package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig  {
	@Bean
	public UserDetailsService userDetailsService() {
		return new ShopmeUserDetailsService();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
		}


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http.authorizeRequests(auth -> auth
	    		.requestMatchers("/users/**").hasAuthority("Admin")
	    		.requestMatchers("/categories/**", "/brands/**").hasAnyAuthority("Admin", "Editor")
				.requestMatchers("/products/new", "/products/delete/**")
					.hasAnyAuthority("Admin", "Editor")
				.requestMatchers("/products/edit/**", "/products/save", "/products/check_unique")
					.hasAnyAuthority("Admin", "Editor", "Salesperson")
				.requestMatchers("/products", "/products/", "/products/detail/**", "/product/page/**")
					.hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
				.requestMatchers("/products/**").hasAnyAuthority("Admin", "Editor")
	    		.requestMatchers("/images/**", "/js/**", "/webjars/**").permitAll()
				.anyRequest().authenticated()) // yêu cầu xác thực cho tất cả các request
	        .formLogin()
	            .loginPage("/login") // trang đăng nhập custom
	            .usernameParameter("email")
	            .permitAll() // cho phép truy cập trang login mà không cần xác thực
	        .and() // kết nối cấu hình tiếp theo
	        .logout()
	            .permitAll() // cấu hình cho phép truy cập trang logout
	        .and()
	        .rememberMe() // cấu hình remember me
	            .key("AbcDefgHijKlmnopqrs_1234567890") // mã khóa bảo mật cho remember me
	    		.tokenValiditySeconds(14 * 24 * 60 *60)
	    		.rememberMeCookieName("remember-me-cookie");
	    return http.build();
	}

}
