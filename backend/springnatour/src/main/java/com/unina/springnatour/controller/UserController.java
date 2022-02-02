package com.unina.springnatour.controller;

import com.unina.springnatour.dto.user.UserDto;
import com.unina.springnatour.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Get an user
     * @param id the identifier of the user
     * @return UserDTO
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {

        UserDto userDto = userService.getUserById(id);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * Get all the users
     * @return List of UserDTO
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {

        List<UserDto> userDtoList = userService.getAllUsers();

        if (!userDtoList.isEmpty()) {
            return new ResponseEntity<>(userDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Create a new user
     * @param userDto the UserDTO Object containing the required fields
     * @return HTTP Status CREATED after insertion
     */
    @PostMapping("/users/add")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto) {

        userService.addUser(userDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Update existing user
     * @param id the identifier of the user
     * @param userDto the UserDTO Object containing updated user
     * @return HTTP Status CREATED after update
     */
    @PutMapping("/users/{id}/update")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @RequestBody UserDto userDto) {

        userService.updateUser(id, userDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Delete existing user
     * @param id the identifier of the user
     * @return HTTP Status OK after deletion
     */
    @DeleteMapping("/users/{id}/delete")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
