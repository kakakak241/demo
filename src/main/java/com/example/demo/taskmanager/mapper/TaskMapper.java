package com.example.demo.taskmanager.mapper;

import com.example.demo.taskmanager.dto.TaskDto;
import com.example.demo.taskmanager.entity.Task;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDto taskToTaskDto(Task task);

    @Mapping(target = "id", ignore = true)
    Task taskDtoToTask(TaskDto taskDto);

    @InheritConfiguration
    void updateTaskFromDto(TaskDto taskDto, @MappingTarget Task task);
}
