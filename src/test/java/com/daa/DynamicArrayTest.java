package com.daa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DynamicArrayTest {

    private OperationCounter counter;
    private DynamicArray dynamicArray;

    @BeforeEach
    void setUp() {
        counter = new OperationCounter();
        dynamicArray = new DynamicArray(2, counter);
    }

    @Test
    void testEmptyArray() {
        assertTrue(dynamicArray.isEmpty());
        assertEquals(0, dynamicArray.size());
        assertThrows(IndexOutOfBoundsException.class, () -> dynamicArray.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> dynamicArray.remove(0));
    }

    @Test
    void testSingleElement() {
        dynamicArray.add(42);
        assertFalse(dynamicArray.isEmpty());
        assertEquals(1, dynamicArray.size());
        assertEquals(42, dynamicArray.get(0));
        assertTrue(dynamicArray.contains(42));
        assertFalse(dynamicArray.contains(100));

        assertEquals(42, dynamicArray.remove(0));
        assertTrue(dynamicArray.isEmpty());
    }

    @Test
    void testDuplicatesAndResizing() {
        for (int i = 0; i < 100; i++) {
            dynamicArray.add(10);
        }
        assertEquals(100, dynamicArray.size());
        for (int i = 0; i < 100; i++) {
            assertEquals(10, dynamicArray.get(i));
        }
    }

    @Test
    void testAddAndRemoveAtIndex() {
        dynamicArray.add(10); // [10]
        dynamicArray.add(20); // [10, 20]
        dynamicArray.add(1, 15); // [10, 15, 20]

        assertEquals(3, dynamicArray.size());
        assertEquals(10, dynamicArray.get(0));
        assertEquals(15, dynamicArray.get(1));
        assertEquals(20, dynamicArray.get(2));

        assertEquals(15, dynamicArray.remove(1)); // [10, 20]
        assertEquals(2, dynamicArray.size());
        assertEquals(20, dynamicArray.get(1));
    }

    @Test
    void testInvalidIndices() {
        dynamicArray.add(5);
        assertThrows(IndexOutOfBoundsException.class, () -> dynamicArray.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> dynamicArray.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> dynamicArray.add(2, 10));
        assertThrows(IndexOutOfBoundsException.class, () -> dynamicArray.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> dynamicArray.remove(1));
    }

    @Test
    void testRandomizedComparisonWithArrayList() {
        ArrayList<Integer> reference = new ArrayList<>();
        Random random = new Random(42);

        for (int i = 0; i < 500; i++) {
            int val = random.nextInt(1000);
            dynamicArray.add(val);
            reference.add(val);
        }

        assertEquals(reference.size(), dynamicArray.size());

        for (int i = 0; i < reference.size(); i++) {
            assertEquals(reference.get(i), dynamicArray.get(i));
        }

        for (int i = 0; i < 50; i++) {
            int idx = random.nextInt(reference.size());
            assertEquals(reference.remove(idx), dynamicArray.remove(idx));
        }

        assertEquals(reference.size(), dynamicArray.size());

    }
    @Test
    void testFirstAndLastIndex() {
        dynamicArray.add(1);
        dynamicArray.add(2);
        dynamicArray.add(0, 0);      // вставка в начало: [0,1,2]
        dynamicArray.add(3, 3);      // вставка в конец:  [0,1,2,3]
        assertEquals(0, dynamicArray.get(0));
        assertEquals(3, dynamicArray.get(3));
        assertEquals(0, dynamicArray.remove(0));
        assertEquals(3, dynamicArray.remove(2));
        assertEquals(2, dynamicArray.size());
    }

    @Test
    void testCounters() {
        dynamicArray.add(1);
        dynamicArray.add(2);
        counter.reset();
        dynamicArray.add(0, 9);
        assertEquals(4, counter.getMoves());
        counter.reset();
        dynamicArray.get(1);
        assertEquals(1, counter.getSteps());
    }
}