package com.craftinginterpreters.lox.expr;

public interface ExprVisitor<R> {
    R visitLiteral(Expr.Literal literal) throws Exception;

    R visitUnary(Expr.Unary unary) throws Exception;

    R visitBinary(Expr.Binary binary) throws Exception;

    R visitGrouping(Expr.Grouping grouping) throws Exception;
}
