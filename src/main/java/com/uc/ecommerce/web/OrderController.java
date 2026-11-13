package com.uc.ecommerce.web;

import com.uc.ecommerce.domain.Order;
import com.uc.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    public record CreateOrderRequest(Long productId, Integer quantity) {}

    /** 下单 */
    @PostMapping
    public Order create(@RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request.productId(), request.quantity());
    }

    /** 查订单 */
    @GetMapping("/{orderNo}")
    public Order get(@PathVariable String orderNo) {
        return orderService.getOrder(orderNo);
    }
}
