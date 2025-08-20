package com.example.groupproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDetail {
    private Long id;
    private Long orderId;
    private String deliveryName;
    private String location;
    private Short status;
    private String deliveryPhoneNumber;
    private String goodsName;
    private String receiverName;
    private String receiverPhoneNumber;
    private String region;
    private String detailedAddress;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
