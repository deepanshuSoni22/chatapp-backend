package org.example.chatbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.chatbackend.dto.request.UserRequest;
import org.example.chatbackend.dto.response.UserResponse;
import org.example.chatbackend.entity.User;
import org.example.chatbackend.exception.custom.UsernameAlreadyExistsException;
import org.example.chatbackend.mapper.UserMapper;
import org.example.chatbackend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;

    public UserResponse register(UserRequest request) {
        if (request == null) {
            return null;
        }

        if (userRepository.existsUserByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));

        // save to database
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public List<UserResponse> getAllUsersExcept(String username) {
        return userRepository.findAll().stream()
                .filter(user -> !user.getUsername().equals(username))
                .map(userMapper::toDto)
                .toList();
    }

}
