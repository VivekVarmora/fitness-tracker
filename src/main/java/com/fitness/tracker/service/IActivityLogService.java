package com.fitness.tracker.service;

import java.util.List;

import com.fitness.tracker.dto.ActivityLogDTO;

public interface IActivityLogService {

	ActivityLogDTO logActivity(ActivityLogDTO activityLog);

    List<ActivityLogDTO> getActivitiesByUser(Long userId);

    ActivityLogDTO updateActivityLog(Long id, ActivityLogDTO activityDetails);

    void deleteActivityLog(Long id);
}
