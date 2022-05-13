package com.craftinginterpreters.lox.parser_deprecated;

import com.craftinginterpreters.lox.expr.Expr;
import com.craftinginterpreters.lox.parser_deprecated.util.Container;
import com.craftinginterpreters.lox.parser_deprecated.util.Queue;
import com.craftinginterpreters.lox.parser_deprecated.util.Stack;
import com.craftinginterpreters.lox.parser_deprecated.util.TokenContainer;
import com.craftinginterpreters.lox.scanner.Token;
import com.craftinginterpreters.lox.scanner.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Parser {
    private final List<Token> tokenList;
    private final List<Object> workingList;
    private final Container<Token> parenBuffer = new Stack<>();
    private final TokenContainer unaryOperatorContainer = new TokenContainer(new Stack<>(), 1);
    private final TokenContainer factorOperatorContainer = new TokenContainer(new Queue<>(), 2);
    private final TokenContainer termOperatorContainer = new TokenContainer(new Queue<>(), 2);
    private final TokenContainer comparisonOperatorContainer = new TokenContainer(new Queue<>(), 2);
    private final TokenContainer equalityOperatorContainer = new TokenContainer(new Queue<>(), 2);
    private final TokenContainer andOperatorContainer = new TokenContainer(new Queue<>(), 2);
    private final TokenContainer orOperatorContainer = new TokenContainer(new Queue<>(), 2);

    private final List<TokenContainer> operatorContainerList = new ArrayList<>();
    private Expr parseResult;

    {
        operatorContainerList.add(unaryOperatorContainer);
        operatorContainerList.add(factorOperatorContainer);
        operatorContainerList.add(termOperatorContainer);
        operatorContainerList.add(comparisonOperatorContainer);
        operatorContainerList.add(equalityOperatorContainer);
        operatorContainerList.add(andOperatorContainer);
        operatorContainerList.add(orOperatorContainer);
    }

    public Parser(List<Token> tokenList) {
        this.tokenList = tokenList;
        workingList = tokenList.stream()
                .filter(token -> token.getType() != Type.EOF)
                .collect(Collectors.toList());
    }

    public List<Object> getWorkingList() {
        return new ArrayList<>(workingList);
    }

    public Expr getParseResult() {
        return parseResult;
    }

    private void error(String msg) {
        System.out.println(msg);
        System.out.println(workingList);
        System.exit(65);
    }

    private int find(Direction direction, int start, int end, int index) {
        int delta = direction == Direction.LEFT ? -1 : 1;
        while (true) {
            index += delta;
            if (start <= index && index < end) {
                Object obj = workingList.get(index);
                if (obj instanceof Token || obj instanceof Expr) {
                    return index;
                }
            } else {
                break;
            }
        }

        return -1;
    }

    private Expr getExprAtIndex(int index) {
        if (0 <= index && index < workingList.size()) {
            Object obj = workingList.get(index);
            if (obj instanceof Token) {
                obj = tokenToExpr((Token) obj);
            }
            if (obj instanceof Expr) {
                return (Expr) obj;
            }
        }

        return null;
    }

    private Expr.Literal tokenToExpr(Token token) {
        Type tokenType = token.getType();
        if (tokenType == Type.NUMBER || tokenType == Type.STRING
                || tokenType == Type.TRUE || tokenType == Type.FALSE || tokenType == Type.NIL) {
            return new Expr.Literal(token);
        } else {
            error(tokenType + " is not value.");
            return null;
        }
    }

    private void handle(Token rightParen) {
        int start;
        int end;
        Token leftParen = null;
        if (rightParen != null && rightParen.getType() == Type.RIGHT_PAREN) {
            if (parenBuffer.size() == 0) {
                error("Paren not matched.");
                return;
            } else {
                leftParen = parenBuffer.pop();
                start = leftParen.getIndexInTokenList() + 1;
                end = rightParen.getIndexInTokenList();
            }
        } else if (rightParen == null) {
            if (parenBuffer.size() != 0) {
                error("Paren not matched.");
                return;
            } else {
                start = 0;
                end = workingList.size();
            }
        } else {
            error("Bug : %s not allowed for Parser.handle(Token)");
            return;
        }

        for (TokenContainer operatorContainer : operatorContainerList) {
            int operandCountRequired = operatorContainer.getOperandCountRequired();

            while (operatorContainer.size() > 0) {
                if (operatorContainer.peek().getIndexInTokenList() < start
                        || operatorContainer.peek().getIndexInTokenList() >= end) {
                    break;
                }
                Token operator = operatorContainer.pop();

                if (operandCountRequired == 1) {
                    int index = find(Direction.RIGHT, start, end, operator.getIndexInTokenList());
                    Expr expr = getExprAtIndex(index);
                    workingList.set(operator.getIndexInTokenList(), new Expr.Unary(operator, expr));
                    workingList.set(index, null);
                } else if (operandCountRequired == 2) {
                    int leftIndex = find(Direction.LEFT, start, end, operator.getIndexInTokenList());
                    Expr leftExpr = getExprAtIndex(leftIndex);

                    int rightIndex = find(Direction.RIGHT, start, end, operator.getIndexInTokenList());
                    Expr rightExpr = getExprAtIndex(rightIndex);

                    workingList.set(operator.getIndexInTokenList(),
                            new Expr.Binary(leftExpr, operator, rightExpr));
                    workingList.set(leftIndex, null);
                    workingList.set(rightIndex, null);
                } else {
                    error("Bug.");
                }
            }
        }

        int index = find(Direction.LEFT, start, end, end);
        Expr expr = getExprAtIndex(index);

        if (rightParen != null) {
            if (expr == null) {
                error("Empty paren pair.");
            }

            workingList.set(leftParen.getIndexInTokenList(), new Expr.Grouping(expr));
            for (int i = leftParen.getIndexInTokenList() + 1; i < rightParen.getIndexInTokenList(); i++) {
                workingList.set(i, null);
            }
            workingList.set(rightParen.getIndexInTokenList(), null);
        } else {
            index = find(Direction.LEFT, 0, workingList.size(), workingList.size());
            parseResult = getExprAtIndex(index);
        }
    }

    public void parse() {
        for (Token token : tokenList) {
            switch (token.getType()) {
                case IDENTIFIER://todo
                    break;

                case NUMBER:
                case STRING:
                case TRUE:
                case FALSE:
                case NIL:
                    break;

                case LEFT_PAREN:
                    parenBuffer.push(token);
                    break;

                case RIGHT_PAREN:
                    handle(token);
                    break;

                case BANG:
                    unaryOperatorContainer.push(token);
                    break;

                case MINUS: {
                    int leftIndex = token.getIndexInTokenList() - 1;
                    Type leftTokenType = null;
                    if (leftIndex >= 0) {
                        leftTokenType = tokenList.get(leftIndex).getType();
                    }

                    // todo
                    if (leftTokenType == Type.RIGHT_PAREN || leftTokenType == Type.NUMBER) {
                        termOperatorContainer.push(token);
                    } else {
                        unaryOperatorContainer.push(token);
                    }
                }
                break;

                case SLASH:
                case STAR:
                    factorOperatorContainer.push(token);
                    break;

                case PLUS:
                    termOperatorContainer.push(token);
                    break;

                case GREATER:
                case GREATER_EQUAL:
                case LESS:
                case LESS_EQUAL:
                    comparisonOperatorContainer.push(token);
                    break;

                case EQUAL_EQUAL:
                case BANG_EQUAL:
                    equalityOperatorContainer.push(token);
                    break;

                case AND:
                    andOperatorContainer.push(token);
                    break;

                case OR:
                    orOperatorContainer.push(token);
                    break;

                case EOF:
                    break;

                default:
                    error(String.format("Token %s not allowed.", token));
                    break;
            }
        }

        handle(null);
    }

    private enum Direction {
        LEFT, RIGHT
    }
}
