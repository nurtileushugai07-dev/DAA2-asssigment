package com.daa;

public class DynamicArray {
    private int[] data;
    private int size;
    private final OperationCounter counter;

    public DynamicArray(int capacity, OperationCounter counter) {
        if (capacity <= 0) capacity = 10;
        this.data = new int[capacity];
        this.size = 0;
        this.counter = counter;
    }

    public DynamicArray(OperationCounter counter) {
        this(10, counter);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(int x) {
        if (size == data.length) {
            resize();
        }
        data[size] = x;
        size++;
    }

    public void add(int index, int x) {
        checkIndexForAdd(index);
        if (size == data.length) {
            resize();
        }
        // Сдвиг элементов вправо (чтение предыдущего и запись в следующий)
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
            counter.addStep(); // чтение data[i-1]
            counter.addMove(); // перенос элемента
        }
        data[index] = x;
        size++;
    }

    public int remove(int index) {
        checkIndex(index);
        int removed = data[index];

        // Сдвиг элементов влево
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
            counter.addStep(); // чтение data[i+1]
            counter.addMove(); // перенос элемента
        }
        size--;
        data[size] = 0; // обнуляем освободившуюся ячейку
        return removed;
    }

    public int get(int index) {
        checkIndex(index);
        counter.addStep(); // доступ к ячейке массива
        return data[index];
    }

    public boolean contains(int x) {
        for (int i = 0; i < size; i++) {
            counter.addStep();
            counter.addComparison();
            if (data[i] == x) {
                return true;
            }
        }
        return false;
    }

    private void resize() {
        int newCapacity = data.length * 2;
        int[] newData = new int[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
            counter.addStep(); // чтение старого массива
            counter.addMove(); // перенос в новый массив
        }
        data = newData;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}