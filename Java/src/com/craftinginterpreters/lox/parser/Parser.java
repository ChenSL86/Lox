package com.craftinginterpreters.lox.parser;

import com.craftinginterpreters.lox.expr.Expr;
import com.craftinginterpreters.lox.token.Token;
import com.craftinginterpreters.lox.token.Type;

import java.util.List;

public class Parser {
    private List<Token> tokenList;

    private int cursor;

    public Parser(List<Token> tokenList) {
        this.tokenList = tokenList;
    }

    public Expr expression() {
        return assignment();
    }

    //todo
    private Expr assignment() {
        return logicOr();
    }

    private Expr logicOr() {
        Expr expr = logicAnd();

        while (match(peek(), Type.OR)) {
            Token token = peek();
            cursor++;
            Expr right = logicAnd();
            expr = new Expr.Binary(expr, token, right);
        }

        return expr;
    }

    private Expr logicAnd() {
        Expr expr = equality();

        while (match(peek(), Type.AND)) {
            Token token = peek();
            cursor++;
            Expr right = equality();
            expr = new Expr.Binary(expr, token, right);
        }

        return expr;
    }

    private Expr equality() {
        Expr expr = comparison();

        while (match(peek(), Type.BANG_EQUAL, Type.EQUAL_EQUAL)) {
            Token token = peek();
            cursor++;
            Expr right = comparison();
            expr = new Expr.Binary(expr, token, right);
        }

        return expr;
    }

    private Expr comparison() {
        Expr expr = term();

        while (match(peek(),
                Type.GREATER, Type.GREATER_EQUAL,
                Type.LESS, Type.LESS_EQUAL)) {
            Token token = peek();
            cursor++;
            Expr right = term();
            expr = new Expr.Binary(expr, token, right);
        }

        return expr;
    }


    private Expr term() {
        Expr expr = factor();

        while (match(peek(), Type.MINUS, Type.PLUS)) {
            Token token = peek();
            cursor++;
            Expr right = factor();
            expr = new Expr.Binary(expr, token, right);
        }

        return expr;
    }

    private Expr factor() {
        Expr expr = unary();

        while (match(peek(), Type.SLASH, Type.STAR)) {
            Token token = peek();
            cursor++;
            Expr right = unary();
            expr = new Expr.Binary(expr, token, right);
        }

        return expr;
    }

    private Expr unary() {
        if (match(peek(), Type.BANG, Type.MINUS)) {
            Token token = peek();
            cursor++;
            Expr right = unary();
            return new Expr.Unary(token, right);
        } else {
            return call();
        }
    }

    // todo
    private Expr call() {
        return primary();
    }

    private Expr primary() {
        if (match(peek(),
                Type.TRUE, Type.FALSE, Type.NIL, //todo
                Type.NUMBER, Type.STRING, Type.IDENTIFIER)) {
            Token token = peek();
            cursor++;
            return new Expr.Literal(token);
        } else if (match(peek(), Type.LEFT_PAREN)) {
            cursor++;
            Expr expr = expression();
            if (match(peek(), Type.RIGHT_PAREN)) {
                cursor++;
                return new Expr.Grouping(expr);
            } else {
                error("Right paren required.");
            }
        } else {
            error("Token not expected.");
        }

        return null;
    }


    private void error(String msg) {
        throw new RuntimeException(msg);
    }


    private Token peek() {
        if (cursor < tokenList.size()) {
            return tokenList.get(cursor);
        } else {
            error("Bug.");
            return null;
        }
    }

    private boolean match(Token token, Type... types) {
        for (Type type : types) {
            if (token.getType().equals(type)) {
                return true;
            }
        }

        return false;
    }
}
