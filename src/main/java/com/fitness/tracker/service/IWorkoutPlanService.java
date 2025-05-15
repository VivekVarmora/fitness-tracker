package com.fitness.tracker.service;

import java.util.List;

import com.fitness.tracker.dto.WorkoutPlanDTO;

public interface IWorkoutPlanService {

	WorkoutPlanDTO createWorkoutPlan(WorkoutPlanDTO workoutPlan);

    List<WorkoutPlanDTO> getWorkoutPlansByUser(Long userId);

    WorkoutPlanDTO updateWorkoutPlan(Long id, WorkoutPlanDTO workoutPlanDetails);

    void deleteWorkoutPlan(Long id);
}
