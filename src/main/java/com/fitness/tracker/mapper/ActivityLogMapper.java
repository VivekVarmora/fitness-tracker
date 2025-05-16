package com.fitness.tracker.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.fitness.tracker.dto.ActivityLogDTO;
import com.fitness.tracker.model.ActivityLog;

@Mapper(componentModel = "spring", uses = { WorkoutPlanMapper.class })
public interface ActivityLogMapper {

	@Mapping(source = "user.id", target = "userId")
	@Mapping(source = "workoutPlan.id", target = "workoutPlanId")
	ActivityLogDTO toDto(ActivityLog activityLog);

	@Mapping(target = "user", ignore = true)
	@Mapping(target = "workoutPlan", ignore = true)
	ActivityLog toEntity(ActivityLogDTO activityLogDTO);

	List<ActivityLogDTO> toDTOList(List<ActivityLog> activityLogs);
}
