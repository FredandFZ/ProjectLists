package org.example.codesignconnect.service.impl;

import org.example.codesignconnect.dto.FolderResponse;
import org.example.codesignconnect.dto.IterationDetail;
import org.example.codesignconnect.dto.SubTaskDetail;
import org.example.codesignconnect.dto.TaskDetail;
import org.example.codesignconnect.mapper.IterationMapper;
import org.example.codesignconnect.mapper.TaskMapper;
import org.example.codesignconnect.mapper.UserMapper;
import org.example.codesignconnect.model.Iteration;
import org.example.codesignconnect.model.Task;
import org.example.codesignconnect.service.IterationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IterationServiceImpl implements IterationService {

    @Autowired
    private IterationMapper iterationMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public int createIteration(Iteration iteration) {
        List<Iteration> iterations = iterationMapper.getIterations(iteration.getProjectId(), iteration.getProjectStatus());
        int iteratedTime;
        if (iterations.isEmpty()) iteratedTime = 1;
        else iteratedTime = iterations.size() + 1;
        iteration.setIteratedTime(iteratedTime);
        iteration.setTitle("Iteration-" + iteratedTime);
        return iterationMapper.insertIteration(iteration);
    }

    @Override
    public int updateIteration(Iteration iteration) {
        return iterationMapper.updateIteration(iteration);
    }

    @Override
    public int deleteIteration(Integer id) {
        return iterationMapper.deleteIteration(id);
    }

    @Override
    public List<IterationDetail> getIterations(Integer projectId, Short status) {
        List<Iteration> iterations = iterationMapper.getIterations(projectId, status);
        List<IterationDetail> iterationDetails = new ArrayList<>();
        for (Iteration iteration : iterations) {
            List<Task> mainTasks = taskMapper.getMainTasksByIterationId(iteration.getId());
            List<TaskDetail> taskDetails = new ArrayList<>();
            for (Task mainTask : mainTasks) {
                List<Task> subTasks = taskMapper.getSubTasksByTaskId(mainTask.getId());
                List<SubTaskDetail> subTaskDetails = new ArrayList<>();
                for (Task subTask : subTasks) {
                    String creator = userMapper.getUsernameById(subTask.getCreatorId());
                    String assignee = userMapper.getUsernameById(subTask.getAssigneeId());
                    String subTaskType = (subTask.getTaskId() == null ? "task" : "subtask");
                    subTaskDetails.add(new SubTaskDetail(subTask, creator, assignee, subTaskType));
                }
                String mainTaskType = (mainTask.getTaskId() == null ? "task" : "subtask");
                taskDetails.add(new TaskDetail(subTaskDetails, mainTask,
                        userMapper.getUsernameById(mainTask.getCreatorId()),
                        userMapper.getUsernameById(mainTask.getAssigneeId()), mainTaskType));
            }
            iterationDetails.add(new IterationDetail(taskDetails, iteration));
        }
        return iterationDetails;
    }

    @Override
    public List<Iteration> getIterationsByProjectId(Integer projectId) {
        return iterationMapper.getIterationsByProjectId(projectId);
    }

    @Override
    public List<FolderResponse> getFolders(Integer projectId) {
        List<Iteration> iterationList = iterationMapper.getIterationsByProjectId(projectId);
        List<FolderResponse> folderList = new ArrayList<>();
        for (Iteration iteration : iterationList) {
            folderList.add(new FolderResponse(iteration.getProjectStatus(), iteration.getIteratedTime()));
        }
        return folderList;
    }
}

