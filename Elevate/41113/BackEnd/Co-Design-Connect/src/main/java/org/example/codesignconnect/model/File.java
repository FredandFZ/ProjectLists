package org.example.codesignconnect.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class File {
    private Integer id;
    private Integer projectId;
    private Short projectStatus;
    private Short type;
    private Integer iterationId;
    private String name;
    private String source;
    private Integer creatorId;
    private Timestamp createTime;
    private Timestamp updateTime;
}
