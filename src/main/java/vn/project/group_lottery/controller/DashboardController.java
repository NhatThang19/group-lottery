package vn.project.group_lottery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class DashboardController {

    private static final String PATH = "DASHBOARD";

    @GetMapping("")
    public String getRegister(Model model) {
        model.addAttribute("path", PATH);
        return "admin/dashboard";
    }
}
