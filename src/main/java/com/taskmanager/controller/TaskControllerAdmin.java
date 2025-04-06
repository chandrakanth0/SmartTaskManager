package com.taskmanager.controller;

import com.taskmanager.entity.Task;
import com.taskmanager.entity.User;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/tasks")
public class TaskControllerAdmin {

    @Autowired private TaskRepository taskRepo;
    @Autowired private UserRepository userRepo;

    @PostMapping("/create")
    public String createTask(@RequestBody Task task) {
        taskRepo.save(task);
        return "Task created successfully";
    }

    @PutMapping("/assign/{taskId}/user/{userId}")
    public String assignTask(@PathVariable Long taskId, @PathVariable Long userId) {
        Task task = taskRepo.findById(taskId).orElse(null);
        User user = userRepo.findById(userId).orElse(null);

        if (task == null || user == null) return "Task or User not found";

        task.setAssignedTo(user);
        taskRepo.save(task);
        return "Task assigned to user successfully";
    }

    @PutMapping("/update/{id}")
    public String updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        Task task = taskRepo.findById(id).orElse(null);
        if (task == null) return "Task not found";

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setCompleted(updatedTask.isCompleted());
        taskRepo.save(task);
        return "Task updated";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        if (!taskRepo.existsById(id)) return "Task not found";
        taskRepo.deleteById(id);
        return "Task deleted";
    }

    @GetMapping("/all")
    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }
}
