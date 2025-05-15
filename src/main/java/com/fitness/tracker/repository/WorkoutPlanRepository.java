package com.fitness.tracker.repository;

import com.fitness.tracker.model.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {

	List<WorkoutPlan> findByUserId(Long userId);

}