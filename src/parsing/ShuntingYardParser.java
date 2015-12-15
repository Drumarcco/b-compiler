package parsing;

import lexis.Token;
import syntax.Function;

import java.util.*;

/**
 * Created by MarcoAntonio on 9/27/2015.
 */
public class ShuntingYardParser {
	private final Map<String, Operator> operators;

	private static void addNode(Stack<ASTNode> stack, Token operator) {
		if (!operator.lexeme.equals("write")) {
			final ASTNode rightASTNode = stack.pop();
			final ASTNode leftASTNode = stack.pop();
			stack.push(new ASTNode(operator, leftASTNode, rightASTNode));
		} else {
			final ASTNode leftASTNode = null;
			final ASTNode rightASTNode = stack.pop();
			stack.push(new ASTNode(operator, leftASTNode, rightASTNode));
		}
	}

	public ShuntingYardParser(Collection<Operator> operators) {
		this.operators = new HashMap<>();
		for (Operator o : operators) {
			this.operators.put(o.getSymbol(), o);
		}
	}

	public ASTNode convertInfixNotationToAST(final LinkedList<Token> input) {
		final Stack<Token> operatorStack = new Stack<>();
		final Stack<ASTNode> operandStack = new Stack<>();

		main:
		for (Token t : input) {
			Token popped;
			switch (t.lexeme) {
				case " ":
					break;
				case "(":
					operatorStack.push(t);
					break;
				case ")":
					while (!operatorStack.isEmpty()) {
						popped = operatorStack.pop();
						if (Objects.equals("(", popped.lexeme)) {
							continue main;
						} else {
							addNode(operandStack, popped);
						}
					}
					throw new IllegalStateException("Unbalanced right parentheses");
				default:
					if (operators.containsKey(t.lexeme)) {
						final Operator operator = operators.get(t.lexeme);
						Operator operator2;
						while (!operatorStack.isEmpty() &&
								null != (operator2 = operators.get(operatorStack.peek().lexeme))) {
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
		while (!operatorStack.isEmpty()) {
			addNode(operandStack, operatorStack.pop());
		}
		return operandStack.pop();
	}

	public ArrayList<Object> convertInfixNotationToRPN(ArrayList<Object> input) {
		final Stack<Token> operatorStack = new Stack<>();
		final ArrayList<Object> output = new ArrayList<>();
		main:
		for (Object object : input) {
			Token popped;
			Token token;
			if (object instanceof Token || object instanceof Function) {
				if (object instanceof Token) {
					token = (Token) object;
				} else {
					Function fn = (Function) object;
					token = new Token(fn.Name, 100, 1);
				}
				switch (token.lexeme) {
					case " ":
						break;
					case "(":
						operatorStack.push(token);
						break;
					case ")":
						while (!operatorStack.isEmpty()) {
							popped = operatorStack.pop();
							if (Objects.equals("(", popped.lexeme)) {
								continue main;
							} else {
								output.add(popped);
							}
						}
						throw new IllegalStateException("Unbalanced right " +
								"parentheses.");

					default:
						if (operators.containsKey(token.lexeme)) {
							final Operator o1 = operators.get(token.lexeme);
							Operator o2;
							while (!operatorStack.isEmpty() &&
									null != (o2 = operators.get(operatorStack.peek().lexeme))) {
								if ((!o1.isRightAssociative() &&
										0 == o1.comparePrecedence(o2)) ||
										o1.comparePrecedence(o2) < 0) {
									operatorStack.pop();
									output.add(new Token(o2.getSymbol(), 111, 1));
								} else {
									break;
								}
							}
							operatorStack.push(token);
						} else {
							output.add(token);
						}
						break;
				}
			}
			else {
				output.add(object);
			}
		}
		while (!operatorStack.isEmpty()) {
			output.add(operatorStack.pop());
		}
		return output;
	}
}
