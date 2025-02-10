package vn.project.group_lottery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/user")
public class UserController {

    private static final String PATH = "USER";

    @GetMapping("")
    public String getUserPage(Model model) {
        model.addAttribute("path", PATH);
        return "admin/user/show";
    }

    @GetMapping("/create")
    public String getCreateUser(Model model) {
        model.addAttribute("path", PATH);
        return "admin/user/create";
    }

}
