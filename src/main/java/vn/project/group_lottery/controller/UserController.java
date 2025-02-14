package vn.project.group_lottery.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import vn.project.group_lottery.dto.Converter;
import vn.project.group_lottery.dto.UserDTO;
import vn.project.group_lottery.enums.Gender;
import vn.project.group_lottery.enums.UserStatus;
import vn.project.group_lottery.model.User;
import vn.project.group_lottery.service.RoleService;
import vn.project.group_lottery.service.UploadImgService;
import vn.project.group_lottery.service.UserService;

@Controller
@RequestMapping("/admin/user")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final UploadImgService uploadImgService;

    public UserController(UserService userService, RoleService roleService, UploadImgService uploadImgService) {
        this.userService = userService;
        this.roleService = roleService;
        this.uploadImgService = uploadImgService;
    }

    private static final String PATH = "USER";

    @GetMapping("")
    public String getUsersPage(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String role,
            Model model) {
        model.addAttribute("path", PATH);

        Page<UserDTO> users = userService.getAllUser(page, size, sortBy, direction, search, status, role);
        model.addAttribute("users", users);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("search", search);
        model.addAttribute("status", status);
        model.addAttribute("role", role);

        model.addAttribute("statusOptions", List.of("active", "banned"));
        model.addAttribute("roleOptions", List.of("admin", "user"));

        return "admin/user/show";
    }

    @GetMapping("/create")
    public String getCreateUser(Model model) {
        model.addAttribute("path", PATH);

        model.addAttribute("user", new User());

        return "admin/user/create";
    }

    @PostMapping("/create")
    public String postCreateUser(@ModelAttribute @Valid User user, BindingResult bindingResult,
            @RequestParam("user-avatar") MultipartFile avatarFile,
            RedirectAttributes redirectAttributes) {

        if (this.userService.isEmailExists(user.getEmail())) {
            bindingResult.rejectValue("email", "", "Email đã tồn tại");
        }

        if (this.userService.isPhoneExists(user.getPhone())) {
            bindingResult.rejectValue("phone", "", "Số điện thoại đã tồn tại");
        }

        if (bindingResult.hasErrors()) {
            return "admin/user/create";
        }

        try {
            this.userService.handleCreateNewUser(user, avatarFile);
            redirectAttributes.addFlashAttribute("toastMessage", "Tạo người dùng thành công!");
            redirectAttributes.addFlashAttribute("toastHeading", "Thành công");
            redirectAttributes.addFlashAttribute("toastIcon", "success");
            redirectAttributes.addFlashAttribute("toastLoaderBg", "#31ce36");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toastMessage", "Lỗi khi tạo người dùng: " + e.getMessage());
            redirectAttributes.addFlashAttribute("toastHeading", "Lỗi");
            redirectAttributes.addFlashAttribute("toastIcon", "error");
            redirectAttributes.addFlashAttribute("toastLoaderBg", "#f96868");
        }

        return "redirect:/admin/user/create";
    }

    @PostMapping("/delete")
    public String postDeleteUser(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            this.userService.deleteUserById(id);
            redirectAttributes.addFlashAttribute("toastMessage", "Xóa người dùng thành công!");
            redirectAttributes.addFlashAttribute("toastHeading", "Thành công");
            redirectAttributes.addFlashAttribute("toastIcon", "success");
            redirectAttributes.addFlashAttribute("toastLoaderBg", "#31ce36");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toastMessage", "Lỗi khi xóa người dùng: " + e.getMessage());
            redirectAttributes.addFlashAttribute("toastHeading", "Thất bại");
            redirectAttributes.addFlashAttribute("toastIcon", "error");
            redirectAttributes.addFlashAttribute("toastLoaderBg", "#f96868");
        }

        return "redirect:/admin/user";
    }

    @GetMapping("/update/{id}")
    public String getUpdateUser(@PathVariable Long id, Model model) {
        User user = this.userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy:" + id));
        UserDTO userDTO = new UserDTO();
        Converter.userConvertToUserDTO(user, userDTO);

        model.addAttribute("userDTO", userDTO);

        return "admin/user/update";
    }

    @PostMapping("/update")
    public String postUpdateUser(@ModelAttribute @Valid UserDTO userDTO,
            BindingResult bindingResult,
            @RequestParam(value = "user-avatar", required = false) MultipartFile avatarFile,
            RedirectAttributes redirectAttributes,
            Model model) {

        User existingUser = this.userService.getUserById(userDTO.getId())
                .orElseThrow(
                        () -> new IllegalArgumentException("Không tìm thấy người dùng với ID: " + userDTO.getId()));

        if (!existingUser.getEmail().equals(userDTO.getEmail())) {
            if (this.userService.isEmailExists(userDTO.getEmail())) {
                bindingResult.rejectValue("email", "", "Email đã tồn tại");
            }
        }

        if (!existingUser.getPhone().equals(userDTO.getPhone())) {
            if (this.userService.isPhoneExists(userDTO.getPhone())) {
                bindingResult.rejectValue("phone", "", "Số điện thoại đã tồn tại");
            }
        }

        if (bindingResult.hasErrors()) {
            return "admin/user/update";
        }

        try {
            existingUser.setUsername(userDTO.getUsername());
            existingUser.setEmail(userDTO.getEmail());
            existingUser.setPhone(userDTO.getPhone());
            existingUser.setAddress(userDTO.getAddress());
            existingUser.setDateOfBirth(userDTO.getDateOfBirth());
            existingUser.setGender(Gender.valueOf(userDTO.getGender()));
            existingUser.setStatus(UserStatus.valueOf(userDTO.getStatus()));
            existingUser.setRole(this.roleService.getRoleByName(userDTO.getRole()));

            if (avatarFile != null) {
                existingUser.setAvatar(uploadImgService.handleSaveUploadImg(avatarFile, "avatar"));
            }

            redirectAttributes.addFlashAttribute("toastMessage", "Cập nhật người dùng thành công!");
            redirectAttributes.addFlashAttribute("toastHeading", "Thành công");
            redirectAttributes.addFlashAttribute("toastIcon", "success");
            redirectAttributes.addFlashAttribute("toastLoaderBg", "#31ce36");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toastMessage", "Lỗi khi cập nhật người dùng: " + e.getMessage());
            redirectAttributes.addFlashAttribute("toastHeading", "Lỗi");
            redirectAttributes.addFlashAttribute("toastIcon", "error");
            redirectAttributes.addFlashAttribute("toastLoaderBg", "#f96868");
        }

        return "redirect:/admin/user/update/" + existingUser.getId();
    }
}
