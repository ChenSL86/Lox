package com.craftinginterpreters.lox.declaration;

import com.craftinginterpreters.lox.stmt.Stmt;

public interface DeclarationVisitor {
    void visitStmt(Stmt stmt) throws Exception;
}
