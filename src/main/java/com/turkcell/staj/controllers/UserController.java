package com.turkcell.staj.controllers;

import com.turkcell.staj.business.abstracts.UserService;
import com.turkcell.staj.dtos.user.requests.RequestAddUserDTO;
import com.turkcell.staj.dtos.user.responses.GetResponseUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseAddUserDTO;
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

    @PostMapping("/v1/add")
    public ResponseEntity<ResponseAddUserDTO> addUser(@RequestBody @Valid RequestAddUserDTO requestAddUserDTO) {
        ResponseAddUserDTO responseAddUserDTO = this.userService.addUser(requestAddUserDTO);
        return ResponseEntity.ok(responseAddUserDTO);
    }

    @GetMapping("/v1/{id}")
    public ResponseEntity<GetResponseUserDTO> getUserById(@PathVariable @Valid @Min(value = 1) int id) {
        GetResponseUserDTO responseAddUserDTO = this.userService.getUserById(id);
        return ResponseEntity.ok(responseAddUserDTO);
    }

    @PutMapping("/v1/update/{id}")
    public ResponseEntity<ResponseAddUserDTO> updateUser(@PathVariable @Valid @Min(value = 1) int id, @RequestBody @Valid RequestAddUserDTO requestUpdateUserDTO) {
        ResponseAddUserDTO responseAddUserDTO = this.userService.updateUser(id, requestUpdateUserDTO);
        return ResponseEntity.ok(responseAddUserDTO);
    }

    @DeleteMapping("/v1/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Valid @Min(value = 1) int id) {
        this.userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
