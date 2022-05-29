package DSL.NODES;

import DSL.TOKENS.Token;

public class UnOpNode extends Node {
    Token operator;
    Node arg;

    public UnOpNode(Token operator, Node arg) {
        this.operator = operator;
        this.arg = arg;
    }

    public Token getOperator() { return operator; }

    public Node getArg() { return arg; }
}
