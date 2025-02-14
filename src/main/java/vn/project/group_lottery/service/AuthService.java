package vn.project.group_lottery.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.project.group_lottery.dto.Converter;
import vn.project.group_lottery.dto.Request.RegisterInfoReq;
import vn.project.group_lottery.dto.Request.RegisterReq;
import vn.project.group_lottery.enums.UserStatus;
import vn.project.group_lottery.model.User;
import vn.project.group_lottery.model.Wallet;

@Service
public class AuthService {
    private final RoleService roleService;
    private final UserService userService;
    private final WalletService walletService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(RoleService roleService, UserService userService, WalletService walletService,
            PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userService = userService;
        this.walletService = walletService;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(RegisterReq registerReq, RegisterInfoReq registerInfoReq) {
        User user = new User();

        Converter.registerReqConvertToUser(user, registerReq, registerInfoReq);
        user.setStatus(UserStatus.active);
        user.setAvatar("default_avatar.jpg");
        user.setRole(this.roleService.getRoleByName("admin"));

        Wallet wallet = new Wallet();
        this.walletService.saveWallet(wallet);
        user.setWallet(wallet);

        return this.userService.createUser(user);
    }

    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
