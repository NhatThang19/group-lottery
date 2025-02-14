package vn.project.group_lottery.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import vn.project.group_lottery.service.DrawService;

@Component
public class betScheduled {
    private final DrawService drawService;

    public betScheduled(DrawService drawService) {
        this.drawService = drawService;
    }

    // @Scheduled(cron = "0 0 18 * * SAT")
    // @Scheduled(fixedRate = 120000)
    public void schedulePower655Draw() {
        drawService.runPower655Draw();
    }
}
