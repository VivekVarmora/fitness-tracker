package com.fitness.tracker.dto;

import java.time.LocalDate;
import java.util.List;

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
public class WorkoutPlanDTO {

	@Schema(hidden = true)
	private Long id;

	private Long userId;

	private String name;

	private String description;

	private LocalDate startDate;

	private LocalDate endDate;

	@Schema(hidden = true)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private List<ActivityLogDTO> activityLogs;
}
