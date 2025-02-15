package vn.project.group_lottery.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import vn.project.group_lottery.dto.TicketDTO;
import vn.project.group_lottery.dto.TransactionDTO;
import vn.project.group_lottery.dto.WithdrawalDTO;
import vn.project.group_lottery.enums.BankName;
import vn.project.group_lottery.enums.TicketStatus;
import vn.project.group_lottery.enums.TransactionStatus;
import vn.project.group_lottery.enums.TransactionType;
import vn.project.group_lottery.model.Draw;
import vn.project.group_lottery.model.Prize;
import vn.project.group_lottery.model.Ticket;
import vn.project.group_lottery.model.Transaction;
import vn.project.group_lottery.model.User;
import vn.project.group_lottery.model.Wallet;
import vn.project.group_lottery.model.Withdrawal;
import vn.project.group_lottery.repository.TransactionRepository;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final WalletService walletService;
    private final AuthService authService;
    private final DrawService drawService;
    private final TicketService ticketService;

    public TransactionService(TransactionRepository transactionRepository, UserService userService,
            WalletService walletService, AuthService authService, DrawService drawService,
            TicketService ticketService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.walletService = walletService;
        this.authService = authService;
        this.drawService = drawService;
        this.ticketService = ticketService;
    }

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Transactional
    public void handleDeposit(String username, Long amount) {
        User user = userService.getUserByUsername(username);
        Wallet wallet = user.getWallet();

        if (wallet == null) {
            throw new RuntimeException("Người dùng không có ví.");
        }

        wallet.setBalance(wallet.getBalance() + amount);

        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setCreatedDate(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.SUCCESS);

        walletService.saveWallet(wallet);
        this.saveTransaction(transaction);
    }

    @Transactional
    public void handleWithdrawal(String username, WithdrawalDTO withdrawalDTO) throws Exception {
        User user = userService.getUserByUsername(username);
        Wallet wallet = user.getWallet();

        if (!authService.isPasswordMatch(withdrawalDTO.getPassword(), user.getPassword())) {
            throw new Exception("Sai mật khẩu");
        }

        if (wallet.getBalance() < withdrawalDTO.getAmount()) {
            throw new Exception("Số dư không đủ để rút tiền.");
        }

        wallet.setBalance(wallet.getBalance() - withdrawalDTO.getAmount());

        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setBankName(BankName.valueOf(withdrawalDTO.getBankName()));
        withdrawal.setAccountNumberHolder(withdrawalDTO.getAccountNumberHolder());
        withdrawal.setBankNameHolder(withdrawalDTO.getBankNameHolder());

        Transaction transaction = new Transaction();
        transaction.setAmount(withdrawalDTO.getAmount());
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setCreatedDate(LocalDateTime.now());
        transaction.setWallet(wallet);
        transaction.setWithdrawal(withdrawal);

        walletService.saveWallet(wallet);
        this.saveTransaction(transaction);
    }

    @Transactional
    public void handelBetTransaction(TicketDTO ticketDTO, String username) {
        Ticket ticket = new Ticket();
        ticket.setNumbersTicket(ticketDTO.getTicketNumbers());
        ticket.setStatus(TicketStatus.PENDING);

        Draw latestDraw = drawService.findLatestDraw();
        if (latestDraw == null) {
            throw new IllegalStateException("Không tìm thấy đợt quay số nào!");
        }

        ticketService.checkForDuplicateTicket(ticket, latestDraw);

        User user = userService.getUserByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("Không tìm thấy người dùng!");
        }

        if (user.getWallet().getBalance() < ticketDTO.getPrice()) {
            throw new IllegalStateException("Số dư không đủ để thực hiện giao dịch");
        }

        user.getWallet().setBalance(user.getWallet().getBalance() - ticketDTO.getPrice());

        ticket.setUser(user);
        ticket.setDraw(latestDraw);
        ticketService.saveTicket(ticket);

        Transaction transaction = new Transaction();
        transaction.setAmount(ticketDTO.getPrice());
        transaction.setTransactionType(TransactionType.BET);
        transaction.setCreatedDate(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setWallet(user.getWallet());
        transaction.setTicket(ticket);

        this.saveTransaction(transaction);
    }

    @Transactional
    public long handelPrizeTransaction() {

        List<Ticket> winningTickets = ticketService.findWinningTicket();

        List<Ticket> allTickets = ticketService.findTicketsByDraw();

        if (allTickets.isEmpty()) {
            return 0;
        }

        Draw draw = allTickets.get(0).getDraw();
        Prize prize = draw.getPrize();
        if (prize == null) {
            throw new EntityNotFoundException("Không tìm thấy thông tin giải thưởng cho vòng quay này!");
        }

        Long totalPrizeAmount = 0L;
        for (Ticket winningTicket : winningTickets) {
            switch (winningTicket.getStatus()) {
                case JACKPOT:
                    totalPrizeAmount += prize.getJackpot();
                    break;
                case FIRST:
                    totalPrizeAmount += prize.getFirstPrize();
                    break;
                case SECOND:
                    totalPrizeAmount += prize.getSecondPrize();
                    break;
                case THIRD:
                    totalPrizeAmount += prize.getThirdPrize();
                    break;
                default:
                    break;
            }
        }

        // Nhóm các vé theo người chơi
        Map<User, List<Ticket>> ticketsByUser = allTickets.stream()
                .collect(Collectors.groupingBy(Ticket::getUser));

        // Tính số lượng người chơi duy nhất
        int numberOfParticipants = ticketsByUser.size();

        // Chia đều tổng tiền thưởng cho số lượng người chơi
        Long prizePerParticipant = totalPrizeAmount / numberOfParticipants;

        // Cập nhật số dư ví và lưu giao dịch
        List<Wallet> walletsToUpdate = new ArrayList<>();
        List<Transaction> transactionsToSave = new ArrayList<>();

        for (Map.Entry<User, List<Ticket>> entry : ticketsByUser.entrySet()) {
            User user = entry.getKey();
            if (user == null) {
                throw new EntityNotFoundException("Không tìm thấy thông tin người dùng");
            }

            Wallet wallet = user.getWallet();
            wallet.setBalance(wallet.getBalance() + prizePerParticipant);
            walletsToUpdate.add(wallet);

            Transaction transaction = new Transaction();
            transaction.setAmount(prizePerParticipant);
            transaction.setTransactionType(TransactionType.PRIZE);
            transaction.setWallet(wallet);
            transaction.setStatus(TransactionStatus.SUCCESS);
            transaction.setCreatedDate(LocalDateTime.now());
            transactionsToSave.add(transaction);

        }

        walletService.saveAllWallet(walletsToUpdate);
        transactionRepository.saveAll(transactionsToSave);

        return totalPrizeAmount;
    }

    public List<TransactionDTO> getTransactionsByWalletId(Long walletId) {
        List<Transaction> transactions = transactionRepository.findByWalletId(walletId);
        return transactions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setTransactionType(transaction.getTransactionType().toString());
        dto.setCreatedDate(transaction.getCreatedDate());
        dto.setStatus(transaction.getStatus().toString());

        return dto;
    }

}
