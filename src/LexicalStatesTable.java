import java.util.HashMap;

/**
 * Created by drumarcco on 10/04/15.
 * Time: 03:35 PM
 */
public class LexicalStatesTable {
    private static LexicalStatesTable instance = null;
    public HashMap<Integer, HashMap<String, Integer>> table;

    private LexicalStatesTable(){
        table = new HashMap<>();
        table.put(0, hash0());
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
        return hash;
    }

    public static LexicalStatesTable getInstance(){
        if (instance == null) {
            instance = new LexicalStatesTable();
        }
        return instance;
    }
}
