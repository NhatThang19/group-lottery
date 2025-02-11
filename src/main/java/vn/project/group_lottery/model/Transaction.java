package vn.project.group_lottery.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.project.group_lottery.enums.TransactionStatus;
import vn.project.group_lottery.enums.TransactionType;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Positive
    @NotNull
    private long amount;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @CreatedDate
    private LocalDateTime CreatedDate;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @ManyToOne()
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "withdrawal_id")
    private Withdrawal withdrawal;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;
}
