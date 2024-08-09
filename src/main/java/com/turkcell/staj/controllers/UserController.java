package com.turkcell.staj.controllers;

import com.turkcell.staj.business.abstracts.UserService;
import com.turkcell.staj.dtos.user.requests.RequestAddUserDTO;
import com.turkcell.staj.dtos.user.requests.RequestUpdateUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseGetUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseAddUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseUpdateUserDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<ResponseAddUserDTO> addUser(@Valid @RequestBody RequestAddUserDTO requestAddUserDTO) {
        ResponseAddUserDTO responseAddUserDTO = userService.addUser(requestAddUserDTO);
        return ResponseEntity.ok(responseAddUserDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseGetUserDTO> getUserById(@PathVariable @Valid @Min(value = 1) int id) {
        ResponseGetUserDTO responseGetUserDTO = userService.getUser(id);
        return ResponseEntity.ok(responseGetUserDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseUpdateUserDTO> updateUser(@PathVariable @Valid @Min(value = 1) int id, @Valid @RequestBody RequestUpdateUserDTO requestUpdateUserDTO) {
        ResponseUpdateUserDTO responseUpdateUserDTO = userService.updateUser(id, requestUpdateUserDTO);
        return ResponseEntity.ok(responseUpdateUserDTO);
    }

}
