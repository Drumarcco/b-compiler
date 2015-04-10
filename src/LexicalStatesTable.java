import java.util.HashMap;

/**
 * Created by drumarcco on 10/04/15.
 * Time: 03:35 PM
 */
public class LexicalStatesTable {
    private static LexicalStatesTable instance = null;
    public HashMap<Integer, HashMap<String, Integer>> table;

    private LexicalStatesTable(){
        initializeLexicalTable();
    }

    private void initializeLexicalTable(){
        table = new HashMap<>();
        table.put(0, hash0());
        table.put(1, hash1());
        table.put(2, hash2());
        table.put(3, hash3());
        table.put(4, hash4());
        table.put(5, hash5());
        table.put(6, hash6());
        table.put(7, hash7());
        table.put(8, hash8());
        table.put(9, hash9());
        table.put(10, hash10());
        table.put(11, hash11());
        table.put(12, hash12());
    }

    private HashMap<String, Integer> hash0(){
        HashMap<String, Integer> hash= new HashMap<>();
        hash.put("Alpha", 1);
        hash.put("Digit", 2);
        hash.put("/", 3);
        hash.put("*", 105);
        hash.put("-", 103);
        hash.put("+", 104);
        hash.put("\'", 6);
        hash.put("\"", 8);
        hash.put("!", 9);
        hash.put("=", 10);
        hash.put("|", 109);
        hash.put("&", 110);
        hash.put("<", 11);
        hash.put(">", 12);
        hash.put(";", 117);
        hash.put(",", 118);
        hash.put("(", 119);
        hash.put(")", 120);
        hash.put("{", 121);
        hash.put("}", 122);
        hash.put("[", 123);
        hash.put("]", 124);
        hash.put(" ", 0);
        hash.put("\t", 0);
        hash.put("\n", 0);
        return hash;
    }

    private HashMap<String, Integer> hash1(){
        HashMap<String, Integer> hash= new HashMap<>();
        hash.put("Alpha", 1);
        hash.put("Digit", 1);
        hash.put("/", 100);
        hash.put("*", 100);
        hash.put("-", 100);
        hash.put("+", 100);
        hash.put("\'", 100);
        hash.put("\"", 100);
        hash.put("!", 100);
        hash.put("=", 100);
        hash.put("|", 100);
        hash.put("&", 100);
        hash.put("<", 100);
        hash.put(">", 100);
        hash.put(";", 100);
        hash.put(",", 100);
        hash.put("(", 100);
        hash.put(")", 100);
        hash.put("{", 100);
        hash.put("}", 100);
        hash.put("[", 100);
        hash.put("]", 100);
        hash.put(" ", 100);
        hash.put("\t", 100);
        hash.put("\n", 100);
        return hash;
    }

    private HashMap<String, Integer> hash2(){
        HashMap<String, Integer> hash= new HashMap<>();
        hash.put("Alpha", 101);
        hash.put("Digit", 2);
        hash.put("/", 101);
        hash.put("*", 101);
        hash.put("-", 101);
        hash.put("+", 101);
        hash.put("\'", 101);
        hash.put("\"", 101);
        hash.put("!", 101);
        hash.put("=", 101);
        hash.put("|", 101);
        hash.put("&", 101);
        hash.put("<", 101);
        hash.put(">", 101);
        hash.put(";", 101);
        hash.put(",", 101);
        hash.put("(", 101);
        hash.put(")", 101);
        hash.put("{", 101);
        hash.put("}", 101);
        hash.put("[", 101);
        hash.put("]", 101);
        hash.put(" ", 101);
        hash.put("\t", 101);
        hash.put("\n", 101);
        return hash;
    }

    private HashMap<String, Integer> hash3(){
        HashMap<String, Integer> hash= new HashMap<>();
        hash.put("Alpha", 102);
        hash.put("Digit", 102);
        hash.put("/", 102);
        hash.put("*", 4);
        hash.put("-", 102);
        hash.put("+", 102);
        hash.put("\'", 102);
        hash.put("\"", 102);
        hash.put("!", 102);
        hash.put("=", 102);
        hash.put("|", 102);
        hash.put("&", 102);
        hash.put("<", 102);
        hash.put(">", 102);
        hash.put(";", 102);
        hash.put(",", 102);
        hash.put("(", 102);
        hash.put(")", 102);
        hash.put("{", 102);
        hash.put("}", 102);
        hash.put("[", 102);
        hash.put("]", 102);
        hash.put(" ", 102);
        hash.put("\t", 102);
        hash.put("\n", 102);
        return hash;
    }

    private HashMap<String, Integer> hash4(){
        HashMap<String, Integer> hash= new HashMap<>();
        hash.put("Alpha", 4);
        hash.put("Digit", 4);
        hash.put("/", 4);
        hash.put("*", 5);
        hash.put("-", 4);
        hash.put("+", 4);
        hash.put("\'", 4);
        hash.put("\"", 4);
        hash.put("!", 4);
        hash.put("=", 4);
        hash.put("|", 4);
        hash.put("&", 4);
        hash.put("<", 4);
        hash.put(">", 4);
        hash.put(";", 4);
        hash.put(",", 4);
        hash.put("(", 4);
        hash.put(")", 4);
        hash.put("{", 4);
        hash.put("}", 4);
        hash.put("[", 4);
        hash.put("]", 4);
        hash.put(" ", 4);
        hash.put("\t", 4);
        hash.put("\n", 4);
        return hash;
    }

    private HashMap<String, Integer> hash5(){
        HashMap<String, Integer> hash= new HashMap<>();
        hash.put("Alpha", 4);
        hash.put("Digit", 4);
        hash.put("/", 0);
        hash.put("*", 4);
        hash.put("-", 4);
        hash.put("+", 4);
        hash.put("\'", 4);
        hash.put("\"", 4);
        hash.put("!", 4);
        hash.put("=", 4);
        hash.put("|", 4);
        hash.put("&", 4);
        hash.put("<", 4);
        hash.put(">", 4);
        hash.put(";", 4);
        hash.put(",", 4);
        hash.put("(", 4);
        hash.put(")", 4);
        hash.put("{", 4);
        hash.put("}", 4);
        hash.put("[", 4);
        hash.put("]", 4);
        hash.put(" ", 4);
        hash.put("\t", 4);
        hash.put("\n", 4);
        return hash;
    }

    private HashMap<String, Integer> hash6(){
        HashMap<String, Integer> hash= new HashMap<>();
        hash.put("Alpha", 7);
        hash.put("Digit", 7);
        hash.put("/", 7);
        hash.put("*", 7);
        hash.put("-", 7);
        hash.put("+", 7);
        hash.put("\'", 7);
        hash.put("\"", 7);
        hash.put("!", 7);
        hash.put("=", 7);
        hash.put("|", 7);
        hash.put("&", 7);
        hash.put("<", 7);
        hash.put(">", 7);
        hash.put(";", 7);
        hash.put(",", 7);
        hash.put("(", 7);
        hash.put(")", 7);
        hash.put("{", 7);
        hash.put("}", 7);
        hash.put("[", 7);
        hash.put("]", 7);
        hash.put(" ", 7);
        hash.put("\t", 7);
        hash.put("\n", 7);
        return hash;
    }

    private HashMap<String, Integer> hash7(){
        HashMap<String, Integer> hash= new HashMap<>();
        hash.put("Alpha", 500);
        hash.put("Digit", 500);
        hash.put("/", 500);
        hash.put("*", 500);
        hash.put("-", 500);
        hash.put("+", 500);
        hash.put("\'", 106);
        hash.put("\"", 500);
        hash.put("!", 500);
        hash.put("=", 500);
        hash.put("|", 500);
        hash.put("&", 500);
        hash.put("<", 500);
        hash.put(">", 500);
        hash.put(";", 500);
        hash.put(",", 500);
        hash.put("(", 500);
        hash.put(")", 500);
        hash.put("{", 500);
        hash.put("}", 500);
        hash.put("[", 500);
        hash.put("]", 500);
        hash.put(" ", 500);
        hash.put("\t", 500);
        hash.put("\n", 500);
        return hash;
    }

    private HashMap<String, Integer> hash8(){
        HashMap<String, Integer> hash= new HashMap<>();
        hash.put("Alpha", 8);
        hash.put("Digit", 8);
        hash.put("/", 8);
        hash.put("*", 8);
        hash.put("-", 8);
        hash.put("+", 8);
        hash.put("\'", 8);
        hash.put("\"", 107);
        hash.put("!", 8);
        hash.put("=", 8);
        hash.put("|", 8);
        hash.put("&", 8);
        hash.put("<", 8);
        hash.put(">", 8);
        hash.put(";", 8);
        hash.put(",", 8);
        hash.put("(", 8);
        hash.put(")", 8);
        hash.put("{", 8);
        hash.put("}", 8);
        hash.put("[", 8);
        hash.put("]", 8);
        hash.put(" ", 8);
        hash.put("\t", 8);
        hash.put("\n", 502);
        return hash;
    }

    private HashMap<String, Integer> hash9(){
        HashMap<String, Integer> hash= new HashMap<>();
        hash.put("Alpha", 108);
        hash.put("Digit", 108);
        hash.put("/", 108);
        hash.put("*", 108);
        hash.put("-", 108);
        hash.put("+", 108);
        hash.put("\'", 108);
        hash.put("\"", 108);
        hash.put("!", 108);
        hash.put("=", 109);
        hash.put("|", 108);
        hash.put("&", 108);
        hash.put("<", 108);
        hash.put(">", 108);
        hash.put(";", 108);
        hash.put(",", 108);
        hash.put("(", 108);
        hash.put(")", 108);
        hash.put("{", 108);
        hash.put("}", 108);
        hash.put("[", 108);
        hash.put("]", 108);
        hash.put(" ", 108);
        hash.put("\t", 108);
        hash.put("\n", 502);
        return hash;
    }

    private HashMap<String, Integer> hash10(){
        HashMap<String, Integer> hash= new HashMap<>();
        hash.put("Alpha", 111);
        hash.put("Digit", 111);
        hash.put("/", 111);
        hash.put("*", 111);
        hash.put("-", 111);
        hash.put("+", 111);
        hash.put("\'", 111);
        hash.put("\"", 111);
        hash.put("!", 111);
        hash.put("=", 112);
        hash.put("|", 111);
        hash.put("&", 111);
        hash.put("<", 111);
        hash.put(">", 111);
        hash.put(";", 111);
        hash.put(",", 111);
        hash.put("(", 111);
        hash.put(")", 111);
        hash.put("{", 111);
        hash.put("}", 111);
        hash.put("[", 111);
        hash.put("]", 111);
        hash.put(" ", 111);
        hash.put("\t", 111);
        hash.put("\n", 111);
        return hash;
    }

    private HashMap<String, Integer> hash11(){
        HashMap<String, Integer> hash= new HashMap<>();
        hash.put("Alpha", 113);
        hash.put("Digit", 113);
        hash.put("/", 113);
        hash.put("*", 113);
        hash.put("-", 113);
        hash.put("+", 113);
        hash.put("\'", 113);
        hash.put("\"", 113);
        hash.put("!", 113);
        hash.put("=", 114);
        hash.put("|", 113);
        hash.put("&", 113);
        hash.put("<", 113);
        hash.put(">", 113);
        hash.put(";", 113);
        hash.put(",", 113);
        hash.put("(", 113);
        hash.put(")", 113);
        hash.put("{", 113);
        hash.put("}", 113);
        hash.put("[", 113);
        hash.put("]", 113);
        hash.put(" ", 113);
        hash.put("\t", 113);
        hash.put("\n", 113);
        return hash;
    }

    private HashMap<String, Integer> hash12(){
        HashMap<String, Integer> hash= new HashMap<>();
        hash.put("Alpha", 115);
        hash.put("Digit", 115);
        hash.put("/", 115);
        hash.put("*", 115);
        hash.put("-", 115);
        hash.put("+", 115);
        hash.put("\'", 115);
        hash.put("\"", 115);
        hash.put("!", 115);
        hash.put("=", 116);
        hash.put("|", 115);
        hash.put("&", 115);
        hash.put("<", 115);
        hash.put(">", 115);
        hash.put(";", 115);
        hash.put(",", 115);
        hash.put("(", 115);
        hash.put(")", 115);
        hash.put("{", 115);
        hash.put("}", 115);
        hash.put("[", 115);
        hash.put("]", 115);
        hash.put(" ", 115);
        hash.put("\t", 115);
        hash.put("\n", 115);
        return hash;
    }


    public static LexicalStatesTable getInstance(){
        if (instance == null) {
            instance = new LexicalStatesTable();
        }
        return instance;
    }
}
