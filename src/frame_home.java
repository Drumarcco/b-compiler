import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by drumarcco on 10/04/15.
 * Time: 03:37 PM
 */
public class frame_home extends JFrame {
    private JTextArea txt_output;
    private JPanel panel1;

    public frame_home(){
        super("B Compiler Lexical Analysis");
        setContentPane(panel1);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
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
        FileReader fr = null;

        try {
            String stringFile = "";
            int ASCIIChar;
            fr = new FileReader(file);

            while ((ASCIIChar = fr.read()) != -1){
                char character = (char) ASCIIChar;


                String currentCharString = String.valueOf(character);
                stringFile += currentCharString;
            }

            txt_output.setText(stringFile);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fr != null) fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
