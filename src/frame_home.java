import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by drumarcco on 10/04/15.
 * Time: 03:37 PM
 */
public class frame_home extends JFrame {
    private JTextArea txt_output;
    private JPanel panel1;
    private LexicalStatesTable lexicalStatesTable;
    private LinkedList<Token> tokenList;
    private String fileString = "";

    public frame_home(){
        super("B Compiler Lexical Analysis");
        setContentPane(panel1);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        lexicalStatesTable = LexicalStatesTable.getInstance();
        tokenList = new LinkedList<>();
        selectFile();

    }

    private void selectFile(){
        JFileChooser fileChooser= new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION){
            File selectedFile = fileChooser.getSelectedFile();
            readFile(selectedFile);
        }
    }

    private void readFile(File file){
        FileReader fr;
        BufferedReader br = null;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null){
                fileString += line + "\n";
            }
            txt_output.setText(fileString);
            generateTokens();
            printTokenList();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try{
                if(br!=null){
                    br.close();
                }
            } catch (IOException exc){
                exc.printStackTrace();
            }
        }
    }

    private void generateTokens(){
        int lineNumber = 1;
        int state = 0;
        String lexeme = "";
        for (int i=0; i<fileString.length(); i++){
            char currentChar = fileString.charAt(i);
            if (state==0) lexeme += currentChar;
            String key = checkAlphaDigit(currentChar);
            if (currentChar == '\n') lineNumber++;

            int tableValue = getTableValue(state, key);

            if (tableValue == 0){
                state = 0;
                lexeme = "";
            } else if (tableValue < 100){
                if (state != 0) lexeme += currentChar;
                state = tableValue;
            } else if (tableValue >= 100 && tableValue < 500){
                if (isIndexDecrementRequired(tableValue)){
                    i--;
                } else if (lexeme.length() > 1){
                    lexeme += currentChar;
                }
                Token token = new Token(lexeme, tableValue, lineNumber);
                addTokenToList(token);
                state = 0;
                lexeme = "";
            } else if (tableValue >= 500) {
                System.out.println("ERROR!");
                break;
            }
        }
    }

    private boolean isIndexDecrementRequired(int state){
        switch (state){
            case 100: return true;
            case 101: return true;
            case 102: return true;
            case 103: return true;
            case 108: return true;
            case 111: return true;
            case 113: return true;
            case 115: return true;
        }
        return state >= 200 && state < 500;
    }

    private void printTokenList(){
        for(Token token : tokenList){
            String line = "Token: ";
            line += token.tableValue + " ";
            line += token.lexeme;
            System.out.println(line);
        }
    }

    private String checkAlphaDigit(char character){
        if (Character.isLetter(character) || character == '_') return "Alpha";
        if (Character.isDigit(character)) return "Digit";
        return character + "";
    }

    private int getTableValue(int state, String key){
        HashMap row = lexicalStatesTable.table.get(state);
        return (int) row.get(key);
    }

    private int searchKeyword(String lexeme){
        switch (lexeme){
            case "main": return 200;
            default: return 100;
        }
    }

    private void addTokenToList(Token token){
        tokenList.add(token);
    }

}
