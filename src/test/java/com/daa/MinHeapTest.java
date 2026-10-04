package com.daa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.PriorityQueue;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MinHeapTest {

    private OperationCounter counter;
    private MinHeap heap;

    @BeforeEach
    void setUp() {
        counter = new OperationCounter();
        heap = new MinHeap(4, counter);
    }

    @Test
    void testEmptyHeap() {
        assertTrue(heap.isEmpty());
        assertEquals(0, heap.size());
        assertThrows(IllegalStateException.class, () -> heap.peekMin());
        assertThrows(IllegalStateException.class, () -> heap.extractMin());
    }

    @Test
    void testSingleElement() {
        heap.insert(10);
        assertFalse(heap.isEmpty());
        assertEquals(1, heap.size());
        assertEquals(10, heap.peekMin());
        assertTrue(heap.isHeap());

        assertEquals(10, heap.extractMin());
        assertTrue(heap.isEmpty());
    }

    @Test
    void testHeapPropertyAndSorting() {
        int[] values = {45, 20, 10, 30, 5, 50, 15};
        for (int v : values) {
            heap.insert(v);
            assertTrue(heap.isHeap());
        }

        assertEquals(values.length, heap.size());

        int prevMin = Integer.MIN_VALUE;
        while (!heap.isEmpty()) {
            int currentMin = heap.extractMin();
            assertTrue(currentMin >= prevMin, "Extracted elements must be in non-decreasing order");
            prevMin = currentMin;
            if (!heap.isEmpty()) {
                assertTrue(heap.isHeap());
            }
        }
    }

    @Test
    void testRandomizedComparisonWithPriorityQueue() {
        PriorityQueue<Integer> reference = new PriorityQueue<>();
        Random random = new Random(42);

        for (int i = 0; i < 300; i++) {
            int val = random.nextInt(1000);
            heap.insert(val);
            reference.add(val);
            assertTrue(heap.isHeap());
        }

        assertEquals(reference.size(), heap.size());

        while (!reference.isEmpty()) {
            assertEquals(reference.poll(), heap.extractMin());
        }

        assertTrue(heap.isEmpty());
    }

    @Test
    void testDuplicates() {
        heap.insert(5); heap.insert(5); heap.insert(5);
        assertEquals(5, heap.extractMin());
        assertEquals(5, heap.extractMin());
        assertEquals(5, heap.extractMin());
        assertTrue(heap.isEmpty());
    }
}