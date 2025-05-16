package com.fitness.tracker.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fitness.tracker.dto.ActivityLogDTO;
import com.fitness.tracker.exception.ResourceNotFoundException;
import com.fitness.tracker.mapper.ActivityLogMapper;
import com.fitness.tracker.model.ActivityLog;
import com.fitness.tracker.model.User;
import com.fitness.tracker.model.WorkoutPlan;
import com.fitness.tracker.repository.ActivityLogRepository;
import com.fitness.tracker.repository.WorkoutPlanRepository;

class ActivityLogServiceImplTest {

	@Mock
	private ActivityLogRepository activityLogRepository;
	@Mock
	private WorkoutPlanRepository workoutPlanRepository;
	@Mock
	private ActivityLogMapper activityLogMapper;

	@InjectMocks
	private ActivityLogServiceImpl activityLogService;

	private ActivityLogDTO dto;
	private ActivityLog entity;
	private WorkoutPlan workoutPlan;
	private User user;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		dto = new ActivityLogDTO();
		dto.setId(1L);
		dto.setWorkoutPlanId(10L);
		dto.setActivity("Running");
		dto.setDate(LocalDate.now());
		dto.setCaloriesBurned(200);
		dto.setDurationMin(30);

		user = new User();
		user.setId(99L);

		workoutPlan = new WorkoutPlan();
		workoutPlan.setId(10L);
		workoutPlan.setUser(user);

		entity = new ActivityLog();
		entity.setId(1L);
		entity.setWorkoutPlan(workoutPlan);
		entity.setUser(user);
		entity.setActivity(dto.getActivity());
		entity.setDate(dto.getDate());
		entity.setCaloriesBurned(dto.getCaloriesBurned());
		entity.setDurationMin(dto.getDurationMin());
	}

	@Test
    void testLogActivity_success() {
        when(workoutPlanRepository.findById(10L)).thenReturn(Optional.of(workoutPlan));
        when(activityLogMapper.toEntity(dto)).thenReturn(entity);
        when(activityLogRepository.save(entity)).thenReturn(entity);
        when(activityLogMapper.toDto(entity)).thenReturn(dto);

        var result = activityLogService.logActivity(dto);

        assertNotNull(result);
        assertEquals(dto.getActivity(), result.getActivity());
        verify(activityLogRepository).save(entity);
    }

	@Test
	void testLogActivity_nullInput() {
		var ex = assertThrows(IllegalArgumentException.class, () -> activityLogService.logActivity(null));
		assertEquals("Activity details cannot be null", ex.getMessage());
	}

	@Test
	void testLogActivity_missingWorkoutPlanId() {
		dto.setWorkoutPlanId(null);
		var ex = assertThrows(IllegalArgumentException.class, () -> activityLogService.logActivity(dto));
		assertEquals("Workout plan is required", ex.getMessage());
	}

	@Test
    void testLogActivity_invalidWorkoutPlanId() {
        when(workoutPlanRepository.findById(10L)).thenReturn(Optional.empty());

        var ex = assertThrows(ResourceNotFoundException.class,
                () -> activityLogService.logActivity(dto));
        assertEquals("Invalid workout plan ID: 10", ex.getMessage());
    }

	@Test
    void testGetActivitiesByUser_success() {
        when(activityLogRepository.findByUserId(99L)).thenReturn(List.of(entity));
        when(activityLogMapper.toDTOList(anyList())).thenReturn(List.of(dto));

        var results = activityLogService.getActivitiesByUser(99L);
        assertEquals(1, results.size());
    }

	@Test
	void testGetActivitiesByUser_invalidUserId() {
		var ex = assertThrows(IllegalArgumentException.class, () -> activityLogService.getActivitiesByUser(-1L));
		assertEquals("Invalid user ID", ex.getMessage());
	}

	@Test
    void testUpdateActivityLog_success() {
        when(activityLogRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(activityLogRepository.save(entity)).thenReturn(entity);
        when(activityLogMapper.toDto(entity)).thenReturn(dto);

        var result = activityLogService.updateActivityLog(1L, dto);

        assertEquals(dto.getActivity(), result.getActivity());
    }

	@Test
	void testUpdateActivityLog_invalidId() {
		var ex = assertThrows(IllegalArgumentException.class, () -> activityLogService.updateActivityLog(-5L, dto));
		assertEquals("Activity ID must be a positive number", ex.getMessage());
	}

	@Test
	void testUpdateActivityLog_nullDetails() {
		var ex = assertThrows(IllegalArgumentException.class, () -> activityLogService.updateActivityLog(1L, null));
		assertEquals("Activity details cannot be null", ex.getMessage());
	}

	@Test
    void testUpdateActivityLog_notFound() {
        when(activityLogRepository.findById(1L)).thenReturn(Optional.empty());

        var ex = assertThrows(ResourceNotFoundException.class,
                () -> activityLogService.updateActivityLog(1L, dto));
        assertEquals("Activity log not found for ID: 1", ex.getMessage());
    }

	@Test
    void testDeleteActivityLog_success() {
        when(activityLogRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> activityLogService.deleteActivityLog(1L));
        verify(activityLogRepository).deleteById(1L);
    }

	@Test
	void testDeleteActivityLog_invalidId() {
		var ex = assertThrows(IllegalArgumentException.class, () -> activityLogService.deleteActivityLog(0L));
		assertEquals("Activity ID must be a positive number", ex.getMessage());
	}

	@Test
    void testDeleteActivityLog_notFound() {
        when(activityLogRepository.existsById(1L)).thenReturn(false);

        var ex = assertThrows(ResourceNotFoundException.class,
                () -> activityLogService.deleteActivityLog(1L));
        assertEquals("Activity log not found for ID: 1", ex.getMessage());
    }
}
