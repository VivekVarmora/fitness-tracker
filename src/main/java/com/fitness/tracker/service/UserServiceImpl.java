package com.fitness.tracker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fitness.tracker.dto.UserDTO;
import com.fitness.tracker.exception.ApplicationException;
import com.fitness.tracker.mapper.UserMapper;
import com.fitness.tracker.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserMapper userMapper;

	@Override
	public UserDTO createUser(UserDTO user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		var newUser = userMapper.toEntity(user);
		return userMapper.toDTO(userRepository.save(newUser));
	}

	@Override
	public List<UserDTO> getAllUsers() {
		return userMapper.toDTOList(userRepository.findAll());
	}

	@Override
	public UserDTO getUserById(Long id) {
		var user = userRepository.findById(id).orElseThrow(() -> new ApplicationException("User not found"));
		return userMapper.toDTO(user);
	}

	@Override
	public UserDTO updateUser(Long id, UserDTO userDetails) {
		var user = userRepository.findById(id).orElseThrow(() -> new ApplicationException("User not found"));
		user.setUsername(userDetails.getUsername());
		user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
		user.setEmail(userDetails.getEmail());
		return userMapper.toDTO(userRepository.save(user));
	}

	@Override
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}
}
