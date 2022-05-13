package com.craftinginterpreters.lox.test;

import com.craftinginterpreters.lox.parser.Parser;
import com.craftinginterpreters.lox.scanner.Scanner;
import com.craftinginterpreters.lox.token.Token;

import java.util.List;

public class ParserTest {
    public static void main(String[] args) {
        String testProgram = "(1+2)*(3+!-4)";

        Scanner scanner = new Scanner(testProgram);
        scanner.scan();
        List<Token> tokenList = scanner.getTokenList();
        System.out.println(tokenList);

        Parser parser = new Parser(tokenList);
        System.out.println(parser.expression());

        com.craftinginterpreters.lox.parser_deprecated.Parser parser1
                = new com.craftinginterpreters.lox.parser_deprecated.Parser(tokenList);
        parser1.parse();
        System.out.println(parser1.getParseResult());
    }
}
