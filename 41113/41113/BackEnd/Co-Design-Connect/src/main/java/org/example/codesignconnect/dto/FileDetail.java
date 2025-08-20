package org.example.codesignconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.codesignconnect.model.File;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDetail {
    private Integer id;
    private Integer projectId;
    private Short projectStatus;
    private Short type;
    private Integer iterationId;
    private String name;
    private String source;
    private Integer creatorId;
    private String creator;
    private Timestamp createTime;
    private Timestamp updateTime;

    public FileDetail(File file, String creator){
        this.id = file.getId();
        this.projectId = file.getProjectId();
        this.projectStatus = file.getProjectStatus();
        this.type = file.getType();
        this.iterationId = file.getIterationId();
        this.name = file.getName();
        this.source = file.getSource();
        this.creatorId = file.getCreatorId();
        this.creator = creator;
        this.createTime = file.getCreateTime();
        this.updateTime = file.getUpdateTime();
    }
}
