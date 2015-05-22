import javax.swing.*;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Created by drumarcco on 20/05/15.
 * Time: 12:06 PM
 */
public class SyntacticalAnalysis {
    private Token currentToken;
    private ListIterator<Token> listIterator;
    private String errorStack;

    public SyntacticalAnalysis() {
        listIterator = LexicalAnalysis.tokenList.listIterator();
        getNextToken();
        program();
    }

    private Boolean program(){
        if (currentToken == null) return true;
        else if (isDefinition()) program();
        JOptionPane.showMessageDialog(null, "Sintaxis correcta.");
        return true;
    }

    private Boolean isDefinition() {
        if (isName(currentToken)) {                                                     // name
            if (isLeftBracket(currentToken)) {                                          // [
                if (isConstant(currentToken)){                                          // constant
                    if (isRightBracket(currentToken)){                                  // ]
                        if (isIval(currentToken)) {                                     // ival
                            if(isComma(currentToken)){                                  // ,
                                while (isIval(currentToken)){                           // ival
                                    if(isComma(currentToken));                          // ,
                                    else if (isSemiColon(currentToken)) return true;    // ;
                                }
                                return false;
                            } else if (isSemiColon(currentToken)){                      // ;
                                getNextToken();
                                return true;
                            }
                        } else if (isSemiColon(currentToken)) {
                            getNextToken();
                            return true;
                        }
                    }
                } else if (isRightBracket(currentToken)){
                    if (isIval(currentToken)) {                                     // ival
                        if(isComma(currentToken)){                                  // ,
                            while (isIval(currentToken)){                           // ival
                                if(isComma(currentToken));                          // ,
                                else if (isSemiColon(currentToken)) {
                                    getNextToken();
                                    return true;    // ;
                                }
                            }
                            return false;
                        } else if (isSemiColon(currentToken)){
                            getNextToken();
                            return true;
                        }
                    } else if (isSemiColon(currentToken)) {
                        getNextToken();
                        return true;
                    }
                }
            } else if (isLeftParenthesis(currentToken)){
                if (isName(currentToken)){
                    if (isComma(currentToken)){
                        while (isName(currentToken)){
                            if (isComma(currentToken));
                            else if (isRightParenthesis(currentToken)) break;
                        }
                        if (isStatement(currentToken)) {
                            getNextToken();
                            return true;
                        }
                        return false;
                    }
                }
            } else if(isIval(currentToken)){
                if(isComma(currentToken)){                                  // ,
                    while (isIval(currentToken)){                           // ival
                        if(isComma(currentToken));                          // ,
                        else if (isSemiColon(currentToken)) {
                            getNextToken();
                            return true;    // ;
                        }
                    }
                    return false;
                } else if (isSemiColon(currentToken)){                      // ;
                    getNextToken();
                    return true;
                }
            }
            else if (isSemiColon(currentToken)) {
                getNextToken();
                return true;
            }
            else {
                printErrorStack(new int[]{504, 505, 506});
            }
        }
        printError(503);
        return false;
    }

    private void getNextToken(){
        try{
            currentToken = listIterator.next();
        } catch (NoSuchElementException exception){
            currentToken = null;
        }

    }

    private void printErrorStack(int[] errorList){
        this.errorStack = "Errores de Sintáxis:\n";
        for (int error : errorList){
            this.errorStack += "- " + getErrorMessage(error) + "\n";
        }
        JOptionPane.showMessageDialog(null, this.errorStack);
    }

    private void printError(int error){
        this.errorStack = "Error " + error + ": " + getErrorMessage(error);
        JOptionPane.showMessageDialog(null, this.errorStack);
    }

    private String getErrorMessage(int errorNumber){
        switch (errorNumber){
            case 503 : return "Se espera bloque 'name'.";
            case 504 : return "Se espera apertura de corchete.";
            case 505 : return "Se espera apertura de parentesis.";
            case 506 : return "Se espera bloque 'ival'";
        }
        return "";
    }

    private Boolean isName(Token token){
        if (token.tableValue == 100){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isLeftBracket(Token token){
        if (token.tableValue == 123){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isRightBracket(Token token){
        if (token.tableValue == 124){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isConstant(Token token){
        if (token.tableValue == 101 || token.tableValue == 106 || token.tableValue == 107) {
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isIval(Token token){
        if (isConstant(token) || isName(token)) return true;
        return false;
    }

    private Boolean isComma(Token token){
        if (token.tableValue == 118){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isSemiColon(Token token){
        if (token.tableValue == 117){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isLeftParenthesis(Token token){
        if (token.tableValue == 119){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isRightParenthesis(Token token){
        if (token.tableValue == 120){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isStatement(Token token){
        return false;
    }
}
