package com.fitness.tracker.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.tracker.model.User;
import com.fitness.tracker.model.User.Role;
import com.fitness.tracker.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	public void setup() {
		userRepository.deleteAll();
	}

	@Test
	public void testCreateUser() throws Exception {
		var userNode = objectMapper.createObjectNode();
		userNode.put("username", "testuser");
		userNode.put("password", "password123");
		userNode.put("email", "test@example.com");
		userNode.put("role", "ADMIN");

		mockMvc.perform(post("/fitness/users").contentType(MediaType.APPLICATION_JSON).content(userNode.toString()))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.username", is("testuser")))
				.andExpect(jsonPath("$.email", is("test@example.com"))).andExpect(jsonPath("$.role", is("ADMIN")));

	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testGetAllUsers() throws Exception {
		createTestUser("user1", "user1@example.com", Role.USER);
		createTestUser("user2", "user2@example.com", Role.ADMIN);

		mockMvc.perform(get("/fitness/users")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].username", is("user1"))).andExpect(jsonPath("$[1].username", is("user2")));
	}

	@Test
	@WithMockUser(roles = "USER")
	public void testGetUserById() throws Exception {
		var savedUser = createTestUser("testuser", "test@example.com", Role.USER);

		mockMvc.perform(get("/fitness/users/{id}", savedUser.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$.username", is("testuser")))
				.andExpect(jsonPath("$.email", is("test@example.com")));
	}

	@Test
	@WithMockUser(roles = "USER")
	public void testUpdateUser() throws Exception {
		var savedUser = createTestUser("oldusername", "old@example.com", Role.USER);

		var userNode = objectMapper.createObjectNode();
		userNode.put("username", "newusername");
		userNode.put("password", "newpassword");
		userNode.put("email", "new@example.com");
		userNode.put("role", "USER");

		mockMvc.perform(put("/fitness/users/{id}", savedUser.getId()).contentType(MediaType.APPLICATION_JSON)
				.content(userNode.toString())).andExpect(status().isOk())
				.andExpect(jsonPath("$.username", is("newusername")))
				.andExpect(jsonPath("$.email", is("new@example.com")));
	}

	@Test
	@WithMockUser(roles = "USER")
	public void testDeleteUser() throws Exception {
		var savedUser = createTestUser("userToDelete", "delete@example.com", Role.USER);

		mockMvc.perform(delete("/fitness/users/{id}", savedUser.getId())).andExpect(status().isNoContent());

		var deletedUser = userRepository.findById(savedUser.getId());
		assertFalse(deletedUser.isPresent());
	}

	@Test
	public void testUnauthorizedAccess() throws Exception {
		mockMvc.perform(get("/fitness/users")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(roles = "USER")
	public void testForbiddenAccess() throws Exception {
		mockMvc.perform(get("/fitness/users")).andExpect(status().isForbidden());
	}

	private User createTestUser(String username, String email, Role role) {
		var user = new User();
		user.setUsername(username);
		user.setPassword("password");
		user.setEmail(email);
		user.setRole(role);
		return userRepository.save(user);
	}
}