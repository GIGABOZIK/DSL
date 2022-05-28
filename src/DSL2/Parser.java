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
        // Перебор подходящих токенов для текущей позиции
        if (position < tokenList.size()) {
            Token curToken = tokenList.get(position);
            for (String sampleTokenType : sampleTypes) {
                if (sampleTokenType.equals(curToken.type())) {
                    position++;
                    return curToken;
                }
            }
        }
        // Если все указанные типы не подошли
        return null;
    }
    private Token expect(String[] expected) { // Нафига здесь массив ? void ?~~~~~~~~~~~~~~~~~~~~~~
        // Функция, которая требует наличие обязательных токенs
        Token seekToken = seekToken(expected);
        if (seekToken == null) {
//            thrower(expected, position, tokenList.get(position));
//            throw new Error(Arrays.toString(expected) + "\nexpected at position " + position);
            throw new Error(Arrays.toString(expected) + "\nexpected at position " + position +
                    "\ninstead was" + tokenList.get(position));
        }
        return seekToken;
    }
    //
    //
    // getOperations() ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    private void maybeEOL(Node codeChainNode) {
        // Функция определения символа конца строки (SEP_END_LINE)
        // он нужен не для всех узлов (типа for, while...)
        String ccncName = codeChainNode.getClass().getName();
        if (!(ccncName.equals("ForNode")
                | ccncName.equals("WhileNode")
                | ccncName.equals("IfElseNode")
        )) expect(new String[]{"SEP_END_LINE"});
        else
            // Но его наличие - не проблема
            seekToken(new String[]{"SEP_END_LINE"});
    }
    // Основной метод
    public RootNode parseLang() {
        // Основной метод обработки грамматики
        RootNode rootNode = new RootNode();
        while (position < tokenList.size()) {
            Node codeChainNode = parseExpr();
//            maybeEOL(codeChainNode);
            seekToken(new String[]{"SEP_END_LINE"});
            rootNode.addNode(codeChainNode);
        }
        // Если ошибок не было выявлено
        return rootNode;
    }
    //
    private Node parseExpr() {
        Token seekToken = expect(new String[]{"IDENT", "KW_WRITE", "KW_FOR", "KW_WHILE"});
        switch (seekToken.type()) {
//        switch (tokenList.get(position).type()) {
            case "IDENT" -> {
                Node idNode = parseValue();
                Token assign = expect(new String[]{"ASSIGN_OP"}); // seekToken?
                    Node asValue = parseOperation();
                    return new BinOpNode(assign, idNode, asValue);
//                thrower(new String[]{"ASSIGN_OP"}, position, tokenList.get(position));
            }
            case "KW_WRITE" -> {
                position++;
                return new UnOpNode(tokenList.get(position - 1), parseOperation()); // this ~~~~~~~~~~~~~~~~
            }
            case "KW_FOR" -> {
                position++;
                return parseFor();
            }
            case "KW_WHILE" -> {
                position++;
                return parseWhile();
            }
        }
        return null;
    }
    //
    private Node parseValue() {
        return null;
    }
    //
    //
    //
    //
    //
    //
    //
}
