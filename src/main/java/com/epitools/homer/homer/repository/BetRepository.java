package com.epitools.homer.homer.repository;

import com.epitools.homer.homer.model.Bet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BetRepository extends JpaRepository<Bet, Integer> {

    List<Bet> findByProjectId(Integer id);

    List<Bet> findByUserId(Integer id);

}
