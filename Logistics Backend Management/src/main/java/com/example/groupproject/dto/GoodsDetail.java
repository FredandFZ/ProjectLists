package com.example.groupproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsDetail {
    private Long id;
    private String name;
    private Double price;
    private Double discount;
    private String goodsType;
    private String goodsBrand;
    private Integer collage;
    private String picture;
    private Short ifCheck;
    private Long shopId;
    private String shopName;
    private Short shopType;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
