package com.craftinginterpreters.lox.interpreter;

import com.craftinginterpreters.lox.expr.Expr;
import com.craftinginterpreters.lox.expr.ExprVisitor;
import com.craftinginterpreters.lox.token.Token;
import com.craftinginterpreters.lox.token.Type;

import java.util.Objects;

public class Interpreter implements ExprVisitor<Object> {
    @Override
    public Object visitLiteral(Expr.Literal literal) throws Exception {
        Object ret = null;

        Token token = literal.getToken();
        switch (token.getType()) {
            case IDENTIFIER:
                //todo
                break;
            case STRING:
                ret = token.getText().substring(1, token.getText().length() - 1);
                break;
            case NUMBER:
                ret = Double.parseDouble(token.getText());
                break;
            case TRUE:
                ret = true;
                break;
            case FALSE:
                ret = false;
                break;
            case NIL:
                ret = null;
                break;
            default:
                throw new InterpreterException("Bug.");
        }

        return ret;
    }

    @Override
    public Object visitUnary(Expr.Unary unary) throws Exception {
        Object ret;

        Token operator = unary.getOperator();
        if (operator.getType() == Type.BANG) {
            Object rightValue = interprete(unary.getRight());
            if (rightValue instanceof Boolean) {
                ret = !(Boolean) rightValue;
            } else {
                throw new Exception("Type error : ! should be followed by Boolean.");
            }
        } else if (operator.getType() == Type.MINUS) {
            Object rightValue = interprete(unary.getRight());
            if (rightValue instanceof Double) {
                ret = -(Double) rightValue;
            } else {
                throw new Exception("Type error : - should be followed by number.");
            }
        } else {
            throw new InterpreterException("Bug.");
        }

        return ret;
    }

    private boolean checkType(Class klass, Object... objects) {
        for (Object object : objects) {
            if (!klass.isInstance(object)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Object visitBinary(Expr.Binary binary) throws Exception {
        Object ret;

        Token operator = binary.getOperator();
        Object leftValue = interprete(binary.getLeft());
        Object rightValue = null;
        if (!checkType(Boolean.class, leftValue)) {
            rightValue = interprete(binary.getRight());
        }

        switch (operator.getType()) {
            case PLUS:
                if (checkType(Double.class, leftValue, rightValue)) {
                    ret = (Double) leftValue + (Double) rightValue;
                } else if (checkType(String.class, leftValue)) {
                    ret = leftValue.toString() + rightValue.toString();
                } else {
                    throw new Exception("Type error : + requires numbers of strings.");
                }
                break;

            case MINUS:
                if (checkType(Double.class, leftValue, rightValue)) {
                    ret = (Double) leftValue - (Double) rightValue;
                } else {
                    throw new Exception("Type error : + requires numbers.");
                }
                break;

            case STAR:
                if (checkType(Double.class, leftValue, rightValue)) {
                    ret = (Double) leftValue * (Double) rightValue;
                } else {
                    throw new Exception("Type error : * requires numbers.");
                }
                break;

            case SLASH:
                if (checkType(Double.class, leftValue, rightValue)) {
                    ret = (Double) leftValue / (Double) rightValue;
                } else {
                    throw new Exception("Type error : / requires numbers.");
                }
                break;

            case EQUAL_EQUAL:
                return Objects.equals(leftValue, rightValue);

            case BANG_EQUAL:
                return !Objects.equals(leftValue, rightValue);

            case GREATER:
                if (checkType(Double.class, leftValue, rightValue)) {
                    ret = (Double) leftValue > (Double) rightValue;
                } else {
                    throw new Exception("Type error : > requires numbers.");
                }
                break;

            case GREATER_EQUAL:
                if (checkType(Double.class, leftValue, rightValue)) {
                    ret = (Double) leftValue >= (Double) rightValue;
                } else {
                    throw new Exception("Type error : >= requires numbers.");
                }
                break;

            case LESS:
                if (checkType(Double.class, leftValue, rightValue)) {
                    ret = (Double) leftValue < (Double) rightValue;
                } else {
                    throw new Exception("Type error : < requires numbers.");
                }
                break;

            case LESS_EQUAL:
                if (checkType(Double.class, leftValue, rightValue)) {
                    ret = (Double) leftValue <= (Double) rightValue;
                } else {
                    throw new Exception("Type error : <= requires numbers.");
                }
                break;

            case AND:
                if (checkType(Boolean.class, leftValue)) {
                    if ((Boolean) leftValue) {
                        rightValue = interprete(binary.getRight());
                        if (checkType(Boolean.class, rightValue)) {
                            ret = rightValue;
                        } else {
                            throw new Exception("Type error : and requires Booleans.");
                        }
                    } else {
                        ret = false;
                    }
                } else {
                    throw new Exception("Type error : and requires Booleans.");
                }
                break;

            case OR:
                if (checkType(Boolean.class, leftValue)) {
                    if ((Boolean) leftValue) {
                        ret = true;
                    } else {
                        rightValue = interprete(binary.getRight());
                        if (checkType(Boolean.class, rightValue)) {
                            ret = rightValue;
                        } else {
                            throw new Exception("Type error : or requires Booleans.");
                        }
                    }
                } else {
                    throw new Exception("Type error : or requires Booleans.");
                }
                break;

            default:
                throw new Exception(operator.getType() + " not supported.");
        }

        return ret;
    }

    @Override
    public Object visitGrouping(Expr.Grouping grouping) throws Exception {
        return interprete(grouping.getExpr());
    }

    public Object interprete(Expr expr) throws Exception {
        return expr.accept(this);
    }
}
