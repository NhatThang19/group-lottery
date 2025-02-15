package vn.project.group_lottery.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    @Size(min = 3, max = 25, message = "Tên đăng nhập phải có ít nhất {min} ký tự và tối đa {max} ký tự")
    private String username;
    @Email(message = "Email không hợp lệ", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;
    @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại không hợp lệ")
    private String phone;
    @NotNull(message = "Ngày sinh không được để trống")
    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    @NotNull(message = "Giới tính không được để trống")
    private String gender;
    private String avatar;
    @NotNull(message = "Trạng thái không được để trống")
    private String status;
    private LocalDateTime lastLogin;
    @NotNull(message = "Vai trò không được để trống")
    private String role;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime lastModifiedDate;
    private String lastModifiedBy;
    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;
    private Long balance;
    private List<TransactionDTO> transactionDTO;
    private List<TicketRes> ticketsDTO;

}
