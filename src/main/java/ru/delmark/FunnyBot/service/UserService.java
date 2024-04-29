package ru.delmark.FunnyBot.service;

import org.springframework.data.domain.Page;
import ru.delmark.FunnyBot.model.DTO.UserDto;
import ru.delmark.FunnyBot.model.Role;
import ru.delmark.FunnyBot.model.User;

import java.util.List;

public interface UserService {
    Page<User> getAllUsers(int pageNumber);
    List<Role> getAllRoles();
    UserDto registerUser(String username, String password);
    UserDto addRoleToUser(Long userId, Long roleId);
    UserDto removeRoleFromUser(Long userId, Long roleId);

    void deleteUser(Long userId);
}
