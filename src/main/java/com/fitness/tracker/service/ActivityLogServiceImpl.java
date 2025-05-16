package com.fitness.tracker.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fitness.tracker.dto.ActivityLogDTO;
import com.fitness.tracker.exception.ResourceNotFoundException;
import com.fitness.tracker.mapper.ActivityLogMapper;
import com.fitness.tracker.repository.ActivityLogRepository;
import com.fitness.tracker.repository.WorkoutPlanRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ActivityLogServiceImpl implements IActivityLogService {

	private static final Logger LOG = LoggerFactory.getLogger(ActivityLogServiceImpl.class);

	private final ActivityLogRepository activityLogRepository;
	private final ActivityLogMapper activityLogMapper;
	private final WorkoutPlanRepository workoutPlanRepository;

	@Override
	public ActivityLogDTO logActivity(ActivityLogDTO activityLog) {
		LOG.info("Logging new activity: {}", activityLog);

		if (activityLog == null) {
			LOG.error("ActivityLogDTO is null");
			throw new IllegalArgumentException("Activity details cannot be null");
		}

		if (activityLog.getWorkoutPlanId() == null) {
			LOG.warn("WorkoutPlanId is missing");
			throw new IllegalArgumentException("Workout plan is required");
		}

		var workoutPlan = workoutPlanRepository.findById(activityLog.getWorkoutPlanId()).orElseThrow(() -> {
			LOG.error("WorkoutPlan not found for ID: {}", activityLog.getWorkoutPlanId());
			return new ResourceNotFoundException("Invalid workout plan ID: " + activityLog.getWorkoutPlanId());
		});

		var alog = activityLogMapper.toEntity(activityLog);
		alog.setWorkoutPlan(workoutPlan);
		alog.setUser(workoutPlan.getUser());

		var savedLog = activityLogRepository.save(alog);
		LOG.info("Activity logged successfully with ID: {}", savedLog.getId());

		return activityLogMapper.toDto(savedLog);
	}

	@Override
	public List<ActivityLogDTO> getActivitiesByUser(Long userId) {
		LOG.info("Fetching activities for userId: {}", userId);

		if (isIdValid(userId)) {
			LOG.error("Invalid userId: {}", userId);
			throw new IllegalArgumentException("Invalid user ID");
		}

		return activityLogMapper.toDTOList(activityLogRepository.findByUserId(userId));
	}

	@Override
	public ActivityLogDTO updateActivityLog(Long id, ActivityLogDTO activityDetails) {
		LOG.info("Updating activity log with ID: {}", id);

		if (isIdValid(id)) {
			LOG.error("Invalid activity ID: {}", id);
			throw new IllegalArgumentException("Activity ID must be a positive number");
		}

		if (activityDetails == null) {
			LOG.error("Activity details are null");
			throw new IllegalArgumentException("Activity details cannot be null");
		}

		var activityLog = activityLogRepository.findById(id).orElseThrow(() -> {
			LOG.error("Activity log not found for ID: {}", id);
			return new ResourceNotFoundException("Activity log not found for ID: " + id);
		});

		activityLog.setDate(activityDetails.getDate());
		activityLog.setActivity(activityDetails.getActivity());
		activityLog.setDurationMin(activityDetails.getDurationMin());
		activityLog.setCaloriesBurned(activityDetails.getCaloriesBurned());

		var updatedLog = activityLogRepository.save(activityLog);
		LOG.info("Activity log updated successfully: {}", updatedLog.getId());

		return activityLogMapper.toDto(updatedLog);
	}

	@Override
	public void deleteActivityLog(Long id) {
		LOG.info("Deleting activity log with ID: {}", id);

		if (isIdValid(id)) {
			LOG.error("Invalid activity ID for deletion: {}", id);
			throw new IllegalArgumentException("Activity ID must be a positive number");
		}

		boolean exists = activityLogRepository.existsById(id);
		if (!exists) {
			LOG.error("Activity log not found for deletion, ID: {}", id);
			throw new ResourceNotFoundException("Activity log not found for ID: " + id);
		}

		activityLogRepository.deleteById(id);
		LOG.info("Activity log deleted successfully: {}", id);
	}

	private boolean isIdValid(Long id) {
		return id != null && id > 0;
	}
}
