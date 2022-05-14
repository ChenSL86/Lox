package com.craftinginterpreters.lox.test;

import com.craftinginterpreters.lox.declaration.Declaration;
import com.craftinginterpreters.lox.interpreter.Interpreter;
import com.craftinginterpreters.lox.parser.Parser;
import com.craftinginterpreters.lox.scanner.Scanner;
import com.craftinginterpreters.lox.token.Token;

import java.util.List;

public class InterpreterTest {
    public static void main(String[] args) throws Exception {
        String testProgram = " print aaa; ";

        Scanner scanner = new Scanner(testProgram);
        scanner.scan();
        List<Token> tokenList = scanner.getTokenList();
        System.out.println(tokenList);

        Parser parser = new Parser(tokenList);
        List<Declaration> program = parser.program();
        System.out.println(program);

        Interpreter interpreter = new Interpreter();
        interpreter.runProgram(program);

    }
}
