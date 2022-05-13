package com.craftinginterpreters.lox.parser_deprecated.util;

public class Queue<E> extends Container.AbstractContainer<E> {
    @Override
    public E peek() {
        return list.getFirst();
    }

    @Override
    public E pop() {
        return list.pollFirst();
    }
}
