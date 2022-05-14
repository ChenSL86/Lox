package com.craftinginterpreters.lox.declaration;

import com.craftinginterpreters.lox.stmt.Stmt;

public abstract class Declaration {
    public abstract void accept(DeclarationVisitor visitor) throws Exception;

    public static class StmtDeclaration extends Declaration {
        private Stmt stmt;

        public StmtDeclaration(Stmt stmt) {
            this.stmt = stmt;
        }

        @Override
        public void accept(DeclarationVisitor visitor) throws Exception {
            visitor.visitStmt(this.stmt);
        }
    }
}
