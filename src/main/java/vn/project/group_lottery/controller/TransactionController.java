package vn.project.group_lottery.controller;

import java.security.Principal;
import java.util.Arrays;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.project.group_lottery.dto.TicketDTO;
import vn.project.group_lottery.dto.WithdrawalDTO;
import vn.project.group_lottery.enums.BankName;
import vn.project.group_lottery.service.TransactionService;

@Controller
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/deposit")
    public String getDeposit(Model model) {
        return "client/transaction/deposit";
    }

    @PostMapping("/deposit")
    public String postDeposit(@RequestParam("totalAmount") Long amount, Principal principal,
            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/auth/login";
        }

        String username = principal.getName();

        try {
            transactionService.handleDeposit(username, amount);

            redirectAttributes.addFlashAttribute("toastMessage", "Nạp tiền thành công!");
            redirectAttributes.addFlashAttribute("toastHeading", "Thành công");
            redirectAttributes.addFlashAttribute("toastIcon", "success");
            redirectAttributes.addFlashAttribute("toastLoaderBg", "#31ce36");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toastMessage", "Lỗi khi nạp tiền: " + e.getMessage());
            redirectAttributes.addFlashAttribute("toastHeading", "Lỗi");
            redirectAttributes.addFlashAttribute("toastIcon", "error");
            redirectAttributes.addFlashAttribute("toastLoaderBg", "#f96868");
        }
        return "redirect:/deposit";
    }

    @GetMapping("/withdrawal")
    public String getWithdrawal(Model model) {
        model.addAttribute("withdrawalDTO", new WithdrawalDTO());
        model.addAttribute("bankNames", Arrays.asList(BankName.values()));

        return "client/transaction/withdrawal";
    }

    @PostMapping("/withdrawal")
    public String postWithdrawal(@ModelAttribute() WithdrawalDTO withdrawalDTO,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/auth/login";
        }

        String username = principal.getName();

        try {
            transactionService.handleWithdrawal(username, withdrawalDTO);

            redirectAttributes.addFlashAttribute("toastMessage", "Rút tiền thành công!");
            redirectAttributes.addFlashAttribute("toastHeading", "Thành công");
            redirectAttributes.addFlashAttribute("toastIcon", "success");
            redirectAttributes.addFlashAttribute("toastLoaderBg", "#31ce36");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toastMessage", "Lỗi khi rút tiền: " + e.getMessage());
            redirectAttributes.addFlashAttribute("toastHeading", "Lỗi");
            redirectAttributes.addFlashAttribute("toastIcon", "error");
            redirectAttributes.addFlashAttribute("toastLoaderBg", "#f96868");
        }

        return "redirect:/withdrawal";
    }

    @GetMapping("/bet")
    public String getBet(Model model) {
        model.addAttribute("ticketDTO", new TicketDTO());

        return "client/transaction/bet";
    }

    @PostMapping("/bet")
    public String postBet(@ModelAttribute TicketDTO ticketDTO,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/auth/login";
        }

        String username = principal.getName();

        try {
            transactionService.handelBetTransaction(ticketDTO, username);

            redirectAttributes.addFlashAttribute("toastMessage", "Mua vé thành công!");
            redirectAttributes.addFlashAttribute("toastHeading", "Thành công");
            redirectAttributes.addFlashAttribute("toastIcon", "success");
            redirectAttributes.addFlashAttribute("toastLoaderBg", "#31ce36");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("toastMessage", "Lỗi khi mua vé: " + e.getMessage());
            redirectAttributes.addFlashAttribute("toastHeading", "Lỗi");
            redirectAttributes.addFlashAttribute("toastIcon", "error");
            redirectAttributes.addFlashAttribute("toastLoaderBg", "#f96868");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("toastMessage", "Đã xảy ra lỗi khi mua vé. Vui lòng thử lại!");
            redirectAttributes.addFlashAttribute("toastHeading", "Lỗi");
            redirectAttributes.addFlashAttribute("toastIcon", "error");
            redirectAttributes.addFlashAttribute("toastLoaderBg", "#f96868");
        }

        return "redirect:/bet";
    }
}
