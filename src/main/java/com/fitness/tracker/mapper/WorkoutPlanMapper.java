package com.fitness.tracker.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.fitness.tracker.dto.WorkoutPlanDTO;
import com.fitness.tracker.model.WorkoutPlan;

@Mapper(componentModel = "spring", uses = { ActivityLogMapper.class })
public interface WorkoutPlanMapper {

	@Mapping(source = "user.id", target = "userId")
	WorkoutPlanDTO toDto(WorkoutPlan workoutPlan);

	WorkoutPlan toEntity(WorkoutPlanDTO workoutPlanDTO);

	List<WorkoutPlanDTO> toDTOList(List<WorkoutPlan> entities);
}
