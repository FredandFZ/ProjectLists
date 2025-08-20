package com.example.groupproject.service;

import com.example.groupproject.dto.OrderDetail;
import com.example.groupproject.model.Order;
import com.example.groupproject.model.PageBean;
import com.example.groupproject.model.Shop;

import java.util.List;

public interface OrderService {

    void addOrder(Order order);

    void deleteOrder(Long id);

    void updateOrder(Order order);

    PageBean listAllOrder(Integer page, Integer pageSize, Long userId);

    OrderDetail selectOrderById(Long id);

    List<OrderDetail> selectOrderByName(String name, Long userId);
}
