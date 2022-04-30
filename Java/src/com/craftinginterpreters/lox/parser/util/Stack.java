package com.craftinginterpreters.lox.parser.util;

public class Stack<E> extends Container.AbstractContainer<E> {
    @Override
    public E peek() {
        return list.getLast();
    }

    @Override
    public E pop() {
        return list.pollLast();
    }
}
