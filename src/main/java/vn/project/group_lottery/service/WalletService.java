package vn.project.group_lottery.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.project.group_lottery.model.Wallet;
import vn.project.group_lottery.repository.WalletRepository;

@Service
public class WalletService {
    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Optional<Wallet> getWalletById(Long id) {
        return this.walletRepository.findById(id);
    }

    public Wallet saveWallet(Wallet wallet) {
        return this.walletRepository.save(wallet);
    }
}
