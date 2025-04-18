package com.taskmanager.controller;

import com.taskmanager.entity.Task;
import com.taskmanager.entity.User;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/tasks")
public class TaskControllerUser {

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private UserRepository userRepo;

    private static final Logger logger = LoggerFactory.getLogger(TaskControllerUser.class);

    // ✅ Get tasks assigned to a user by their ID
    @GetMapping("/my/{userId}")
    public List<Task> getMyTasksById(@PathVariable Long userId) {
        logger.info("Fetching tasks for user ID: {}", userId);
        User user = userRepo.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("User with ID {} not found", userId);
                    return new RuntimeException("User not found");
                });

        List<Task> tasks = taskRepo.findAll()
                .stream()
                .filter(task -> task.getAssignedTo() != null && task.getAssignedTo().getId().equals(user.getId()))
                .collect(Collectors.toList());

        logger.info("Found {} tasks for user ID: {}", tasks.size(), userId);
        return tasks;
    }

    // ✅ Mark a task as completed (only if assigned to current authenticated user)
    @PutMapping("/complete/{taskId}")
    public String markAsComplete(@PathVariable Long taskId, Authentication authentication) {
        String username = authentication.getName();
        logger.info("User '{}' attempting to mark task {} as complete", username, taskId);

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("User '{}' not found", username);
                    return new RuntimeException("User not found");
                });

        Task task = taskRepo.findById(taskId).orElse(null);
        if (task == null || task.getAssignedTo() == null || !task.getAssignedTo().getId().equals(user.getId())) {
            logger.warn("Task {} not found or not assigned to user '{}'", taskId, username);
            return "Task not found or not assigned to you";
        }

        task.setCompleted(true);
        taskRepo.save(task);
        logger.info("Task {} marked as complete by user '{}'", taskId, username);
        return "Task marked as complete";
    }

    // ✅ View a specific task (only if assigned to user)
    @GetMapping("/{taskId}")
    public Task getTaskDetails(@PathVariable Long taskId, Authentication authentication) {
        String username = authentication.getName();
        logger.info("User '{}' requesting details for task {}", username, taskId);

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("User '{}' not found", username);
                    return new RuntimeException("User not found");
                });

        Task task = taskRepo.findById(taskId).orElse(null);
        if (task == null || task.getAssignedTo() == null || !task.getAssignedTo().getId().equals(user.getId())) {
            logger.warn("Task {} not found or not assigned to user '{}'", taskId, username);
            throw new RuntimeException("Task not found or not assigned to you");
        }

        logger.info("Returning task details for task {} to user '{}'", taskId, username);
        return task;
    }

    // ✅ Update title and description of task (only if assigned to user)
    @PutMapping("/update/{taskId}")
    public String updateTaskDetails(@PathVariable Long taskId, @RequestBody Task updatedTask, Authentication authentication) {
        String username = authentication.getName();
        logger.info("User '{}' updating task {}", username, taskId);

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("User '{}' not found", username);
                    return new RuntimeException("User not found");
                });

        Task task = taskRepo.findById(taskId).orElse(null);
        if (task == null || task.getAssignedTo() == null || !task.getAssignedTo().getId().equals(user.getId())) {
            logger.warn("Task {} not found or not assigned to user '{}'", taskId, username);
            return "Task not found or not assigned to you";
        }

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        taskRepo.save(task);

        logger.info("Task {} updated by user '{}'", taskId, username);
        return "Task updated successfully";
    }
}
