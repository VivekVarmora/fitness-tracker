package com.fitness.tracker.dto;

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

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	private String email;

	@Schema(description = "User role (e.g., ADMIN or USER)")
	private Role role;

}
