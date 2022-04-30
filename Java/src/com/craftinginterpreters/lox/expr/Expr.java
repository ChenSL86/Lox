package com.craftinginterpreters.lox.expr;

import com.craftinginterpreters.lox.scanner.Token;

public abstract class Expr {
    public static class Literal extends Expr {
        private final Token token;

        public Literal(Token token) {
            this.token = token;
        }

        @Override
        public String toString() {
            return token.getText();
        }
    }

    public static class Unary extends Expr {
        private final Token operator;
        private final Expr right;

        public Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        public String toString() {
            return String.format("[%s%s]", operator.getText(), right);
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

        @Override
        public String toString() {
            return String.format("[%s %s %s]", left, operator.getText(), right);
        }
    }

    public static class Grouping extends Expr {
        private final Expr expr;

        public Grouping(Expr expr) {
            this.expr = expr;
        }

        @Override
        public String toString() {
            return String.format("(%s)", expr);
        }
    }
}
