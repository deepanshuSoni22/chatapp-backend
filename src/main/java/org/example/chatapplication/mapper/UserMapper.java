package org.example.chatapplication.mapper;

import org.example.chatapplication.dto.response.UserResponse;
import org.example.chatapplication.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toDto(User user);
}
