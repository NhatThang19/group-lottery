package vn.project.group_lottery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.project.group_lottery.model.Draw;

@Repository
public interface DrawRepository extends JpaRepository<Draw, Long> {
    Draw findTopByOrderByDrawDateDesc();
}
