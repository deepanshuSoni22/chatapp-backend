package org.example.chatbackend.mapper;

import org.example.chatbackend.dto.response.UserResponse;
import org.example.chatbackend.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toDto(User user);
}
