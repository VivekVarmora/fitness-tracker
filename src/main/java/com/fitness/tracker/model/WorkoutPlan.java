package com.fitness.tracker.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutPlan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	@NotNull(message = "User must be specified")
	private User user;

	@Column(nullable = false)
	@NotBlank(message = "Plan name cannot be blank")
	@Size(max = 100, message = "Plan name must be less than 100 characters")
	private String name;

	@Size(max = 500, message = "Description must be less than 500 characters")
	private String description;

	@Column(nullable = false)
	@NotNull(message = "Start date cannot be null")
	private LocalDate startDate;

	@Future(message = "End date must be in the future")
	private LocalDate endDate;

	@OneToMany(mappedBy = "workoutPlan", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ActivityLog> activityLogs;

}