package vn.project.group_lottery.dto;

import java.time.LocalDateTime;

import lombok.Data;

public class PrizeTransactionDTO {
    @Data
    public static class PrizeTransactionDto {
        private Long transactionId;
        private LocalDateTime CreatedDate;
        private Double amount;
        private String status;
    }
}
