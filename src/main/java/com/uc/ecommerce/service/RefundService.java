package com.uc.ecommerce.service;

import com.uc.ecommerce.domain.Order;
import com.uc.ecommerce.domain.OrderStatus;
import com.uc.ecommerce.domain.Refund;
import com.uc.ecommerce.exception.BusinessException;
import com.uc.ecommerce.repository.OrderRepository;
import com.uc.ecommerce.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundRepository refundRepository;
    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;

    /**
     * 退款。核心：幂等（refundNo 唯一）+ 回补库存。
     */
    @Transactional
    public void refund(String refundNo, String orderNo) {
        // 幂等：重复退款请求直接返回
        if (refundRepository.existsByRefundNo(refundNo)) {
            return;
        }

        Order order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new BusinessException("订单不存在：" + orderNo));
        if (order.getStatus() != OrderStatus.PAID) {
            throw new BusinessException("订单不是已支付状态，无法退款，当前：" + order.getStatus());
        }

        Refund refund = Refund.builder()
                .refundNo(refundNo)
                .orderNo(orderNo)
                .amount(order.getAmount())
                .status("SUCCESS")
                .build();
        refundRepository.save(refund);

        // 回补库存
        inventoryService.restore(order.getProductId(), order.getQuantity());

        // 订单置为已退款
        order.setStatus(OrderStatus.REFUNDED);
        orderRepository.save(order);
    }
}
