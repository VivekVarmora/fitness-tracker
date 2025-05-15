package com.fitness.tracker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fitness.tracker.dto.ActivityLogDTO;
import com.fitness.tracker.exception.ApplicationException;
import com.fitness.tracker.mapper.ActivityLogMapper;
import com.fitness.tracker.repository.ActivityLogRepository;
import com.fitness.tracker.repository.UserRepository;
import com.fitness.tracker.repository.WorkoutPlanRepository;

@Service
@Transactional
public class ActivityLogServiceImpl implements IActivityLogService {

	@Autowired
	private ActivityLogRepository activityLogRepository;

	@Autowired
	private ActivityLogMapper activityLogMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private WorkoutPlanRepository workoutPlanRepository;

	@Override
	public ActivityLogDTO logActivity(ActivityLogDTO activityLog) {
		var alog = activityLogMapper.toEntity(activityLog);
		if (activityLog.getWorkoutPlanId() != null) {
			var workoutPlan = workoutPlanRepository.findById(activityLog.getWorkoutPlanId())
					.orElseThrow(() -> new IllegalArgumentException("Invalid workoutPlan"));
			if (workoutPlan != null) {
				alog.setWorkoutPlan(workoutPlan);
				alog.setUser(workoutPlan.getUser());
			} else {
				throw new IllegalArgumentException("User is required");
			}
		} else {
			throw new IllegalArgumentException("Workout plan is required");
		}

		return activityLogMapper.toDto(activityLogRepository.save(alog));
	}

	@Override
	public List<ActivityLogDTO> getActivitiesByUser(Long userId) {
		return activityLogMapper.toDTOList(activityLogRepository.findByUserId(userId));
	}

	@Override
	public ActivityLogDTO updateActivityLog(Long id, ActivityLogDTO activityDetails) {
		var activityLog = activityLogRepository.findById(id)
				.orElseThrow(() -> new ApplicationException("Activity log not found"));
		activityLog.setDate(activityDetails.getDate());
		activityLog.setActivity(activityDetails.getActivity());
		activityLog.setDurationMin(activityDetails.getDurationMin());
		activityLog.setCaloriesBurned(activityDetails.getCaloriesBurned());

		return activityLogMapper.toDto(activityLogRepository.save(activityLog));
	}

	@Override
	public void deleteActivityLog(Long id) {
		activityLogRepository.deleteById(id);
	}
}
