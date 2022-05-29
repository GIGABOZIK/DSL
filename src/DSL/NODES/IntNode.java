package DSL.NODES;

import DSL.TOKENS.Token;

public class IntNode extends Node {
    Token number;
    public IntNode(Token number) { this.number = number; }

    public Token getNumber() {
        return number;
    }
}
