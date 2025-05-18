package com.fitness.tracker.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fitness.tracker.dto.WorkoutPlanDTO;
import com.fitness.tracker.exception.ResourceNotFoundException;
import com.fitness.tracker.mapper.WorkoutPlanMapper;
import com.fitness.tracker.repository.UserRepository;
import com.fitness.tracker.repository.WorkoutPlanRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkoutPlanServiceImpl implements IWorkoutPlanService {

	private static final Logger LOG = LoggerFactory.getLogger(WorkoutPlanServiceImpl.class);

	private final WorkoutPlanRepository workoutPlanRepository;
	private final UserRepository userRepository;
	private final WorkoutPlanMapper workoutPlanMapper;

	@Override
	public WorkoutPlanDTO createWorkoutPlan(WorkoutPlanDTO workoutPlan) {
		LOG.info("Creating workout plan for userId={}", workoutPlan.getUserId());

		var wplan = workoutPlanMapper.toEntity(workoutPlan);
		if (workoutPlan.getUserId() != null) {
			var user = userRepository.findById(workoutPlan.getUserId()).orElseThrow(
					() -> new ResourceNotFoundException("User with ID " + workoutPlan.getUserId() + " not found."));
			wplan.setUser(user);
		} else {
			LOG.error("User ID is missing in workout plan creation");
			throw new IllegalArgumentException("User ID is required to create a workout plan.");
		}

		var savedPlan = workoutPlanRepository.save(wplan);
		LOG.info("Workout plan created with ID={}", savedPlan.getId());
		return workoutPlanMapper.toDto(savedPlan);
	}

	@Override
	public List<WorkoutPlanDTO> getWorkoutPlansByUser(Long userId) {
		LOG.info("Fetching workout plans for userId={}", userId);
		return workoutPlanMapper.toDTOList(workoutPlanRepository.findByUserId(userId));
	}

	@Override
	public WorkoutPlanDTO updateWorkoutPlan(Long id, WorkoutPlanDTO workoutPlanDetails) {
		LOG.info("Updating workout plan with ID={}", id);

		var workoutPlan = workoutPlanRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Workout plan with ID " + id + " not found."));

		workoutPlan.setName(workoutPlanDetails.getName());
		workoutPlan.setDescription(workoutPlanDetails.getDescription());
		workoutPlan.setStartDate(workoutPlanDetails.getStartDate());
		workoutPlan.setEndDate(workoutPlanDetails.getEndDate());

		var updatedPlan = workoutPlanRepository.save(workoutPlan);
		LOG.info("Workout plan updated with ID={}", updatedPlan.getId());
		return workoutPlanMapper.toDto(updatedPlan);
	}

	@Override
	public void deleteWorkoutPlan(Long id) {
		LOG.info("Deleting workout plan with ID={}", id);

		var workoutPlan = workoutPlanRepository.findById(id).orElseThrow(() -> {
			LOG.warn("Attempted to delete non-existent workout plan with ID={}", id);
			return new ResourceNotFoundException("Workout plan with ID " + id + " does not exist.");
		});

		workoutPlanRepository.delete(workoutPlan);
		LOG.info("Workout plan deleted with ID={}", id);
	}
}
