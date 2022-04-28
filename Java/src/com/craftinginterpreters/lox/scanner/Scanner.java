package com.craftinginterpreters.lox.scanner;

import java.util.ArrayList;
import java.util.List;

import static com.craftinginterpreters.lox.scanner.Type.*;

public class Scanner {
    private final String source;
    private final List<Token> tokenList = new ArrayList<>();
    private int cursor;
    private int lineNumber;
    private boolean hasError;

    public Scanner(String source) {
        this.source = source;
    }

    public List<Token> getTokenList() {
        return new ArrayList<>(tokenList);
    }

    private boolean isAlphabetic(char c) {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_';
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }


    private String peekTwo() {
        if (cursor + 2 > source.length()) {
            return null;
        }

        return source.substring(cursor, cursor + 2);
    }

    private char advance() {
        char ret;

        if (cursor < source.length()) {
            ret = source.charAt(cursor);
        } else {
            ret = '\0';
        }

        cursor++;

        return ret;
    }

    private void consume(int count) {
        if (cursor < 0 || cursor >= source.length()
                || cursor + count < 1 || cursor + count > source.length()) {
            error("Scanner.scan() bug.");
            return;
        }

        String s = source.substring(cursor, cursor + count);
        Type type = Type.from(s);
        if (type == null) {
            char c = s.charAt(0);
            if (isAlphabetic(c)) {
                type = IDENTIFIER;
            } else if (c == '"' && s.charAt(count - 1) == '"') {
                type = STRING;
            } else if (isDigit(c)) {
                type = NUMBER;
            } else {
                error("Scanner.scan() bug.");
            }
        }

        tokenList.add(new Token(type, s));
        cursor += count;
    }

    private void error(String msg) {
        hasError = true;

        String[] lines = source.split("\n");

        int columnNumber = cursor;
        for (int i = 0; i < lineNumber; i++) {
            columnNumber -= lines[i].length();
            columnNumber--;
        }

        System.out.println(String.format("Error in scanning, line %d, column %d.", lineNumber, columnNumber));
        System.out.println(lines[lineNumber]);
        for (int i = 0; i < columnNumber; i++) {
            System.out.print(" ");
        }
        System.out.println("^ " + (msg != null ? msg : ""));
        System.exit(65);
    }

    private void handleComment() {
        advance();
        advance();
        while (true) {
            char c = advance();
            if (c == '\n') {
                lineNumber++;
                break;
            }
            if (c == '\0') {
                break;
            }
        }
    }

    private void handleString() {
        int mark = cursor;
        advance();
        while (true) {
            char c = advance();
            if (c == '"') {
                break;
            } else if (c == '\n') {
                lineNumber++;
            } else if (c == '\0') {
                error("Incomplete string.");
                return;
            }
        }
        int consumeCount = cursor - mark;
        cursor = mark;
        consume(consumeCount);
    }

    private void handleIdentifierOrKeyword() {
        int mark = cursor;
        advance();
        while (true) {
            char c = advance();
            if (!isAlphabetic(c) && !isDigit(c)) {
                break;
            }
        }
        int consumeCount = cursor - mark - 1;
        cursor = mark;
        consume(consumeCount);
    }

    private void handleNumber() {
        int mark = cursor;
        advance();

        boolean dotPassed = false;
        while (true) {
            char c = advance();
            if (dotPassed) {
                if (!Character.isDigit(c)) {
                    break;
                }
            } else {
                if (!Character.isDigit(c) && c != '.') {
                    break;
                }
                if (c == '.') {
                    dotPassed = true;
                }
            }
        }
        int consumeCount = cursor - mark - 1;
        cursor = mark;
        consume(consumeCount);
    }

    private void handleOperatorOrPunctuation() {
        Type type = Type.from(source.substring(cursor, cursor + 1));
        if (type != null) {
            if (type.peekTwoRequired() && Type.from(peekTwo()) != null) {
                consume(2);
            } else {
                consume(1);
            }
        } else {
            error("Char not allowed here.");
        }
    }

    public void scan() {
        while (cursor < source.length()) {
            char c = source.charAt(cursor);

            if (c == ' ' || c == '\t' || c == '\r') {
                advance();
            } else if (c == '\n') {
                lineNumber++;
                advance();
            } else if (c == '/' && "//".equals(peekTwo())) {
                handleComment();
            } else if (c == '"') {
                handleString();
            } else if (isAlphabetic(c)) {
                handleIdentifierOrKeyword();
            } else if (isDigit(c)) {
                handleNumber();
            } else {
                handleOperatorOrPunctuation();
            }
        }

        tokenList.add(new Token(EOF, ""));
    }
}
