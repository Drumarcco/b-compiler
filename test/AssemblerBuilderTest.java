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
		LinkedList<Variable> variables = new LinkedList<>();
		variables.add(new Variable("var1", 101));
		variables.add(new Variable("var2", 102));
		variables.add(new Variable("var3", 101));

		ArrayList<Object> program = new ArrayList<>();
		program.add(new Variable("var1", 100));
		program.add(new Token("'s'", 107, 1));
		program.add(new Token("=", 111, 1));
		program.add(new Variable("var2", 100));
		program.add(new Token("5", 101, 1));
		program.add(new Token("=", 111, 1));
		program.add(new Variable("var1", 100));
		program.add(new Variable("var2", 100));
		program.add(new Token("=", 111, 1));
		assembler = new AssemblerBuilder(program, variables);
	}

	@Test
	public void writeProgram() {
		assembler.processProgram();
	}
}
