package org.example.codesignconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.codesignconnect.model.Task;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDetail {
    private Integer id;
    private String code;
    private String content;
    private Short status;
    private Integer creatorId;
    private String creator;
    private Integer assigneeId;
    private String assignee;
    private List<SubTaskDetail> children;
    private String type;
    private Timestamp createTime;
    private Timestamp updateTime;

    public TaskDetail(List<SubTaskDetail> children, Task task, String creator, String assignee, String type){
        this.id = task.getId();
        this.code = task.getCode();
        this.content = task.getContent();
        this.status = task.getStatus();
        this.creatorId = task.getCreatorId();
        this.assigneeId = task.getAssigneeId();
        this.creator = creator;
        this.assignee = assignee;
        this.children = children;
        this.type = type;
        this.createTime = task.getCreateTime();
        this.updateTime = task.getUpdateTime();
    }
}
