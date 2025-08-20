package com.example.groupproject.mapper;

import com.example.groupproject.model.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
    void addOrder (Order order);

    void deleteOrder (Long id);

    void updateOrder (Order order);

    List<Order> listAllOrder(Long userId);

    Order selectOrderById(Long id);

    List<Order> SelectOrderByGoodsId(List<Long> goodsIdList, Long userId);
}
