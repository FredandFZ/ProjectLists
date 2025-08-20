package org.example.codesignconnect.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.codesignconnect.model.Project;

import java.util.List;

@Mapper
public interface ProjectMapper {

    List<Project> listProjectsBySearch(@Param("offset") int offset,
                                       @Param("size") int size,
                                       @Param("searchType") Integer searchType,
                                       @Param("searchValue") String searchValue);

    Long countProjectsBySearch(@Param("searchType") Integer searchType,
                               @Param("searchValue") String searchValue);

    List<Project> getMyProjectsBySearch(@Param("userId") Integer userId,
                                        @Param("searchType") Integer searchType,
                                        @Param("searchValue") String searchValue);


    Project getProjectById(Integer id);

    List<Project> findProjectsByIds(@Param("projectIds") List<Integer> projectIds);

    int insertProject(Project project);

    int updateProject(Project project);

    int deleteProject(Integer id);

    List<Project> searchProjects(@Param("name") String name,
                                 @Param("category") String category,
                                 @Param("creatorId") Integer creatorId,
                                 @Param("status") Integer status);

    List<Project> findProjectsByUserId(Integer userId);

    List<Project> searchByName(@Param("name") String name);

    int deleteMemberFromProject(@Param("projectId") Integer projectId, @Param("userId") Integer userId);

    int deleteProjectById(Integer projectId);

    int getProjectStatus(@Param("projectId") Integer projectId);

    int updateProjectStatus(@Param("projectId") Integer projectId, @Param("status") int status);

    List<Project> getAllProjects();
}