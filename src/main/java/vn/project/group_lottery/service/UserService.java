package vn.project.group_lottery.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import vn.project.group_lottery.dto.Converter;
import vn.project.group_lottery.dto.UserDTO;
import vn.project.group_lottery.model.User;
import vn.project.group_lottery.model.Wallet;
import vn.project.group_lottery.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final WalletService walletService;
    private final UploadImgService uploadImgService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, WalletService walletService,
            UploadImgService uploadImgService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.walletService = walletService;
        this.uploadImgService = uploadImgService;
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

    @Transactional
    public void deleteUserById(Long id) {
        this.userRepository.deleteById(id);
    }

    public Optional<User> getUserById(long id) {
        return this.userRepository.findById(id);
    }

    public User handleCreateNewUser(User user, MultipartFile image) {
        user.setAvatar(this.uploadImgService.handleSaveUploadImg(image, "avatar"));

        Wallet wallet = new Wallet();
        this.walletService.saveWallet(wallet);
        user.setWallet(wallet);

        return this.createUser(user);
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

    public Page<UserDTO> getAllUser(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findAll(pageable);

        return users.map(user -> Converter.userConvertToUserDTO(user, new UserDTO()));
    }
}
