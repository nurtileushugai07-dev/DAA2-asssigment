package com.daa;

public class MinHeap {
    private int[] data;
    private int size;
    private final OperationCounter counter;

    public MinHeap(int capacity, OperationCounter counter) {
        if (capacity <= 0) capacity = 10;
        this.data = new int[capacity];
        this.size = 0;
        this.counter = counter;
    }

    public MinHeap(OperationCounter counter) {
        this(10, counter);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void insert(int x) {
        if (size == data.length) {
            resize();
        }
        data[size] = x;
        counter.addStep();
        counter.addMove();
        bubbleUp(size);
        size++;
    }

    public int peekMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        counter.addStep();
        return data[0];
    }

    public int extractMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        int min = data[0];
        counter.addStep();

        data[0] = data[size - 1];
        counter.addStep();
        counter.addMove();

        size--;
        if (size > 0) {
            bubbleDown(0);
        }
        return min;
    }

    private void bubbleUp(int index) {
        int curr = index;
        while (curr > 0) {
            int parent = (curr - 1) / 2;
            counter.addStep(); // доступ к родителю
            counter.addComparison();
            if (data[parent] > data[curr]) {
                swap(curr, parent);
                curr = parent;
            } else {
                break;
            }
        }
    }

    private void bubbleDown(int index) {
        int curr = index;
        while (2 * curr + 1 < size) {
            int left = 2 * curr + 1;
            int right = left + 1;
            int smallest = left;

            counter.addStep();
            counter.addStep();

            if (right < size) {
                counter.addComparison();
                if (data[right] < data[left]) {
                    smallest = right;
                }
            }

            counter.addComparison();
            if (data[curr] > data[smallest]) {
                swap(curr, smallest);
                curr = smallest;
            } else {
                break;
            }
        }
    }

    private void swap(int i, int j) {
        int temp = data[i];
        data[i] = data[j];
        data[j] = temp;
        counter.addMoves(3);
    }

    private void resize() {
        int newCapacity = data.length * 2;
        int[] newData = new int[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
            counter.addStep();
            counter.addMove();
        }
        data = newData;
    }

    public boolean isHeap() {
        for (int i = 0; i < size; i++) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            if (left < size && data[i] > data[left]) return false;
            if (right < size && data[i] > data[right]) return false;
        }
        return true;
    }
}