package com.arenic.backend.modules.club.internal.repository;

import com.arenic.backend.modules.club.internal.model.PriceRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PriceRuleRepository extends JpaRepository<PriceRule, UUID> {
    List<PriceRule> findByClubId(UUID clubId);
}
