package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;

import org.junit.jupiter.api.DisplayName;

import org.springframework.http.ResponseEntity;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private final UserRepository userRepo = mock(UserRepository.class);
    private final OrderRepository orderRepo = mock(OrderRepository.class);

    @Before
    public void beforeEach() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController,"orderRepository", orderRepo);
    }

    @DisplayName("Create an order from a user's cart")
    @Test
    public void create_order_from_cart() {

        String username = "test";
        User user = new User();

        BigDecimal itemPrice = new BigDecimal("200");
        String itemName = "test item";

        Item item = new Item();
        item.setPrice(itemPrice);
        item.setName(itemName);
        item.setDescription(item.getDescription());
        item.setId(1L);

        List<Item> cartItems = new ArrayList<>();
        cartItems.add(item);
        BigDecimal cartTotal = new BigDecimal("200");

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setTotal(cartTotal);
        cart.setItems(cartItems);

        user.setCart(cart);

        when(userRepo.findByUsername(username)).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit(username);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder returned = response.getBody();
        assertNotNull(returned);

        assertEquals(cartItems, returned.getItems());
        assertEquals(cartTotal, returned.getTotal());
        assertEquals(user, returned.getUser());
    }

    @DisplayName("Get a list of orders by username")
    @Test
    public void get_list_of_orders_by_username() {
        String username = "test";
        User user = new User();
        UserOrder order = new UserOrder();
        List<UserOrder> orders = new ArrayList<>();
        orders.add(order);

        when(userRepo.findByUsername(username)).thenReturn(user);
        when(orderRepo.findByUser(user)).thenReturn(orders);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(username);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> returned = response.getBody();
        assertNotNull(returned);
    }

    @Test
    @DisplayName("Create order with bad username")
    public void create_order_bad_username() {
        String username = "test";
        when(userRepo.findByUsername(username)).thenReturn(null);
        ResponseEntity<UserOrder> response = orderController.submit(username);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("Get order list with bad username")
    public void get_orders_bad_username() {
        String username = "test";
        when(userRepo.findByUsername(username)).thenReturn(null);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(username);
        assertEquals(404, response.getStatusCodeValue());


    }
}
