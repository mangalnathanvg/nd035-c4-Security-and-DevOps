package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private final ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp()
    {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
    }

    @Test
    @DisplayName("Get all items")
    public void get_all_items()
    {
        List items = new ArrayList<Item>();
        items.add(new Item());
        when(itemRepo.findAll()).thenReturn(items);
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> returned = response.getBody();
        assertNotNull(returned);
    }

    @Test
    @DisplayName("Get item by id")
    public void get_item_by_id()
    {
        Long id = 1L;
        Item item = new Item();

        when(itemRepo.findById(id)).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(id);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item returned = response.getBody();
        assertNotNull(returned);
    }

    @Test
    @DisplayName("Get item by name")
    public void get_item_by_name(){
        String username = "test";
        Item item = new Item();
        List<Item> items = new ArrayList<>();
        items.add(item);

        when(itemRepo.findByName(username)).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItemsByName(username);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> returned = response.getBody();
        assertNotNull(returned);

        // Null item
        when(itemRepo.findByName(username)).thenReturn(null);
        response = itemController.getItemsByName(username);
        assertEquals(404, response.getStatusCodeValue());

        //Empty item lists
        List<Item> items2 = new ArrayList<>();
        when(itemRepo.findByName(username)).thenReturn(items2);
        response = itemController.getItemsByName(username);
        assertEquals(404, response.getStatusCodeValue());
    }

}
