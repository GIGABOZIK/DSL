package DSL2.NODES;

import DSL2.TOKENS.Token;

public class UnOpNode extends Node {
    Token operator;
    Node arg;

    public UnOpNode(Token operator, Node arg) {
        this.operator = operator;
        this.arg = arg;
    }
}