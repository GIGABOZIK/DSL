package DSL.fparser;

import DSL.flexer.Tokens;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class Parser {
    public static final LinkedHashMap<String, Pattern> tokenA = new Tokens().tokenA;
    private LinkedList<Tokens.Token> tokenList;
    private int iterio = 0;

    public Parser(LinkedList<Tokens.Token> tokenList) { this.tokenList = tokenList; }

    public void lang() {
//        for (Tokens.Token token : tokenList) {
//            System.out.println("#" + token.getLine() + " #" + token.getPosL() + " #~ " + token.getType() + " ~#" + token.getValue() + "#~");
//        }
        //
//        StringBuilder sB = new StringBuilder();
//        for (Tokens.Token token : tokenList) {
//            sB.append(token.getType()).append(" ");
//        }
//        System.out.println(sB);
        //

        // Чооо
        //
//        void lang() {
//        }

    }
}

