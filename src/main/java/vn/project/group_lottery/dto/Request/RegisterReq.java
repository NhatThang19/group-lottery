package vn.project.group_lottery.dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import jakarta.validation.constraints.AssertTrue;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterReq implements Serializable {
    @Size(min = 3, max = 25, message = "Tên đăng nhập phải có ít nhất {min} ký tự và tối đa {max} ký tự")
    private String username;

    @Size(min = 6, message = "Mật khẩu phải có ít nhất {min} ký tự")
    private String password;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String confirmPassword;

    @AssertTrue(message = "Mật khẩu không khớp")
    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }
}
