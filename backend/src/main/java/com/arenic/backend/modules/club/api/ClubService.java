package com.arenic.backend.modules.club.api;

import com.arenic.backend.modules.club.internal.model.Club;
import com.arenic.backend.modules.club.internal.model.dto.ClubDto;
import com.arenic.backend.modules.club.internal.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService{
    final ClubRepository clubRepository;

    @Transactional(readOnly = true)
    public ClubDto.SearchResponse search(String query){
        if (query == null || query.length() < 2){
            return new ClubDto.SearchResponse(List.of(), List.of());
        }

        /* 1. Get the clubs by name or city */
        List<Club> searchResults = clubRepository.searchByNameOrCity(query);

        /* 2. Get the cities data */
        List<ClubDto.CityResult> cityResults = searchResults.stream()
                .map(club -> club.getLocation().getCity())
                .distinct()
                .map(city -> {
                    return new ClubDto.CityResult(
                            city,
                            searchResults.stream()
                                    .filter(club -> club.getLocation().getCity().toLowerCase().equals(city.toLowerCase()))
                                    .count()
                    );
                })
                .toList();

        /* 3. Paste the clubs to the dto */
        List<ClubDto.Summary> summaries = searchResults.stream()
                .map(ClubDto.Summary::from)
                .toList();

        return new ClubDto.SearchResponse(cityResults, summaries);
    }
}
