package parsing;

/**
 * Created by MarcoAntonio on 9/27/2015.
 */
public interface Operator {

    boolean isRightAssociative();

    int  comparePrecedence(Operator o);

    String getSymbol();
}
