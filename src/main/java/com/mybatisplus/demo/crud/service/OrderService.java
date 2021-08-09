package com.mybatisplus.demo.crud.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mybatisplus.demo.crud.domain.Order;
import com.mybatisplus.demo.crud.mapper.OrderMapper;
import com.mybatisplus.demo.crud.thread.OrderThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @author Fangheng Sun on 2021/8/9
 */
@Service
@Slf4j
public class OrderService extends ServiceImpl<OrderMapper, Order> {
    private static Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderMapper orderMapper;

    private CountDownLatch threadsSignal;
    //每个线程处理的数据量
    private static final int count = 1000;
    //定义线程池数量为8,每个线程处理1000条数据
    private static ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public Order getOrderById(Long id) {
        return this.getById(id);
    }

    @Transactional
    public void saveData() {
        FutureTask<Void> futureTask = new FutureTask<>(() -> {
            for (int i = 0; i < 10; i++) {
                Order order = new Order();
                order.setOrderOID(UUID.randomUUID().toString());
                orderMapper.insert(order);
            }
            return null;
        });
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(futureTask);
        executor.shutdown();
    }

    public String batchSaveData() {
        List<Order> orderList = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            orderList.add(Order.builder().orderOID(UUID.randomUUID().toString()).build());
        }
        long start = System.currentTimeMillis();
        long end;
        logger.info("线程开始");
        if (orderList.size() < count) {
            threadsSignal = new CountDownLatch(1);
            pool.submit(new OrderThread(threadsSignal, orderList));
        } else {
            List<List<Order>> lists = ListUtils.partition(orderList, count);
            threadsSignal = new CountDownLatch(lists.size());
            for (List<Order> orders : lists) {
                pool.submit(new OrderThread(threadsSignal, orders));
            }
        }

        try {
            threadsSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pool.shutdown();
        end = System.currentTimeMillis();
        System.out.println("time:" + (end - start) + "ms");
        return "result";

    }
}
