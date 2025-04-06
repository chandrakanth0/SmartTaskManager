package com.taskmanager.controller;

import com.taskmanager.entity.User;
import com.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserRepository userRepo;

    @Mock
    private PasswordEncoder encoder;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        // Arrange
        User user = new User();
        user.setUsername("john");
        user.setPassword("rawpassword");

        when(encoder.encode("rawpassword")).thenReturn("encodedPassword");

        // Act
        String result = authController.register(user);

        // Assert
        verify(userRepo, times(1)).save(user);
        assertEquals("encodedPassword", user.getPassword());
        assertEquals("User registered successfully", result);
    }
}
