package com.daa;

public interface IntList {
    int size();
    boolean isEmpty();
    void add(int x);
    void add(int index, int x);
    int remove(int index);
    int get(int index);
    boolean contains(int x);
}
