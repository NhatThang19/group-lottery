package vn.project.group_lottery.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "draws")
public class Draw {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String winningNumbers;

    private LocalDateTime drawDate;

    private long totalPrize = 0;

    private boolean isProcessed = false;

    @OneToMany(mappedBy = "draw", cascade = CascadeType.ALL)
    private List<Ticket> ticket;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "prize_id")
    private Prize prize;

}
