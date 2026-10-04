package com.daa;

public class MyLinkedList implements IntList {
    private static class Node {
        int val;
        Node next;

        Node(int val) {
            this.val = val;
            this.next = null;
        }
    }

    private Node head;
    private Node tail;
    private int size;
    private final OperationCounter counter;

    public MyLinkedList(OperationCounter counter) {
        this.head = null;
        this.tail = null;
        this.size = 0;
        this.counter = counter;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(int x) {
        Node newNode = new Node(x);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            counter.addMove();
            tail = newNode;
        }
        size++;
    }

    public void add(int index, int x) {
        checkIndexForAdd(index);
        if (index == size) {
            add(x);
            return;
        }

        Node newNode = new Node(x);
        if (index == 0) {
            newNode.next = head;
            counter.addMove();
            head = newNode;
            counter.addMove();
        } else {
            Node prev = getNode(index - 1);
            newNode.next = prev.next;
            counter.addMove();
            prev.next = newNode;
            counter.addMove();
        }
        size++;
    }

    public int remove(int index) {
        checkIndex(index);
        int removedVal;

        if (index == 0) {
            removedVal = head.val;
            head = head.next;
            counter.addMove();
            if (head == null) {
                tail = null;
            }
        } else {
            Node prev = getNode(index - 1);
            Node target = prev.next;
            removedVal = target.val;
            prev.next = target.next;
            counter.addMove();
            if (target == tail) {
                tail = prev;
            }
        }
        size--;
        return removedVal;
    }

    public int get(int index) {
        checkIndex(index);
        Node node = getNode(index);
        return node.val;
    }

    public boolean contains(int x) {
        Node curr = head;
        while (curr != null) {
            counter.addStep();
            counter.addComparison();
            if (curr.val == x) {
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    private Node getNode(int index) {
        Node curr = head;
        for (int i = 0; i < index; i++) {
            counter.addStep();
            curr = curr.next;
        }
        return curr;
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
