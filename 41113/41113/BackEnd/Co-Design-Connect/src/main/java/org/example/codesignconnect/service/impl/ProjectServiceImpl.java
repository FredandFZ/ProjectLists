package org.example.codesignconnect.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.codesignconnect.dto.ProjectDetail;
import org.example.codesignconnect.mapper.ChannelMapper;
import org.example.codesignconnect.mapper.ProjectMapper;
import org.example.codesignconnect.mapper.ProjectMemberMapper;
import org.example.codesignconnect.mapper.UserMapper;
import org.example.codesignconnect.model.*;
import org.example.codesignconnect.service.ProjectService;
import org.example.codesignconnect.utils.AliyunOSSOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ProjectMemberMapper projectMemberMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ChannelMapper channelMapper;

    @Override
    public PageResult<Project> listAllProjects(Integer page, Integer size, Integer searchType, String searchValue) {
        if (page == null || page <= 0) page = 1;
        if (size == null || size <= 0) size = 10;
        int offset = (page - 1) * size;

        if (searchValue != null && !searchValue.trim().isEmpty()) {
            searchValue = "%" + searchValue.trim().toLowerCase() + "%";
        } else {
            searchType = null;
            searchValue = null;
        }

        List<Project> projects = projectMapper.listProjectsBySearch(offset, size, searchType, searchValue);
        Long count = projectMapper.countProjectsBySearch(searchType, searchValue);

        return new PageResult<>(count, projects);
    }

    @Override
    public List<Project> getProjectsByUserId(Integer userId, Integer searchType, String searchValue) {
        if (searchValue != null && !searchValue.trim().isEmpty()) {
            searchValue = "%" + searchValue.toLowerCase() + "%";
        }
        return projectMapper.getMyProjectsBySearch(userId, searchType, searchValue);
    }


    @Override
    public int add(Project project) {
        return projectMapper.insertProject(project);
    }

    @Override
    public Project getProjectById(Integer id) {
        return projectMapper.getProjectById(id);
    }

    @Override
    public int update(Project project) {
        return projectMapper.updateProject(project);
    }

    @Override
    public int delete(Integer id) {
        return projectMapper.deleteProject(id);
    }

    @Override
    public List<Project> searchProjects(String name, String category, Integer creatorId, Integer status) {
        return projectMapper.searchProjects(name, category, creatorId, status);
    }

    @Override
    public List<Project> getProjectsByUserId(Integer userId) {
        return projectMapper.findProjectsByUserId(userId);
    }

    @Override
    public boolean addMemberToProject(Integer projectId, Integer userId) {
        // Check if the user is already a member
        ProjectMember existing = projectMemberMapper.findProjectMember(projectId, userId);
        if (existing != null) {
            // Already a member, do not add again
            return false;
        }

        // Create a new ProjectMember
        ProjectMember projectMember = new ProjectMember();
        projectMember.setProjectId(projectId);
        projectMember.setUserId(userId);
        projectMember.setRole("MEMBER"); // Default role
        projectMember.setJoinedTime(LocalDateTime.now());

        int rows = projectMemberMapper.insertProjectMember(projectMember);
        return rows > 0;
    }

    @Override
    public boolean exitProject(Integer projectId, Integer userId) {
        int rows = projectMemberMapper.removeProjectMember(projectId, userId);
        return rows > 0;
    }

    @Override
    public boolean dismissProject(Integer projectId) {
        int deleted = projectMapper.deleteProjectById(projectId);
        return deleted > 0;
    }

    @Override
    public List<User> listMembersByProjectId(Integer projectId) {
        List<ProjectMember> projectMembers = projectMemberMapper.findMembersByProjectId(projectId);
        List<User> members = new ArrayList<>();
        for(ProjectMember projectMember : projectMembers){
            User user = userMapper.getUserById(projectMember.getUserId());
            members.add(user);
        }
        return members;
    }

    @Override
    public Project createProject(Project project) {
        int rows = projectMapper.insertProject(project);
        if (rows <= 0 || project.getId() == null) {
            throw new RuntimeException("Failed to create project.");
        }

        // Insert into project_member table, role=OWNER
        ProjectMember projectMember = new ProjectMember();
        projectMember.setProjectId(project.getId());
        projectMember.setUserId(project.getCreatorId());
        projectMember.setRole("OWNER");
        projectMember.setJoinedTime(LocalDateTime.now());

        int memberRows = projectMemberMapper.insertProjectMember(projectMember);
        if (memberRows <= 0) {
            throw new RuntimeException("Failed to assign creator as project owner.");
        }

        Channel channel = new Channel();
        channel.setProjectId(project.getId());
        channel.setTitle(project.getName());
        channelMapper.insertChannel(channel);

        return project;
    }

    @Override
    public boolean deleteProject(Integer projectId, Integer userId) {
        // Check if the user is the OWNER
        ProjectMember projectMember = projectMemberMapper.findProjectMember(projectId, userId);
        if (projectMember == null || !"OWNER".equalsIgnoreCase(projectMember.getRole())) {
            throw new RuntimeException("Only the project owner can delete the project.");
        }

        // Step 1: Delete all members first
        projectMemberMapper.deleteAllMembersByProjectId(projectId);

        // Step 2: Delete the project itself
        int rows = projectMapper.deleteProject(projectId);

        return rows > 0;
    }

    @Override
    public boolean removeMemberFromProject(Integer projectId, Integer userId) {
        int rows = projectMapper.deleteMemberFromProject(projectId, userId);
        return rows > 0;
    }

    @Override
    public boolean isUserMemberOfProject(Integer projectId, Integer userId) {
        return projectMemberMapper.countUserInProject(projectId, userId) > 0;
    }

    @Override
    public int getMemberCount(Integer projectId) {
        return projectMemberMapper.countMembers(projectId);
    }

    @Override
    public List<Project> searchProjectByName(String name) {
        return projectMapper.searchByName("%" + name.toLowerCase() + "%");
    }

    @Override
    public int getProjectStatus(Integer projectId) {
        return projectMapper.getProjectStatus(projectId);
    }

    @Override
    public int updateProjectStatus(Integer projectId, int status) {
        return projectMapper.updateProjectStatus(projectId, status);
    }

    @Override
    public List<ProjectDetail> getAllProjects() {
        List<Project> projectList = projectMapper.getAllProjects();
        List<ProjectDetail> projectDetailList = new ArrayList<>();
        for (Project project : projectList) {
            projectDetailList.add(new ProjectDetail(project, null, userMapper.getUsernameById(project.getCreatorId())));
        }
        return projectDetailList;
    }
}
