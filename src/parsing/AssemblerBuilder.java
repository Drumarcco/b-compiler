package parsing;

import lexis.Token;
import syntax.Function;
import syntax.Variable;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;

/**
 * Created by Marco on 12/12/2015.
 */
public class AssemblerBuilder {
	private BaseOperator ADDITION = new BaseOperator("+", false, 7);
	private BaseOperator SUBTRACTION = new BaseOperator("-", false, 7);
	private BaseOperator MULTIPLICATION = new BaseOperator("*", false, 15);
	private BaseOperator DIVISION = new BaseOperator("/", false, 15);

	private LinkedList<Variable> variables;
	private ArrayList<Object> program;
	private StringBuilder s;
	private LinkedBlockingQueue<Variable> temporals;
	private Stack<Variable> stackedTemporals = new Stack<>();
	private int currentTemporal = 0;
	private BaseOperator lastOperator = null;
	final Stack<Object> computation = new Stack<>();

	final String CODE_PREVARS =
			";/StartHeader\n" +
					"INCLUDE macros.mac\n" +
					"INCLUDE emu8086.inc\n" +
					".MODEL SMALL\n" +
					".DATA\n" +
					"\tTEMPORAL\tDW ?\n";

	final String CODE_POSTVARS =
			".CODE\n" +
					"BEGIN:\n" +
					"\tMOV     AX, @DATA\n" +
					"\tMOV     DS, AX\n" +
					"CALL  COMPI\n" +
					"\tMOV AX, 4C00H\n" +
					"\tINT 21H\n" +
					"COMPI  PROC\n";

	final String CODE_FOOTER = "\tret\n" +
			"COMPI  ENDP\n" +
			"DEFINE_SCAN_NUM\n" +
			"DEFINE_PRINT_STRING\n" +
			"DEFINE_PRINT_NUM\n" +
			"DEFINE_PRINT_NUM_UNS\n" +
			"END BEGIN";

	public AssemblerBuilder(ArrayList<Object> program, LinkedList<Variable> variables) {
		this.variables = variables;
		this.program = program;
		generateTemporals();
		this.s = new StringBuilder();
	}

	private void generateTemporals() {
		temporals = new LinkedBlockingQueue<>();
		for (Object obj : program) {
			if (!(obj instanceof Token)) continue;
			Token token = (Token) obj;
			if (!isOperator(token)) continue;
			try {
				temporals.put(new Variable("T" + currentTemporal, 101));
				currentTemporal++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		variables.addAll(temporals.stream().collect(Collectors.toList()));
	}

	private boolean isOperator(Token token) {
		switch (token.lexeme) {
			case "*":
				return true;
			case "/":
				return true;
			case "+":
				return true;
			case "-":
				return true;
			default:
				return false;
		}
	}

	public void processProgram() {
		s.append(CODE_PREVARS);
		s.append(parseVariables());
		s.append(CODE_POSTVARS);
		try {
			iterateList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		s.append(CODE_FOOTER);
		writeFile(s.toString());
	}

	private void iterateList() throws Exception {
		/**
		 * Type 0: Incompatible types
		 * Type 1: boolean
		 * Type 100: Variable
		 * Type 101: Number
		 * Type 106: Char
		 * Type 107: String**/
		for (Object obj : program) {
			Object o1 = null;
			Object o2 = null;
			Variable temp = null;
			if (obj instanceof Token) {
				Token token = (Token) obj;
				switch (token.lexeme) {
					case "*":
						temp = temporals.poll();
						if (stackedTemporals.size() == 2) {
							o2 = stackedTemporals.pop();
							o1 = stackedTemporals.pop();
						} else {
							o2 = computation.pop();
							o1 = requiresTemporalFromStack(MULTIPLICATION)
									? stackedTemporals.pop()
									: computation.pop();
						}
						multiplication(o1, o2, temp);
						break;
					case "/":
						temp = temporals.poll();
						if (stackedTemporals.size() == 2) {
							o2 = stackedTemporals.pop();
							o1 = stackedTemporals.pop();
						} else {
							o2 = computation.pop();
							o1 = requiresTemporalFromStack(DIVISION)
									? stackedTemporals.pop()
									: computation.pop();
						}
						division(o1, o2, temp);
						break;
					case "+":
						temp = temporals.poll();
						if (stackedTemporals.size() == 2) {
							o2 = stackedTemporals.pop();
							o1 = stackedTemporals.pop();
						} else {
							o2 = computation.pop();
							o1 = requiresTemporalFromStack(ADDITION)
									? stackedTemporals.pop()
									: computation.pop();
						}
						addition(o1, o2, temp);
						break;
					case "-":
						temp = temporals.poll();
						if (stackedTemporals.size() == 2) {
							o2 = stackedTemporals.pop();
							o1 = stackedTemporals.pop();
						} else {
							o2 = computation.pop();
							o1 = requiresTemporalFromStack(SUBTRACTION)
									? stackedTemporals.pop()
									: computation.pop();
						}
						subtraction(o1, o2, temp);
						break;
					case "=":
						if (stackedTemporals.isEmpty()) {
							o2 = computation.pop();
							o1 = computation.pop();
						}
						else {
							o1 = computation.pop();
							o2 = stackedTemporals.pop();
						}
						assignment(o1, o2);
						break;
					case "write":
						o2 = computation.pop();
						function(new Function("write"), o2);
						break;
					default:
						computation.push(obj);
				}
			}
			else {
				computation.push(obj);
			}
		}
	}

	private boolean requiresTemporalFromStack(BaseOperator operator) {
		if (lastOperator == null) return false;
		int result = operator.comparePrecedence(lastOperator);
		switch (result) {
			case 1:
				return false;
			case 0:
				return true;
			case -1:
				return true;
			default:
				return false;
		}
	}

	private void subtraction(Object o1, Object o2, Variable temporal) {
		if (o1 instanceof Variable && o2 instanceof Variable) {
			Variable v1 = (Variable) o1;
			Variable v2 = (Variable) o2;
			s.append("\tRESTA\t").append(v1.Name).append(",\t").append(v2.Name)
					.append(",\t").append(temporal.Name).append("\n\n");
		}
		else if (o1 instanceof Variable && o2 instanceof Token) {
			Variable v1 = (Variable) o1;
			Token t2 = (Token) o2;
			s.append("\tRESTA\t").append(v1.Name).append(",\t").append(t2.lexeme)
					.append(",\t").append(temporal.Name).append("\n\n");
		}
		else if (o1 instanceof Token && o2 instanceof Variable) {
			Token t1 = (Token) o1;
			Variable v2 = (Variable) o2;
			s.append("\tRESTA\t").append(t1.lexeme).append(",\t").append(v2.Name)
					.append(",\t").append(temporal.Name).append("\n\n");
		}
		else if (o1 instanceof Token && o2 instanceof Token) {
			Token t1 = (Token) o1;
			Token t2 = (Token) o2;
			s.append("\tRESTA\t").append(t1.lexeme).append(",\t").append(t2.lexeme)
					.append(",\t").append(temporal.Name).append("\n\n");
		}
		stackedTemporals.push(temporal);
		lastOperator = SUBTRACTION;
	}

	private void division(Object o1, Object o2, Variable temporal) {
		if (o1 instanceof Variable && o2 instanceof Variable) {
			Variable v1 = (Variable) o1;
			Variable v2 = (Variable) o2;
			s.append("\tDIVIDE\t").append(v1.Name).append(",\t").append(v2.Name)
					.append(",\t").append(temporal.Name).append("\n\n");
		}
		else if (o1 instanceof Variable && o2 instanceof Token) {
			Variable v1 = (Variable) o1;
			Token t2 = (Token) o2;
			s.append("\tDIVIDE\t").append(v1.Name).append(",\t").append(t2.lexeme)
					.append(",\t").append(temporal.Name).append("\n\n");
		}
		else if (o1 instanceof Token && o2 instanceof Variable) {
			Token t1 = (Token) o1;
			Variable v2 = (Variable) o2;
			s.append("\tDIVIDE\t").append(t1.lexeme).append(",\t").append(v2.Name)
					.append(",\t").append(temporal.Name).append("\n\n");
		}
		else if (o1 instanceof Token && o2 instanceof Token) {
			Token t1 = (Token) o1;
			Token t2 = (Token) o2;
			s.append("\tDIVIDE\t").append(t1.lexeme).append(",\t").append(t2.lexeme)
					.append(",\t").append(temporal.Name).append("\n\n");
		}
		stackedTemporals.push(temporal);
		lastOperator = DIVISION;
	}

	private void multiplication(Object o1, Object o2, Variable temporal) {
		if (o1 instanceof Variable && o2 instanceof Variable) {
			Variable v1 = (Variable) o1;
			Variable v2 = (Variable) o2;
			s.append("\tMULTI\t").append(v1.Name).append(",\t").append(v2.Name)
					.append(",\t").append(temporal.Name).append("\n\n");
		}
		else if (o1 instanceof Variable && o2 instanceof Token) {
			Variable v1 = (Variable) o1;
			Token t2 = (Token) o2;
			s.append("\tMULTI\t").append(v1.Name).append(",\t").append(t2.lexeme)
					.append(",\t").append(temporal.Name).append("\n\n");
		}
		else if (o1 instanceof Token && o2 instanceof Variable) {
			Token t1 = (Token) o1;
			Variable v2 = (Variable) o2;
			s.append("\tMULTI\t").append(t1.lexeme).append(",\t").append(v2.Name)
					.append(",\t").append(temporal.Name).append("\n\n");
		}
		else if (o1 instanceof Token && o2 instanceof Token) {
			Token t1 = (Token) o1;
			Token t2 = (Token) o2;
			s.append("\tMULTI\t").append(t1.lexeme).append(",\t").append(t2.lexeme)
					.append(",\t").append(temporal.Name).append("\n\n");
		}
		stackedTemporals.push(temporal);
		lastOperator = MULTIPLICATION;
	}

	private void addition(Object o1, Object o2, Variable temporal) throws InterruptedException {
		if (o1 instanceof Variable && o2 instanceof Variable) {
			Variable v1 = (Variable) o1;
			Variable v2 = (Variable) o2;
			s.append("\tSUMAR\t").append(v1.Name).append(",\t").append(v2.Name)
					.append(",\t").append(temporal.Name).append("\n\n");
		}
		else if (o1 instanceof Variable && o2 instanceof Token) {
			Variable v1 = (Variable) o1;
			Token t2 = (Token) o2;
			s.append("\tSUMAR\t").append(v1.Name).append(",\t").append(t2.lexeme)
					.append(",\t").append(temporal.Name).append("\n\n");
		}
		else if (o1 instanceof Token && o2 instanceof Variable) {
			Token t1 = (Token) o1;
			Variable v2 = (Variable) o2;
			s.append("\tSUMAR\t").append(t1.lexeme).append(",\t").append(v2.Name)
					.append(",\t").append(temporal.Name).append("\n\n");
		}
		else if (o1 instanceof Token && o2 instanceof Token) {
			Token t1 = (Token) o1;
			Token t2 = (Token) o2;
			s.append("\tSUMAR\t").append(t1.lexeme).append(",\t").append(t2.lexeme)
					.append(",\t").append(temporal.Name).append("\n\n");
		}
		stackedTemporals.push(temporal);
		lastOperator = ADDITION;
	}

	private void function(Function function, Object obj) {
		if (obj instanceof Token) {
			Token token = (Token) obj;
			if (token.tableValue == 101) {
				s.append("\tMOV\tAX,\t").append(token.lexeme).append("\n");
				s.append("\tCALL\tPRINT_NUM\n\n");
			} else if (token.tableValue == 107) {
				s.append("\tPRINTN\t").append(token.lexeme).append("\n\n");
			} else {
				if (function.Name.toLowerCase().equals("write")) {
					s.append("\tMOV\tTEMPORAL,\t").append(token.lexeme).append("\n")
							.append("\tWRITE\tTEMPORAL").append("\n\n");
				}
			}
		} else if (obj instanceof Variable) {
			Variable var = (Variable) obj;
			if (function.Name.toLowerCase().equals("write")) {
				if (var.TypeTableNumber == 101) {
					s.append("\tMOV\tAX,\t").append(var.Name).append("\n")
							.append("\tCALL\tPRINT_NUM").append("\n\n");
				} else
					s.append("\tWRITE\t").append(var.Name).append("\n\n");
			}
		}
	}

	private void assignment(Object o1, Object o2) throws Exception {
		Token t2;
		Variable v1;
		Variable v2;
		if (!(o1 instanceof Variable)) throw new Exception(
				"Error: No se puede asignar a algo diferente a una variable."
		);

		v1 = (Variable) o1;
		if (o2 == null) {
			s.append("\tI_ASIGNAR\t").append(v1.Name).append(",\t").append("TEMPORAL\n\n");
		} else if (o2 instanceof Token) {
			t2 = (Token) o2;
			s.append("\tMOV\tTEMPORAL,\t").append(t2.lexeme).append("\n")
					.append("\tI_ASIGNAR\t").append(v1.Name).append(",\t").append("TEMPORAL\n\n");
		} else if (o2 instanceof Variable) {
			v2 = (Variable) o2;
			s.append("\tI_ASIGNAR\t").append(v1.Name).append(",\t").append(v2.Name).append("\n\n");
		}
	}

	private String parseVariables() {
		StringBuilder s = new StringBuilder();
		for (Variable var : variables) {
			s.append("\t")
					.append(var.Name)
					.append("\tdw\t?\n");
		}
		return s.toString();
	}

	public void writeFile(String code) {
		try (Writer writer = new BufferedWriter(
				new OutputStreamWriter(
						new FileOutputStream("c:/asm/compi.asm"), "UTF-8"))) {
			writer.write(code);
		} catch (Exception ignored) {

		}
	}
}
