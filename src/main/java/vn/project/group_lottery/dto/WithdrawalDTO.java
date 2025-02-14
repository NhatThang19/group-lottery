package vn.project.group_lottery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalDTO {
    private String password;
    private long amount;
    private String bankName;
    private long accountNumberHolder;
    private String bankNameHolder;
}
