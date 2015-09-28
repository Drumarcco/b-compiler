package parsing;

import lexis.Token;
import java.util.LinkedList;

/**
 * Created by MarcoAntonio on 9/27/2015.
 */
public class ASTNode {
    private final Token value;
    private final ASTNode leftASTNode;
    private final ASTNode rightASTNode;

    public ASTNode(Token value, ASTNode leftASTNode, ASTNode rightASTNode) {
        this.value = value;
        this.leftASTNode = leftASTNode;
        this.rightASTNode = rightASTNode;
    }

    public Token getValue(){
        return value;
    }

    public ASTNode getLeftASTNode(){
        return leftASTNode;
    }

    public ASTNode getRightASTNode(){
        return rightASTNode;
    }
}
