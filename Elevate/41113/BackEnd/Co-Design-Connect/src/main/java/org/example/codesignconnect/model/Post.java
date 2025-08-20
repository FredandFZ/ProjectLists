package org.example.codesignconnect.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private Integer id;
    private String title;
    private Integer channelId;
    private Integer authorId;
    private String content;
    private Timestamp createTime;
    private Timestamp updateTime;
}
