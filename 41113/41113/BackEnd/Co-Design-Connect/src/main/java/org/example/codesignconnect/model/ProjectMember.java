package org.example.codesignconnect.model;

import java.time.LocalDateTime;

/**
 * Entity class representing a project member.
 */
public class ProjectMember {

    private Integer id; // Primary key ID
    private Integer projectId; // Associated project ID
    private Integer userId; // Associated user ID
    private String role; // Role in the project (e.g., OWNER, MEMBER)
    private LocalDateTime joinedTime; // The time when the user joined the project

    // Constructors
    public ProjectMember() {}

    public ProjectMember(Integer id, Integer projectId, Integer userId, String role, LocalDateTime joinedTime) {
        this.id = id;
        this.projectId = projectId;
        this.userId = userId;
        this.role = role;
        this.joinedTime = joinedTime;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getJoinedTime() {
        return joinedTime;
    }

    public void setJoinedTime(LocalDateTime joinedTime) {
        this.joinedTime = joinedTime;
    }

    @Override
    public String toString() {
        return "ProjectMember{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", userId=" + userId +
                ", role='" + role + '\'' +
                ", joinedTime=" + joinedTime +
                '}';
    }
}