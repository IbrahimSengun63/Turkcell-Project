package com.turkcell.staj.controllers;

import com.turkcell.staj.business.abstracts.UserService;
import com.turkcell.staj.dtos.user.requests.RequestAddUserDTO;
import com.turkcell.staj.dtos.user.requests.RequestUpdateUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseAddUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseGetUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseUpdateUserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CacheConfig(cacheNames = "users")
@Tag(name = "User Controller", description = "Manage users in the system")
public class UserController {

    private final UserService userService;

    @PostMapping("/add")
    @Operation(summary = "Add User", description = "Adds a new user to the database based on the provided details.")
    public ResponseEntity<ResponseAddUserDTO> addUser(@Valid @RequestBody RequestAddUserDTO requestAddUserDTO) {
        ResponseAddUserDTO responseAddUserDTO = userService.addUser(requestAddUserDTO);
        return ResponseEntity.ok(responseAddUserDTO);
    }

    @GetMapping("/{id}")
    @Cacheable(key = "#id")
    @Operation(summary = "Get User by ID", description = "Fetches a user from the database using the provided user ID.")
    public ResponseEntity<ResponseGetUserDTO> getUserById(@PathVariable @Valid @Min(value = 1) int id) {
        ResponseGetUserDTO responseGetUserDTO = userService.getUser(id);
        return ResponseEntity.ok(responseGetUserDTO);
    }

    @PutMapping("/update/{id}")
    @CachePut(key = "#id")
    @Operation(summary = "Update User", description = "Updates the details of an existing user based on the provided user ID and update data.")
    public ResponseEntity<ResponseUpdateUserDTO> updateUser(@PathVariable @Valid @Min(value = 1) int id, @Valid @RequestBody RequestUpdateUserDTO requestUpdateUserDTO) {
        ResponseUpdateUserDTO responseUpdateUserDTO = userService.updateUser(id, requestUpdateUserDTO);
        return ResponseEntity.ok(responseUpdateUserDTO);
    }

}
