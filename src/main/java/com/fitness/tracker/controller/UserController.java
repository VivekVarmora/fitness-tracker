package com.fitness.tracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

@RestController
@RequestMapping("/fitness/users")
@Tag(name = "User Management", description = "Manage users and their fitness data")
public class UserController {

	@Autowired
	private IUserService userService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Create a new user")
	public UserDTO createUser(@Valid @RequestBody UserDTO user) {
		return userService.createUser(user);
	}

	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Get all users")
	public List<UserDTO> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get user by ID")
	public UserDTO getUserById(@PathVariable Long id) {
		return userService.getUserById(id);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update user details")
	public UserDTO updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDetails) {
		return userService.updateUser(id, userDetails);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Delete a user")
	public void deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
	}
}