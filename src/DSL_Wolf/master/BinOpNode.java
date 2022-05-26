package DSL_Wolf.master;

public class BinOpNode extends Node {
    Token operator;
    Node leftVal;
    Node rightVal;

    public BinOpNode(Token operator, Node leftVal, Node rightVal) {
        this.operator = operator;
        this.leftVal = leftVal;
        this.rightVal = rightVal;
    }
}
