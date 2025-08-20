package org.example.codesignconnect;

import org.example.codesignconnect.mapper.UserMapper;
import org.example.codesignconnect.model.PageResult;
import org.example.codesignconnect.model.User;
import org.example.codesignconnect.model.Project;
import org.example.codesignconnect.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CoDesignConnectApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void setupTest(){
        List<User> userList = userMapper.findAllUsers();
        userList.forEach(System.out::println);
    }
    @Autowired
    private ProjectService projectService;

    @Test
    public void testAddProject() {
        Project project = new Project();
        project.setName("测试项目");
        project.setDescription("这是一个测试插入的项目");
        project.setCategory("测试");
        project.setCreatorId(1);
        project.setStatus(0);
        project.setImageUrl("");
        project.setChannelId(1);
        project.setTags("单元测试");

        int rows = projectService.add(project);
        System.out.println("影响行数: " + rows);
        assert rows > 0;
    }

    @Test
    public void testListAllProjectsWithPaginationAndSearch() {
        int page = 1;
        int size = 10;
        Integer searchType = 0;
        String searchValue = "测试";

        PageResult<Project> result = projectService.listAllProjects(page, size, searchType, searchValue);

        assert result != null;
        System.out.println("Total projects: " + result.getTotal());
        System.out.println("Projects returned:");
        result.getRecords().forEach(System.out::println);

        if (result.getRecords() != null) {
            for (Project p : result.getRecords()) {
                assert p.getName().toLowerCase().contains(searchValue.toLowerCase())
                        || p.getCategory().toLowerCase().contains(searchValue.toLowerCase())
                        || (p.getTags() != null && p.getTags().toLowerCase().contains(searchValue.toLowerCase()));
            }
        }
    }

    @Test
    public void testUpdateProject() {
        Project project = new Project();
        project.setId(1);
        project.setName("更新后的项目名称");
        project.setDescription("修改后的描述");
        project.setCategory("改后的分类");
        project.setImageUrl("https://example.com/image.jpg");
        project.setChannelId(1);
        project.setTags("更新,测试");
        project.setStatus(1);

        int rows = projectService.update(project);
        System.out.println("更新影响行数: " + rows);
        assert rows > 0;
    }

    @Test
    public void testDeleteProject() {
        int projectId = 1;
        int rows = projectService.delete(projectId);
        System.out.println("删除影响行数: " + rows);
        assert rows > 0;
    }

    @Test
    public void testSearchProjects() {
        List<Project> results = projectService.searchProjects("AI", null, 1, 1);
        results.forEach(System.out::println);
    }

    @Test
    public void testGetMyProjects() {
        Integer userId = 1; //替换成数据库中的userId

        List<Project> myProjects = projectService.getProjectsByUserId(userId);
        System.out.println("Projects joined by user " + userId + ": " + myProjects.size());
        myProjects.forEach(System.out::println);

    }

    @Test
    public void testAddMemberToProject() {
        Integer projectId = 1; //替换成实际存在的项目ID
        Integer userId = 2;    //替换成实际存在且还没加入该项目的用户ID

        boolean success = projectService.addMemberToProject(projectId, userId);

        if (success) {
            System.out.println("User " + userId + " was successfully added to project " + projectId);
        } else {
            System.out.println("User " + userId + " is already a member of project " + projectId);
        }

        assert success;
    }

    @Test
    public void testExitProject() {
        Integer projectId = 1; //替换成实际存在的项目ID
        Integer userId = 2;    //替换成实际存在且已加入该项目的用户ID

        boolean success = projectService.exitProject(projectId, userId);

        if (success) {
            System.out.println("User " + userId + " exited project " + projectId + " successfully.");
        } else {
            System.out.println("User " + userId + " failed to exit project " + projectId + ".");
        }

        assert success;
    }

    @Test
    public void testCreateProject() {
        Project project = new Project();
        project.setName("Unit Test Project");
        project.setDescription("This is a project created during unit testing.");
        project.setCategory("Testing");
        project.setStatus(0);
        project.setImageUrl("https://example.com/test.png");
        project.setChannelId(1);
        project.setTags("test,unit");

        Project newProject = projectService.createProject(project);

        System.out.println("New Project: " + newProject);
        assert newProject != null;
    }

    @Test
    public void trueTestDeleteProject() {
        Integer projectId = 5;
        Integer ownerUserId = 2;
        boolean success = projectService.deleteProject(projectId, ownerUserId);

        if (success) {
            System.out.println("Project " + projectId + " deleted successfully.");
        } else {
            System.out.println("Failed to delete project " + projectId + ".");
        }

        assert success;
    }
}
