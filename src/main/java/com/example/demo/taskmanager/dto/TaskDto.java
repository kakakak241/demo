package com.example.demo.taskmanager.dto;

import com.example.demo.taskmanager.entity.Task;

import java.time.LocalDateTime;

public record TaskDto(Long id, String description, Task.TaskStatus status, LocalDateTime creationDate) {
}
