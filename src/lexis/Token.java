package lexis;

/**
 * Created by drumarcco on 11/04/15.
 * Time: 02:16 AM
 */
public class Token {
    public String lexeme;
    public int tableValue;
    public int lineNumber;

    public Token(String lexeme, int tableValue, int lineNumber){
        this.lexeme = lexeme;
        this.tableValue = tableValue;
        this.lineNumber = lineNumber;
    }
}
