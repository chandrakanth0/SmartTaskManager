package com.taskmanager.controller;

import com.taskmanager.entity.Task;
import com.taskmanager.entity.User;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskControllerUserTest {

    @InjectMocks
    private TaskControllerUser taskController;

    @Mock
    private TaskRepository taskRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private Authentication authentication;

    private AutoCloseable closeable;

    private User testUser;
    private Task testTask;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testTask = new Task();
        testTask.setId(10L);
        testTask.setTitle("Test Task");
        testTask.setAssignedTo(testUser);
        testTask.setCompleted(false);
    }

    @Test
    void testGetMyTasksById_ReturnsTaskList() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(testUser));
        when(taskRepo.findAll()).thenReturn(List.of(testTask));

        List<Task> tasks = taskController.getMyTasksById(1L);

        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getTitle());
    }

    @Test
    void testMarkAsComplete_Success() {
        when(authentication.getName()).thenReturn("testuser");
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(taskRepo.findById(10L)).thenReturn(Optional.of(testTask));

        String result = taskController.markAsComplete(10L, authentication);
        assertEquals("Task marked as complete", result);
        assertTrue(testTask.isCompleted());
    }

    @Test
    void testGetTaskDetails_ReturnsTask() {
        when(authentication.getName()).thenReturn("testuser");
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(taskRepo.findById(10L)).thenReturn(Optional.of(testTask));

        Task result = taskController.getTaskDetails(10L, authentication);
        assertEquals("Test Task", result.getTitle());
    }

    @Test
    void testUpdateTaskDetails_UpdatesTitleAndDescription() {
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Title");
        updatedTask.setDescription("Updated Desc");

        when(authentication.getName()).thenReturn("testuser");
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(taskRepo.findById(10L)).thenReturn(Optional.of(testTask));

        String result = taskController.updateTaskDetails(10L, updatedTask, authentication);
        assertEquals("Task updated successfully", result);
        assertEquals("Updated Title", testTask.getTitle());
    }
}
