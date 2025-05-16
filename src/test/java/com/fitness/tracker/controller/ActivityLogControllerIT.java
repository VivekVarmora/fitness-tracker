package com.fitness.tracker.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fitness.tracker.dto.ActivityLogDTO;
import com.fitness.tracker.model.ActivityLog;
import com.fitness.tracker.model.User;
import com.fitness.tracker.model.User.Role;
import com.fitness.tracker.model.WorkoutPlan;
import com.fitness.tracker.repository.ActivityLogRepository;
import com.fitness.tracker.repository.UserRepository;
import com.fitness.tracker.repository.WorkoutPlanRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ActivityLogControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private WorkoutPlanRepository workoutPlanRepository;

	@Autowired
	private ActivityLogRepository activityLogRepository;

	private ObjectMapper objectMapper;
	private User testUser;
	private WorkoutPlan testWorkoutPlan;
	private ActivityLogDTO testActivityLogDTO;

	@BeforeEach
	public void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		// Create test user
		testUser = new User();
		testUser.setUsername("testuser");
		testUser.setEmail("test@example.com");
		testUser.setPassword("password");
		testUser.setRole(Role.USER);
		testUser = userRepository.save(testUser);

		// Create test workout plan
		testWorkoutPlan = new WorkoutPlan();
		testWorkoutPlan.setUser(testUser);
		testWorkoutPlan.setName("Test Workout Plan");
		testWorkoutPlan.setDescription("Test Description");
		testWorkoutPlan.setStartDate(LocalDate.now());
		testWorkoutPlan.setEndDate(LocalDate.now().plusDays(30));
		testWorkoutPlan = workoutPlanRepository.save(testWorkoutPlan);

		// Create test activity log DTO
		testActivityLogDTO = new ActivityLogDTO();
		testActivityLogDTO.setWorkoutPlanId(testWorkoutPlan.getId());
		testActivityLogDTO.setDate(LocalDate.now());
		testActivityLogDTO.setActivity("Running");
		testActivityLogDTO.setDurationMin(30);
		testActivityLogDTO.setCaloriesBurned(300);
	}

	@Test
	@WithMockUser(roles = "USER")
	public void testLogActivity() throws Exception {
		String content = objectMapper.writeValueAsString(testActivityLogDTO);

		mockMvc.perform(post("/fitness/activity").contentType(MediaType.APPLICATION_JSON).content(content))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.activity", is(testActivityLogDTO.getActivity())))
				.andExpect(jsonPath("$.durationMin", is(testActivityLogDTO.getDurationMin())))
				.andExpect(jsonPath("$.caloriesBurned", is(testActivityLogDTO.getCaloriesBurned())))
				.andExpect(jsonPath("$.userId", is(testUser.getId().intValue())))
				.andExpect(jsonPath("$.workoutPlanId", is(testWorkoutPlan.getId().intValue())))
				.andExpect(jsonPath("$.id", notNullValue()));
	}

	@Test
	@WithMockUser(roles = "USER")
	public void testGetActivitiesByUser() throws Exception {
		// Create and save activity logs for test user
		ActivityLog log1 = new ActivityLog();
		log1.setUser(testUser);
		log1.setWorkoutPlan(testWorkoutPlan);
		log1.setDate(LocalDate.now().minusDays(1));
		log1.setActivity("Running");
		log1.setDurationMin(30);
		log1.setCaloriesBurned(300);
		activityLogRepository.save(log1);

		ActivityLog log2 = new ActivityLog();
		log2.setUser(testUser);
		log2.setWorkoutPlan(testWorkoutPlan);
		log2.setDate(LocalDate.now());
		log2.setActivity("Swimming");
		log2.setDurationMin(45);
		log2.setCaloriesBurned(450);
		activityLogRepository.save(log2);

		mockMvc.perform(get("/fitness/activity/user/{userId}", testUser.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].activity", is("Running")))
				.andExpect(jsonPath("$[1].activity", is("Swimming")));
	}

	@Test
	@WithMockUser(roles = "USER")
	public void testUpdateActivityLog() throws Exception {
		// Create and save activity log
		ActivityLog log = new ActivityLog();
		log.setUser(testUser);
		log.setWorkoutPlan(testWorkoutPlan);
		log.setDate(LocalDate.now());
		log.setActivity("Original Activity");
		log.setDurationMin(20);
		log.setCaloriesBurned(200);
		log = activityLogRepository.save(log);

		// Create updated DTO
		ActivityLogDTO updateDTO = new ActivityLogDTO();
		updateDTO.setWorkoutPlanId(testWorkoutPlan.getId());
		updateDTO.setDate(LocalDate.now().plusDays(1));
		updateDTO.setActivity("Updated Activity");
		updateDTO.setDurationMin(40);
		updateDTO.setCaloriesBurned(400);

		String content = objectMapper.writeValueAsString(updateDTO);

		mockMvc.perform(
				put("/fitness/activity/{id}", log.getId()).contentType(MediaType.APPLICATION_JSON).content(content))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(log.getId().intValue())))
				.andExpect(jsonPath("$.activity", is("Updated Activity"))).andExpect(jsonPath("$.durationMin", is(40)))
				.andExpect(jsonPath("$.caloriesBurned", is(400)));
	}

	@Test
	@WithMockUser(roles = "USER")
	public void testDeleteActivityLog() throws Exception {
		// Create and save activity log
		ActivityLog log = new ActivityLog();
		log.setUser(testUser);
		log.setWorkoutPlan(testWorkoutPlan);
		log.setDate(LocalDate.now());
		log.setActivity("Activity to Delete");
		log.setDurationMin(15);
		log.setCaloriesBurned(150);
		log = activityLogRepository.save(log);

		mockMvc.perform(delete("/fitness/activity/{id}", log.getId())).andExpect(status().isNoContent());

		// Verify log is deleted
		mockMvc.perform(get("/fitness/activity/user/{userId}", testUser.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	@WithMockUser(roles = "USER")
	public void testLogActivityWithInvalidData() throws Exception {
		// Set invalid data - negative duration
		testActivityLogDTO.setWorkoutPlanId(null);

		String content = objectMapper.writeValueAsString(testActivityLogDTO);

		mockMvc.perform(post("/fitness/activity").contentType(MediaType.APPLICATION_JSON).content(content))
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(roles = "USER")
	public void testLogActivityWithNonExistentWorkoutPlan() throws Exception {
		// Set a non-existent workout plan ID
		testActivityLogDTO.setWorkoutPlanId(999999L);

		String content = objectMapper.writeValueAsString(testActivityLogDTO);

		mockMvc.perform(post("/fitness/activity").contentType(MediaType.APPLICATION_JSON).content(content))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(roles = "USER")
	public void testUpdateNonExistentActivityLog() throws Exception {
		String content = objectMapper.writeValueAsString(testActivityLogDTO);

		mockMvc.perform(put("/fitness/activity/{id}", 99999L).contentType(MediaType.APPLICATION_JSON).content(content))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(roles = "USER")
	public void testDeleteNonExistentActivityLog() throws Exception {
		mockMvc.perform(delete("/fitness/activity/{id}", 99999L)).andExpect(status().isNotFound());
	}

	@Test
	public void testUnauthorizedAccess() throws Exception {
		// Without authentication
		mockMvc.perform(get("/fitness/activity/user/{userId}", testUser.getId())).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testAdminCanAccessActivities() throws Exception {
		mockMvc.perform(get("/fitness/activity/user/{userId}", testUser.getId())).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "USER")
	public void testLogActivityWithMissingWorkoutPlanId() throws Exception {
		ActivityLogDTO invalidDTO = new ActivityLogDTO();
		invalidDTO.setDate(LocalDate.now());
		invalidDTO.setActivity("Running");
		invalidDTO.setDurationMin(30);
		invalidDTO.setCaloriesBurned(300);
		// Missing workoutPlanId

		String content = objectMapper.writeValueAsString(invalidDTO);

		mockMvc.perform(post("/fitness/activity").contentType(MediaType.APPLICATION_JSON).content(content))
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(roles = "USER")
	public void testLogActivityWithMissingActivity() throws Exception {
		var invalidDTO = new ActivityLogDTO();
		invalidDTO.setWorkoutPlanId(testWorkoutPlan.getId());
		invalidDTO.setDate(LocalDate.now());
		invalidDTO.setDurationMin(30);
		invalidDTO.setCaloriesBurned(300);
		var content = objectMapper.writeValueAsString(invalidDTO);
		mockMvc.perform(post("/fitness/activity").contentType(MediaType.APPLICATION_JSON).content(content))
				.andExpect(status().isBadRequest());
	}
}