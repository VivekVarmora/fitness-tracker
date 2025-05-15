package com.fitness.tracker.model;

import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(hidden = true)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "workout_plan_id", nullable = false)
	private WorkoutPlan workoutPlan;

	@Column(nullable = false)
	@CreatedDate
	private LocalDate date;

	@Column(nullable = false)
	@NotBlank(message = "Activity cannot be blank")
	private String activity;

	@Column(nullable = false)
	@Min(value = 1, message = "Duration must be at least 1 minute")
	private int durationMin;

	@Schema(description = "Estimated calories burned", example = "300")
	@PositiveOrZero(message = "Calories burned must be zero or positive")
	private Integer caloriesBurned;

}