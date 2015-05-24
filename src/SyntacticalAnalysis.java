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
    private String errorStack = "";

    public SyntacticalAnalysis() {
        listIterator = LexicalAnalysis.tokenList.listIterator();
        getNextToken();
        if (isProgram()) JOptionPane.showMessageDialog(null, "Sintaxis correcta.");
        else JOptionPane.showMessageDialog(null, errorStack);
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
        if (isName(currentToken) || isMain()) {
            if (isLeftBracket(currentToken)) {
                if (isConstant(currentToken)){
                    if (isRightBracket(currentToken)){
                        if (isIval(currentToken)) {
                            while (isComma(currentToken)){
                                if (!isIval(currentToken)){
                                    printError(506);
                                    return false;
                                }
                            }
                            if (isSemiColon(currentToken)){
                                return true;
                            } else {
                                printError(509);
                                return false;
                            }
                        } else if (isSemiColon(currentToken)) {
                            return true;
                        } else {
                            printError(509);
                            return false;
                        }
                    } else {
                        printError(508);
                        return false;
                    }
                } else if (isRightBracket(currentToken)){
                    if (isIval(currentToken)) {
                        while(isComma(currentToken)){
                            if (!isIval(currentToken)){
                                printError(506);
                                return false;
                            }
                        }
                        if (isSemiColon(currentToken)) {
                            return true;
                        } else {
                            printError(509);
                            return false;
                        }
                    } else if (isSemiColon(currentToken)) {
                        return true;
                    } else {
                        printError(509);
                        return false;
                    }
                } else {
                    printError(508);
                    return false;
                }
            } else if (isLeftParenthesis(currentToken)){
                if (isName(currentToken)){
                    while (isComma(currentToken)){
                        if(!isName(currentToken)){
                            printError(503);
                            return false;
                        }
                    }
                    if (isRightParenthesis(currentToken)){
                        if (isStatement()) {
                            return true;
                        } else {
                            //printError(516);
                            return false;
                        }
                    } else {
                        printError(519);
                        return false;
                    }
                } else if (isRightParenthesis(currentToken)){
                    if (isStatement()) {
                        return true;
                    } else {
                        //printError(516);
                        return false;
                    }
                } else {
                    printError(519);
                    return false;
                }
            } else if(isIval(currentToken)){
                while (isComma(currentToken)){
                    if(!isIval(currentToken)){
                        printError(506);
                        return false;
                    }
                }
                if (isSemiColon(currentToken)){
                    return true;
                } else {
                    printError(509);
                    return false;
                }
            }
            else if (isSemiColon(currentToken)) {
                return true;
            }
            else {
                printError(509);
                return false;
            }
        } else {
            printError(503);
            return false;
        }
    }

    private Boolean isStatement(){
        if (isAuto(currentToken)){
            if (isName(currentToken)){
                if (isConstant(currentToken)){
                    while(isComma(currentToken)){
                        if (isName(currentToken)){
                            isConstant(currentToken);
                        } else {
                            printError(503);
                            return false;
                        }
                    }
                    if (isSemiColon(currentToken)){
                        if (isStatement()){
                            return true;
                        } else {
                            //printError(516);
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
                        //printError(516);
                        return false;
                    }
                } else {
                    while (isComma(currentToken)){
                        if (isName(currentToken)){
                            isConstant(currentToken);
                        } else {
                            printError(503);
                            return false;
                        }
                    }
                    if (isSemiColon(currentToken)){
                        if (isStatement()){
                            return true;
                        } else {
                            //printError(516);
                            return false;
                        }
                    } else {
                        printError(509);
                        return false;
                    }
                }
            } else {
                printError(503);
                return false;
            }
        } else if (isExtrn(currentToken)){
            if (isName(currentToken)){
                while (isComma(currentToken)){
                    if (!isName(currentToken)){
                        printError(503);
                        return false;
                    }
                }
                if (isSemiColon(currentToken)){
                    if (isStatement()){
                        return true;
                    } else {
                        //printError(516);
                        return false;
                    }
                } else {
                    printError(509);
                    return false;
                }
            } else {
                printError(503);
                return false;
            }
        } else if (isLeftBrace(currentToken)) {
            do {
                if (isRightBrace(currentToken)){
                    return true;
                }
            } while (isStatement());
            return false;
        } else if (isIf(currentToken)) {
            if (isLeftParenthesis(currentToken)){
                if (isRvalue()){
                    if (isRightParenthesis(currentToken)){
                        if (isStatement()){
                            if (isElse(currentToken)) {
                                if (isStatement()) {
                                    return true;
                                 } else {
                                    //printError(516);
                                    return false;
                                }
                            } else {
                                return true;
                            }
                        } else {
                            return false;
                        }
                    } else {
                        printError(519);
                        return false;
                    }
                } else {
                    //printError(520);
                    return false;
                }
            } else {
                printError(505);
                return false;
            }
        } else if (isWhile(currentToken)){
            if (isLeftParenthesis(currentToken)){
                if (isRvalue()){
                    if (isRightParenthesis(currentToken)){
                        if (isStatement()){
                            return true;
                        } else {
                            //printError(516);
                            return false;
                        }
                    } else {
                        printError(519);
                        return false;
                    }
                } else {
                    //printError(520);
                    return false;
                }
            } else {
                printError(505);
                return false;
            }
        } else if (isGoto(currentToken)){
            if (isRvalue()){
                if (isSemiColon(currentToken)){
                    return true;
                } else {
                    printError(509);
                    return false;
                }
            } else {
                //printError(520);
                return false;
            }
        } else if (isReturn(currentToken)){
            if (isLeftParenthesis(currentToken)){
                if (isRvalue()){
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
                    //printError(520);
                    return false;
                }
            } else if (isSemiColon(currentToken)){
                return true;
            } else {
                printError(509);
                return false;
            }
        } else if (isRvalue()) {
            if(isSemiColon(currentToken)){
                return true;
            } else {
                printError(509);
                return false;
            }
        } else if (isSemiColon(currentToken)){
            return true;
        } else {
            printError(509);
            return false;
        }
    }

    private Boolean isMain(){
        if(currentToken == null){
            return false;
        } else if (currentToken.tableValue == 209){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isRvalue(){
        if (isLeftParenthesis(currentToken)){
            if (isRvalue()){
                if (isRightParenthesis(currentToken)){
                    if (isRvalueTail()){
                        return true;
                    } else {
                        //printError(520);
                        return false;
                    }
                } else {
                    printError(519);
                    return false;
                }
            } else {
                //printError(520);
                return false;
            }
        } else if (isLvalue()){
            if (isAssign()){
                if (isRvalue()){
                    if (isRvalueTail()){
                        return true;
                    } else {
                        //printError(520);
                        return false;
                    }
                } else {
                    printError(520);
                    return false;
                }
            }
            if (isRvalueTail()){
                return true;
            } else {
                printError(523);
                return false;
            }
        } else if (isConstant(currentToken)) {
            if (isRvalueTail()){
                return true;
            } else {
                //printError(520);
                return false;
            }
        } else if (isUnary()){
            if (isRvalue()){
                if (isRvalueTail()){
                    return true;
                } else {
                    //printError(520);
                    return false;
                }
            } else {
                printError(520);
                return false;
            }
        } else if (isAmpersand()){
            if (isLvalue()){
                if (isRvalueTail()){
                    return true;
                } else {
                    //printError(520);
                    return false;
                }
            } else {
                //printError(521);
                return false;
            }
        }
        else {
            return false;
        }
    }

    private Boolean isRvalueTail(){
        if (isBinary()){
            if (isRvalue()){
                return true;
            }
            else {
                printError(520);
                return false;
            }
        } else if (isLeftParenthesis(currentToken)){
            if(isRightParenthesis(currentToken)){
                return true;
            } else if (isRvalue()){
                while (isComma(currentToken)){
                    if(!isRvalue()){
                        printError(520);
                        return false;
                    }
                }
                if (isRightParenthesis(currentToken)){
                    return true;
                } else {
                    printError(519);
                    return false;
                }
            } else {
                printError(519);
                return false;
            }
        }
        return true;
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
        this.errorStack += "Error " + error + " en linea " + (currentToken.lineNumber-1) + ": " + getErrorMessage(error) + "\n";
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
            case 521 : return "Se espera bloque 'lvalue'.";
            case 522 : return "Se espera bloque 'binary'.";
            case 523 : return "Se espera signo de '='.";

        }
        return "";
    }

    private Boolean isBinary(){
        int tableValue = currentToken.tableValue;
        if  (
                tableValue == 125 ||
                tableValue == 110 ||
                tableValue == 112 ||
                tableValue == 109 ||
                tableValue == 113 ||
                tableValue == 114 ||
                tableValue == 115 ||
                tableValue == 116 ||
                tableValue == 103 ||
                tableValue == 104 ||
                tableValue == 105 ||
                tableValue == 102
            )
        {
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isAmpersand(){
        if (currentToken == null) {
            return false;
        } else if (currentToken.tableValue == 110){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isLvalue(){
        if (isName(currentToken)){
            return true;
        } else if (isAsterisk()) {
            if (isRvalue()){
                return true;
            } else {
                //printError(520);
                return false;
            }
        }
        return false;
    }

    private Boolean isAsterisk(){
        if (currentToken == null){
            return false;
        } else if (currentToken.tableValue == 105){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isUnary(){
        if (currentToken == null){
            return false;
        } else if (currentToken.tableValue == 103 || currentToken.tableValue == 108){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isAssign(){
        if(isEqualsSign()){
            if (isBinary()){
                return true;
            }
            return true;
        } else {
            //printError(523);
            return false;
        }
    }

    private Boolean isEqualsSign(){
        if (currentToken == null){
            return false;
        } else if (currentToken.tableValue == 111){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isName(Token token){
        if (token == null){
            return false;
        } else if (token.tableValue == 100){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isLeftBracket(Token token){
        if (token == null){
            return false;
        } else if (token.tableValue == 123){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isRightBracket(Token token){
        if (token == null){
            return false;
        } else if (token.tableValue == 124){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isConstant(Token token){
        if (token == null) {
            return false;
        } else if (token.tableValue == 101 || token.tableValue == 106 || token.tableValue == 107) {
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isLeftBrace(Token token){
        if (token == null) {
            return false;
        } else if (token.tableValue == 121) {
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isRightBrace(Token token){
        if (token == null) {
            return false;
        } else if (token.tableValue == 122){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isIf(Token token){
        if (token == null) {
            return false;
        } else if (token.tableValue == 200){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isWhile(Token token){
        if (token == null) {
            return false;
        } else if (token.tableValue == 202){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isGoto(Token token){
        if (token == null){
            return false;
        } else if (token.tableValue == 205){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isReturn(Token token){
        if (token == null) {
            return false;
        } else if (token.tableValue == 203){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isIval(Token token){
        if (token == null){
            return false;
        } else if (isConstant(token) || isName(token)) return true;
        return false;
    }

    private Boolean isComma(Token token){
        if (token == null){
            return false;
        } else if (token.tableValue == 118){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isSemiColon(Token token){
        if (token == null) {
            return false;
        } else if (token.tableValue == 117){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isLeftParenthesis(Token token){
        if (token == null){
            return false;
        } else if (token.tableValue == 119){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isRightParenthesis(Token token){
        if (token == null) {
            return false;
        } else if (token.tableValue == 120){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isAuto(Token token){
        if (token == null) {
            return false;
        } else if (token.tableValue == 207) {
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isExtrn(Token token){
        if (token == null) {
            return false;
        } else if (token.tableValue == 208){
            getNextToken();
            return true;
        }
        return false;
    }

    private Boolean isElse(Token token){
        if (token == null){
            return false;
        } else if (token.tableValue == 201){
            getNextToken();
            return true;
        }
        return false;
    }
}
