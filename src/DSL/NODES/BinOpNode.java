package DSL.NODES;

import DSL.TOKENS.Token;

public class BinOpNode extends Node {
    Token operator;
    Node leftOperand;
    Node rightOperand;

    public BinOpNode(Token operator, Node leftOperand, Node rightOperand) {
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
    }

    public Token getOperator() {
        return operator;
    }

    public Node getLeftOperand() {
        return leftOperand;
    }

    public Node getRightOperand() {
        return rightOperand;
    }
}
