package parsing;

import lexis.Token;

import java.util.*;

/**
 * Created by MarcoAntonio on 9/27/2015.
 */
public class ShuntingYardParser {
    private final Map<String, Operator> operators;

    private static void addNode(Stack<ASTNode> stack, Token operator){
        final ASTNode rightASTNode = stack.pop();
        final ASTNode leftASTNode = stack.pop();
        stack.push(new ASTNode(operator, leftASTNode, rightASTNode));
    }

    public ShuntingYardParser(Collection<Operator> operators){
        this.operators = new HashMap<>();
        for (Operator o : operators){
            this.operators.put(o.getSymbol(), o);
        }
    }

    public ASTNode convertInfixNotationToAST(final LinkedList<Token> input){
        final Stack<Token> operatorStack = new Stack<>();
        final Stack<ASTNode> operandStack = new Stack<>();

        main:
        for (Token t : input){
            Token popped;
            switch(t.lexeme) {
                case " ":
                    break;
                case "(":
                    operatorStack.push(t);
                    break;
                case ")":
                    while(!operatorStack.isEmpty()){
                        popped = operatorStack.pop();
                        if (Objects.equals("(", popped.lexeme)){
                            continue main;
                        } else {
                            addNode(operandStack, popped);
                        }
                    }
                    throw new IllegalStateException("Unbalanced right parentheses");
                default:
                    if(operators.containsKey(t.lexeme)){
                        final Operator operator = operators.get(t.lexeme);
                        Operator operator2;
                        while(!operatorStack.isEmpty() &&
                                null != (operator2 = operators.get(operatorStack.peek().lexeme))){
                            if ((!operator.isRightAssociative()
                                    && 0 == operator.comparePrecedence(operator2))
                                    || operator.comparePrecedence(operator2) < 0) {
                                Token token = operatorStack.pop();
                                addNode(operandStack, token);
                            } else {
                                break;
                            }
                        }
                        operatorStack.push(t);
                    } else {
                        operandStack.push(new ASTNode(t, null, null));
                    }
                    break;
            }
        }
        while (!operatorStack.isEmpty()){
            addNode(operandStack, operatorStack.pop());
        }
        return operandStack.pop();
    }
}
