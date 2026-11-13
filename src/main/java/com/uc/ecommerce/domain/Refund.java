package com.uc.ecommerce.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款记录。refundNo 唯一，是退款幂等的关键。
 */
@Entity
@Table(name = "refund")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 退款单号，唯一，幂等键 */
    @Column(nullable = false, unique = true)
    private String refundNo;

    @Column(nullable = false)
    private String orderNo;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String status;

    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
