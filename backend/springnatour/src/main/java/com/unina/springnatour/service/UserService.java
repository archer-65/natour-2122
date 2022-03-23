package com.unina.springnatour.service;

import com.unina.springnatour.dto.user.UserDto;
import com.unina.springnatour.dto.user.UserMapper;
import com.unina.springnatour.exception.UserNotFoundException;
import com.unina.springnatour.model.User;
import com.unina.springnatour.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    /**
     * Gets a user
     *
     * @param id the identifier of the user
     * @return UserDTO Object after mapping from Entity, or throws Exception
     */
    public UserDto getUserById(Long id) {
        return userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }

    public UserDto getUserByUUID(String uuid) {
        return userMapper.toDto(userRepository.findByCognitoId(uuid));
    }

    /**
     * Gets all the users
     *
     * @return List of UserDTO Objects mapped from Entity
     */
    public List<UserDto> getAllUsers() {
        return userMapper.toDto(userRepository.findAll()
                .stream()
                .toList());
    }

    public List<UserDto> getAllUsers(String query, Long userId, Integer pageNo, Integer pageSize) {
        return userMapper.toDto(userRepository.findByUsernameContainingIgnoreCaseAndIdNot(query, userId, PageRequest.of(pageNo, pageSize))
                .stream()
                .toList());
    }

    /**
     * Adds a user
     *
     * @param userDto UserDTO Object with required fields, mapped to Entity and saved
     */
    public void addUser(UserDto userDto) {
        userRepository.save(userMapper.toEntity(userDto));
    }

    /**
     * Updates a user
     *
     * @param id      the identifier of the user
     * @param userDto UserDTO Object, mapped to Entity, or throw Exception
     */
    public void updateUser(Long id, UserDto userDto) {
        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.save(userMapper.toEntity(userDto));
    }

    /**
     * Deletes a user
     *
     * @param id the identifier of the user
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}