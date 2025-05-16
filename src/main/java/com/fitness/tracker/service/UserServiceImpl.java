package com.fitness.tracker.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fitness.tracker.dto.UserDTO;
import com.fitness.tracker.exception.ResourceNotFoundException;
import com.fitness.tracker.mapper.UserMapper;
import com.fitness.tracker.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

	private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserMapper userMapper;

	@Override
	public UserDTO createUser(UserDTO user) {
		if (userRepository.findByUsername(user.getUsername()) != null) {
			LOG.warn("User creation failed: Username '{}' already exists", user.getUsername());
			throw new IllegalArgumentException("Username already exists");
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		var newUser = userMapper.toEntity(user);
		var savedUser = userRepository.save(newUser);

		LOG.info("User created successfully with ID: {}", savedUser.getId());
		return userMapper.toDTO(savedUser);
	}

	@Override
	public List<UserDTO> getAllUsers() {
		LOG.debug("Retrieving all users from the repository");
		return userMapper.toDTOList(userRepository.findAll());
	}

	@Override
	public UserDTO getUserById(Long id) {
		LOG.debug("Searching for user with ID: {}", id);
		var user = userRepository.findById(id).orElseThrow(() -> {
			LOG.error("User with ID {} not found", id);
			return new ResourceNotFoundException("User not found");
		});
		return userMapper.toDTO(user);
	}

	@Override
	public UserDTO updateUser(Long id, UserDTO userDetails) {
		LOG.debug("Updating user with ID: {}", id);
		var user = userRepository.findById(id).orElseThrow(() -> {
			LOG.error("User with ID {} not found for update", id);
			return new ResourceNotFoundException("User not found");
		});

		user.setUsername(userDetails.getUsername());
		user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
		user.setEmail(userDetails.getEmail());

		var updatedUser = userRepository.save(user);
		LOG.info("User with ID {} updated successfully", id);
		return userMapper.toDTO(updatedUser);
	}

	@Override
	public void deleteUser(Long id) {
		LOG.debug("Deleting user with ID: {}", id);
		if (!userRepository.existsById(id)) {
			LOG.error("Cannot delete: User with ID {} does not exist", id);
			throw new ResourceNotFoundException("User not found");
		}
		userRepository.deleteById(id);
		LOG.info("User with ID {} deleted successfully", id);
	}
}
