package DSL2;

import DSL2.NODES.*;
import DSL2.TOKENS.Token;

import java.util.ArrayList;
import java.util.Arrays;

public class Parser {
    ArrayList<Token> tokenList;
    int position = 0;

    public Parser() { this.tokenList = new Lexer().getTokens(); }
    public Parser(ArrayList<Token> tokenList) { this.tokenList = tokenList; }

    private Token seekToken(String[] sampleTypes) {
        if (position < tokenList.size()) {
            Token curToken = tokenList.get(position);
            for (String sampleTokenType : sampleTypes) {
                if (sampleTokenType.equals(curToken.type())) {
                    position++;
                    return curToken;
                }
            }
        }
        return null; // Если все указанные типы не подошли
    }
    private void expect(String[] expected) {
        if (seekToken(expected) == null) {
            throw new Error(Arrays.toString(expected) + "\nexpected at position " + position);
        }
    }
    //
    // getOperations()
    //
    private void maybeEOL(Node codeChainNode) {
        // Функция определения символа конца строки (SEP_END_LINE)
        // он нужен не для всех узлов (типа for, while...)
        String cccName = codeChainNode.getClass().getName();
        if (!(cccName.equals("ForNode")
                | cccName.equals("WhileNode")
                | cccName.equals("IfElseNode")
        )) expect(new String[]{"SEP_END_LINE"});
        else
            // Но его наличие - не проблема
            seekToken(new String[]{"SEP_END_LINE"});
    }
    // Основной метод
    public RootNode parseLang() {
        RootNode rootNode = new RootNode();
        while (position < tokenList.size()) {
            Node codeChainNode = parseExpr();...
            maybeEOL(codeChainNode);
            rootNode.addNode(codeChainNode);
        }
        return null;
    }
    //
    private ...
    //
    //
    //
    //
    //
    //
    //
    //
}
