/**
 * Created by Marco on 12/12/2015.
 */
import lexis.Token;
import org.junit.BeforeClass;
import org.junit.Test;
import parsing.BaseOperator;
import parsing.Operator;
import parsing.ShuntingYardParser;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class ShuntingYardParserTest {
    static ShuntingYardParser parser;

    @BeforeClass
    public static void setUpShuntingYardParser() {
        final Collection<Operator> operators = new ArrayList<>();
        operators.add(new BaseOperator("*", false, 15));
        operators.add(new BaseOperator("/", false, 15));
        operators.add(new BaseOperator("+", false, 7));
        operators.add(new BaseOperator("-", false, 7));
        operators.add(new BaseOperator("<", false, 6));
        operators.add(new BaseOperator("<=", false, 6));
        operators.add(new BaseOperator(">", false, 6));
        operators.add(new BaseOperator(">=", false, 6));
        operators.add(new BaseOperator("==", false, 5));
        operators.add(new BaseOperator("!=", false, 5));
        operators.add(new BaseOperator("&", false, 4));
        operators.add(new BaseOperator("|", false, 4));
        operators.add(new BaseOperator("=", true, 3));
        parser = new ShuntingYardParser(operators);
    }

    @Test
    public void convertToRPN(){
        ArrayList<Object> tokenList = new ArrayList<>();
        tokenList.add(new Token("variable", 100, 1));
        tokenList.add(new Token("=", 108, 1));
        tokenList.add(new Token("5", 101, 1));

        ArrayList<Token> expected = new ArrayList<>();
        expected.add(new Token("variable", 100, 1));
        expected.add(new Token("5", 101, 1));
        expected.add(new Token("=", 108, 1));

        ArrayList<Object> output = parser.convertInfixNotationToRPN(tokenList);
        assertEquals(expected, output);
    }
}
