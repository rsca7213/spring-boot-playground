package com.playground.api.repositories;

import com.playground.api.entities.Coverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoverageRepository extends JpaRepository<Coverage, Integer> {
}
