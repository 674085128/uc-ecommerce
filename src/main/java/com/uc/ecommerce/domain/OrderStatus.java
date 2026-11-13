package com.uc.ecommerce.domain;

public enum OrderStatus {
    /** 待支付（下单后已预扣库存） */
    PENDING_PAYMENT,
    /** 已支付（确认扣减库存） */
    PAID,
    /** 已取消（超时关单，释放库存） */
    CANCELLED,
    /** 退款中 */
    REFUNDING,
    /** 已退款（回补库存） */
    REFUNDED
}
