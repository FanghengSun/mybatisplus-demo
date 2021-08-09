package com.mybatisplus.demo.crud.thread;

import com.mybatisplus.demo.crud.component.ApplicationContextUtils;
import com.mybatisplus.demo.crud.domain.Order;
import com.mybatisplus.demo.crud.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author Fangheng Sun on 2021/8/9
 */
@Slf4j
public class OrderThread implements Runnable {

    Logger logger = LoggerFactory.getLogger(OrderThread.class);
    CountDownLatch cdl;
    List<Order> orderList;

    public OrderThread(CountDownLatch cdl, List<Order> orderList) {
        this.cdl = cdl;
        this.orderList = orderList;
    }

    @Override
    public void run() {
        OrderService orderService = (OrderService) ApplicationContextUtils.getBean(OrderService.class);
        orderService.saveBatch(orderList);
        logger.info("插入成功，当前线程是:" + Thread.currentThread().getName());
        cdl.countDown();
    }
}
