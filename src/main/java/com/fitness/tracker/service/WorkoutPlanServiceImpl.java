package com.fitness.tracker.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fitness.tracker.dto.WorkoutPlanDTO;
import com.fitness.tracker.mapper.WorkoutPlanMapper;
import com.fitness.tracker.repository.UserRepository;
import com.fitness.tracker.repository.WorkoutPlanRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkoutPlanServiceImpl implements IWorkoutPlanService {

	private final WorkoutPlanRepository workoutPlanRepository;
	private final UserRepository userRepository;
	private final WorkoutPlanMapper workoutPlanMapper;

	@Override
	public WorkoutPlanDTO createWorkoutPlan(WorkoutPlanDTO workoutPlan) {
		var wplan = workoutPlanMapper.toEntity(workoutPlan);
		if (workoutPlan.getUserId() != null) {
			var user = userRepository.findById(workoutPlan.getUserId())
					.orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
			wplan.setUser(user);
		} else {
			throw new IllegalArgumentException("User is required");
		}
		return workoutPlanMapper.toDto(workoutPlanRepository.save(wplan));
	}

	@Override
	public List<WorkoutPlanDTO> getWorkoutPlansByUser(Long userId) {
		return workoutPlanMapper.toDTOList(workoutPlanRepository.findByUserId(userId));
	}

	@Override
	public WorkoutPlanDTO updateWorkoutPlan(Long id, WorkoutPlanDTO workoutPlanDetails) {
		var workoutPlan = workoutPlanRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Workout plan not found"));
		workoutPlan.setName(workoutPlanDetails.getName());
		workoutPlan.setDescription(workoutPlanDetails.getDescription());
		workoutPlan.setStartDate(workoutPlanDetails.getStartDate());
		workoutPlan.setEndDate(workoutPlanDetails.getEndDate());
		return workoutPlanMapper.toDto(workoutPlanRepository.save(workoutPlan));
	}

	@Override
	public void deleteWorkoutPlan(Long id) {
		workoutPlanRepository.deleteById(id);
	}
}
