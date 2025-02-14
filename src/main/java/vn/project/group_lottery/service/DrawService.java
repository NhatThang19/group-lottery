package vn.project.group_lottery.service;

import org.springframework.stereotype.Service;

import vn.project.group_lottery.model.Draw;
import vn.project.group_lottery.repository.DrawRepository;

@Service
public class DrawService {
    private final DrawRepository drawRepository;

    public DrawService(DrawRepository drawRepository) {
        this.drawRepository = drawRepository;
    }

    public Draw findLatestDraw() {
        return drawRepository.findTopByOrderByDrawDateDesc();
    }
}
