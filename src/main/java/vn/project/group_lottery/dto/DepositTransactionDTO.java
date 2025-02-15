package vn.project.group_lottery.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DepositTransactionDTO {
    private Long transactionId;
    private LocalDateTime CreatedDate;
    private String status;
    private Double amount;
}
