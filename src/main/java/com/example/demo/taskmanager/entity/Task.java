package com.example.demo.taskmanager.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private LocalDateTime creationDate;

    @Getter
    public enum TaskStatus {
        COMPLETED("выполнено"),
        IN_PROGRESS("в процессе"),
        NOT_COMPLETED("не выполнено");

        private final String description;

        TaskStatus(String description) {
            this.description = description;
        }

    }

}
