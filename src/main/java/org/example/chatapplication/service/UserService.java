package org.example.chatapplication.service;

import lombok.RequiredArgsConstructor;
import org.example.chatapplication.dto.request.UserRequest;
import org.example.chatapplication.dto.response.UserResponse;
import org.example.chatapplication.entity.User;
import org.example.chatapplication.exception.custom.UsernameAlreadyExistsException;
import org.example.chatapplication.mapper.UserMapper;
import org.example.chatapplication.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


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

}
