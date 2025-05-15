package com.fitness.tracker.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fitness.tracker.service.IUserService;

@WebMvcTest(UserController.class)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private IUserService userService;

	@Test
	@WithMockUser(roles = "ADMIN")
	void shouldCreateUser() throws Exception {
//		User user = new User(123l, "test", "password", "test@email.com", null, null);
//		given(userService.createUser(any(UserDTO.class))).willReturn(user);
//
//		mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
//				.content("{\"username\":\"test\",\"password\":\"password\",\"email\":\"test@email.com\"}"))
//				.andExpect(status().isCreated());
	}
}