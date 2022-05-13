package com.craftinginterpreters.lox.parser_deprecated.util;

import com.craftinginterpreters.lox.expr.Expr;
import com.craftinginterpreters.lox.token.Token;

import java.util.function.Consumer;

public class TokenExprChoice {
    private static final TokenExprChoice EMPTY = new TokenExprChoice(null);
    private final Object obj;

    private TokenExprChoice(Object obj) {
        this.obj = obj;
    }

    public static TokenExprChoice from(Object obj) {
        if (obj instanceof Token || obj instanceof Expr) {
            return new TokenExprChoice(obj);
        } else {
            return EMPTY;
        }
    }

    public static TokenExprChoice empty() {
        return EMPTY;
    }

    public TokenExprChoice consumeAsToken(Consumer<? super Token> consumer) {
        if (obj instanceof Token) {
            consumer.accept((Token) obj);
        }

        return this;
    }

    public TokenExprChoice consumeAsExpr(Consumer<? super Expr> consumer) {
        if (obj instanceof Expr) {
            consumer.accept((Expr) obj);
        }

        return this;
    }

    public TokenExprChoice consumeAsNull(Consumer<?> consumer) {
        if (obj == null) {
            consumer.accept(null);
        }

        return this;
    }


}
