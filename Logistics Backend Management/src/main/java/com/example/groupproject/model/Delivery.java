package com.example.groupproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {
    private Long id;
    private Long orderId;
    private Long addressId;
    private String deliveryName;
    private String location;
    private String deliveryPhoneNumber;
    private Short status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
