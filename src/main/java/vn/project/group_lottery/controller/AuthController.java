package vn.project.group_lottery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vn.project.group_lottery.dto.Request.RegisterInfoReq;
import vn.project.group_lottery.dto.Request.RegisterReq;
import vn.project.group_lottery.service.AuthService;
import vn.project.group_lottery.service.UserService;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    private final UserService userService;
    private final AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("registerReq", new RegisterReq());

        return "auth/register";
    }

    @PostMapping("/register")
    public String postRegister(@ModelAttribute @Valid RegisterReq registerReq,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (this.userService.isUsernameExists(registerReq.getUsername())) {
            bindingResult.rejectValue("username", "", "Tên đăng nhập đã được tồn tại");
        }

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        redirectAttributes.addFlashAttribute("registerReq", registerReq);

        return "redirect:/auth/register/info";
    }

    @GetMapping("/register/info")
    public String getRegisterInfo(@ModelAttribute RegisterReq registerReq,
            Model model) {
        model.addAttribute("registerReq", registerReq);
        model.addAttribute("registerInfoReq", new RegisterInfoReq());

        return "auth/register-info";
    }

    @PostMapping("/register/info")
    public String postRegisterInfo(@ModelAttribute RegisterReq registerReq,
            @ModelAttribute @Valid RegisterInfoReq registerInfoReq,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (this.userService.isEmailExists(registerInfoReq.getEmail())) {
            bindingResult.rejectValue("email", "", "Email đã tồn tại");
        }

        if (this.userService.isPhoneExists(registerInfoReq.getPhone())) {
            bindingResult.rejectValue("phone", "", "Số điện thoại đã tồn tại");
        }

        if (bindingResult.hasErrors()) {
            return "auth/register-info";
        }

        this.authService.registerUser(registerReq, registerInfoReq);

        redirectAttributes.addFlashAttribute("regSuccessMessage", "Đăng ký thành công!");

        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String getLogin(HttpSession session, Model model) {
        String loginErrorMessage = (String) session.getAttribute("loginErrorMessage");
        String logoutSuccessMessage = (String) session.getAttribute("logoutSuccessMessage");

        if (loginErrorMessage != null) {
            model.addAttribute("loginErrorMessage", loginErrorMessage);
            session.removeAttribute("loginErrorMessage");
        }

        if (logoutSuccessMessage != null) {
            model.addAttribute("logoutSuccessMessage", logoutSuccessMessage);
            session.removeAttribute("logoutSuccessMessage");
        }

        return "auth/login";
    }
}
