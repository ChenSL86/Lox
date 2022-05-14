package com.craftinginterpreters.lox.stmt;


import com.craftinginterpreters.lox.expr.Expr;

public abstract class Stmt {
    public abstract void accept(StmtVisitor visitor) throws Exception;

    public static class ExprStmt extends Stmt {
        private Expr expr;

        public ExprStmt(Expr expr) {
            this.expr = expr;
        }

        public Expr getExpr() {
            return expr;
        }

        @Override
        public void accept(StmtVisitor visitor) throws Exception {
            visitor.visitExprStmt(this);
        }
    }

    public static class PrintStmt extends Stmt {
        private Expr expr;

        public PrintStmt(Expr expr) {
            this.expr = expr;
        }

        public Expr getExpr() {
            return expr;
        }

        @Override
        public void accept(StmtVisitor visitor) throws Exception {
            visitor.visitPrintStmt(this);
        }
    }
}
