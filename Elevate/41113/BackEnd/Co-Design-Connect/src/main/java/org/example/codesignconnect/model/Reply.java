package org.example.codesignconnect.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reply {
    private Integer id;
    private Integer postId;
    private Integer authorId;
    private Integer channelId;
    private String content;
    private Timestamp createTime;
    private Timestamp updateTime;
}
