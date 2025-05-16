package com.fitness.tracker.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.fitness.tracker.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) {
		com.fitness.tracker.model.User user = userRepository.findByUsername(username);
		return User.builder().username(user.getUsername()).password(user.getPassword()).roles(user.getRole().toString())
				.build();
	}
}
