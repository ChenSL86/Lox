package com.craftinginterpreters.lox.test;

import com.craftinginterpreters.lox.scanner.Scanner;

public class Test {
    public static void main(String[] args) {
        String testProgram =
                "\n" +
                        "// Test\n" +
                        "class Test {       \n" +
                        "  var abc = 2 + 1.3 * 4;\n" +
                        "  var s = \"hello\"    \n" +
                        "  3<=5;   8.8.8   abc_def  _ok  v8  #  " +
                        "}";
        System.out.println("------");
        System.out.println(testProgram);
        System.out.println("======");
        Scanner scanner = new Scanner(testProgram);
        scanner.scan();
        System.out.println(scanner.getTokenList());
    }
}
