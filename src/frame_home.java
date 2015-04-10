import javax.swing.*;
import java.io.BufferedReader;
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
        BufferedReader br = null;

        try {
            String stringFile = "";
            String sCurrentLine;

            br = new BufferedReader (new FileReader(file));
            while ((sCurrentLine = br.readLine()) != null){
                stringFile += sCurrentLine + "\n";
            }
            txt_output.setText(stringFile);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
