package com.unina.springnatour.controller;

import com.unina.springnatour.dto.user.UserDto;
import com.unina.springnatour.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Gets an user
     *
     * @param id the identifier of the user
     * @return UserDTO
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {

        UserDto userDto = userService.getUserById(id);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/users/search")
    public ResponseEntity<UserDto> getUserByUUID(@RequestParam String uuid) {

        UserDto userDto = userService.getUserByUUID(uuid);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * Gets all the users
     *
     * @return List of UserDTO
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {

        List<UserDto> userDtoList = userService.getAllUsers();

        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

    /**
     * Gets all the users
     *
     * @return List of UserDTO
     */
    @GetMapping("/users/page")
    public ResponseEntity<List<UserDto>> getAllUsers(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "loggedUser") Long userId,
            @RequestParam(name = "page_no", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "page_size", defaultValue = "10") Integer pageSize
    ) {

        List<UserDto> userDtoList = userService.getAllUsers(query, userId, pageNo, pageSize);

        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

    /**
     * Creates a new user
     *
     * @param userDto the UserDTO Object containing the required fields
     * @return HTTP Status CREATED after insertion
     */
    @PostMapping("/users/add")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto) {

        userService.addUser(userDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Updates an existing user
     *
     * @param id      the identifier of the user
     * @param userDto the UserDTO Object containing updated user
     * @return HTTP Status CREATED after update
     */
    @PutMapping("/users/{id}/update")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id,
                                              @RequestBody UserDto userDto) {

        UserDto savedUser = userService.updateUser(id, userDto);

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    /**
     * Deletes an existing user
     *
     * @param id the identifier of the user
     * @return HTTP Status OK after deletion
     */
    @DeleteMapping("/users/{id}/delete")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
