package com.uc.ecommerce.repository;

import com.uc.ecommerce.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    /** 支付幂等：同一条支付单号是否已处理过 */
    boolean existsByPayNo(String payNo);
}
