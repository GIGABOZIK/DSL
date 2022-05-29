package DSL2.NODES;

import DSL2.TOKENS.Token;

public class IntNode extends Node {
    Token number;
    public IntNode(Token number) { this.number = number; }

    public Token getNumber() {
        return number;
    }
}
