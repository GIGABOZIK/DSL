package DSL2.NODES;

import DSL2.TOKENS.Token;

public class StringNode extends Node {
    Token string;

    public StringNode(Token string) { this.string = string; }

    public Token getString() { return string; }
}
