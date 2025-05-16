package com.fitness.tracker.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fitness.tracker.dto.WorkoutPlanDTO;
import com.fitness.tracker.service.IWorkoutPlanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/fitness/workout-plans")
@RequiredArgsConstructor
@Tag(name = "Workout Plan Management", description = "Manage workout plans")
public class WorkoutPlanController {

	private final IWorkoutPlanService workoutPlanService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Create a new workout plan")
	public WorkoutPlanDTO createWorkoutPlan(@Valid @RequestBody WorkoutPlanDTO workoutPlan) {
		return workoutPlanService.createWorkoutPlan(workoutPlan);
	}

	@GetMapping("/user/{userId}")
	@Operation(summary = "Get workout plans by user")
	public List<WorkoutPlanDTO> getWorkoutPlansByUser(@PathVariable Long userId) {
		return workoutPlanService.getWorkoutPlansByUser(userId);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update workout plan")
	public WorkoutPlanDTO updateWorkoutPlan(@PathVariable Long id,
			@Valid @RequestBody WorkoutPlanDTO workoutPlanDetails) {
		return workoutPlanService.updateWorkoutPlan(id, workoutPlanDetails);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Delete workout plan")
	public void deleteWorkoutPlan(@PathVariable Long id) {
		workoutPlanService.deleteWorkoutPlan(id);
	}
}