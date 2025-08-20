package org.example.codesignconnect.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.codesignconnect.dto.ProjectDetail;
import org.example.codesignconnect.model.Result;
import org.example.codesignconnect.service.ProjectService;
import org.example.codesignconnect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class ManagerController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @GetMapping("/manager/projects")
    public Result getProjectList(){
        List<ProjectDetail> projectList = projectService.getAllProjects();
        return Result.success(projectList);
    }

    @GetMapping("/manager/users")
    public Result getAllUsers() {
        return Result.success(userService.getAllUsers());
    }

    @DeleteMapping("/manager/users/{id}")
    public Result deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return Result.success();
    }

    @PostMapping("/manager/sendInvitationCode")
    public Result sendInviteCode(String email, Short type){
        log.info("/inviteCode: {}, {}", email, type);
        return userService.generateCode(email, type);
    }
}
