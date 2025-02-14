package vn.project.group_lottery.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.project.group_lottery.dto.TicketDTO;
import vn.project.group_lottery.dto.WithdrawalDTO;
import vn.project.group_lottery.enums.BankName;
import vn.project.group_lottery.enums.TicketStatus;
import vn.project.group_lottery.enums.TransactionStatus;
import vn.project.group_lottery.enums.TransactionType;
import vn.project.group_lottery.model.Draw;
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
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setWallet(user.getWallet());
        transaction.setTicket(ticket);

        this.saveTransaction(transaction);
    }

}
