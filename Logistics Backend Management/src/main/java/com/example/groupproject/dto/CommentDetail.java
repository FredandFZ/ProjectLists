package com.example.groupproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDetail {
    private Long id;
    private String content;
    private String picture;
    private Short ifAnonymous;
    private Long ifAppended;
    private Long goodsId;
    private String GoodsName;
    private Double price;
    private Double discount;
    private String goodsType;
    private String goodsBrand;
    private String goodsPicture;
    private Long userId;
    private String username;
    private Long shopId;
    private String shopName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
