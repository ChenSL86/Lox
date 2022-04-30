package com.craftinginterpreters.lox.parser.util;

import com.craftinginterpreters.lox.scanner.Token;

public class TokenContainer implements Container<Token> {
    private final Container<Token> container;
    private final int operandCountRequired;

    public TokenContainer(Container<Token> container, int operandCountRequired) {
        this.container = container;
        this.operandCountRequired = operandCountRequired;
    }

    public int getOperandCountRequired() {
        return operandCountRequired;
    }

    @Override
    public Token peek() {
        return container.peek();
    }

    @Override
    public Token pop() {
        return container.pop();
    }

    @Override
    public void push(Token token) {
        container.push(token);
    }

    @Override
    public int size() {
        return container.size();
    }
}
