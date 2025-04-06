package com.taskmanager.controller;

import com.taskmanager.entity.Task;
import com.taskmanager.entity.User;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/admin/tasks")
public class TaskControllerAdmin {

    @Autowired private TaskRepository taskRepo;
    @Autowired private UserRepository userRepo;

    private static final Logger logger = LoggerFactory.getLogger(TaskControllerAdmin.class);

    @PostMapping("/create")
    public String createTask(@RequestBody Task task) {
        taskRepo.save(task);
        logger.info("Admin created a task: {}", task.getTitle());
        return "Task created successfully";
    }

    @PutMapping("/assign/{taskId}/user/{userId}")
    public String assignTask(@PathVariable Long taskId, @PathVariable Long userId) {
        Task task = taskRepo.findById(taskId).orElse(null);
        User user = userRepo.findById(userId).orElse(null);

        if (task == null || user == null) {
            logger.warn("Failed to assign task. Task or User not found. TaskId: {}, UserId: {}", taskId, userId);
            return "Task or User not found";
        }

        task.setAssignedTo(user);
        taskRepo.save(task);
        logger.info("Task {} assigned to user {}", taskId, userId);
        return "Task assigned to user successfully";
    }

    @PutMapping("/update/{id}")
    public String updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        Task task = taskRepo.findById(id).orElse(null);
        if (task == null) {
            logger.warn("Update failed. Task not found: {}", id);
            return "Task not found";
        }

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setCompleted(updatedTask.isCompleted());
        taskRepo.save(task);
        logger.info("Task updated: {}", id);
        return "Task updated";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        if (!taskRepo.existsById(id)) {
            logger.warn("Delete failed. Task not found: {}", id);
            return "Task not found";
        }
        taskRepo.deleteById(id);
        logger.info("Task deleted: {}", id);
        return "Task deleted";
    }

    @GetMapping("/all")
    public List<Task> getAllTasks() {
        logger.info("Admin requested all tasks");
        return taskRepo.findAll();
    }
}
