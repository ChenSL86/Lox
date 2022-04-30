package com.craftinginterpreters.lox.parser.util;

import java.util.LinkedList;

public interface Container<E> {
    E peek();

    E pop();

    void push(E e);

    int size();

    abstract class AbstractContainer<E> implements Container<E> {
        final LinkedList<E> list = new LinkedList<>();

        @Override
        public void push(E e) {
            list.add(e);
        }

        @Override
        public int size() {
            return list.size();
        }
    }
}
