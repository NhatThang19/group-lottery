package vn.project.group_lottery.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TransactionDTO {
    private long id;
    private long amount;
    private String transactionType;
    private LocalDateTime createdDate;
    private String status;
}
