package com.uc.ecommerce.service;

import com.uc.ecommerce.domain.Order;
import com.uc.ecommerce.domain.OrderStatus;
import com.uc.ecommerce.domain.Product;
import com.uc.ecommerce.exception.BusinessException;
import com.uc.ecommerce.mq.InMemoryDelayQueue;
import com.uc.ecommerce.repository.OrderRepository;
import com.uc.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final InventoryService inventoryService;
    private final InMemoryDelayQueue delayQueue;
    private final OrderTimeoutHandler orderTimeoutHandler;

    /** 订单超时时间（分钟），可在 application.yml 里配置 */
    @Value("${uc.order.timeout-minutes:30}")
    private long timeoutMinutes;

    /** 下单：预扣库存 → 创建订单 → 投递延迟关单任务 */
    @Transactional
    public Order createOrder(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException("商品不存在：" + productId));

        // 1. 预扣库存（防超卖）
        inventoryService.tryDeduct(productId, quantity);

        // 2. 创建订单（待支付）
        Order order = Order.builder()
                .orderNo(generateOrderNo())
                .productId(productId)
                .quantity(quantity)
                .amount(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .status(OrderStatus.PENDING_PAYMENT)
                .build();
        orderRepository.save(order);

        // 3. 投递延迟关单任务（模拟 RocketMQ 延迟消息）
        delayQueue.schedule(
                () -> orderTimeoutHandler.handleTimeout(order.getOrderNo()),
                timeoutMinutes * 60 * 1000
        );

        return order;
    }

    public Order getOrder(String orderNo) {
        return orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new BusinessException("订单不存在：" + orderNo));
    }

    private String generateOrderNo() {
        return "ORD" + System.currentTimeMillis()
                + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
