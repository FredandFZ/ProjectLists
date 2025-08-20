package org.example.codesignconnect.mapper;

import org.example.codesignconnect.model.ProjectMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Mapper interface for operations related to project members.
 */
@Mapper
public interface ProjectMemberMapper {
    List<Integer> findProjectIdsByUserId(@Param("userId") Integer userId);
    int insertProjectMember(ProjectMember projectMember);
    int removeProjectMember(@Param("projectId") Integer projectId, @Param("userId") Integer userId);
    ProjectMember findProjectMember(@Param("projectId") Integer projectId, @Param("userId") Integer userId);
    List<ProjectMember> findMembersByProjectId(@Param("projectId") Integer projectId);
    int deleteAllMembersByProjectId(@Param("projectId") Integer projectId);
    int countUserInProject(@Param("projectId") Integer projectId,
                           @Param("userId") Integer userId);
    int countMembers(@Param("projectId") Integer projectId);
}