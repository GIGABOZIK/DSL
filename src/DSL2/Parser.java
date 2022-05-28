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
            if (curToken.type().equals("OL_COMMENT")) {
                position++;
                return seekToken(sampleTypes);
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
            throw new Error("\n" + Arrays.toString(expected) + " expected at position " + position +
                    ", instead was\n" + tokenList.get(position));
        }
        return seekToken;
    }
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
        Token seekToken = expect(new String[]{"IDENT", "KW_WRITE", "KW_FOR", "KW_WHILE", "OL_COMMENT"});
        switch (seekToken.type()) {
            case "IDENT" -> {
                IdNode idNode = new IdNode(seekToken);
                Token assign = expect(new String[]{"ASSIGN_OP"});
                Node asValue = parseValue();
                return new BinOpNode(assign, idNode, asValue);
            }
            case "KW_FOR" -> {
                return parseFor();
            }
            case "KW_WHILE" -> {
                return parseWhile();
            }
            case "KW_WRITE" -> {
                Token string = seekToken(new String[]{"STRING"});
                Node valueToPrint = string != null ? new StringNode(seekToken) : parseValue();
                return new UnOpNode(seekToken, valueToPrint); // this ~~~~~~~~~~~~~~~~
            }
        }
        return null;
    }
    //
    private Node parseValue() { // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Node leftOperand = parseOpMulDiv();
        Token operator = seekToken(new String[]{"ADD_OP", "SUB_OP"});
        while (operator != null) {
            Node rightOperand = parseOpMulDiv();
            leftOperand = new BinOpNode(operator, leftOperand, rightOperand);
            operator = seekToken(new String[]{"ADD_OP", "SUB_OP"});
        }
        return leftOperand;
    }
    private Node parseOpMulDiv() {
        Node leftOperand = parseBracketsOp();
        Token operator = seekToken(new String[]{"MUL_OP", "DIV_OP"});
        while (operator != null) {
            Node rightOperand = parseBracketsOp();
            leftOperand = new BinOpNode(operator, leftOperand, rightOperand);
            operator = seekToken(new String[]{"MUL_OP", "DIV_OP"});
        }
        return leftOperand;
    }
    private Node parseBracketsOp() {
        if (seekToken(new String[]{"SEP_L_BRACKET"}) != null) {
            Node inner = parseValue();
            expect(new String[]{"SEP_R_BRACKET"});
            return inner;
        }
//        return parseValue();
        Token next = expect(new String[]{"INT", "IDENT"}); // "STRING"
        switch (next.type()) {
            case "INT" -> { return new IntNode(next); }
            case "IDENT" -> { return new IdNode(next); }
        }
        return null;
    }
    //
    //
    //
    // getOperations() ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //
    //
    //
    //
    //
//    private Node parseValue() { // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//        Token seekToken = expect(new String[]{"INT", "IDENT", "STRING", "SEP_L_BRACKET"});
//        switch (seekToken.type()) {
//            case "INT" -> {
//                Token muldiv = seekToken(new String[]{"MUL_OP", "DIV_OP"});
//                if (muldiv != null) {
//                     //Mul_Div_op
//                    parseValue();
//                }
//                Token addsub = seekToken(new String[]{"ADD_OP", "SUB_OP"});
//                if (addsub != null) {
//                     //Add_Sub_op
//                }
//                return IntNode(seekToken);
//            }
//            case "IDENT" -> {
//                return new IdNode(seekToken);
//            }
//            case "STRING" -> {
//                return new StringNode(seekToken);
//            }
//        }
//        return null;
//    }
    //
    //
    //
    //
    //
    //
    //
}
