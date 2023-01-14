package com.isd.application.repository;

import com.isd.application.domain.PlacedBet;
import com.isd.application.domain.PlacedBetMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlacedBetRepository extends JpaRepository<PlacedBet, Integer> {

    PlacedBetMatch findAllByUserId(Integer userId);
    PlacedBet save(PlacedBet toSave);
}