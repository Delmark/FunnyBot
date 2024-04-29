package ru.delmark.FunnyBot.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.delmark.FunnyBot.model.DTO.UserDto;
import ru.delmark.FunnyBot.model.DTO.UserRegDto;
import ru.delmark.FunnyBot.model.Role;
import ru.delmark.FunnyBot.model.User;
import ru.delmark.FunnyBot.service.UserService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @GetMapping("/getAllRoles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(userService.getAllRoles());
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<Page<User>> getAllUsers(@RequestParam("page") int page) {
        return ResponseEntity.ok(userService.getAllUsers(page));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserRegDto dto) {
        if (dto.getUsername() != null && dto.getPassword() != null) {
            return ResponseEntity.ok(userService.registerUser(dto.getUsername(), dto.getPassword()));
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/addRole")
    public ResponseEntity<UserDto> addRoleToUser(@RequestParam("userId") Long userId,
                                                 @RequestParam("roleId") Long roleId) {
        return ResponseEntity.ok(userService.addRoleToUser(userId, roleId));
    }

    @PutMapping("/removeRole")
    public ResponseEntity<UserDto> removeRoleFromUser(@RequestParam("userId") Long userId,
                                                 @RequestParam("roleId") Long roleId) {
        return ResponseEntity.ok(userService.removeRoleFromUser(userId, roleId));
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<Void> deleteUser(@RequestParam("userId") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
