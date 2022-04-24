package DSL.fparser;

import DSL.flexer.Tokens;

public class Parseption extends Exception {
    String Message = null;

    public Parseption() {
    }

    public Parseption(Tokens.Token token, String expected) {
        Message = "Line " + token.getLine() + " at " + token.getPosL() + ":\n"
            + expected + " expected, instead was " + token;
    }

    public String getMessage() {
        return Message;
    }

    public static void main(String[] args) throws Parseption {
        throw new Parseption(
                new Tokens.Token("TokenType", "TokenVal"), "SmthElse"
        );
    }
}
