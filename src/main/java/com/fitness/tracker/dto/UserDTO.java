package com.fitness.tracker.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fitness.tracker.model.User.Role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

	@Schema(hidden = true)
	private Long id;

	private String username;

	private String password;

	private String email;

	private Role role;

	@Schema(hidden = true)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private List<WorkoutPlanDTO> workoutPlans;
}
