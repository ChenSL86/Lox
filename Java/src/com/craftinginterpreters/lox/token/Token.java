package com.craftinginterpreters.lox.token;

public class Token {
    private final Type type;
    private final String text;
    private final int indexInTokenList;

    public Token(Type type, String text, int indexInTokenList) {
        this.type = type;
        this.text = text;
        this.indexInTokenList = indexInTokenList;
    }

    public Type getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public int getIndexInTokenList() {
        return indexInTokenList;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", type, text);
    }
}
