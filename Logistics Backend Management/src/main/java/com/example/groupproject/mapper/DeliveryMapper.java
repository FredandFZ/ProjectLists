package com.example.groupproject.mapper;

import com.example.groupproject.dto.DeliveryDetail;
import com.example.groupproject.model.Address;
import com.example.groupproject.model.Delivery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DeliveryMapper {
    void addDelivery(Delivery delivery);

    void updateDelivery(Delivery delivery);

    void deleteDelivery(Long id);

    List<Delivery> selectDelivery(List<Long> orderIdList1, List<Long> orderIdList2, List<Long> addressIdList, String deliveryName);

    Delivery selectDeliveryById(Long id);

    Address selectAddressById(Long id);
}
