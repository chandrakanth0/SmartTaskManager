package com.taskmanager.controller;

import com.taskmanager.entity.Task;
import com.taskmanager.entity.User;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;
import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TaskControllerAdminTest {

    @InjectMocks
    private TaskControllerAdmin taskControllerAdmin;

    @Mock
    private TaskRepository taskRepo;

    @Mock
    private UserRepository userRepo;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTask() {
        Task task = new Task();
        task.setTitle("Test Task");

        String result = taskControllerAdmin.createTask(task);

        verify(taskRepo).save(task);
        assertEquals("Task created successfully", result);
    }

    @Test
    void testAssignTask_Success() {
        Task task = new Task();
        User user = new User();
        when(taskRepo.findById(1L)).thenReturn(Optional.of(task));
        when(userRepo.findById(2L)).thenReturn(Optional.of(user));

        String result = taskControllerAdmin.assignTask(1L, 2L);

        verify(taskRepo).save(task);
        assertEquals(user, task.getAssignedTo());
        assertEquals("Task assigned to user successfully", result);
    }

    @Test
    void testAssignTask_Failure() {
        when(taskRepo.findById(1L)).thenReturn(Optional.empty());

        String result = taskControllerAdmin.assignTask(1L, 2L);

        assertEquals("Task or User not found", result);
    }

    @Test
    void testUpdateTask_Success() {
        Task existing = new Task();
        Task updated = new Task();
        updated.setTitle("New");
        updated.setDescription("Desc");
        updated.setCompleted(true);

        when(taskRepo.findById(1L)).thenReturn(Optional.of(existing));

        String result = taskControllerAdmin.updateTask(1L, updated);

        verify(taskRepo).save(existing);
        assertEquals("Task updated", result);
    }

    @Test
    void testUpdateTask_NotFound() {
        when(taskRepo.findById(1L)).thenReturn(Optional.empty());

        String result = taskControllerAdmin.updateTask(1L, new Task());

        assertEquals("Task not found", result);
    }

    @Test
    void testDeleteTask_Success() {
        when(taskRepo.existsById(1L)).thenReturn(true);

        String result = taskControllerAdmin.deleteTask(1L);

        verify(taskRepo).deleteById(1L);
        assertEquals("Task deleted", result);
    }

    @Test
    void testDeleteTask_NotFound() {
        when(taskRepo.existsById(1L)).thenReturn(false);

        String result = taskControllerAdmin.deleteTask(1L);

        assertEquals("Task not found", result);
    }

    @Test
    void testGetAllTasks() {
        when(taskRepo.findAll()).thenReturn(Collections.singletonList(new Task()));

        List<Task> result = taskControllerAdmin.getAllTasks();

        assertEquals(1, result.size());
    }
}
