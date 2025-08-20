package org.example.codesignconnect.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Channel {
    private Integer id;
    private Integer projectId;
    private String title;
    private String description;
    private Integer totalPosts;
    private Timestamp lastPostTime;
    private Timestamp createTime;
    private Timestamp updateTime;
}
