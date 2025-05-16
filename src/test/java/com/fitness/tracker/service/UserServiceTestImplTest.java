package com.fitness.tracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fitness.tracker.dto.UserDTO;
import com.fitness.tracker.exception.ResourceNotFoundException;
import com.fitness.tracker.mapper.UserMapper;
import com.fitness.tracker.model.User;
import com.fitness.tracker.model.User.Role;
import com.fitness.tracker.repository.UserRepository;

class UserServiceTestImplTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private UserMapper userMapper;

	@InjectMocks
	private UserServiceImpl userService;

	private AutoCloseable closeable;

	private final String encodedPassword = "encodedPass";

	@BeforeEach
	void setup() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateUser_Success() {
		var userDTO = new UserDTO();
		userDTO.setUsername("john");
		userDTO.setPassword("pass123");
		userDTO.setEmail("john@example.com");
		userDTO.setRole(Role.ADMIN);

		var userEntity = new User();
		userEntity.setId(1L);

		when(userRepository.findByUsername("john")).thenReturn(null);
		when(passwordEncoder.encode("pass123")).thenReturn(encodedPassword);
		when(userMapper.toEntity(any(UserDTO.class))).thenReturn(userEntity);
		when(userRepository.save(any(User.class))).thenReturn(userEntity);
		when(userMapper.toDTO(userEntity)).thenReturn(userDTO);

		UserDTO result = userService.createUser(userDTO);

		assertEquals(userDTO, result);
		verify(userRepository).save(userEntity);
	}

	@Test
	void testCreateUser_UsernameExists() {
		var userDTO = new UserDTO();
		userDTO.setUsername("john");
		userDTO.setPassword("pass123");
		userDTO.setEmail("john@example.com");
		userDTO.setRole(Role.ADMIN);

		when(userRepository.findByUsername("john")).thenReturn(new User());

		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> userService.createUser(userDTO));

		assertEquals("Username already exists", ex.getMessage());
	}

	@Test
	void testGetAllUsers() {
		List<User> users = List.of(new User(), new User());
		List<UserDTO> userDTOs = List.of(new UserDTO(), new UserDTO());

		when(userRepository.findAll()).thenReturn(users);
		when(userMapper.toDTOList(users)).thenReturn(userDTOs);

		List<UserDTO> result = userService.getAllUsers();
		assertEquals(2, result.size());
	}

	@Test
	void testGetUserById_Success() {
		User user = new User();
		user.setId(1L);
		UserDTO dto = new UserDTO();

		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(userMapper.toDTO(user)).thenReturn(dto);

		UserDTO result = userService.getUserById(1L);
		assertNotNull(result);
	}

	@Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
    }

	@Test
	void testUpdateUser_Success() {
		var user = new User();
		user.setId(1L);

		var userDTO = new UserDTO();
		userDTO.setUsername("newUser");
		userDTO.setPassword("newPass");
		userDTO.setEmail("new@example.com");
		userDTO.setRole(Role.ADMIN);

		User updatedUser = new User();
		updatedUser.setId(1L);

		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(passwordEncoder.encode("newPass")).thenReturn(encodedPassword);
		when(userRepository.save(any(User.class))).thenReturn(updatedUser);
		when(userMapper.toDTO(updatedUser)).thenReturn(userDTO);

		UserDTO result = userService.updateUser(1L, userDTO);
		assertEquals(userDTO, result);
	}

	@Test
    void testUpdateUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(1L, new UserDTO()));
    }

	@Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

	@Test
    void testDeleteUser_NotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(1L));
    }

	@BeforeEach
	void tearDown() throws Exception {
		if (closeable != null)
			closeable.close();
	}
}
