package com.example.demo.taskmanager.service;

import com.example.demo.taskmanager.dto.TaskDto;
import com.example.demo.taskmanager.entity.Task;
import com.example.demo.taskmanager.mapper.TaskMapper;
import com.example.demo.taskmanager.repository.TaskRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final EntityManager entityManager;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Value("${task.file-path}")
    private String filePath;

    @Override
    public TaskDto createTask(TaskDto taskDto) {
        Task task = taskMapper.taskDtoToTask(taskDto);
        task.setCreationDate(LocalDateTime.now());
        task = taskRepository.save(task);
        return taskMapper.taskToTaskDto(task);
    }

    @Override
    public List<TaskDto> getAllTasks(List<Task.TaskStatus> statuses, LocalDate startDate, LocalDate endDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> cq = cb.createQuery(Task.class);
        Root<Task> task = cq.from(Task.class);
        List<Predicate> predicates = new ArrayList<>();

        if (statuses != null && !statuses.isEmpty()) {
            predicates.add(task.get("status").in(statuses));
        }
        if (startDate != null && endDate != null) {
            predicates.add(cb.between(task.get("creationDate"), startDate, endDate));
        } else if (startDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(task.get("creationDate"), startDate));
        } else if (endDate != null) {
            predicates.add(cb.lessThanOrEqualTo(task.get("creationDate"), endDate));
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        List<Task> result = entityManager.createQuery(cq).getResultList();
        return result.stream().map(taskMapper::taskToTaskDto).collect(Collectors.toList());
    }

    @Override
    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        return taskMapper.taskToTaskDto(task);
    }

    @Override
    public TaskDto updateTask(Long id, TaskDto taskDto) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        taskMapper.updateTaskFromDto(taskDto, task);
        task = taskRepository.save(task);
        return taskMapper.taskToTaskDto(task);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public void saveTasksToFile() throws IOException {
        List<Task> tasks = taskRepository.findAll();
        objectMapper.writeValue(new File(filePath), tasks.stream().map(taskMapper::taskToTaskDto).collect(Collectors.toList()));
    }

    @Override
    public void loadTasksFromFile() throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            List<TaskDto> tasks = objectMapper.readValue(file, new TypeReference<>() {
            });
            taskRepository.saveAll(tasks.stream().map(taskMapper::taskDtoToTask).collect(Collectors.toList()));
        }
    }
}
