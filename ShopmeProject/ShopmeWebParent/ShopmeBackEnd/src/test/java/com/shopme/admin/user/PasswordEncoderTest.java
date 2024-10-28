package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
	@Test
	public void testEncodePassword() {
		BCryptPasswordEncoder PassEncoder = new BCryptPasswordEncoder();
		String rawPass = "Vietnam";
		String encodedPass = PassEncoder.encode(rawPass);
		System.out.println(encodedPass);
		boolean matches = PassEncoder.matches(rawPass, encodedPass);
		assertThat(matches).isTrue();
	}
}
