package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class ItemControllerTest {

    private ItemController itemController;

    @Mock
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){

        this.itemController = new ItemController();
        TestUtils.inject(itemController,"itemRepository", itemRepository);
    }


    @Test
    public void getItemsTest()throws Exception{

        ResponseEntity<List<Item>> response = itemController.getItems();
        Assert.assertEquals(200, response.getStatusCodeValue());

        Assert.assertNotNull(response);
        Assert.assertTrue(response.hasBody());
    }

    @Test
    public void findUserByIdTest()throws Exception{

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        Optional<Item> mockItem = Optional.of(item);
        doReturn(mockItem).when(itemRepository).findById(1L);

        ResponseEntity<Item> response = itemController.getItemById(1L);

        Assert.assertNotNull(response);
        Assert.assertTrue(response.hasBody());
        Assert.assertEquals(200, response.getStatusCodeValue());

        Item foundItem = response.getBody();

        Assert.assertNotNull(foundItem);
        Assert.assertEquals(item.getId(), foundItem.getId());
    }

    @Test
    public void findItemByNameTest()throws Exception{

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        List<Item> mockItems = Arrays.asList(item);
        doReturn(mockItems).when(itemRepository).findByName("Round Widget");

        ResponseEntity<List<Item>> response = itemController.getItemsByName("Round Widget");

        Assert.assertNotNull(response);
        Assert.assertTrue(response.hasBody());
        Assert.assertEquals(200, response.getStatusCodeValue());

        List<Item> foundItems = response.getBody();

        Assert.assertNotNull(foundItems);
        Assert.assertEquals(foundItems.size(), 1);
    }

}
