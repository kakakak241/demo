package com.example.demo.taskmanager.controller;

import com.example.demo.taskmanager.dto.TaskDto;
import com.example.demo.taskmanager.entity.Task;
import com.example.demo.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class TaskConsoleController implements CommandLineRunner {

    private final TaskService taskService;

    @Override
    public void run(String... args) {
        System.out.println("""
                Доступные команды:\s
                create - Создать задачу
                update - Обновить задачу
                delete - Удалить задачу
                get_all - Получить все задачи
                get_by_id - Получить задачу
                filter - Фильтровать задачи
                save - Сохранить задачи в файл
                load - Загрузить задачи из файла
                exit - Выход""");
        try (Scanner scanner = new Scanner(System.in)) {
            String input;
            do {
                System.out.print("Введите команду: ");
                input = scanner.nextLine().trim().toLowerCase();
                switch (input) {
                    case "create":
                        createTask(scanner);
                        break;
                    case "update":
                        updateTask(scanner);
                        break;
                    case "delete":
                        deleteTask(scanner);
                        break;
                    case "get_all":
                        getAllTasks();
                        break;
                    case "get_by_id":
                        getTask(scanner);
                        break;
                    case "filter":
                        filterTasks(scanner);
                        break;
                    case "save":
                        taskService.saveTasksToFile();
                        System.out.println("Задачи сохранены.");
                        break;
                    case "load":
                        taskService.loadTasksFromFile();
                        System.out.println("Задачи загружены.");
                        break;
                    case "exit":
                        System.out.println("Выход из приложения...");
                        break;
                    default:
                        System.out.println("Неизвестная команда.");
                }
            } while (!"exit".equals(input));
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void createTask(Scanner scanner) {
        System.out.println("Введите описание задачи:");
        String description = scanner.nextLine();
        Task.TaskStatus status = readTaskStatus(scanner);
        TaskDto task = new TaskDto(null, description, status, null);
        TaskDto createdTask = taskService.createTask(task);
        System.out.println("Создана задача: " + createdTask);
    }

    private void updateTask(Scanner scanner) {
        System.out.println("Введите ID задачи для обновления:");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.println("Введите новое описание задачи:");
        String description = scanner.nextLine();
        Task.TaskStatus status = readTaskStatus(scanner);
        TaskDto task = new TaskDto(id, description, status, null);
        TaskDto updatedTask = taskService.updateTask(id, task);
        System.out.println("Обновлена задача: " + updatedTask);
    }

    private void deleteTask(Scanner scanner) {
        System.out.println("Введите ID задачи для удаления:");
        Long id = Long.parseLong(scanner.nextLine());
        taskService.deleteTask(id);
        System.out.println("Задача удалена.");
    }

    private void getAllTasks() {
        taskService.getAllTasks(null, null, null).forEach(System.out::println);
    }

    private void getTask(Scanner scanner) {
        System.out.println("Введите ID задачи для получения");
        Long id = Long.parseLong(scanner.nextLine());
        TaskDto task = taskService.getTaskById(id);
        System.out.println("Задача получена: " + task);
    }

    private void filterTasks(Scanner scanner) {
        List<Task.TaskStatus> statuses = readTaskStatuses(scanner);
        LocalDate startDate = readDate(scanner, "Введите начальную дату (YYYY-MM-DD), оставьте пустым для пропуска:");
        LocalDate endDate = readDate(scanner, "Введите конечную дату (YYYY-MM-DD), оставьте пустым для пропуска:");
        List<TaskDto> filteredTasks = taskService.getAllTasks(statuses, startDate, endDate);
        if (filteredTasks.isEmpty()) {
            System.out.println("Задачи не найдены.");
        } else {
            filteredTasks.forEach(System.out::println);
        }
    }

    private Task.TaskStatus readTaskStatus(Scanner scanner) {
        Task.TaskStatus status = null;
        while (status == null) {
            System.out.println("Выберите статус задачи (IN_PROGRESS, COMPLETED, NOT_COMPLETED):");
            try {
                status = Task.TaskStatus.valueOf(scanner.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Неверный статус. Попробуйте еще раз.");
            }
        }
        return status;
    }

    private List<Task.TaskStatus> readTaskStatuses(Scanner scanner) {
        List<Task.TaskStatus> statuses = new ArrayList<>();
        System.out.println("Введите статусы для фильтрации через запятую (IN_PROGRESS, COMPLETED, NOT_COMPLETED), оставьте пустым для пропуска:");
        String line = scanner.nextLine().trim();
        if (!line.isEmpty()) {
            for (String statusStr : line.split(",")) {
                try {
                    statuses.add(Task.TaskStatus.valueOf(statusStr.trim().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    System.out.println("Пропуск неизвестного статуса: " + statusStr);
                }
            }
        }
        return statuses;
    }

    private LocalDate readDate(Scanner scanner, String prompt) {
        LocalDate date = null;
        while (date == null) {
            System.out.println(prompt);
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                break;
            }
            try {
                date = LocalDate.parse(line);
            } catch (Exception e) {
                System.out.println("Неверный формат даты. Попробуйте еще раз.");
            }
        }
        return date;
    }
}
