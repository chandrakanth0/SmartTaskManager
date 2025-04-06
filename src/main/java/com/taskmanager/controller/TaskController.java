package com.taskmanager.controller;

import com.taskmanager.entity.Task;
import com.taskmanager.entity.User;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/tasks")
public class TaskController {

    @Autowired private TaskRepository taskRepo;
    @Autowired private UserRepository userRepo;

    @GetMapping
    public List<Task> getUserTasks(Authentication auth) {
        User user = userRepo.findByUsername(auth.getName()).get();
        return taskRepo.findByAssignedTo(user);
    }

    @PostMapping
    public Task createTask(@RequestBody Task task, Authentication auth) {
        User user = userRepo.findByUsername(auth.getName()).get();
        task.setAssignedTo(user);
        return taskRepo.save(task);
    }

    @PutMapping("/{id}/complete")
    public Task markComplete(@PathVariable Long id) {
        Task task = taskRepo.findById(id).orElseThrow();
        task.setCompleted(true);
        return taskRepo.save(task);
    }
}
