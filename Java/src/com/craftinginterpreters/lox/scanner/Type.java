package com.craftinginterpreters.lox.scanner;

import java.util.HashMap;
import java.util.Map;

enum Type {
    // Single-character tokens.
    LEFT_PAREN("("), RIGHT_PAREN(")"), LEFT_BRACE("{"), RIGHT_BRACE("}"),
    COMMA(","), DOT("."), MINUS("-"), PLUS("+"),
    SEMICOLON(";"), SLASH("/"), STAR("*"),

    // One or two character tokens.
    BANG("!"), BANG_EQUAL("!="),
    EQUAL("="), EQUAL_EQUAL("=="),
    GREATER(">"), GREATER_EQUAL(">="),
    LESS("<"), LESS_EQUAL("<="),

    // Literals.
    IDENTIFIER(null), STRING(null), NUMBER(null),

    // Keywords.
    AND("and"), CLASS("class"), ELSE("else"), FALSE("false"),
    FUN("fun"), FOR("for"), IF("if"), NIL("nil"),
    OR("or"), PRINT("print"), RETURN("return"), SUPER("super"),
    THIS("this"), TRUE("true"), VAR("var"), WHILE("while"),

    EOF(null);

    private static Map<String, Type> map = new HashMap<>();

    static {
        Type[] values = values();
        for (Type type : values) {
            if (type.text != null) {
                map.put(type.text, type);
            }
        }
    }

    private String text;

    Type(String text) {
        this.text = text;
    }

    static Type from(String text) {
        return map.get(text);
    }

    String getText() {
        return text;
    }

    boolean peekTwoRequired() {
        return this == EQUAL || this == LESS || this == GREATER || this == BANG;
    }
}
