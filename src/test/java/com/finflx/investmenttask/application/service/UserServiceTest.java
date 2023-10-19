package com.finflx.investmenttask.application.service;

import com.finflx.investmenttask.application.service.UserService;
import com.finflx.investmenttask.domain.entity.User;
import com.finflx.investmenttask.domain.enumuration.UserRole;
import com.finflx.investmenttask.domain.repository.UserRepository;
import com.finflx.investmenttask.infrastructure.security.UserDetailsDto;
import com.finflx.investmenttask.presentation.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        user.setScopeType(UserRole.INVESTOR);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetailsDto userDetails = userService.loadUserByUsername(email);

        assertEquals(email, userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.loadUserByUsername(email));
    }

    @Test
    void testGetByEmail() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User retrievedUser = userService.getByEmail(email);

        assertEquals(email, retrievedUser.getEmail());
    }

    @Test
    void testGetByEmailNotFound() {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getByEmail(email));
    }

    @Test
    void testCreateUser() {
        String email = "newuser@example.com";
        String password = "password";
        UserRole role = UserRole.VISITOR;

        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(userCaptor.capture())).thenReturn(null); // Return value doesn't matter for this test


        userService.create(email, password, role);

        User createdUser = userCaptor.getValue();

        assertNotNull(createdUser);
        assertEquals(email, createdUser.getEmail());
        assertEquals("encodedPassword", createdUser.getPassword());
        assertEquals(role, createdUser.getScopeType());

        verify(userRepository, times(1)).save(createdUser);
    }
}
