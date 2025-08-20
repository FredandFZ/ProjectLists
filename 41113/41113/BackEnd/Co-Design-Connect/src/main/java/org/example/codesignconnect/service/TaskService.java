package org.example.codesignconnect.service;

import org.example.codesignconnect.model.Task;

import java.util.List;

public interface TaskService {
    int createTask(Task task);
    int updateTask(Task task);
    int deleteTask(Integer id);
    Task getTaskById(Integer id);
    List<Task> getTasksByIterationId(Integer iterationId);
}