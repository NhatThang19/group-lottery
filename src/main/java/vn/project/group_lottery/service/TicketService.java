package vn.project.group_lottery.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import vn.project.group_lottery.enums.TicketStatus;
import vn.project.group_lottery.model.Draw;
import vn.project.group_lottery.model.Ticket;
import vn.project.group_lottery.repository.TicketRepository;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final DrawService drawService;

    public TicketService(TicketRepository ticketRepository, DrawService drawService) {
        this.ticketRepository = ticketRepository;
        this.drawService = drawService;
    }

    public void checkForDuplicateTicket(Ticket ticket, Draw latestDraw) {
        Set<String> inputNumbersSet = new HashSet<>(Arrays.asList(ticket.getNumbersTicket().split(",")));

        boolean isDuplicate = ticketRepository.findByDrawId(latestDraw.getId()).stream()
                .map(t -> new HashSet<>(Arrays.asList(t.getNumbersTicket().split(","))))
                .anyMatch(existingNumbersSet -> existingNumbersSet.equals(inputNumbersSet));

        if (isDuplicate) {
            throw new IllegalArgumentException("Vé với số " + ticket.getNumbersTicket() + " đã tồn tại!");
        }
    }

    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public List<Ticket> findTicketsByDraw() {
        List<Draw> draws = drawService.findSecondLatestDraw();
        if (draws.isEmpty()) {
            throw new EntityNotFoundException("Không tìm thấy kỳ quay hợp lệ!");
        }

        Draw targetDraw = draws.size() >= 2 ? draws.get(1) : draws.get(0);
        return ticketRepository.findByDrawId(targetDraw.getId());
    }

    public List<Ticket> findWinningTicket() {
        List<Draw> draws = drawService.findSecondLatestDraw();
        if (draws.isEmpty()) {
            throw new EntityNotFoundException("Không tìm thấy kỳ quay hợp lệ!");
        }

        Draw targetDraw = draws.size() >= 2 ? draws.get(1) : draws.get(0);

        if (targetDraw == null) {
            throw new EntityNotFoundException("Không tìm thấy kỳ quay hợp lệ để xử lý!");
        }

        List<Ticket> tickets = ticketRepository.findByDrawId(targetDraw.getId());

        List<Ticket> winningTickets = new ArrayList<>();

        List<String> winningNumbersList = Arrays.asList(targetDraw.getWinningNumbers().split(","));

        for (Ticket ticket : tickets) {
            if (ticket.getNumbersTicket() == null) {
                throw new RuntimeException("Số vé không hợp lệ!");
            }

            List<String> ticketNumbersList = Arrays.asList(ticket.getNumbersTicket().split(","));

            int matchingCount = 0;
            for (int i = 0; i < winningNumbersList.size(); i++) {
                if (i < ticketNumbersList.size() && ticketNumbersList.get(i).equals(winningNumbersList.get(i))) {
                    matchingCount++;
                } else {
                    break;
                }
            }

            if (matchingCount == winningNumbersList.size()) {
                ticket.setStatus(TicketStatus.JACKPOT);
            } else if (matchingCount == 5) {
                ticket.setStatus(TicketStatus.FIRST);
            } else if (matchingCount == 4) {
                ticket.setStatus(TicketStatus.SECOND);
            } else if (matchingCount == 3) {
                ticket.setStatus(TicketStatus.THIRD);
            } else {
                ticket.setStatus(TicketStatus.LOSER);
            }

            if (ticket.getStatus() != TicketStatus.LOSER) {
                winningTickets.add(ticket);
            }
        }

        ticketRepository.saveAll(tickets);

        return winningTickets;
    }

}
