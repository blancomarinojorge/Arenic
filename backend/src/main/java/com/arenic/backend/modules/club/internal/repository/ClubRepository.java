package com.arenic.backend.modules.club.internal.repository;

import com.arenic.backend.modules.club.internal.model.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClubRepository extends JpaRepository<Club, UUID> {
    List<Club> findByCreatorId(UUID creatorId);
}
