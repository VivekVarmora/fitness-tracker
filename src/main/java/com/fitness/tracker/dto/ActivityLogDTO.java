package com.fitness.tracker.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLogDTO {

	@Schema(hidden = true)
	private Long id;

	@Schema(hidden = true)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long userId;

	private Long workoutPlanId;

	private LocalDate date;

	private String activity;

	private int durationMin;

	private Integer caloriesBurned;
}
