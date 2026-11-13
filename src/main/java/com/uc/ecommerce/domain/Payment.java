package com.uc.ecommerce.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付记录。payNo 唯一，是支付回调幂等的关键。
 */
@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 支付单号，唯一，幂等键 */
    @Column(nullable = false, unique = true)
    private String payNo;

    @Column(nullable = false)
    private String orderNo;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    /** SUCCESS / FAILED */
    @Column(nullable = false)
    private String status;

    private LocalDateTime paidAt;
}
