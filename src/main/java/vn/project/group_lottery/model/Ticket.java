package vn.project.group_lottery.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import vn.project.group_lottery.enums.TicketStatus;

@Entity
@Table(name = "tickets")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numbersTicket;

    private TicketStatus status = TicketStatus.PENDING;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @ManyToOne()
    @JoinColumn(name = "draw_id")
    @ToString.Exclude
    private Draw draw;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Transaction> transactions;
}
