package com.isd.application.repository;

import com.isd.application.domain.PlacedBet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlacedBetRepository extends JpaRepository<PlacedBet, Integer> {

    PlacedBet save(PlacedBet toSave);
}