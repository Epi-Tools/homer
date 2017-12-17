package com.epitools.homer.homer.repository;

import com.epitools.homer.homer.model.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetRepository extends JpaRepository<Bet, Integer> {

    List<Bet> findByProjectId(Integer id);

    List<Bet> findByUserId(Integer id);

}
