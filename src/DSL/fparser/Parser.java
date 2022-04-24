package DSL.fparser;

import DSL.flexer.Tokens;

import java.util.LinkedList;

public class Parser {
    private LinkedList<Tokens.Token> tokenList;
    private int iterio = 0;

    public Parser(LinkedList<Tokens.Token> tokenList) { this.tokenList = tokenList; }

    public void lang() {

    }
}

