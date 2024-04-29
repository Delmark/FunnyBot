package ru.delmark.FunnyBot.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.delmark.FunnyBot.exceptions.NoSuchUserException;
import ru.delmark.FunnyBot.exceptions.UserDoesNotHaveRoleException;
import ru.delmark.FunnyBot.exceptions.UsernameAlreadyExistsException;
import ru.delmark.FunnyBot.model.DTO.UserDto;
import ru.delmark.FunnyBot.model.Role;
import ru.delmark.FunnyBot.repository.RoleRepository;
import ru.delmark.FunnyBot.model.User;
import ru.delmark.FunnyBot.repository.UserRepository;

import java.util.HashSet;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public Page<User> getAllUsers(int pageNumber) {
        return userRepository.findAll(PageRequest.of(pageNumber, 10));
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public UserDto registerUser(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException();
        }

        Role role = roleRepository.findByAuthority("USER").get();
        HashSet<Role> roles = new HashSet<>(List.of(role));

        User user = userRepository.save(new User()
                .setUsername(username)
                .setPassword(passwordEncoder.encode(password))
                .setEnabled(true)
                .setRoles(roles));

        user.getRoles().add(role);

        return new UserDto(user.getId(), user.getUsername(), user.isEnabled(), user.getRoles());
    }

    @Override
    public UserDto addRoleToUser(Long userId, Long roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(NoSuchFieldError::new);
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("Такого пользователя не существует"));

        user.getRoles().add(role);
        userRepository.save(user);

        return new UserDto(user.getId(), user.getUsername(), user.isEnabled(), user.getRoles());
    }

    @Override
    public UserDto removeRoleFromUser(Long userId, Long roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(NoSuchFieldError::new);
        User user = userRepository.findById(userId).orElseThrow(NoSuchUserException::new);

        if (!user.getRoles().contains(role)) {
            throw new UserDoesNotHaveRoleException();
        }

        user.getRoles().remove(role);
        userRepository.save(user);

        return new UserDto(user.getId(), user.getUsername(), user.isEnabled(), user.getRoles());
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NoSuchUserException::new);

        userRepository.delete(user);
    }
}
