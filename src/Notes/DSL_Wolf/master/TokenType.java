package Notes.DSL_Wolf.master;

public class TokenType {
    String typeName;
    String reg;
    public static TokenType[] tokenTypeList={
            new TokenType("NUM", "^0|[1-9][0-9]*"),

            new TokenType("SPACE", "\\ "),
            new TokenType("EndL", "[\\n]"),
            new TokenType("Karetka", "[\\r]"),

            new TokenType("ASSIGN", "[=]"),
            new TokenType("PLUS", "[+]"),
            new TokenType("MINUS", "[-]"),
            new TokenType("MULT", "[*]"),
            new TokenType("DIV", "[/]"),

            new TokenType("LESS", "[<]"),
            new TokenType("MORE", "[>]"),
            new TokenType("EQUAL", "(?i)=="),

            new TokenType("PRINT", "(?i)print"),
            new TokenType("FOR", "(?i)for"),
            new TokenType("WHILE","(?i)while"),

            new TokenType("END", "[;]"),
            new TokenType("LPAR", "[(]"),
            new TokenType("RPAR", "[)]"),
            new TokenType("LRectPar", "[{]"),
            new TokenType("RRectPAR", "[}]"),

            new TokenType("VAR", "[a-z][a-z]*"),
            //float string лист
    };
    public TokenType(String typeName, String reg) {
        this.typeName = typeName;
        this.reg = reg;
    }
}
