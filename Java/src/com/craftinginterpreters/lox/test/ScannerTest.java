package com.craftinginterpreters.lox.test;

import com.craftinginterpreters.lox.scanner.Scanner;

public class ScannerTest {
    public static void main(String[] args) {
        String testProgram =
                "\n" +
                        "// ScannerTest\n" +
                        "class ScannerTest {       \n" +
                        "  var abc = 2 + 1.3 * 4;\n" +
                        "  var s = \"hello\"    \n" +
                        "  3<=5;   8.8.8   abc_def  _ok  v8    " +
                        "}";
       // testProgram = "//   ;";
        System.out.println("------");
        System.out.println(testProgram);
        System.out.println("======");
        Scanner scanner = new Scanner(testProgram);
        scanner.scan();
        System.out.println(scanner.getTokenList());
    }
}
