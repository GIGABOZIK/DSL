package DSL.fparser;

import DSL.flexer.Tokens;

public class ParseException extends Exception {
    public ParseException(Tokens.Token token, String expected) {
        super("Line " + token.getLine() + " at " + token.getPosL() + ":\n"
                + expected + " expected, instead was " + token);
    }

    public static void main(String[] args) throws ParseException {
        throw new ParseException(
                new Tokens.Token("TokenType", "TokenVal"), "SmthElse"
        );
    }
}
