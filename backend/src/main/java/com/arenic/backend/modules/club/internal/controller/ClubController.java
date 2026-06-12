package com.arenic.backend.modules.club.internal.controller;

import com.arenic.backend.modules.club.api.ClubService;
import com.arenic.backend.modules.club.internal.model.dto.ClubDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs")
public class ClubController {

    final ClubService clubService;

    @GetMapping("/search")
    public ResponseEntity<ClubDto.SearchResponse> search(@RequestParam(required = false) String query){
        return ResponseEntity.ok(clubService.search(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubDto.Detail> getById(@PathVariable UUID id){
        return clubService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{clubId}/slots")
    public ResponseEntity<List<ClubDto.SlotEntry>> getSlots(
            @PathVariable UUID clubId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(clubService.getSlots(clubId, date));
    }
}
