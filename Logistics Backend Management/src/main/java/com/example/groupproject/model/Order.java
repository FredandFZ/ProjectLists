package com.example.groupproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private Long goodsId;
    private Integer count;
    private Short ifDiscount;
    private Short payStatus;
    private Long userId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
