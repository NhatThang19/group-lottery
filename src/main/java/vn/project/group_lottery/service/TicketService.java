package vn.project.group_lottery.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import vn.project.group_lottery.model.Draw;
import vn.project.group_lottery.model.Ticket;
import vn.project.group_lottery.repository.TicketRepository;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
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

}
