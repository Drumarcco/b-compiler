package parsing;

import lexis.Token;
import syntax.Function;
import syntax.Variable;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.*;

/**
 * Created by Marco on 12/12/2015.
 */
public class AssemblerBuilder {
	private LinkedList<Variable> variables;
	private ArrayList<Object> program;
	private StringBuilder s;

	final String CODE_PREVARS =
			";/StartHeader\n" +
					"INCLUDE macros.mac\n" +
					"INCLUDE emu8086.inc\n" +
					".MODEL SMALL\n" +
					".DATA\n" +
//					"\tBUFFER\t\tDB 8 DUP('$')  ;23h\n" +
//					"\tBUFFERTEMP\tDB 8 DUP('$')  ;23h\n" +
//					"\tBLANCO\tDB '#'\n" +
//					"\tBLANCOS\tDB '$'\n" +
//					"\tMENOS\tDB '-$'\n" +
//					"\tCOUNT\tDW 0\n" +
//					"\tNEGATIVO\tDB 0\n" +
//					"\tARREGLO\tDW 0\n" +
//					"\tARREGLO1\tDW 0\n" +
//					"\tARREGLO2\tDW 0\n" +
//					"\tLISTAPAR\tLABEL BYTE\n" +
//					"\tLONGMAX\tDB 254\n" +
//					"\tTOTCAR\tDB ?\n" +
//					"\tINTRODUCIDOS\tDB 254 DUP ('$')\n" +
//					"\tMULT10\tDW 1\n" +
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
		this.s = new StringBuilder();

	}

	public void processProgram() {
		s.append(CODE_PREVARS);
		s.append(parseVariables());
		s.append(CODE_POSTVARS);
		iterateList();
		s.append(CODE_FOOTER);
		writeFile(s.toString());
	}

	private void iterateList() {
		/**
		 * Type 0: Incompatible types
		 * Type 1: boolean
		 * Type 100: Variable
		 * Type 101: Number
		 * Type 106: Char
		 * Type 107: String**/
		final Stack<Object> computation = new Stack<>();
		for (Object obj : program) {
			Object o1 = null;
			Object o2 = null;
			if (obj instanceof Token) {
				Token token = (Token) obj;
				switch (token.lexeme) {
					case "*":
						o2 = computation.pop();
						o1 = computation.pop();
						break;
					case "/":
						o2 = computation.pop();
						o1 = computation.pop();
						break;
					case "+":
						if (computation.size() > 1) o2 = computation.pop();
						o1 = computation.pop();
						addition(o1, o2);
						break;
					case "-":
						o2 = computation.pop();
						o1 = computation.pop();
						break;
					case "=":
						if (computation.size() > 1) o2 = computation.pop();
						o1 = computation.pop();
						try {
							assignment(o1, o2);
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					case "write":
						o2 = computation.pop();
						function(new Function("write"), o2);
					default:
						computation.push(obj);
				}
			}
			else {
				computation.push(obj);
			}
		}
	}

	private void addition(Object o1, Object o2) {
		if (o2 == null && o1 instanceof Variable) {
			Variable v1 = (Variable) o1;
			s.append("\tSUMAR\t").append(v1.Name).append(",\tTEMPORAL")
					.append(",\t").append("TEMPORAL\n\n");
		}
		else if (o2 == null && o1 instanceof Token) {
			Token t1 = (Token) o1;
			s.append("\tSUMAR\t").append(t1.lexeme).append(",\tTEMPORAL")
					.append(",\t").append("TEMPORAL\n\n");
		}
		else if (o1 instanceof Variable && o2 instanceof Variable) {
			Variable v1 = (Variable) o1;
			Variable v2 = (Variable) o2;
			s.append("\tSUMAR\t").append(v1.Name).append(",\t").append(v2.Name)
						.append(",\t").append("TEMPORAL\n\n");
		}
		else if (o1 instanceof Variable && o2 instanceof Token) {
			Variable v1 = (Variable) o1;
			Token t2 = (Token) o2;
			s.append("\tSUMAR\t").append(v1.Name).append(",\t").append(t2.lexeme)
					.append(",\t").append("TEMPORAL\n\n");
		}
		else if (o1 instanceof Token && o2 instanceof Variable) {
			Token t1 = (Token) o1;
			Variable v2 = (Variable) o2;
			s.append("\tSUMAR\t").append(t1.lexeme).append(",\t").append(v2.Name)
					.append(",\t").append("TEMPORAL\n\n");
		}
		else if (o1 instanceof Token && o2 instanceof Token) {
			Token t1 = (Token) o1;
			Token t2 = (Token) o2;
			s.append("\tSUMAR\t").append(t1.lexeme).append(",\t").append(t2.lexeme)
					.append(",\t").append("TEMPORAL\n\n");
		}
	}

	private void function(Function function, Object obj) {
		if (obj instanceof Token) {
			Token token = (Token) obj;
			if (token.tableValue == 101) {
				s.append("\tMOV\tAX,\t").append(token.lexeme).append("\n");
				s.append("\tCALL\tPRINT_NUM\n\n");
			}
			else if (token.tableValue == 107) {
				s.append("\tPRINTN\t").append(token.lexeme).append("\n\n");
			}
			else {
				if (function.Name.toLowerCase().equals("write")) {
					s.append("\tMOV\tTEMPORAL,\t").append(token.lexeme).append("\n")
							.append("\tWRITE\tTEMPORAL").append("\n\n");
				}
			}
		}
		else if (obj instanceof Variable){
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
		}
		else if (o2 instanceof Token) {
			t2 = (Token) o2;
			s.append("\tMOV\tTEMPORAL,\t").append(t2.lexeme).append("\n")
					.append("\tI_ASIGNAR\t").append(v1.Name).append(",\t").append("TEMPORAL\n\n");
		}
		else if (o2 instanceof Variable) {
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
