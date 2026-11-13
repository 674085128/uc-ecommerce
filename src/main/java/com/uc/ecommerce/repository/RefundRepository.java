package com.uc.ecommerce.repository;

import com.uc.ecommerce.domain.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund, Long> {
    /** 退款幂等：同一条退款单号是否已处理过 */
    boolean existsByRefundNo(String refundNo);
}
