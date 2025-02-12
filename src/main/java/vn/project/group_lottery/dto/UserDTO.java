package vn.project.group_lottery.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String gender;
    private String avatar;
    private String status;
    private LocalDateTime lastLogin;
    private Long roleId;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime lastModifiedDate;
    private String lastModifiedBy;
    private String address;
    private Long balance;
    private TransactionDto transaction;
    private List<TicketDto> tickets;

    @Data
    public static class TicketDto {
        private Long ticketId;
        private String ticketNumbers;
        private LocalDateTime purchaseDate;
        private Double price;
        private LocalDateTime drawDate;
        private String prizeStatus;
        private String winningRank;
    }

    @Data
    public static class TransactionDto {
        private Double totalDeposit;
        private Double totalWithdrawal;
        private Double totalBet;
        private Double totalPrize;
        private Integer transactionCount;
        private List<BetTransactionDto> betTransactions;
        private List<DepositTransactionDto> depositTransactions;
        private List<WithdrawalTransactionDto> withdrawalTransactions;
        private List<PrizeTransactionDto> prizeTransaction;
    }

    @Data
    public static class BetTransactionDto {
        private Long transactionId;
        private LocalDateTime CreatedDate;
        private String status;
        private Double amount;
        private LocalDateTime timestamp;
        private TicketDto ticket;
    }

    @Data
    public static class DepositTransactionDto {
        private Long transactionId;
        private LocalDateTime CreatedDate;
        private String status;
        private Double amount;
    }

    @Data
    public static class WithdrawalTransactionDto {
        private Long transactionId;
        private LocalDateTime CreatedDate;
        private String status;
        private Double amount;
        private String bankName;
        private int accountNumberHolder;
        private String bankNameHolder;
    }

    @Data
    public static class PrizeTransactionDto {
        private Long transactionId;
        private LocalDateTime CreatedDate;
        private Double amount;
        private String status;
    }
}
