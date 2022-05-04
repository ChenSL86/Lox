package com.craftinginterpreters.lox.test;

import com.craftinginterpreters.lox.interpreter.Interpreter;
import com.craftinginterpreters.lox.parser.Parser;
import com.craftinginterpreters.lox.scanner.Scanner;
import com.craftinginterpreters.lox.scanner.Token;

import java.util.List;

public class InterpreterTest {
    public static void main(String[] args) throws Exception {
        String testProgram = "true and false";

        Scanner scanner = new Scanner(testProgram);
        scanner.scan();
        List<Token> tokenList = scanner.getTokenList();
        System.out.println(tokenList);

        Parser parser = new Parser(tokenList);
        parser.parse();
        System.out.println(parser.getWorkingList());
        System.out.println(parser.getParseResult());

        Interpreter interpreter = new Interpreter();
        System.out.println(interpreter.interprete(parser.getParseResult()));
    }
}
