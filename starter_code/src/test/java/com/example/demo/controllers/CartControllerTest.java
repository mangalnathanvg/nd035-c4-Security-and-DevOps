package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class CartControllerTest {

    private String username;
    private String password;
    private String itemId;
    private String quantity;
    private CartController cartController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);

    public CartControllerTest(String username, String password, String itemId, String quantity) {
        super();
        this.username = username;
        this.password = password;
        this.itemId = itemId;
        this.quantity = quantity;
    }

    @Parameterized.Parameters
    public static Collection initData()
    {
        String reqData[][] = {{"testuser1", "testpassword1", "5", "30"}, {"testuser2", "testpassword2", "5", "40"}, {"testuser3", "testpassword3", "5", "100"}};
        return Arrays.asList(reqData);
    }

    @Before
    public void setUp()
    {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
    }

    @Test
    public void add_to_cart_happy_path()
    {
        Item item = new Item();
        item.setId(Long.valueOf(itemId));
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setPrice(new BigDecimal(20));

        Cart cart = new Cart();
        cart.setId(Long.valueOf(itemId));
        cart.setItems(new ArrayList<>());
        cart.setTotal(item.getPrice().multiply(new BigDecimal(quantity)));

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setCart(cart);

        when(userRepo.findByUsername(username)).thenReturn(user);
        when(itemRepo.findById(Long.valueOf(itemId))).thenReturn(Optional.of(item));
        when(cartRepo.save(cart)).thenReturn(null);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(Long.valueOf(itemId));
        request.setUsername(username);
        request.setQuantity(Integer.parseInt(quantity));

        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart returnedCart = response.getBody();
        assertNotNull(cart);
        assertEquals(request.getQuantity(), returnedCart.getItems().size());
        assertEquals(returnedCart.getTotal(), cart.getTotal());
    }

    @Test
    public void add_to_cart_user_not_found()
    {
        when(userRepo.findByUsername(username)).thenReturn(null);
        ModifyCartRequest request = new ModifyCartRequest();
        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void add_to_cart_item_not_found()
    {
        Item item = new Item();
        item.setId(Long.valueOf(itemId));
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setPrice(new BigDecimal(20));

        Cart cart = new Cart();
        cart.setId(Long.valueOf(itemId));
        cart.setItems(new ArrayList<>());
        cart.setTotal(item.getPrice().multiply(new BigDecimal(quantity)));

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setCart(cart);

        when(userRepo.findByUsername(username)).thenReturn(user);
        when(itemRepo.findById(Long.valueOf(itemId))).thenReturn(Optional.empty());

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(Long.valueOf(itemId));
        request.setUsername(username);
        request.setQuantity(Integer.parseInt(quantity));

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertEquals(404, response.getStatusCodeValue());
    }


    @Test
    public void remove_from_cart_happy_path()
    {
        Item item = new Item();
        item.setId(Long.valueOf(itemId));
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setPrice(new BigDecimal(20));

        Cart cart = new Cart();
        cart.setId(Long.valueOf(itemId));
        cart.setItems(new ArrayList<>());
        cart.setTotal(item.getPrice().multiply(new BigDecimal(quantity)));

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setCart(cart);

        when(userRepo.findByUsername(username)).thenReturn(user);
        when(itemRepo.findById(Long.valueOf(itemId))).thenReturn(Optional.of(item));
        when(cartRepo.save(cart)).thenReturn(null);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(Long.valueOf(itemId));
        request.setUsername(username);
        request.setQuantity(Integer.parseInt(quantity));

        ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart returnedCart = response.getBody();
        assertNotNull(cart);
        assertEquals(0, returnedCart.getItems().size());
        assertEquals(returnedCart.getTotal(), cart.getTotal());
    }

    @Test
    public void remove_from_cart_user_not_found()
    {
        when(userRepo.findByUsername(username)).thenReturn(null);
        ModifyCartRequest request = new ModifyCartRequest();
        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_item_not_found()
    {
        Item item = new Item();
        item.setId(Long.valueOf(itemId));
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setPrice(new BigDecimal(20));

        Cart cart = new Cart();
        cart.setId(Long.valueOf(itemId));
        cart.setItems(new ArrayList<>());
        cart.setTotal(item.getPrice().multiply(new BigDecimal(quantity)));

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setCart(cart);

        when(userRepo.findByUsername(username)).thenReturn(user);
        when(itemRepo.findById(Long.valueOf(itemId))).thenReturn(Optional.empty());

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(Long.valueOf(itemId));
        request.setUsername(username);
        request.setQuantity(Integer.parseInt(quantity));

        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertEquals(404, response.getStatusCodeValue());
    }

 }
