package com.fitness.tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fitness.tracker.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

}