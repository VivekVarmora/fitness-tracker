package com.fitness.tracker.service;

import java.util.List;

import com.fitness.tracker.dto.UserDTO;

public interface IUserService {
	UserDTO createUser(UserDTO user);

	List<UserDTO> getAllUsers();

	UserDTO getUserById(Long id);

	UserDTO updateUser(Long id, UserDTO userDetails);

	void deleteUser(Long id);
}
