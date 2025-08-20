package org.example.codesignconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.codesignconnect.model.Task;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubTaskDetail {
    private Integer id;
    private String code;
    private String content;
    private Short status;
    private Integer creatorId;
    private String creator;
    private Integer assigneeId;
    private String assignee;
    private String type;
    private Timestamp createTime;
    private Timestamp updateTime;

    public SubTaskDetail(Task task, String creator, String assignee, String type){
        this.id = task.getId();
        this.code = task.getCode();
        this.content = task.getContent();
        this.status = task.getStatus();
        this.creatorId = task.getCreatorId();
        this.assigneeId = task.getAssigneeId();
        this.creator = creator;
        this.assignee = assignee;
        this.type = type;
        this.createTime = task.getCreateTime();
        this.updateTime = task.getUpdateTime();
    }
}
