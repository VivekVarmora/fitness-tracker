package com.fitness.tracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.springframework.boot.test.context.SpringBootTest;

import com.fitness.tracker.dto.WorkoutPlanDTO;
import com.fitness.tracker.exception.ResourceNotFoundException;
import com.fitness.tracker.mapper.WorkoutPlanMapper;
import com.fitness.tracker.model.User;
import com.fitness.tracker.model.WorkoutPlan;
import com.fitness.tracker.repository.UserRepository;
import com.fitness.tracker.repository.WorkoutPlanRepository;

class WorkoutPlanServiceImplTest {

	@InjectMocks
	private WorkoutPlanServiceImpl workoutPlanService;

	@Mock
	private WorkoutPlanRepository workoutPlanRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private WorkoutPlanMapper workoutPlanMapper;

	private WorkoutPlanDTO workoutPlanDTO;
	private WorkoutPlan workoutPlan;
	private User user;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		user = new User();
		user.setId(1L);

		workoutPlanDTO = new WorkoutPlanDTO();
		workoutPlanDTO.setId(1L);
		workoutPlanDTO.setName("Strength Training");
		workoutPlanDTO.setDescription("Full body routine");
		workoutPlanDTO.setUserId(1L);
		workoutPlanDTO.setStartDate(LocalDate.now());
		workoutPlanDTO.setEndDate(LocalDate.now().plusWeeks(4));

		workoutPlan = new WorkoutPlan();
		workoutPlan.setId(1L);
		workoutPlan.setName(workoutPlanDTO.getName());
		workoutPlan.setDescription(workoutPlanDTO.getDescription());
		workoutPlan.setStartDate(workoutPlanDTO.getStartDate());
		workoutPlan.setEndDate(workoutPlanDTO.getEndDate());
		workoutPlan.setUser(user);
	}

	@Test
    void testCreateWorkoutPlan_Success() {
        when(workoutPlanMapper.toEntity(workoutPlanDTO)).thenReturn(workoutPlan);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(workoutPlanRepository.save(workoutPlan)).thenReturn(workoutPlan);
        when(workoutPlanMapper.toDto(workoutPlan)).thenReturn(workoutPlanDTO);

        WorkoutPlanDTO result = workoutPlanService.createWorkoutPlan(workoutPlanDTO);

        assertEquals(workoutPlanDTO.getName(), result.getName());
        verify(workoutPlanRepository).save(workoutPlan);
    }

	@Test
	void testCreateWorkoutPlan_UserIdMissing() {
		workoutPlanDTO.setUserId(null);
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			workoutPlanService.createWorkoutPlan(workoutPlanDTO);
		});
		assertEquals("User ID is required to create a workout plan.", ex.getMessage());
	}

	@Test
    void testCreateWorkoutPlan_UserNotFound() {
        when(workoutPlanMapper.toEntity(workoutPlanDTO)).thenReturn(workoutPlan);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            workoutPlanService.createWorkoutPlan(workoutPlanDTO);
        });
    }

	@Test
    void testGetWorkoutPlansByUser() {
        when(workoutPlanRepository.findByUserId(1L)).thenReturn(List.of(workoutPlan));
        when(workoutPlanMapper.toDTOList(List.of(workoutPlan))).thenReturn(List.of(workoutPlanDTO));

        List<WorkoutPlanDTO> result = workoutPlanService.getWorkoutPlansByUser(1L);

        assertEquals(1, result.size());
        assertEquals(workoutPlanDTO.getName(), result.get(0).getName());
    }

	@Test
    void testUpdateWorkoutPlan_Success() {
        when(workoutPlanRepository.findById(1L)).thenReturn(Optional.of(workoutPlan));
        when(workoutPlanRepository.save(workoutPlan)).thenReturn(workoutPlan);
        when(workoutPlanMapper.toDto(workoutPlan)).thenReturn(workoutPlanDTO);

        WorkoutPlanDTO result = workoutPlanService.updateWorkoutPlan(1L, workoutPlanDTO);

        assertEquals(workoutPlanDTO.getName(), result.getName());
        verify(workoutPlanRepository).save(workoutPlan);
    }

	@Test
    void testUpdateWorkoutPlan_NotFound() {
        when(workoutPlanRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            workoutPlanService.updateWorkoutPlan(1L, workoutPlanDTO);
        });
    }

	@Test
    void testDeleteWorkoutPlan_Success() {
        when(workoutPlanRepository.existsById(1L)).thenReturn(true);

        workoutPlanService.deleteWorkoutPlan(1L);

        verify(workoutPlanRepository).deleteById(1L);
    }

	@Test
    void testDeleteWorkoutPlan_NotFound() {
        when(workoutPlanRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            workoutPlanService.deleteWorkoutPlan(1L);
        });
    }
}
