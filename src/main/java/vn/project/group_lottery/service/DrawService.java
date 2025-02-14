package vn.project.group_lottery.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import vn.project.group_lottery.model.Draw;
import vn.project.group_lottery.model.Prize;
import vn.project.group_lottery.repository.DrawRepository;

@Service
public class DrawService {
    private final DrawRepository drawRepository;
    private final TransactionService transactionService;

    public DrawService(DrawRepository drawRepository, @Lazy TransactionService transactionService) {
        this.drawRepository = drawRepository;
        this.transactionService = transactionService;
    }

    public Draw findLatestDraw() {
        return drawRepository.findTopByOrderByDrawDateDesc();
    }

    public List<Draw> findSecondLatestDraw() {
        return drawRepository.findTop2ByOrderByDrawDateDesc();
    }

    public void runPower655Draw() {
        try {
            Optional<Draw> latestDrawOpt = Optional.ofNullable(this.findLatestDraw());

            LocalDateTime now = LocalDateTime.now();

            if (latestDrawOpt.isPresent()) {
                Draw latestDraw = latestDrawOpt.get();

                if (latestDraw.getDrawDate().isBefore(now)) {
                    latestDraw.setTotalPrize(transactionService.handelPrizeTransaction());
                    latestDraw.setWinningNumbers("1,2,3,4,5,6");
                    drawRepository.save(latestDraw);

                    createNewDraw("");
                }
            } else {
                createNewDraw("");
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi quay số", e);
        }
    }

    private Draw createNewDraw(String winningNumbers) {
        Draw newDraw = new Draw();
        if (winningNumbers != null) {
            newDraw.setWinningNumbers(winningNumbers);
        }
        newDraw.setDrawDate(LocalDateTime.now());

        Prize prize = new Prize();
        newDraw.setPrize(prize);

        drawRepository.save(newDraw);
        return newDraw;
    }

    private String generateWinningNumbers() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 55; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers, new Random());
        List<Integer> selectedNumbers = numbers.subList(0, 6);
        Collections.sort(selectedNumbers);

        return selectedNumbers.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

}
