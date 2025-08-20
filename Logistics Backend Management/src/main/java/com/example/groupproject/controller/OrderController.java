package com.example.groupproject.controller;

import com.example.groupproject.dto.OrderDetail;
import com.example.groupproject.model.*;
import com.example.groupproject.service.GoodsService;
import com.example.groupproject.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;


    //添加订单
    @PostMapping("/order")
    public Result addOrder(@RequestBody Order order){
        log.info("添加订单：{}", order);
        orderService.addOrder(order);
        return Result.success();
    }
    //删除订单
    @DeleteMapping("/order")
    public Result deleteOrder(Long id){
        log.info("删除订单:{}", id);
        orderService.deleteOrder(id);
        return Result.success();
    }

    //修改订单信息
    @PutMapping("/order")
    public Result updateShop(Order order){
        log.info("修改订单信息：{}", order);
        orderService.updateOrder(order);
        return Result.success();
    }

    //显示所有订单
    @GetMapping("/order/list")
    public Result listAllOrder(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "20") Integer pageSize, Long userId){
        log.info("显示所有订单");
        PageBean pageBean = orderService.listAllOrder(page, pageSize,userId);
        return !pageBean.getRows().isEmpty() ? Result.success(pageBean) : Result.error("暂无订单信息");
    }
    //Id查询订单
    @GetMapping("/order/{id}")
    public Result selectOrderById(@PathVariable Long id){
        log.info("ID查询订单：{}", id);
        OrderDetail orderDetail = orderService.selectOrderById(id);
        return orderDetail != null ? Result.success(orderDetail) : Result.error("无ID为" + id + "的订单");
    }
    //通过商品名字查询订单
    @GetMapping("/order")
    public Result SelectOrderByName(String name, Long userId){
        log.info("查询订单:{}, {}",name, userId);
        List<OrderDetail> orderDetailList=orderService.selectOrderByName(name, userId);
        return !orderDetailList.isEmpty() ? Result.success(orderDetailList) : Result.error("无名字为" + name + "的订单");
    }

}
