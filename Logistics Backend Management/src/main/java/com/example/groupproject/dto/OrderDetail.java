package com.example.groupproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    private Long id;
    private Long goodsId;
    private String goodsName;
    private Double price;
    private String goodsType;
    private String goodsBrand;
    private String picture;
    private Integer count;
    private Short ifDiscount;
    private Short payStatus;
    private Long userId;
    private String phoneNumber;
    private Long shopId;
    private String shopName;
    private Short shopType;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
