package com.arenic.backend.modules.club.internal.repository;

import com.arenic.backend.modules.club.internal.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {
}
