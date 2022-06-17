package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class OrderControllerTest {

    private OrderController orderController;

    @Mock
    private UserRepository userRepository = mock(UserRepository.class);
    @Mock
    private OrderRepository orderRepository = mock(OrderRepository.class);


    @Before
    public void setUp(){

        this.orderController = new OrderController();

        TestUtils.inject(orderController,"userRepository", userRepository);
        TestUtils.inject(orderController,"orderRepository", orderRepository);

    }

    private User configureUserAndCart(){

        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("testPass");

        Cart cart = new Cart();
        cart.setUser(user);

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        cart.addItem(item);
        user.setCart(cart);

        doReturn(user).when(userRepository).findByUsername("test");

        return user;
    }

    @Test
    public void submitOrderTest(){

        User createdUser = configureUserAndCart();
        Assert.assertNotNull(createdUser);

        ResponseEntity<UserOrder> response = orderController.submit(createdUser.getUsername());

        Assert.assertNotNull(response);
        Assert.assertTrue(response.hasBody());
        Assert.assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();
        Assert.assertNotNull(userOrder);

        Assert.assertEquals(userOrder.getUser().getId(), createdUser.getId());
        Assert.assertEquals(userOrder.getTotal(), createdUser.getCart().getTotal());

    }
}
