package org.example.codesignconnect.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.sql.Timestamp;

@Data
public class Task {
    private Integer id;
    private Integer taskId;
    private Integer projectId;
    private Integer iterationId;
    private String code;
    private String content;
    private Short status;
    private Short projectStatus;
    private Integer creatorId;
    private Integer assigneeId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp updateTime;
}
