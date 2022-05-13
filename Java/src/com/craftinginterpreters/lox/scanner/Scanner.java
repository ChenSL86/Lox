package com.craftinginterpreters.lox.scanner;

import com.craftinginterpreters.lox.token.Token;
import com.craftinginterpreters.lox.token.Type;

import java.util.ArrayList;
import java.util.List;

import static com.craftinginterpreters.lox.token.Type.*;

public class Scanner {
    private final String source;
    private final List<Token> tokenList = new ArrayList<>();
    private int mark;
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

    private char peek() {
        if (cursor < source.length()) {
            return source.charAt(cursor);
        } else {
            return '\0';
        }
    }

    private void consume() {
        if (mark >= source.length() || cursor < 1 || cursor > source.length()) {
            error("Scanner bug.");
            return;
        }

        String s = source.substring(mark, cursor);
        Type type = Type.from(s);
        if (type == null) {
            char c = s.charAt(0);
            if (isAlphabetic(c)) {
                type = IDENTIFIER;
            } else if (c == '"' && s.charAt(s.length() - 1) == '"') {
                type = STRING;
            } else if (isDigit(c)) {
                type = NUMBER;
            } else {
                error("Scanner bug.");
            }
        }

        tokenList.add(new Token(type, s, tokenList.size()));
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
        cursor++;

        while (true) {
            cursor++;
            char c = peek();
            if (c == '\n') {
                cursor++;
                lineNumber++;
                return;
            }
            if (c == '\0') {
                return;
            }
        }
    }

    private void handleString() {
        mark = cursor;

        while (true) {
            cursor++;
            char c = peek();
            if (c == '"') {
                cursor++;
                break;
            } else if (c == '\n') {
                lineNumber++;
            } else if (c == '\0') {
                error("Incomplete string.");
                return;
            }
        }

        consume();
    }

    private void handleIdentifierOrKeyword() {
        mark = cursor;

        while (true) {
            cursor++;
            char c = peek();
            if (!isAlphabetic(c) && !isDigit(c)) {
                break;
            }
        }

        consume();
    }

    private void handleNumber() {
        mark = cursor;

        boolean dotPassed = false;
        while (true) {
            cursor++;
            char c = peek();
            if (dotPassed) {
                if (!isDigit(c)) {
                    break;
                }
            } else {
                if (!isDigit(c) && c != '.') {
                    break;
                }
                if (c == '.') {
                    dotPassed = true;
                }
            }
        }

        consume();
    }

    private void handleOperatorOrPunctuation() {
        mark = cursor;

        Type type = Type.from(source.substring(cursor, cursor + 1));
        if (type != null) {
            if (type.peekTwoRequired() && Type.from(peekTwo()) != null) {
                cursor += 2;
                consume();
            } else {
                cursor += 1;
                consume();
            }
        } else {
            error("Char not allowed here.");
        }
    }

    public void scan() {
        while (cursor < source.length()) {
            char c = source.charAt(cursor);

            if (c == ' ' || c == '\t' || c == '\r') {
                cursor++;
            } else if (c == '\n') {
                cursor++;
                lineNumber++;
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

        tokenList.add(new Token(EOF, "", tokenList.size()));
    }
}
