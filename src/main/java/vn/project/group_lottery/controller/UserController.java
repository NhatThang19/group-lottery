package vn.project.group_lottery.controller;

import java.util.List;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.project.group_lottery.dto.Response.UserRes;
import vn.project.group_lottery.service.UserService;

@Controller
@RequestMapping("/admin/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private static final String PATH = "USER";

    @GetMapping("")
    public String getUserPage(Model model) {
        model.addAttribute("path", PATH);

        List<UserRes> userResponses = this.userService.getAllUser();
        model.addAttribute("users", userResponses);

        return "admin/user/show";
    }

    @GetMapping("/create")
    public String getCreateUser(Model model) {
        model.addAttribute("path", PATH);
        return "admin/user/create";
    }

}
