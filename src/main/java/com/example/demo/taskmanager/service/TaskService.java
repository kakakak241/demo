package com.example.demo.taskmanager.service;

import com.example.demo.taskmanager.dto.TaskDto;
import com.example.demo.taskmanager.entity.Task;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    TaskDto createTask(TaskDto taskDto);

    List<TaskDto> getAllTasks(List<Task.TaskStatus> statuses, LocalDate startDate, LocalDate endDate);


    TaskDto getTaskById(Long id);

    TaskDto updateTask(Long id, TaskDto taskDto);

    void deleteTask(Long id);

    void saveTasksToFile() throws IOException;

    void loadTasksFromFile() throws IOException;
}
