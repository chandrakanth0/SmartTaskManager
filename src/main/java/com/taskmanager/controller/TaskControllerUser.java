package com.taskmanager.controller;

import com.taskmanager.entity.Task;
import com.taskmanager.entity.User;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/tasks")
public class TaskControllerUser {

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private UserRepository userRepo;

    // ✅ Get tasks assigned to a user by their ID
    @GetMapping("/my/{userId}")
    public List<Task> getMyTasksById(@PathVariable Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return taskRepo.findAll()
                .stream()
                .filter(task -> task.getAssignedTo() != null && task.getAssignedTo().getId().equals(user.getId()))
                .collect(Collectors.toList());
    }

    // ✅ Mark a task as completed (only if assigned to current authenticated user)
    @PutMapping("/complete/{taskId}")
    public String markAsComplete(@PathVariable Long taskId, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = taskRepo.findById(taskId).orElse(null);
        if (task == null || task.getAssignedTo() == null || !task.getAssignedTo().getId().equals(user.getId())) {
            return "Task not found or not assigned to you";
        }

        task.setCompleted(true);
        taskRepo.save(task);
        return "Task marked as complete";
    }

    // ✅ View a specific task (only if assigned to user)
    @GetMapping("/{taskId}")
    public Task getTaskDetails(@PathVariable Long taskId, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = taskRepo.findById(taskId).orElse(null);
        if (task == null || task.getAssignedTo() == null || !task.getAssignedTo().getId().equals(user.getId())) {
            throw new RuntimeException("Task not found or not assigned to you");
        }

        return task;
    }

    // ✅ Update title and description of task (only if assigned to user)
    @PutMapping("/update/{taskId}")
    public String updateTaskDetails(@PathVariable Long taskId, @RequestBody Task updatedTask, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = taskRepo.findById(taskId).orElse(null);
        if (task == null || task.getAssignedTo() == null || !task.getAssignedTo().getId().equals(user.getId())) {
            return "Task not found or not assigned to you";
        }

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        taskRepo.save(task);

        return "Task updated successfully";
    }
}
