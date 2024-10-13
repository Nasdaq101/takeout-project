package com.sky.task;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * time-out order
     *
     * @return
     */
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder() {
        log.info("process time-out order: {}", LocalDateTime.now());
        LocalDateTime tm = LocalDateTime.now().plusMinutes(-15);
        List<Orders> odList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, tm);
        if (odList != null && odList.size() > 0) {
            odList.forEach(od -> {
                od.setStatus(Orders.CANCELLED);
                od.setCancelReason("time out, no payment!");
                od.setCancelTime(LocalDateTime.now());
                orderMapper.update(od);
            });
        }
    }

    /**
     * always on delivery
     *
     * @return
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder() {
        log.info("process delivery order");
        LocalDateTime tm = LocalDateTime.now().plusHours(-1);
        List<Orders> odList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, tm);
        if (odList != null && odList.size() > 0) {
            odList.forEach(od -> {
                od.setStatus(Orders.COMPLETED);
                od.setCancelReason("time out, no delivery confirmation!");
                od.setCancelTime(LocalDateTime.now());
                orderMapper.update(od);
            });
        }
    }
}