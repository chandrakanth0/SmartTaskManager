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

@RestController
@RequestMapping("/user/tasks")
public class TaskController {

    @Autowired private TaskRepository taskRepo;
    @Autowired private UserRepository userRepo;

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @GetMapping
    public List<Task> getUserTasks(Authentication auth) {
        User user = userRepo.findByUsername(auth.getName()).get();
        logger.info("Fetching tasks for user: {}", user.getUsername());
        return taskRepo.findByAssignedTo(user);
    }

    @PostMapping
    public Task createTask(@RequestBody Task task, Authentication auth) {
        User user = userRepo.findByUsername(auth.getName()).get();
        task.setAssignedTo(user);
        Task savedTask = taskRepo.save(task);
        logger.info("Task created by user {}: {}", user.getUsername(), savedTask.getTitle());
        return savedTask;
    }

    @PutMapping("/{id}/complete")
    public Task markComplete(@PathVariable Long id) {
        Task task = taskRepo.findById(id).orElseThrow();
        task.setCompleted(true);
        Task updatedTask = taskRepo.save(task);
        logger.info("Task marked as complete: Task ID {}", id);
        return updatedTask;
    }
}
