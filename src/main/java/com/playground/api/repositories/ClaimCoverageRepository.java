package com.playground.api.repositories;

import com.playground.api.entities.ClaimCoverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimCoverageRepository extends JpaRepository<ClaimCoverage, Integer> {
}
