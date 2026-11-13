package com.uc.ecommerce.service;

import com.uc.ecommerce.domain.Order;
import com.uc.ecommerce.domain.OrderStatus;
import com.uc.ecommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 超时关单处理器。
 * 独立成一个 bean，避免在 OrderService 内部自调用导致 @Transactional 事务失效的坑。
 */
@Service
@RequiredArgsConstructor
public class OrderTimeoutHandler {

    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;

    @Transactional
    public void handleTimeout(String orderNo) {
        orderRepository.findByOrderNo(orderNo).ifPresent(order -> {
            if (order.getStatus() == OrderStatus.PENDING_PAYMENT) {
                order.setStatus(OrderStatus.CANCELLED);
                orderRepository.save(order);
                // 释放预扣的库存
                inventoryService.releaseDeduct(order.getProductId(), order.getQuantity());
            }
            // 已支付 / 已取消 / 已退款：不处理
        });
    }
}
