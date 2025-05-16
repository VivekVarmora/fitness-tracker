package com.fitness.tracker.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.fitness.tracker.dto.UserDTO;
import com.fitness.tracker.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	UserDTO toDTO(User user);

	User toEntity(UserDTO dto);

	List<UserDTO> toDTOList(List<User> users);
}
