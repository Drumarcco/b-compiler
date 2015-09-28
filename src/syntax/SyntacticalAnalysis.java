package syntax;

import lexis.LexicalAnalysis;
import lexis.Token;
import parsing.ASTNode;
import parsing.BaseOperator;
import parsing.Operator;
import parsing.ShuntingYardParser;

import javax.swing.*;
import java.util.*;

/**
 * Created by drumarcco on 20/05/15.
 * Time: 12:06 PM
 */
public class SyntacticalAnalysis {
    private Token currentToken;
    private ListIterator<Token> listIterator;
    private String errorStack = "";
    private LinkedList<Variable> variables;
    private LinkedList<Token> operationTokenList;
    private ShuntingYardParser parser;

    public SyntacticalAnalysis() {
        variables = new LinkedList<>();
        listIterator = LexicalAnalysis.tokenList.listIterator();
        initializeParser();
        getNextToken();
        if (isProgram()) JOptionPane.showMessageDialog(null, "Sintaxis correcta.");
        else JOptionPane.showMessageDialog(null, errorStack);
    }

    private void initializeParser() {
        final Collection<Operator> operators = new ArrayList<>();
        operators.add(new BaseOperator("*", false, 15));
        operators.add(new BaseOperator("/", false, 15));
        operators.add(new BaseOperator("+", false, 6));
        operators.add(new BaseOperator("-", false, 6));
        operators.add(new BaseOperator("=", true, 5));

        parser = new ShuntingYardParser(operators);
    }

    private void addToOperationList(Token token){
        operationTokenList.add(token);
        getNextToken();
    }

    private void evaluateOperation() {
        ASTNode parseTree = parser.convertInfixNotationToAST(operationTokenList);
        String hola ="";
        return;
    }

    private void addToVariablesList(Variable variable){
        if (variableIsInList(variable.Name)){
            printError(524);
            return;
        }
        variables.add(variable);
        getNextToken();
    }

    private void changeVariableType(Variable variable){
        for (Variable var : variables){
            if (var.Name == variable.Name) {
                var.TypeTableNumber = variable.TypeTableNumber;
                getNextToken();
                return;
            }
        }
    }

    private boolean variableIsInList(String variableName) {
        for (Variable variable : variables){
            if (variable.Name == variableName) return true;
        }
        return false;
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
            if (isName(currentToken)) getNextToken();
            if (isLeftParenthesis(currentToken)){
                getNextToken();
                if (isName(currentToken)){
                    getNextToken();
                    while (isComma(currentToken)){
                        if(!isName(currentToken)){
                            printError(503);
                            return false;
                        }
                        getNextToken();
                    }
                    if (isRightParenthesis(currentToken)){
                        getNextToken();
                        return isStatement();
                    } else {
                        printError(519);
                        return false;
                    }
                } else if (isRightParenthesis(currentToken)){
                    getNextToken();
                    return isStatement();
                } else {
                    printError(519);
                    return false;
                }
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
                Variable variable = new Variable(currentToken.lexeme, 0);
                addToVariablesList(variable);
                if (isConstant(currentToken)){
                    variable.TypeTableNumber = currentToken.tableValue;
                    changeVariableType(variable);
                    while(isComma(currentToken)){
                        if (isName(currentToken)){
                            variable = new Variable(currentToken.lexeme, 0);
                            addToVariablesList(variable);
                            if (isConstant(currentToken)){
                                variable.TypeTableNumber = currentToken.tableValue;
                                changeVariableType(variable);
                            }
                        } else {
                            printError(503);
                            return false;
                        }
                    }
                    if (isSemiColon(currentToken)){
                        return isStatement();
                    } else {
                        printError(509);
                        return false;
                    }
                } else if (isSemiColon(currentToken)){
                    return isStatement();
                } else {
                    while (isComma(currentToken)){
                        if (isName(currentToken)){
                            variable = new Variable(currentToken.lexeme, 0);
                            addToVariablesList(variable);
                            if (isConstant(currentToken)){
                                variable.TypeTableNumber = currentToken.tableValue;
                                changeVariableType(variable);
                            }
                        } else {
                            printError(503);
                            return false;
                        }
                    }
                    if (isSemiColon(currentToken)){
                        return isStatement();
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
                Variable variable = new Variable(currentToken.lexeme, 0);
                addToVariablesList(variable);
                while (isComma(currentToken)){
                    if (isName(currentToken)) {
                        addToVariablesList(variable);
                    } else {
                        printError(503);
                        return false;
                    }
                }
                if (isSemiColon(currentToken)){
                    return isStatement();
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
                getNextToken();
                if (isBoolean()){
                    evaluateOperation();
                    if (isRightParenthesis(currentToken)){
                        getNextToken();
                        if (isStatement()){
                            if (isElse(currentToken)) {
                                return isStatement();
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
                    return false;
                }
            } else {
                printError(505);
                return false;
            }
        } else if (isWhile(currentToken)){
            if (isLeftParenthesis(currentToken)){
                getNextToken();
                if (isBoolean()){
                    evaluateOperation();
                    if (isRightParenthesis(currentToken)){
                        getNextToken();
                        return isStatement();
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
                getNextToken();
                if (isRvalue()){
                    if (isRightParenthesis(currentToken)){
                        getNextToken();
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
        }
        else if (isSemiColon(currentToken)){
            return true;
        }
        else if (isRvalue()) {
            evaluateOperation();
            if(isSemiColon(currentToken)){
                return true;
            } else {
                printError(509);
                return false;
            }
        }
        else {
            printError(509);
            return false;
        }
    }

    private Boolean isBoolean(){
        if (isRvalue()){
            if (isComparator()){
                addToOperationList(currentToken);
                if (isRvalue()){
                   if (isAndOr()){
                       addToOperationList(currentToken);
                       return isBoolean();
                   }
                    return true;
                }
            }
        }
        return false;
    }

    private Boolean isComparator(){
        int tableValue = currentToken.tableValue;
        return tableValue == 112 ||
                tableValue == 109 ||
                tableValue == 113 ||
                tableValue == 114 ||
                tableValue == 115 ||
                tableValue == 116;
    }

    private Boolean isAndOr(){
        int tableValue = currentToken.tableValue;
        return tableValue == 125 || tableValue == 110;
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
        if (operationTokenList == null) operationTokenList = new LinkedList<>();
        if (isLeftParenthesis(currentToken)){
            addToOperationList(currentToken);
            if (isRvalue()){
                if (isRightParenthesis(currentToken)){
                    addToOperationList(currentToken);
                    return isRvalueTail();
                } else {
                    printError(519);
                    return false;
                }
            } else {
                return false;
            }
        } else if (isLvalue()){
            if (isAssign()){
                if (isRvalue()){
                    return isRvalueTail();
                } else {
                    printError(520);
                    return false;
                }
            }
            return isRvalueTail();
        } else if (isConstant(currentToken)) {
            addToOperationList(currentToken);
            return isRvalueTail();
        }
//        else if (isUnary()){
//            if (isRvalue()){
//                return isRvalueTail();
//            } else {
//                printError(520);
//                return false;
//            }
//        }
        else if (isConstant(currentToken)){
            addToOperationList(currentToken);
            return isRvalueTail();
        }
        else if (isAmpersand()){
            if (isLvalue()){
                return isRvalueTail();
            } else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    private Boolean isRvalueTail(){
        if (isBinary()){
            addToOperationList(currentToken);
            if (isRvalue()){
                return true;
            }
            else {
                printError(520);
                return false;
            }
        } else if (isLeftParenthesis(currentToken)){
            addToOperationList(currentToken);
            if(isRightParenthesis(currentToken)){
                addToOperationList(currentToken);
                return true;
            } else if (isRvalue()){
                while (isComma(currentToken)){
                    if(!isRvalue()){
                        printError(520);
                        return false;
                    }
                }
                if (isRightParenthesis(currentToken)){
                    addToOperationList(currentToken);
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

    private void printError(int error){
        if (currentToken != null) {
            this.errorStack += "Error " + error + " cerca de '" + currentToken.lexeme + "' linea "
                    + currentToken.lineNumber + ": " + getErrorMessage(error) + "\n";
        } else {
            this.errorStack += "Error " + error + ": " + getErrorMessage(error) + "\n";
        }
    }

    private String getErrorMessage(int errorNumber){
        switch (errorNumber){
            case 503 : return "Se espera identificador.";
            case 504 : return "Se espera apertura de corchete.";
            case 505 : return "Se espera apertura de parentesis.";
            case 506 : return "Se espera bloque constante o identificador.";
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
            case 524 : return "syntax.Variable ya declarada";

        }
        return "";
    }

    private Boolean isBinary(){
        int tableValue = currentToken.tableValue;
        return  tableValue == 103 ||
                tableValue == 104 ||
                tableValue == 105 ||
                tableValue == 102;
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
            addToOperationList(currentToken);
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
            addToOperationList(currentToken);
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
            return true;
        }
        return false;
    }

    private Boolean isName(Token token){
        if (token == null){
            return false;
        } else if (token.tableValue == 100){
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
            return true;
        }
        return false;
    }

    private Boolean isRightParenthesis(Token token){
        if (token == null) {
            return false;
        } else if (token.tableValue == 120){
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
