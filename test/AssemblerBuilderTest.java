import lexis.Token;
import org.junit.BeforeClass;
import org.junit.Test;
import parsing.AssemblerBuilder;
import syntax.Variable;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Marco on 12/12/2015.
 */
public class AssemblerBuilderTest {
	static AssemblerBuilder assembler;

	@BeforeClass
	public static void initialize() {
		Variable var1 = new Variable("var1", 101);
		Variable var2 = new Variable("var2", 102);

		LinkedList<Variable> variables = new LinkedList<>();
		variables.add(var1);
		variables.add(var2);

		ArrayList<Object> program = new ArrayList<>();
		program.add(var1);
		program.add(new Token("'s'", 107, 1));
		program.add(new Token("=", 111, 1));
		program.add(var2);
		program.add(new Token("5", 101, 1));
		program.add(new Token("=", 111, 1));
		program.add(var1);
		program.add(var2);
		program.add(new Token("=", 111, 1));
		program.add(var1);
		program.add(new Token("5", 101, 1));
		program.add(new Token("+", 100, 1));


	}

	@Test
	public void writeProgram() {
		assembler.processProgram();
	}
}
