package com.arenic.backend.modules.club.internal.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.UUID;

@Entity
@Table(name = "price_rule_intervals")
@Getter @Setter @NoArgsConstructor
public class PriceRuleInterval {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "price_rule_id")
    private PriceRule priceRule;

    @Column(name = "interval_minutes")
    private short intervalMinutes;

    @Column(name = "total_price", precision = 12, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "currency", length = 3)
    @JdbcTypeCode(Types.CHAR)
    private String currency;

    @Column(name = "member_discount_percent")
    private short memberDiscountPercent;

    @Column(name = "game_mode")
    private String gameMode;
}
