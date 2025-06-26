package com.playground.api.repositories;

import com.playground.api.entities.PolicyCoverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolicyCoverageRepository extends JpaRepository<PolicyCoverage, Integer> {
    List<PolicyCoverage> findByPolicyId(Integer policyId);
}
