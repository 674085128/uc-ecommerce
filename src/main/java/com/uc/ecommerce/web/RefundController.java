package com.uc.ecommerce.web;

import com.uc.ecommerce.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    public record RefundRequest(String refundNo, String orderNo) {}

    /** 退款 */
    @PostMapping
    public String refund(@RequestBody RefundRequest request) {
        refundService.refund(request.refundNo(), request.orderNo());
        return "ok";
    }
}
