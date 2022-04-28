package com.craftinginterpreters.lox.scanner;

public class Token {
    private final Type type;
    private final String source;

    Token(Type type, String source) {
        this.type = type;
        this.source = source;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", type, source);
    }
}
