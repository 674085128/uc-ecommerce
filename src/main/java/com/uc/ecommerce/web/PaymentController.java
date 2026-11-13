package com.uc.ecommerce.web;

import com.uc.ecommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    public record PayCallbackRequest(String payNo, String orderNo) {}

    /** 模拟第三方支付回调（生产环境由支付渠道主动回调） */
    @PostMapping("/callback")
    public String callback(@RequestBody PayCallbackRequest request) {
        paymentService.handlePayCallback(request.payNo(), request.orderNo());
        return "ok";
    }
}
