package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder passEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){

        this.userController = new UserController();

        TestUtils.inject(userController,"userRepository", userRepository);
        TestUtils.inject(userController,"cartRepository", cartRepository);
        TestUtils.inject(userController,"bCryptPasswordEncoder", passEncoder);
    }

    private User createUser(){

        when(passEncoder.encode("testPass")).thenReturn("hashed");

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("testPass");
        request.setConfirmPassword("testPass");

        ResponseEntity<User> response = userController.createUser(request);

        if(response.getStatusCodeValue() == 200 && response.hasBody()){
            return response.getBody();
        }
        else {
            return null;
        }
    }

    @Test
    public void createUserTest()throws Exception{

        User created = createUser();

        Assert.assertNotNull(created);
        Assert.assertEquals(0, created.getId());
        Assert.assertEquals("test", created.getUsername());
        Assert.assertEquals("hashed", created.getPassword());
    }

    @Test
    public void findUserByIdTest()throws Exception{

        User created = createUser();

        Assert.assertNotNull(created);
        Assert.assertEquals(0, created.getId());
        Assert.assertEquals("test", created.getUsername());
        Assert.assertEquals("hashed", created.getPassword());

        Optional<User> mockItem = Optional.of(created);
        doReturn(mockItem).when(userRepository).findById(0L);

        ResponseEntity<User> response = userController.findById(0L);

        Assert.assertNotNull(response);
        Assert.assertTrue(response.hasBody());
        Assert.assertEquals(200, response.getStatusCodeValue());

        User foundUser = response.getBody();

        Assert.assertNotNull(foundUser);
        Assert.assertEquals(created.getId(), foundUser.getId());
    }

    @Test
    public void findUserByNameTest()throws Exception{

        User created = createUser();

        Assert.assertNotNull(created);
        Assert.assertEquals(0, created.getId());
        Assert.assertEquals("test", created.getUsername());
        Assert.assertEquals("hashed", created.getPassword());

        doReturn(created).when(userRepository).findByUsername("test");
        ResponseEntity<User> response = userController.findByUserName("test");

        Assert.assertNotNull(response);
        Assert.assertTrue(response.hasBody());
        Assert.assertEquals(200, response.getStatusCodeValue());

        User foundUser = response.getBody();

        Assert.assertNotNull(foundUser);
        Assert.assertEquals(created.getId(), foundUser.getId());
    }

}
