package vn.project.group_lottery.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BetTransactionDTO {
    private Long transactionId;
    private LocalDateTime CreatedDate;
    private String status;
    private Double amount;
    private LocalDateTime timestamp;
    private TicketDTO ticket;
}