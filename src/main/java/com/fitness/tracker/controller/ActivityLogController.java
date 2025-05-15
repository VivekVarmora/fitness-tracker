package com.fitness.tracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.fitness.tracker.dto.ActivityLogDTO;
import com.fitness.tracker.service.IActivityLogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/fitness/activity")
@Tag(name = "Activity Log Management", description = "Manage activity")
public class ActivityLogController {

	@Autowired
	private IActivityLogService activityLogService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Log a new activity")
	public ActivityLogDTO logActivity(@Valid @RequestBody ActivityLogDTO activityLog) {
		return activityLogService.logActivity(activityLog);
	}

	@GetMapping("/user/{userId}")
	@Operation(summary = "Get activities by user")
	public List<ActivityLogDTO> getActivitiesByUser(@PathVariable Long userId) {
		return activityLogService.getActivitiesByUser(userId);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update activity log")
	public ActivityLogDTO updateActivityLog(@PathVariable Long id, @Valid @RequestBody ActivityLogDTO activityDetails) {
		return activityLogService.updateActivityLog(id, activityDetails);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Delete activity log")
	public void deleteActivityLog(@PathVariable Long id) {
		activityLogService.deleteActivityLog(id);
	}
}