package com.isd.application.repository;

import com.isd.application.domain.PlacedBet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlacedBetRepository extends JpaRepository<PlacedBet, Integer> {
    PlacedBet findOneById(Integer id);
    List<PlacedBet> findAllByUserId(Integer userId);
    PlacedBet save(PlacedBet toSave);
}