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

    @Test
    void testDuplicates() {
        list.add(5);
        list.add(5);
        list.add(5);
        assertEquals(3, list.size());
        assertTrue(list.contains(5));


        assertEquals(5, list.remove(1));
        assertEquals(2, list.size());
        assertTrue(list.contains(5));

        list.remove(0);
        list.remove(0);
        assertTrue(list.isEmpty());
        assertFalse(list.contains(5));
    }

    @Test
    void testAddAndRemoveAtBoundaries() {
        list.add(10); // [10]
        list.add(0, 5); // [5, 10]
        list.add(20); // [5, 10, 20]

        assertEquals(3, list.size());
        assertEquals(5, list.get(0));
        assertEquals(20, list.get(2));

        assertEquals(5, list.remove(0));
        assertEquals(20, list.remove(list.size() - 1));
        assertEquals(1, list.size());
        assertEquals(10, list.get(0));


        list.add(30); // [10, 30]
        assertEquals(30, list.get(1));
        list.add(40); // [10, 30, 40]
        assertEquals(40, list.get(2));
    }

    @Test
    void testInvalidIndices() {
        list.add(5);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(2, 10));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
    }

    @Test
    void testCounterBehavior() {
        list.add(10);
        list.add(20);
        list.add(30);
        list.add(40);
        list.add(50);

        counter.reset();
        int val = list.get(3); 
        assertEquals(40, val);
        assertEquals(3, counter.getSteps());
    }

    @Test
    void testRandomizedComparisonWithArrayList() {
        ArrayList<Integer> reference = new ArrayList<>();
        Random random = new Random(42);

        for (int i = 0; i < 200; i++) {
            int val = random.nextInt(1000);
            list.add(val);
            reference.add(val);
        }

        assertEquals(reference.size(), list.size());

        for (int i = 0; i < reference.size(); i++) {
            assertEquals(reference.get(i), list.get(i));
        }

        for (int i = 0; i < 20; i++) {
            int idx = random.nextInt(reference.size());
            assertEquals(reference.remove(idx), list.remove(idx));
        }

        assertEquals(reference.size(), list.size());
    }
}