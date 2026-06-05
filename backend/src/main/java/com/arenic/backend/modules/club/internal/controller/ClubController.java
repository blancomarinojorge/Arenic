package com.arenic.backend.modules.club.internal.controller;

import com.arenic.backend.modules.club.api.ClubService;
import com.arenic.backend.modules.club.internal.model.dto.ClubDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs")
public class ClubController {

    final ClubService clubService;

    @GetMapping("/search")
    public ResponseEntity<ClubDto.SearchResponse> search(@RequestParam(required = false) String query){
        return ResponseEntity.ok(clubService.search(query));
    }
}
