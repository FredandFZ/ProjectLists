package com.example.groupproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private Long id;
    private Long userId;
    private Long goodsId;
    private String content;
    private String picture;
    private Short ifAnonymous;
    private Long ifAppended;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
