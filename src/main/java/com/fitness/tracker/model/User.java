package com.fitness.tracker.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "fitness_user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

	public enum Role {
		USER, ADMIN
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// @Schema(hidden = true)
	private Long id;

	@NotBlank(message = "Username is required")
	@Column(unique = true)
	private String username;

	@NotBlank(message = "Password is required")
	private String password;

	@Email(message = "Invalid email format")
	@NotBlank(message = "email is required")
	private String email;

	@Enumerated(EnumType.STRING)
	private Role role;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	// @Schema(hidden = true)
	private List<WorkoutPlan> workoutPlans;
}
