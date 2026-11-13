package com.uc.ecommerce.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * 商品（库存以两阶段模型存放：可售 + 冻结）
 */
@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /** 可售库存 */
    @Column(nullable = false)
    private Integer availableStock;

    /** 冻结库存（下单预扣但未支付） */
    @Column(nullable = false)
    private Integer frozenStock;

    /** 乐观锁版本号，防超卖 */
    @Version
    private Long version;
}
