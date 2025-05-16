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
import com.fitness.tracker.dto.WorkoutPlanDTO;
import com.fitness.tracker.model.User;
import com.fitness.tracker.model.User.Role;
import com.fitness.tracker.model.WorkoutPlan;
import com.fitness.tracker.repository.UserRepository;
import com.fitness.tracker.repository.WorkoutPlanRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class WorkoutPlanControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private WorkoutPlanRepository workoutPlanRepository;

	private ObjectMapper objectMapper = new ObjectMapper();
	private User testUser;
	private WorkoutPlanDTO testWorkoutPlanDTO;

	@BeforeEach
	public void setup() {
		objectMapper.registerModule(new JavaTimeModule());

		// Create test user
		testUser = new User();
		testUser.setUsername("testuser");
		testUser.setEmail("test@example.com");
		testUser.setPassword("password");
		testUser.setRole(Role.ADMIN);
		testUser = userRepository.save(testUser);

		// Create test workout plan DTO
		testWorkoutPlanDTO = new WorkoutPlanDTO();
		testWorkoutPlanDTO.setUserId(testUser.getId());
		testWorkoutPlanDTO.setName("Test Workout Plan");
		testWorkoutPlanDTO.setDescription("Test Description");
		testWorkoutPlanDTO.setStartDate(LocalDate.now());
		testWorkoutPlanDTO.setEndDate(LocalDate.now().plusDays(30));
	}

	@Test
	@WithMockUser(roles = "USER")
	public void testCreateWorkoutPlan() throws Exception {
		var content = objectMapper.writeValueAsString(testWorkoutPlanDTO);

		mockMvc.perform(post("/fitness/workout-plans").contentType(MediaType.APPLICATION_JSON).content(content))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.name", is(testWorkoutPlanDTO.getName())))
				.andExpect(jsonPath("$.description", is(testWorkoutPlanDTO.getDescription())))
				.andExpect(jsonPath("$.userId", is(testWorkoutPlanDTO.getUserId().intValue())))
				.andExpect(jsonPath("$.id", notNullValue()));
	}

	@Test
	@WithMockUser(roles = "USER")
	public void testGetWorkoutPlansByUser() throws Exception {
		// Create and save workout plans for the test user
		var plan1 = new WorkoutPlan();
		plan1.setUser(testUser);
		plan1.setName("Plan 1");
		plan1.setDescription("Description 1");
		plan1.setStartDate(LocalDate.now());
		plan1.setEndDate(LocalDate.now().plusDays(14));
		workoutPlanRepository.save(plan1);

		var plan2 = new WorkoutPlan();
		plan2.setUser(testUser);
		plan2.setName("Plan 2");
		plan2.setDescription("Description 2");
		plan2.setStartDate(LocalDate.now().plusDays(15));
		plan2.setEndDate(LocalDate.now().plusDays(30));
		workoutPlanRepository.save(plan2);

		mockMvc.perform(get("/fitness/workout-plans/user/{userId}", testUser.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].name", is("Plan 1")))
				.andExpect(jsonPath("$[1].name", is("Plan 2")));
	}

	@Test
	@WithMockUser(roles = "USER")
	public void testUpdateWorkoutPlan() throws Exception {
		// Create and save workout plan
		var plan = new WorkoutPlan();
		plan.setUser(testUser);
		plan.setName("Original Plan");
		plan.setDescription("Original Description");
		plan.setStartDate(LocalDate.now());
		plan.setEndDate(LocalDate.now().plusDays(14));
		plan = workoutPlanRepository.save(plan);

		// Create updated DTO
		var updateDTO = new WorkoutPlanDTO();
		updateDTO.setName("Updated Plan");
		updateDTO.setDescription("Updated Description");
		updateDTO.setStartDate(LocalDate.now().plusDays(1));
		updateDTO.setEndDate(LocalDate.now().plusDays(15));
		updateDTO.setUserId(testUser.getId());

		var content = objectMapper.writeValueAsString(updateDTO);

		mockMvc.perform(put("/fitness/workout-plans/{id}", plan.getId()).contentType(MediaType.APPLICATION_JSON)
				.content(content)).andExpect(status().isOk()).andExpect(jsonPath("$.id", is(plan.getId().intValue())))
				.andExpect(jsonPath("$.name", is("Updated Plan")))
				.andExpect(jsonPath("$.description", is("Updated Description")));
	}

	@Test
	@WithMockUser(roles = "USER")
	public void testDeleteWorkoutPlan() throws Exception {
		var plan = new WorkoutPlan();
		plan.setUser(testUser);
		plan.setName("Plan to Delete");
		plan.setDescription("Description");
		plan.setStartDate(LocalDate.now());
		plan.setEndDate(LocalDate.now().plusDays(14));
		plan = workoutPlanRepository.save(plan);

		mockMvc.perform(delete("/fitness/workout-plans/{id}", plan.getId())).andExpect(status().isNoContent());

		mockMvc.perform(get("/fitness/workout-plans/user/{userId}", testUser.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	@WithMockUser(roles = "USER")
	public void testCreateWorkoutPlanWithInvalidData() throws Exception {
		var invalidDTO = new WorkoutPlanDTO();
		invalidDTO.setName("plan1");
		invalidDTO.setDescription("plan1 desc");
		invalidDTO.setDescription("Invalid Plan");

		var content = objectMapper.writeValueAsString(invalidDTO);

		mockMvc.perform(post("/fitness/workout-plans").contentType(MediaType.APPLICATION_JSON).content(content))
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(roles = "USER")
	public void testCreateWorkoutPlanWithNonExistentUser() throws Exception {
		testWorkoutPlanDTO.setUserId(999999L);

		String content = objectMapper.writeValueAsString(testWorkoutPlanDTO);

		mockMvc.perform(post("/fitness/workout-plans").contentType(MediaType.APPLICATION_JSON).content(content))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(roles = "USER")
	public void testUpdateNonExistentWorkoutPlan() throws Exception {
		var content = objectMapper.writeValueAsString(testWorkoutPlanDTO);

		mockMvc.perform(
				put("/fitness/workout-plans/{id}", 99999L).contentType(MediaType.APPLICATION_JSON).content(content))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(roles = "USER")
	public void testDeleteNonExistentWorkoutPlan() throws Exception {
		mockMvc.perform(delete("/fitness/workout-plans/{id}", 99999L)).andExpect(status().isNotFound());
	}

	@Test
	public void testUnauthorizedAccess() throws Exception {
		mockMvc.perform(get("/fitness/workout-plans/user/{userId}", testUser.getId()))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testAdminCanAccessWorkoutPlans() throws Exception {
		mockMvc.perform(get("/fitness/workout-plans/user/{userId}", testUser.getId())).andExpect(status().isOk());
	}
}