import javax.swing.*;
import java.util.LinkedList;

/**
 * Created by drumarcco on 20/05/15.
 * Time: 11:19 AM
 */
public class LexicalAnalysis {
    public static LinkedList<Token> tokenList;
    private LexicalStatesTable lexicalStatesTable;
    public String fileString = "";

    public LexicalAnalysis() {
        lexicalStatesTable = lexicalStatesTable.getInstance();
    }

    public void generateTokens(){
        tokenList = new LinkedList<>();
        int lineNumber = 1;
        int state = 0;
        String lexeme = "";
        for (int i=0; i<fileString.length(); i++){
            char currentChar = fileString.charAt(i);
            if (currentChar == '\n') lineNumber++;
            String key = checkAlphaDigit(currentChar);

            int tableValue = lexicalStatesTable.getTableValue(state, key);

            if (tableValue == 0){
                state = 0;
                lexeme = "";
            } else if (tableValue < 100){
                lexeme += currentChar;
                state = tableValue;
            } else if (tableValue >= 100 && tableValue < 500){
                if (isIndexDecrementRequired(tableValue)){
                    i--;
                } else {
                    lexeme += currentChar;
                }
                if (tableValue == 100) tableValue = searchKeyword(lexeme);
                Token token = new Token(lexeme, tableValue, lineNumber);
                addTokenToList(token);
                state = 0;
                lexeme = "";
            } else if (tableValue >= 500) {
                printError(tableValue, lineNumber);
                break;
            }
        }
    }

    private String checkAlphaDigit(char character){
        if (Character.isLetter(character) || character == '_') return "Alpha";
        if (Character.isDigit(character)) return "Digit";
        return character + "";
    }

    private boolean isIndexDecrementRequired(int state){
        switch (state){
            case 100: return true;
            case 101: return true;
            case 102: return true;
            case 108: return true;
            case 111: return true;
            case 113: return true;
            case 115: return true;
        }
        return state >= 200 && state < 500;
    }

    private int searchKeyword(String lexeme){
        switch (lexeme){
            case "if": return 200;
            case "else": return 201;
            case "while": return 202;
            case "return": return 203;
            case "break": return 204;
            case "goto": return 205;
            case "next": return 206;
            case "auto": return 207;
            case "extrn": return 208;
            case "main": return 209;
            default: return 100;
        }
    }

    private void addTokenToList(Token token){
        tokenList.add(token);
    }

    private void printError(int errorNumber, int lineNumber){
        String message = getError(errorNumber) + "\nLinea: " + lineNumber;
        JOptionPane.showMessageDialog(null, message);
    }

    private String getError(int errorNumber){
        switch (errorNumber){
            case 500: return "Error 500. Caracter no válido.";
            case 501: return "Error 501. Fin de archivo inesperado.";
            case 502: return "Error 502. Nueva linea inesperada.";
            default: return "Error " + errorNumber + ".";
        }
    }
}
