package com.craftinginterpreters.lox.test;

import com.craftinginterpreters.lox.expr.Expr;
import com.craftinginterpreters.lox.interpreter.Interpreter;
import com.craftinginterpreters.lox.parser.Parser;
import com.craftinginterpreters.lox.scanner.Scanner;
import com.craftinginterpreters.lox.scanner.Token;

import java.util.List;

public class InterpreterTest {
    public static void main(String[] args) throws Exception {
        String testProgram = " 1 < 2 and 2 > 3";

        Scanner scanner = new Scanner(testProgram);
        scanner.scan();
        List<Token> tokenList = scanner.getTokenList();
        System.out.println(tokenList);

        Parser parser = new Parser(tokenList);
        Expr expr = parser.expression();
        System.out.println(expr);

        Interpreter interpreter = new Interpreter();
        System.out.println(interpreter.interprete(expr));
    }
}
