package com.fitness.tracker;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fitness.tracker.mapper.ActivityLogMapper;
import com.fitness.tracker.mapper.UserMapper;
import com.fitness.tracker.mapper.WorkoutPlanMapper;

@SpringBootTest
public class MapperLoadTest {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private WorkoutPlanMapper workoutPlanMapper;

	@Autowired
	private ActivityLogMapper activityLogMapper;

	@Test
	void contextLoads() {
		assertNotNull(userMapper);
		assertNotNull(workoutPlanMapper);
		assertNotNull(activityLogMapper);
	}
}