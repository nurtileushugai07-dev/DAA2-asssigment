package com.daa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MyLinkedListTest {

    private OperationCounter counter;
    private MyLinkedList list;

    @BeforeEach
    void setUp() {
        counter = new OperationCounter();
        list = new MyLinkedList(counter);
    }

    @Test
    void testEmptyList() {
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
    }

    @Test
    void testSingleElement() {
        list.add(42);
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        assertEquals(42, list.get(0));
        assertTrue(list.contains(42));
        assertFalse(list.contains(100));

        assertEquals(42, list.remove(0));
        assertTrue(list.isEmpty());
    }
}