package razrpop.DSL.fparser;

import razrpop.DSL.flexer.Tokens;

import java.util.LinkedList;

public class Parser {
    private LinkedList<Tokens.Token> tokList;

    public Parser(LinkedList<Tokens.Token> tokList) {
        this.tokList = tokList;
    }

    public void start() {
        System.out.println("\nTokens:\n"); // temp
        for (Tokens.Token token : tokList) System.out.println(token); // temp
        //

    }
}
