package com.arenic.backend.modules.club.internal.repository;

import com.arenic.backend.modules.club.internal.model.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClubRepository extends JpaRepository<Club, UUID> {
    List<Club> findByCreatorId(UUID creatorId);

    @Query(
    value = """
            select c.*
            from clubs c
            join locations l on c.location_id = l.id
            where c.is_active = true
            and (
                lower(l.city) like lower(concat('%',:query,'%'))
                OR lower(c.name) like lower(concat('%',:query,'%'))
                )
            """,
            nativeQuery = true
    )
    List<Club> searchByNameOrCity(@Param("query") String query);
}
