package vn.project.group_lottery.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class WithdrawalTransactionDTO {
    private Long transactionId;
    private LocalDateTime CreatedDate;
    private String status;
    private Double amount;
    private String bankName;
    private int accountNumberHolder;
    private String bankNameHolder;
}
