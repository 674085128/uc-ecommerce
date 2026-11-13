package com.uc.ecommerce.service;

import com.uc.ecommerce.domain.Order;
import com.uc.ecommerce.domain.OrderStatus;
import com.uc.ecommerce.domain.Payment;
import com.uc.ecommerce.exception.BusinessException;
import com.uc.ecommerce.repository.OrderRepository;
import com.uc.ecommerce.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;

    /**
     * 处理支付回调。核心：幂等（payNo 唯一）+ 确认扣库存。
     */
    @Transactional
    public void handlePayCallback(String payNo, String orderNo) {
        // 幂等：重复回调直接返回，不重复处理
        if (paymentRepository.existsByPayNo(payNo)) {
            return;
        }

        Order order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new BusinessException("订单不存在：" + orderNo));
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new BusinessException("订单状态异常，无法支付，当前：" + order.getStatus());
        }

        // 记录支付（成功）
        Payment payment = Payment.builder()
                .payNo(payNo)
                .orderNo(orderNo)
                .amount(order.getAmount())
                .status("SUCCESS")
                .paidAt(LocalDateTime.now())
                .build();
        paymentRepository.save(payment);

        // 确认扣库存：冻结转实扣
        inventoryService.confirmDeduct(order.getProductId(), order.getQuantity());

        // 订单置为已支付
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
    }
}
