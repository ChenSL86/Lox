package com.craftinginterpreters.lox.stmt;

public interface StmtVisitor {
    void visitExprStmt(Stmt.ExprStmt exprStmt) throws Exception;

    void visitPrintStmt(Stmt.PrintStmt exprStmt) throws Exception;
}
