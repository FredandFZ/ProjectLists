package com.example.groupproject.service.impl;

import com.example.groupproject.dto.DeliveryDetail;
import com.example.groupproject.mapper.AddressMapper;
import com.example.groupproject.mapper.DeliveryMapper;
import com.example.groupproject.mapper.GoodsMapper;
import com.example.groupproject.mapper.OrderMapper;
import com.example.groupproject.model.Address;
import com.example.groupproject.model.Delivery;
import com.example.groupproject.model.Goods;
import com.example.groupproject.model.Order;
import com.example.groupproject.service.DeliverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class DeliverServiceImpl implements DeliverService {
    @Autowired
    private DeliveryMapper deliveryMapper;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private GoodsMapper goodsMapper;

    private DeliveryDetail generateDeliveryDetail(Delivery delivery){
        DeliveryDetail deliveryDetail = new DeliveryDetail();
        deliveryDetail.setId(delivery.getId());
        deliveryDetail.setOrderId(delivery.getOrderId());
        deliveryDetail.setDeliveryName(delivery.getDeliveryName());
        deliveryDetail.setLocation(delivery.getLocation());
        deliveryDetail.setStatus(delivery.getStatus());
        deliveryDetail.setDeliveryPhoneNumber(delivery.getDeliveryPhoneNumber());
        deliveryDetail.setCreateTime(delivery.getCreateTime());
        deliveryDetail.setUpdateTime(delivery.getUpdateTime());

        Order order = orderMapper.selectOrderById(delivery.getOrderId());
        Goods goods = goodsMapper.selectGoodsById(order.getGoodsId());
        deliveryDetail.setGoodsName(goods.getName());

        Address address = addressMapper.selectAddressById(delivery.getAddressId());
        deliveryDetail.setReceiverName(address.getName());
        deliveryDetail.setReceiverPhoneNumber(address.getPhoneNumber());
        deliveryDetail.setRegion(address.getRegion());
        deliveryDetail.setDetailedAddress(address.getDetailedAddress());

        return deliveryDetail;
    }

    @Override
    public void addDeliverAddress(Address address) {
        address.setCreateTime(LocalDateTime.now());
        address.setUpdateTime(LocalDateTime.now());
        address.setIfDelete((short)0);
        if(addressMapper.countAddress() == 0) address.setDeliveryStatus((short)0);
        else address.setDeliveryStatus((short)1);
        addressMapper.addAddress(address);
    }

    @Override
    public List<Address> listDeliverAddress(Long userId) {
        return addressMapper.selectAddressByUserId(userId);
    }

    @Override
    public void updateAddress(Address address) {
        address.setUpdateTime(LocalDateTime.now());
        addressMapper.updateAddress(address);
    }

    @Override
    public void deleteAddress(Long id) {
        addressMapper.deleteAddress(id);
    }

    @Override
    public void addDelivery(Delivery delivery) {
        delivery.setStatus((short)0);
        delivery.setCreateTime(LocalDateTime.now());
        delivery.setUpdateTime(LocalDateTime.now());
        deliveryMapper.addDelivery(delivery);
    }

    @Override
    public void updateDelivery(Delivery delivery) {
        delivery.setUpdateTime(LocalDateTime.now());
        deliveryMapper.updateDelivery(delivery);
    }

    @Override
    public void deleteDelivery(Long id) {
        deliveryMapper.deleteDelivery(id);
    }

    @Override
    public List<DeliveryDetail> selectDelivery(Long userId, String deliveryName, String goodsName, String receiverPhoneNumber) {
        List<Order> orderList = orderMapper.listAllOrder(userId);
        if(orderList.isEmpty()) return new LinkedList<>();
        List<Long> orderIdList1 = new LinkedList<>();
        for(Order order : orderList){
            orderIdList1.add(order.getId());
        }

        List<Long> orderIdList2 = new LinkedList<>();
        if(goodsName != null){
            List<Goods> goodsList = goodsMapper.selectGoodsByName(goodsName);
            if(goodsList.isEmpty()) return new LinkedList<>();
            List<Long> goodsIdList = new LinkedList<>();
            for(Goods goods : goodsList){
                goodsIdList.add(goods.getId());
            }
            orderList = orderMapper.SelectOrderByGoodsId(goodsIdList, userId);
            if(orderList.isEmpty()) return new LinkedList<>();
            for(Order order : orderList){
                orderIdList2.add(order.getId());
            }
        }

        List<Address> addressList = addressMapper.selectAddressByPhoneNumber(receiverPhoneNumber);
        if(addressList.isEmpty() && receiverPhoneNumber != null) return new LinkedList<>();
        List<Long> addressIdList = new LinkedList<>();
        for(Address address : addressList){
            addressIdList.add(address.getId());
        }

        List<Delivery> deliveryList = deliveryMapper.selectDelivery(orderIdList1, orderIdList2, addressIdList, deliveryName);
        List<DeliveryDetail> deliveryDetailList = new LinkedList<>();
        for(Delivery delivery : deliveryList){
            deliveryDetailList.add(generateDeliveryDetail(delivery));
        }
        return deliveryDetailList;
    }

    @Override
    public DeliveryDetail selectDeliveryById(Long id) {
        Delivery delivery = deliveryMapper.selectDeliveryById(id);
        return generateDeliveryDetail(delivery);
    }

    @Override
    public Address selectAddressById(Long id) {
        return deliveryMapper.selectAddressById(id);
    }
}
