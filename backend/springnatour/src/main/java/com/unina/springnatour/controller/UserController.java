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

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {

        UserDto user = userService.getUserById(id);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {

        List<UserDto> users = userService.getAllUsers();

        if (!users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUser(@RequestBody UserDto user) {

        userService.addUser(user);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @RequestBody UserDto user) {

        userService.updateUser(id, user);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
