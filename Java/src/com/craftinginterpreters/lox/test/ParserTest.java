package com.craftinginterpreters.lox.test;

import com.craftinginterpreters.lox.parser.Parser;
import com.craftinginterpreters.lox.scanner.Scanner;
import com.craftinginterpreters.lox.scanner.Token;

import java.util.List;

public class ParserTest {
    public static void main(String[] args) {
        String testProgram = "(1+2)*(3+!-4)";

        Scanner scanner = new Scanner(testProgram);
        scanner.scan();
        List<Token> tokenList = scanner.getTokenList();
        System.out.println(tokenList);

        Parser parser = new Parser(tokenList);
        parser.parse();
        System.out.println(parser.getWorkingList());
        System.out.println(parser.getParseResult());
    }
}
