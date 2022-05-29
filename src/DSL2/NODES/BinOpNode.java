package DSL2.NODES;

import DSL2.TOKENS.Token;

public class BinOpNode extends Node {
    Token operator;
    Node leftOperand;
    Node rightOperand;

    public BinOpNode(Token operator, Node leftOperand, Node rightOperand) {
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
    }
}