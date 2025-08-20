package com.example.groupproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shop {
    private Long id;
    private Short shopType;
    private String name;
    private Long userId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
