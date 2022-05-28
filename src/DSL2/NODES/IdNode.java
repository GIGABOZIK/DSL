package DSL2.NODES;

import DSL2.TOKENS.Token;

public class IdNode extends Node {
    Token ident;
    public IdNode(Token ident) { this.ident = ident; }
}