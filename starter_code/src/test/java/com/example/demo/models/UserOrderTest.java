package com.example.demo.models;

import com.example.demo.model.persistence.UserOrder;
import org.junit.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserOrderTest {

    @Test
    public void testGetId() {
        UserOrder userOrder = new UserOrder();
        userOrder.setId(23L);
        assertEquals(Optional.ofNullable(userOrder.getId()), Optional.of(23L));
    }
}