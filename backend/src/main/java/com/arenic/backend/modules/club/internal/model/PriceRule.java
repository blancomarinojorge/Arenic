package com.arenic.backend.modules.club.internal.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.*;

@Entity
@Table(name = "price_rules")
@Getter @Setter @NoArgsConstructor
public class PriceRule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "club_id")
    private UUID clubId;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "price_rule_courts", joinColumns = @JoinColumn(name = "price_rule_id"))
    @Column(name = "court_id")
    private Set<UUID> courtIds = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "price_rule_day", joinColumns = @JoinColumn(name = "price_rule_id"))
    @Column(name = "week_day_ordinal")
    private Set<Short> weekDays = new HashSet<>();

    @OneToMany(mappedBy = "priceRule", fetch = FetchType.EAGER)
    private List<PriceRuleInterval> intervals = new ArrayList<>();
}
