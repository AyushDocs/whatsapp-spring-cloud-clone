package com.whatsapp.profile_service;

import com.whatsapp.profile_service.configuration.JwtConfig;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class ManageTasksApplicationTests {
	@MockBean
	private JwtConfig jwtConfig;
	@Test
	void contextLoads() {
	}

}
