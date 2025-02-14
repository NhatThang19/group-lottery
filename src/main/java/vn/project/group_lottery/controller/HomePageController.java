package vn.project.group_lottery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePageController {
    @GetMapping("/")
    public String getHomePage() {
        return "client/index";
    }
}
