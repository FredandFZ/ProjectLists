package org.example.codesignconnect.service.impl;

import org.example.codesignconnect.mapper.IterationMapper;
import org.example.codesignconnect.mapper.TaskMapper;
import org.example.codesignconnect.model.Task;
import org.example.codesignconnect.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private IterationMapper iterationMapper;

    @Override
    public int createTask(Task task) {
        task.setProjectStatus(iterationMapper.getProjectStatusById(task.getIterationId()));
        String code = "S" + (task.getProjectStatus() + 1) + "I" + iterationMapper.getIteratedTimeById(task.getIterationId());
        int count = taskMapper.getCountByCode(code);
        task.setCode(code + "-" + (count + 1));
        if (task.getAssigneeId() == 0) task.setAssigneeId(null);
        return taskMapper.insertTask(task);
    }

    @Override
    public int updateTask(Task task) {
        return taskMapper.updateTask(task);
    }

    @Override
    public int deleteTask(Integer id) {
        return taskMapper.deleteTask(id);
    }

    @Override
    public Task getTaskById(Integer id) {
        return taskMapper.getTaskById(id);
    }

    @Override
    public List<Task> getTasksByIterationId(Integer iterationId) {
        return taskMapper.getTasksByIterationId(iterationId);
    }
}
