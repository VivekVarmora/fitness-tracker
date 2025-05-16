package com.fitness.tracker.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fitness.tracker.dto.UserDTO;
import com.fitness.tracker.service.IUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/fitness/users")
@Tag(name = "User Management", description = "Manage users and their fitness data")
public class UserController {

	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
	private final IUserService userService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Create a new user")
	public UserDTO createUser(@Valid @RequestBody UserDTO user) {
		LOG.info("Creating new user with username: {}", user.getUsername());
		return userService.createUser(user);
	}

	@GetMapping
	@Operation(summary = "Get all users")
	public List<UserDTO> getAllUsers() {
		LOG.info("Fetching all users");
		return userService.getAllUsers();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get user by ID")
	public UserDTO getUserById(@PathVariable Long id) {
		LOG.info("Fetching user by ID: {}", id);
		return userService.getUserById(id);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update user details")
	public UserDTO updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDetails) {
		LOG.info("Updating user with ID: {}", id);
		return userService.updateUser(id, userDetails);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Delete a user")
	public void deleteUser(@PathVariable Long id) {
		LOG.info("Deleting user with ID: {}", id);
		userService.deleteUser(id);
	}
}
