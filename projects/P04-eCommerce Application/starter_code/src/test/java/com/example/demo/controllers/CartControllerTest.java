package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class CartControllerTest {

    private CartController cartController;

    @Mock
    private UserRepository userRepository = mock(UserRepository.class);
    @Mock
    private CartRepository cartRepository = mock(CartRepository.class);
    @Mock
    private ItemRepository itemRepository = mock(ItemRepository.class);


    @Before
    public void setUp(){

        this.cartController = new CartController();

        TestUtils.inject(cartController,"userRepository", userRepository);
        TestUtils.inject(cartController,"cartRepository", cartRepository);
        TestUtils.inject(cartController,"itemRepository", itemRepository);

    }

    private User createUser(){

        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("testPass");

        user.setCart(new Cart());
        doReturn(user).when(userRepository).findByUsername("test");

        return user;
    }
    private Item createItem(){

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        Optional<Item> mockItem = Optional.of(item);
        doReturn(mockItem).when(itemRepository).findById(1L);

        return item;
    }

    @Test
    public void addToCartTest(){

        User createdUser = createUser();
        Assert.assertNotNull(createdUser);

        Item createdItem = createItem();
        Assert.assertNotNull(createdItem);

        ModifyCartRequest addingCart = new ModifyCartRequest();
        addingCart.setUsername(createdUser.getUsername());
        addingCart.setItemId(createdItem.getId());
        addingCart.setQuantity(2);

        ResponseEntity<Cart> response = cartController.addTocart(addingCart);

        Assert.assertNotNull(response);
        Assert.assertTrue(response.hasBody());
        Assert.assertEquals(200, response.getStatusCodeValue());

        Cart createdCart = response.getBody();
        Assert.assertNotNull(createdCart);
        Assert.assertEquals(createdCart.getItems().size(), 2);

    }

    @Test
    public void removeFromCartTest(){

        User createdUser = createUser();
        Assert.assertNotNull(createdUser);

        Item createdItem = createItem();
        Assert.assertNotNull(createdItem);

        ModifyCartRequest addingCart = new ModifyCartRequest();
        addingCart.setUsername(createdUser.getUsername());
        addingCart.setItemId(createdItem.getId());
        addingCart.setQuantity(2);

        ResponseEntity<Cart> response = cartController.removeFromcart(addingCart);

        Assert.assertNotNull(response);
        Assert.assertTrue(response.hasBody());
        Assert.assertEquals(200, response.getStatusCodeValue());

        Cart removedCart = response.getBody();
        Assert.assertNotNull(removedCart);
        Assert.assertEquals(removedCart.getItems().size(), 0);

    }
}
