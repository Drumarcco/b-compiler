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
        if (isProgram()) JOptionPane.showMessageDialog(null, "Sintaxis correcta.");
        else JOptionPane.showMessageDialog(null, "Sintaxis incorrecta.");
    }

    private Boolean isProgram(){
        if (currentToken == null) return true;
        else if (isDefinition()) {
            isProgram();
            return true;
        }
        return false;
    }

    private Boolean isDefinition() {
        if (isName(currentToken)) {
            if (isLeftBracket(currentToken)) {
                if (isConstant(currentToken)){
                    if (isRightBracket(currentToken)){
                        if (isIval(currentToken)) {
                            if(isComma(currentToken)){
                                while (isIval(currentToken)){
                                    isComma(currentToken);
                                    if (isSemiColon(currentToken)) return true;
                                }
                                printError(509);
                                return false;
                            } else if (isSemiColon(currentToken)){
                                return true;
                            } else {
                                printErrorStack(new int[]{509, 517});
                                return false;
                            }
                        } else if (isSemiColon(currentToken)) {
                            return true;
                        } else {
                            printErrorStack(new int[]{506, 509});
                            return false;
                        }
                    } else {
                        printError(508);
                        return false;
                    }
                } else if (isRightBracket(currentToken)){
                    if (isIval(currentToken)) {
                        if(isComma(currentToken)){
                            while (isIval(currentToken)){
                                isComma(currentToken);
                                if (isSemiColon(currentToken)) {
                                    return true;
                                }
                            }
                            printError(509);
                            return false;
                        } else if (isSemiColon(currentToken)) {
                            return true;
                        }
                        printErrorStack(new int[]{509, 517});
                        return false;
                    } else if (isSemiColon(currentToken)) {
                        return true;
                    } else {
                        printErrorStack(new int[]{506, 509});
                        return false;
                    }
                } else {
                    printErrorStack(new int[]{507, 508});
                    return false;
                }
            } else if (isLeftParenthesis(currentToken)){
                if (isName(currentToken)){
                    if (isComma(currentToken)){
                        while (isName(currentToken)){
                            isComma(currentToken);
                        }
                        if (isRightParenthesis(currentToken)){
                            if (isStatement()) {
                                return true;
                            } else {
                                printError(516);
                                return false;
                            }
                        } else {
                            printError(519);
                            return false;
                        }
                    }
                } else if (isRightParenthesis(currentToken)){
                    if (isStatement()) {
                        return true;
                    } else {
                        printError(516);
                        return false;
                    }
                } else {
                    printErrorStack(new int[]{503, 519});
                    return false;
                }
            } else if(isIval(currentToken)){
                if(isComma(currentToken)){
                    while (isIval(currentToken)){
                        isComma(currentToken);
                        if (isSemiColon(currentToken)) {
                            return true;
                        }
                    }
                    printError(516);
                    return false;
                } else if (isSemiColon(currentToken)){
                    return true;
                }
            }
            else if (isSemiColon(currentToken)) {
                return true;
            }
            else {
                printErrorStack(new int[]{504, 505, 506});
                return false;
            }
        } else {
            printError(503);
            return false;
        }
        return false;
    }

    private Boolean isStatement(){
        if (isAuto(currentToken)){
            if (isName(currentToken)){
                if (isConstant(currentToken)){
                    if (isComma(currentToken)){
                        while (isName(currentToken)){
                            isConstant(currentToken);
                            isComma(currentToken);
                        }
                        if (isSemiColon(currentToken)){
                            if (isStatement()){
                                return true;
                            } else {
                                printError(516);
                                return false;
                            }
                        } else {
                            printError(509);
                            return false;
                        }
                    } else if (isSemiColon(currentToken)){
                        if (isStatement()){
                            return true;
                        } else {
                            printError(516);
                            return false;
                        }
                    } else {
                        printErrorStack(new int[]{509, 517});
                        return false;
                    }
                } else if (isComma(currentToken)) {
                    while (isName(currentToken)){
                        isConstant(currentToken);
                        isComma(currentToken);
                    }
                    if (isSemiColon(currentToken)){
                        if (isStatement()){
                            return true;
                        } else {
                            printError(516);
                            return false;
                        }
                    } else {
                        printError(509);
                        return false;
                    }
                } else if (isSemiColon(currentToken)){
                    if (isStatement()){
                        return true;
                    } else {
                        printError(516);
                        return false;
                    }
                } else {
                    printErrorStack(new int[]{507, 509, 517});
                    return false;
                }
            } else {
                printError(503);
                return false;
            }
        } else if (isExtrn(currentToken)){
            if (isName(currentToken)){
                if (isComma(currentToken)){
                    while (isName(currentToken)){
                        isComma(currentToken);
                    }
                    if (isSemiColon(currentToken)){
                        if (isStatement()){
                            return true;
                        } else {
                            printError(516);
                            return false;
                        }
                    } else {
                        printError(509);
                        return false;
                    }
                } else if (isSemiColon(currentToken)){
                    if (isStatement()){
                        return true;
                    } else {
                        printError(516);
                        return false;
                    }
                } else {
                    printErrorStack(new int[]{509, 517});
                    return false;
                }
            } else {
                printError(503);
                return false;
            }
        } else if (isLeftBrace(currentToken)) {
            if (isStatement()){
                while(isStatement());
                if (isRightBrace(currentToken)){
                    return true;
                } else {
                    printError(518);
                }
            } else if (isRightBrace(currentToken)){
                return true;
            } else {
                printErrorStack(new int[]{516, 518});
            }
        } else if (isIf(currentToken)) {
            if (isLeftParenthesis(currentToken)){
                if (isRvalue(currentToken)){
                    if (isRightParenthesis(currentToken)){
                        if (isStatement()){
                            if (isElse(currentToken)) {
                                if (isStatement()) {
                                    return true;
                                 } else {
                                    printError(516);
                                    return false;
                                }
                            }
                            return true;
                        }
                    } else {
                        printError(519);
                    }
                } else {
                    printError(520);
                }
            } else {
                printError(505);
            }
        } else if (isWhile(currentToken)){
            if (isLeftParenthesis(currentToken)){
                if (isRvalue(currentToken)){
                    if (isRightParenthesis(currentToken)){
                        if (isStatement()){
                            return true;
                        } else {
                            printError(516);
                            return false;
                        }
                    } else {
                        printError(519);
                        return false;
                    }
                } else {
                    printError(520);
                    return false;
                }
            } else {
                printError(505);
                return false;
            }
        } else if (isGoto(currentToken)){
            if (isRvalue(currentToken)){
                if (isSemiColon(currentToken)){
                    return true;
                } else {
                    printError(509);
                    return false;
                }
            } else {
                printError(520);
                return false;
            }
        } else if (isReturn(currentToken)){
            if (isLeftParenthesis(currentToken)){
                if (isRvalue(currentToken)){
                    if (isRightParenthesis(currentToken)){
                        if(isSemiColon(currentToken)){
                            return true;
                        } else {
                            printError(509);
                            return false;
                        }
                    } else {
                        printError(519);
                        return false;
                    }
                } else {
                    printError(520);
                    return false;
                }
            } else if (isSemiColon(currentToken)){
                return true;
            } else {
                printErrorStack(new int[]{505, 509});
                return false;
            }
        } else if (isRvalue(currentToken)) {
            if(isSemiColon(currentToken)){
                return true;
            } else {
                printError(509);
                return false;
            }
        } else if (isSemiColon(currentToken)){
            return true;
        } else {
            //printErrorStack(new int[]{509, 510, 511, 512, 513, 514, 515});
            return false;
        }
        return false;
    }

    private Boolean isRvalue(Token token){
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
            case 506 : return "Se espera bloque 'ival'.";
            case 507 : return "Se espera un bloque 'constant'.";
            case 508 : return "Se espera cerradura de corchete.";
            case 509 : return "Se espera punto y coma.";
            case 510 : return "Se espera palabra 'auto'.";
            case 511 : return "Se espera palabra 'extrn'.";
            case 512 : return "Se espera apertura de llave.";
            case 513 : return "Se espera palabra 'if'.";
            case 514 : return "Se espera palabra 'while'.";
            case 515 : return "Se espera palabra 'return'.";
            case 516 : return "Se espera un bloque 'statement'.";
            case 517 : return "Se espera coma.";
            case 518 : return "Se espera cerradura de llave.";
            case 519 : return "Se espera cerradura de parentesis.";
            case 520 : return "Se espera bloque 'rvalue'.";

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

    private Boolean isLeftBrace(Token token){
        if (token.tableValue == 121) {
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isRightBrace(Token token){
        if (token.tableValue == 122){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isIf(Token token){
        if (token.tableValue == 200){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isWhile(Token token){
        if (token.tableValue == 202){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isGoto(Token token){
        if (token.tableValue == 205){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isReturn(Token token){
        if (token.tableValue == 203){
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

    private Boolean isAuto(Token token){
        if (token.tableValue == 207) {
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isExtrn(Token token){
        if (token.tableValue == 208){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isElse(Token token){
        if (token.tableValue == 201){
            getNextToken();
            return true;
        }
        return false;
    }
}
