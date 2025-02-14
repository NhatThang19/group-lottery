package vn.project.group_lottery.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDTO implements Serializable {
    private Long ticketId;
    private String ticketNumbers;
    private LocalDateTime purchaseDate;
    private Long price = 10000L;
    private LocalDateTime drawDate;
    private String prizeStatus;
    private String winningRank;
}
