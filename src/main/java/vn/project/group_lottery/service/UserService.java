package vn.project.group_lottery.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.project.group_lottery.dto.Converter;
import vn.project.group_lottery.dto.UserDTO;
import vn.project.group_lottery.model.User;
import vn.project.group_lottery.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createUser(User user) {
        encodedPassword(user);
        return this.userRepository.save(user);
    }

    @Transactional
    public User updateUser(User user) {
        return this.userRepository.save(user);
    }

    public User encodedPassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return user;
    }

    public boolean isEmailExists(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public boolean isUsernameExists(String username) {
        return this.userRepository.existsByUsername(username);
    }

    public boolean isPhoneExists(String phone) {
        return this.userRepository.existsByPhone(phone);
    }

    public User getUserByUsername(String username) {
        return this.userRepository.findUserByUsername(username);
    }

    public List<UserDTO> getAllUser() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> Converter.userConvertToUserDTO(user, new UserDTO()))
                .collect(Collectors.toList());
    }
}
