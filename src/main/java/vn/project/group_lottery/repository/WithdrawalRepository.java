package vn.project.group_lottery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.project.group_lottery.model.Withdrawal;

@Repository
public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {

}
