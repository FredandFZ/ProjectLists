package com.example.groupproject.service;

import com.example.groupproject.dto.DeliveryDetail;
import com.example.groupproject.model.Address;
import com.example.groupproject.model.Delivery;

import java.util.List;

public interface DeliverService {
    void addDeliverAddress(Address address);

    List<Address> listDeliverAddress(Long id);

    void updateAddress(Address address);

    void deleteAddress(Long id);

    void addDelivery(Delivery delivery);

    void updateDelivery(Delivery delivery);

    void deleteDelivery(Long id);

    List<DeliveryDetail> selectDelivery(Long userId, String deliveryName, String goodsName, String receiverPhoneNumber);

    DeliveryDetail selectDeliveryById(Long id);

    Address selectAddressById(Long id);
}
