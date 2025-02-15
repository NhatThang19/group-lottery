package vn.project.group_lottery.dto;

import java.time.LocalDateTime;

import lombok.Data;
import vn.project.group_lottery.enums.TicketStatus;

@Data
public class TicketRes {
    private Long id;
    private String numbersTicket;
    private TicketStatus status;
    private LocalDateTime drawDate;
    private String winningNumbers;
}
