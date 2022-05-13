package com.craftinginterpreters.lox.expr;

import com.craftinginterpreters.lox.token.Token;

public abstract class Expr {
    public abstract <R> R accept(ExprVisitor<R> visitor) throws Exception;

    public static class Literal extends Expr {
        private final Token token;

        public Literal(Token token) {
            this.token = token;
        }

        public Token getToken() {
            return token;
        }

        @Override
        public String toString() {
            return token.getText();
        }

        public <R> R accept(ExprVisitor<R> visitor) throws Exception {
            return visitor.visitLiteral(this);
        }
    }

    public static class Unary extends Expr {
        private final Token operator;
        private final Expr right;

        public Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        public Token getOperator() {
            return operator;
        }

        public Expr getRight() {
            return right;
        }

        @Override
        public String toString() {
            return String.format("[%s%s]", operator.getText(), right);
        }

        public <R> R accept(ExprVisitor<R> visitor) throws Exception {
            return visitor.visitUnary(this);
        }
    }

    public static class Binary extends Expr {
        private final Expr left;
        private final Token operator;
        private final Expr right;

        public Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        public Expr getLeft() {
            return left;
        }

        public Token getOperator() {
            return operator;
        }

        public Expr getRight() {
            return right;
        }

        @Override
        public String toString() {
            return String.format("[%s %s %s]", left, operator.getText(), right);
        }

        public <R> R accept(ExprVisitor<R> visitor) throws Exception {
            return visitor.visitBinary(this);
        }
    }

    public static class Grouping extends Expr {
        private final Expr expr;

        public Grouping(Expr expr) {
            this.expr = expr;
        }

        public Expr getExpr() {
            return expr;
        }

        @Override
        public String toString() {
            return String.format("(%s)", expr);
        }

        public <R> R accept(ExprVisitor<R> visitor) throws Exception {
            return visitor.visitGrouping(this);
        }
    }
}
