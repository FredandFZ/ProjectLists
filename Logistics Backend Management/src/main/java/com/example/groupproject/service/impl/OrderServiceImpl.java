package com.example.groupproject.service.impl;

import com.example.groupproject.dto.OrderDetail;
import com.example.groupproject.mapper.GoodsMapper;
import com.example.groupproject.mapper.OrderMapper;
import com.example.groupproject.mapper.ShopMapper;
import com.example.groupproject.mapper.UserMapper;
import com.example.groupproject.model.*;
import com.example.groupproject.service.OrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ShopMapper shopMapper;

    private OrderDetail generateOrderDetail(Order order){
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(order.getId());
        orderDetail.setGoodsId(order.getGoodsId());
        orderDetail.setCount(order.getCount());
        orderDetail.setIfDiscount(order.getIfDiscount());
        orderDetail.setPayStatus(order.getPayStatus());
        orderDetail.setUserId(order.getUserId());
        orderDetail.setCreateTime(order.getCreateTime());
        orderDetail.setUpdateTime(order.getUpdateTime());

        Goods goods = goodsMapper.selectGoodsById(order.getGoodsId());
        orderDetail.setGoodsName(goods.getName());
        orderDetail.setPrice(goods.getPrice());
        orderDetail.setGoodsType(goods.getGoodsType());
        orderDetail.setGoodsBrand(goods.getGoodsBrand());
        orderDetail.setPicture(goods.getPicture());

        Shop shop = shopMapper.selectShopById(goods.getShopId());
        orderDetail.setShopId(shop.getId());
        orderDetail.setShopName(shop.getName());
        orderDetail.setShopType(shop.getShopType());

        User user = userMapper.selectUserById(order.getUserId());
        orderDetail.setPhoneNumber(user.getPhoneNumber());
        return orderDetail;
    }

    @Override
    public void addOrder(Order order) {
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.addOrder(order);
    }

    @Override
    public void deleteOrder(Long id) {
        orderMapper.deleteOrder(id);
    }

    @Override
    public void updateOrder(Order order) {
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateOrder(order);
    }

    @Override
    public PageBean listAllOrder(Integer page, Integer pageSize, Long userId) {
        PageHelper.startPage(page, pageSize);
        List<Order> orderList = orderMapper.listAllOrder(userId);
        Page<Order> p = (Page<Order>) orderList;
        return new PageBean(p.getTotal(), p.getResult());
    }

    @Override
    public OrderDetail selectOrderById(Long id) {
        Order order = orderMapper.selectOrderById(id);
        return generateOrderDetail(order);
    }

    @Override
    public List<OrderDetail> selectOrderByName(String name, Long userId) {
        List<Goods> goodsList = goodsMapper.selectGoods(name,null,null,null,null);
        if(goodsList.isEmpty()) return new LinkedList<>();
        List<Long> goodsIdList = new LinkedList<>();
        for(Goods goods : goodsList){
            goodsIdList.add(goods.getId());
        }
        List<Order> orderList = orderMapper.SelectOrderByGoodsId(goodsIdList, userId);
        List<OrderDetail> orderDetailList = new LinkedList<>();
        for(Order order : orderList){
            orderDetailList.add(generateOrderDetail(order));
        }
        return orderDetailList;
    }
}
