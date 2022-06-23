package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.Assert.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.swing.text.html.parser.Entity;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private final UserRepository userRepo = mock(UserRepository.class);

    private final CartRepository cartRepo = mock(CartRepository.class);

    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);

    }

    @Test
    @DisplayName("Create user successfully")
    public void create_user_happy_path() {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();

        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    @DisplayName("Password is less than 7 characters")
    public void create_user_short_password(){
        String username = "test";
        String password = "tstpwd";

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername(username);
        request.setPassword(password);
        request.setConfirmPassword(password);

        ResponseEntity response = userController.createUser(request);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("Confirm password does not match password")
    public void  create_user_confirm_password_not_matching_password() {
        String username = "test";
        String password = "testPassword";
        String confirmPassword = "Password";
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername(username);
        request.setPassword(password);
        request.setConfirmPassword(confirmPassword);
        ResponseEntity response = userController.createUser(request);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void find_by_user_name_happy_path() {
        User u = new User();

        String username = "test";
        String password = "testPassword";
        when(userRepo.findByUsername(username)).thenReturn(u);

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername(username);
        request.setPassword(password);
        request.setConfirmPassword(password);

        ResponseEntity<User> response = userController.findByUserName(username);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("Find user by id")
    public void find_user_by_id() throws Exception {
        String username = "test";
        String password = "testPassword";
        User user1 = new User();


        when(userRepo.findById(0L)).thenReturn(Optional.of(user1));

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername(username);
        request.setPassword(password);
        request.setConfirmPassword(password);

        ResponseEntity<User> response = userController.findById(0L);
        assertEquals(200, response.getStatusCodeValue());
    }


}
