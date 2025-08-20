package com.example.groupproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private Long id;
    private String name;
    private String phoneNumber;
    private String region;
    private String detailedAddress;
    private Short deliveryStatus;
    private Long userId;
    private Short ifDelete;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}