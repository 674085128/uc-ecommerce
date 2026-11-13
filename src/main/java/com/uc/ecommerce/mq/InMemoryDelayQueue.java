package com.uc.ecommerce.mq;

import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 内存延迟队列，替代 RocketMQ 的延迟消息。
 * 生产环境应替换为 RocketMQ：下单后投递延迟消息，超时触发关单。
 * demo 里用 ScheduledExecutorService 模拟，接口留好方便以后替换。
 */
@Component
public class InMemoryDelayQueue {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /** 投递一个延迟任务，delayMillis 毫秒后执行 */
    public void schedule(Runnable task, long delayMillis) {
        scheduler.schedule(task, delayMillis, TimeUnit.MILLISECONDS);
    }
}
