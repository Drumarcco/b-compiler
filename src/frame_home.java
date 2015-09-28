import lexis.LexicalAnalysis;
import lexis.Token;
import syntax.SyntacticalAnalysis;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by drumarcco on 10/04/15.
 * Time: 03:37 PM
 */
public class frame_home extends JFrame {
    private JTextArea txt_input;
    private JPanel panel1;
    private JButton btn_open;
    private JTextArea txt_output;
    private JButton btn_read;
    private LexicalAnalysis lexicalAnalysis;

    public frame_home(){
        super("B Compiler Lexical Analysis");
        initializeFrame();
        lexicalAnalysis = new LexicalAnalysis();
        addListeners();

    }

    private void initializeFrame(){
        setContentPane(panel1);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addListeners(){
        txt_input.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                lexicalAnalysis.fileString = txt_input.getText() + "\n";
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                lexicalAnalysis.fileString = txt_input.getText() + "\n";
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        btn_open.addActionListener(e -> selectFile());
        btn_read.addActionListener(e -> {
            txt_output.setText("");
            if(lexicalAnalysis.generateTokens()){
                printTokenList();
                new SyntacticalAnalysis();
            }
        });
    }

    public void selectFile(){
        JFileChooser fileChooser= new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION){
            File selectedFile = fileChooser.getSelectedFile();
            readFile(selectedFile);
        }
    }

    private void readFile(File file){
        lexicalAnalysis.fileString = "";
        FileReader fr;
        BufferedReader br = null;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null){
                lexicalAnalysis.fileString += line + "\n";
            }
            txt_input.setText(lexicalAnalysis.fileString);
            lexicalAnalysis.generateTokens();
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

    private void printTokenList(){
        txt_output.setText("");
        for(Token token : lexicalAnalysis.tokenList){
            String line = "lexis.Token: ";
            line += token.tableValue + " ";
            line += token.lineNumber + " ";
            line += token.lexeme;
            txt_output.append(line + "\n");
        }
    }
}
