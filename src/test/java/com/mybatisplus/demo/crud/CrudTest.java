package com.mybatisplus.demo.crud;

import com.mybatisplus.demo.crud.domain.Order;
import com.mybatisplus.demo.crud.mapper.OrderMapper;
import com.mybatisplus.demo.crud.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Fangheng Sun on 2021/8/9
 */
@SpringBootTest
public class CrudTest {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderService orderService;

    @Test
    public void test() {
        List<Order> orderList = orderMapper.selectList(null);
        assertThat(orderList.size(), equalTo(0));
    }

    @Test
    public void insertTest() {
        Order order = new Order();
        order.setOrderOID(UUID.randomUUID().toString());
        orderMapper.insert(order);
    }

    @Test
    public void getTest() {
        Order order = orderService.getById(1424578400657281025L);
        System.out.println(order);
    }

    @Test
    public void batchSaveTest() {
        String res = orderService.batchSaveData();
        assertThat(res, equalTo("result"));
    }
}
