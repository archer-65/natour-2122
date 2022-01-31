package com.unina.springnatour.service;

import com.unina.springnatour.dto.user.UserDto;
import com.unina.springnatour.dto.user.UserMapper;
import com.unina.springnatour.exception.UserNotFoundException;
import com.unina.springnatour.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public UserDto getUserById(Long id) {
        return userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }

    public List<UserDto> getAllUsers() {
        return userMapper.toDto(userRepository.findAll()
                .stream()
                .toList());
    }

    public void addUser(UserDto userDto) {
        userRepository.save(userMapper.toEntity(userDto));
    }

    public void updateUser(Long id, UserDto userDto) {
        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.save(userMapper.toEntity(userDto));
    }
}