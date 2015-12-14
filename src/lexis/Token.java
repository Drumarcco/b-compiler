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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Token other = (Token) obj;
        if ((this.lexeme == null) ? (other.lexeme != null) : !this.lexeme.equals(other.lexeme))
            return false;
        if (this.tableValue != other.tableValue) return false;
        if (this.lineNumber != other.lineNumber) return false;
        return true;
    }
}
