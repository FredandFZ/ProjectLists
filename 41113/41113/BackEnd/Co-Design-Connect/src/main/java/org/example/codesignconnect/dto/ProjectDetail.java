package org.example.codesignconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.codesignconnect.model.Project;
import org.example.codesignconnect.model.User;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDetail {
    private Project project;
    private List<User> members;
    private String creatorName;
}
