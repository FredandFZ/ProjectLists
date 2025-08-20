package com.example.groupproject.controller;

import com.example.groupproject.dto.DeliveryDetail;
import com.example.groupproject.model.Address;
import com.example.groupproject.model.Delivery;
import com.example.groupproject.model.Result;
import com.example.groupproject.service.DeliverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
public class DeliverController {
    @Autowired
    private DeliverService deliverService;

    //添加送货地址
    @PostMapping("/address")
    public Result addDeliverAddress(@RequestBody Address address){
        log.info("添加送货地址:{}", address);
        deliverService.addDeliverAddress(address);
        return Result.success();
    }

    //显示送货地址列表
    @GetMapping("/address")
    public Result listDeliverAddress(Long userId){
        log.info("送货地址:{}", userId);
        List<Address> addressList = deliverService.listDeliverAddress(userId);
        return !addressList.isEmpty() ? Result.success(addressList) : Result.error("暂无送货地址");
    }

    //更改地址信息
    @PutMapping("/address")
    public Result updateAddress(@RequestBody Address address){
        log.info("更改地址信息:{}", address);
        deliverService.updateAddress(address);
        return Result.success();
    }

    //删除地址信息
    @DeleteMapping("/address")
    public Result deleteAddress(Long id){
        log.info("删除地址信息:{}", id);
        deliverService.deleteAddress(id);
        return Result.success();
    }

    //添加物流信息
    @PostMapping("/delivery")
    public Result addDelivery(@RequestBody Delivery delivery){
        log.info("添加物流信息:{}", delivery);
        deliverService.addDelivery(delivery);
        return Result.success();
    }

    //修改物流状态
    @PutMapping("/delivery")
    public Result updateDelivery(@RequestBody Delivery delivery){
        log.info("修改物流状态:{}", delivery);
        deliverService.updateDelivery(delivery);
        return Result.success();
    }

    //删除物流信息
    @DeleteMapping("/delivery")
    public Result deleteDelivery(Long id){
        log.info("删除物流信息:{}", id);
        deliverService.deleteDelivery(id);
        return Result.success();
    }

    //显示所有物流信息
    @GetMapping("/delivery/list")
    public Result listDelivery(Long userId){
        log.info("显示所有物流信息:{}", userId);
        List<DeliveryDetail> deliveryDetailList = deliverService.selectDelivery(userId, null, null, null);
        return !deliveryDetailList.isEmpty() ? Result.success(deliveryDetailList) : Result.error("暂无物流信息");
    }

    //查询物流信息
    @GetMapping("/delivery")
    public Result selectDeliveryByDeliveryName(Long userId, String deliveryName, String goodsName, String receiverPhoneNumber){
        log.info("查询物流信息:{}, {}, {}, {}", userId, deliveryName, goodsName, receiverPhoneNumber);
        List<DeliveryDetail> deliveryDetailList = deliverService.selectDelivery(userId, deliveryName, goodsName, receiverPhoneNumber);
        return !deliveryDetailList.isEmpty() ? Result.success(deliveryDetailList) : Result.error("暂无物流信息");
    }

    //ID查询物流
    @GetMapping("/delivery/{id}")
    public Result selectDeliveryById(@PathVariable Long id){
        log.info("ID查询物流:{}", id);
        DeliveryDetail deliveryDetail = deliverService.selectDeliveryById(id);
        return deliveryDetail != null ? Result.success(deliveryDetail) : Result.error("无查询结果");
    }

    //ID查询地址
    @GetMapping("/address/{id}")
    public Result selectAddressById(@PathVariable Long id){
        log.info("ID查询地址:{}", id);
        Address address = deliverService.selectAddressById(id);
        return address != null ? Result.success(address) : Result.error("无查询结果");
    }
}
