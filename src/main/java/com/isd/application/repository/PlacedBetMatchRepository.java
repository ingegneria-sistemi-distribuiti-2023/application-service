package com.isd.application.repository;

import com.isd.application.domain.PlacedBetMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlacedBetMatchRepository extends JpaRepository<PlacedBetMatch, Integer> {

    PlacedBetMatch save(PlacedBetMatch toSave);
}
