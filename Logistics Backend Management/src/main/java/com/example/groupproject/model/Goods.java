package com.example.groupproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goods {
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
    private Short ifDelete;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
