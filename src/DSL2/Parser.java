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
    private Token expect(String[] expected) {
        // Функция, которая требует наличие ОБЯЗАТЕЛЬНЫХ токенs
        Token seekToken = seekToken(expected);
        if (seekToken == null) {
//            thrower(expected, position, tokenList.get(position));
//            throw new Error(Arrays.toString(expected) + "\nexpected at position " + position);
            throw new Error('\n' +
                    "\nCODE:" + tokenList.get(position).line() + ':' +
                    "\n " + Arrays.toString(expected) + "\n expected at position " + position + ' ' +
                    "(after " + tokenList.get(position - 1).insteadWas() + ")" +
                    ",\n instead was [" + tokenList.get(position).insteadWas() + "]!\n");
        }
        return seekToken;
    }
    private void maybeEOL(Node codeChainNode) {
        // Функция определения символа конца строки (SEP_END_LINE)
        // он нужен не для всех узлов (типа for, while...)
        String ccncName = codeChainNode.getClass().getName();
        if (!(
                ccncName.equals("ForNode")
                | ccncName.equals("WhileNode")
                | ccncName.equals("IfElseNode")
        )) expect(new String[]{"SEP_END_LINE"});
//        else
            // Но его наличие - не проблема \\\ НЕТ - ПРОБЛЕМА ((: .. Лучше следовать стандарту
//            seekToken(new String[]{"SEP_END_LINE"});
    }
    // Основной метод
    public RootNode parseLang() {
        // Основной метод обработки грамматики
        RootNode rootNode = new RootNode();
        while (position < tokenList.size()) {
            Node codeChainNode = parseExpr();

//            if (codeChainNode == null) return null;
//            maybeEOL(codeChainNode);
//            seekToken(new String[]{"SEP_END_LINE"}); // Определяется индивидуально

//            System.out.println(codeChainNode);

            rootNode.addNode(codeChainNode);
        }
        // Если ошибок не было выявлено
        return rootNode;
    }
    //
    private Node parseExpr() {
        Token expectToken = expect(new String[]{"IDENT", "KW_WRITE", "KW_FOR", "KW_WHILE", "KW_IF", "OL_COMMENT"});
        switch (expectToken.type()) {
            case "IDENT" -> { // assign_expr
                position--;
                Node assign_expr = parseInit();
                expect(new String[]{"SEP_END_LINE"});
                return assign_expr;
            }
            case "KW_FOR" -> { // loop_for
                return parseLoopFor();
            }
            case "KW_WHILE" -> { // loop_while
                return parseLoopWhile();
            }
            case "KW_IF" -> { // stmt_if
                return parseStmtIf();
            }
            case "KW_WRITE" -> { // io_console
//                expect(new String[]{"SEP_L_BRACKET"});
                Token string = seekToken(new String[]{"STRING"}); // Строки обрабатывать отдельно
                Node valueToPrint = (string != null) ? new StringNode(expectToken) : parseValue();

//                expect(new String[]{"SEP_R_BRACKET"});
                expect(new String[]{"SEP_END_LINE"});
                return new UnOpNode(expectToken, valueToPrint);
            } // + KW_READ ???
            case "OL_COMMENT" -> {
                return parseExpr(); // сделано для пропуска OL_COMMENT и сделано в seekToken() // null
            }
        }
        return null; // недостижимое
    }
    //
    private Node parseInit() {
        IdNode idNode = new IdNode(expect(new String[]{"IDENT"}));
        Token assign = expect(new String[]{"ASSIGN_OP"});
        Node asValue = parseValue();
        return new BinOpNode(assign, idNode, asValue);
    }
    private Node parseValue() {
        // Организовано в порядке приоритета операций:
        // * Возвращает операцию слож-выч,
        // * * либо операцию умн-дел,
        // * * * либо узел в скобках,
        // * * * * либо одиночное значение
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
        // * Возвращает операцию умн-дел,
        // * * либо узел в скобках,
        // * * * либо одиночное значение
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
        // * Возвращает узел в скобках,
        // * * либо одиночное значение
        if (seekToken(new String[]{"SEP_L_BRACKET"}) != null) {
            Node inner = parseValue();
            expect(new String[]{"SEP_R_BRACKET"});
            return inner;
        }
        return parseUnValue();
    }
    private Node parseUnValue() { // ТОЛЬКО INT, IDENT
        // * Возвращает одиночное значение
        Token expectToken = expect(new String[]{"INT", "IDENT"});
        switch (expectToken.type()) {
            case "INT" -> { return new IntNode(expectToken); }
            case "IDENT" -> { return new IdNode(expectToken); }
        }
        return null; // недостижимое
    }
    //
    private Node parseLoopFor() {
        expect(new String[]{"SEP_L_BRACKET"});
        Node init = parseInit();
        expect(new String[]{"SEP_SEMICOLON"});
        Node condition = parseCondition();
        expect(new String[]{"SEP_SEMICOLON"});
        Node expr = parseExpr(); // Прикольно будет (: куски кода \\\ OL_COMMENT переделать
//        Node expr = parseInit();
        expect(new String[]{"SEP_R_BRACKET"});
        ForNode forNode = new ForNode(init, condition, expr);
        //
        expect(new String[]{"SEP_L_BRACE"});
        while (seekToken(new String[]{"SEP_R_BRACE"}) == null) {
            Node codeChainNode = parseExpr();
            forNode.addExpr(codeChainNode);
        }
        return forNode;
    }
    private Node parseLoopWhile() {
        expect(new String[]{"SEP_L_BRACKET"});
        Node condition = parseCondition();
        expect(new String[]{"SEP_R_BRACKET"});
        WhileNode whileNode = new WhileNode(condition);
        //
        expect(new String[]{"SEP_L_BRACE"});
        while (seekToken(new String[]{"SEP_R_BRACE"}) == null) {
            Node codeChainNode = parseExpr();
            whileNode.addExpr(codeChainNode);
        }
        return whileNode;
    }
    private Node parseStmtIf() {
        expect(new String[]{"SEP_L_BRACKET"});
        Node condition = parseCondition();
        expect(new String[]{"SEP_R_BRACKET"});
        IfElseNode ifElseNode = new IfElseNode(condition);
        //
        expect(new String[]{"SEP_L_BRACE"});
        while (seekToken(new String[]{"SEP_R_BRACE"}) == null) {
            Node codeChainNode = parseExpr();
            ifElseNode.addIfExpr(codeChainNode);
        }
        Token tElse = seekToken(new String[]{"KW_ELSE"});
        if (tElse != null) {
            expect(new String[]{"SEP_L_BRACE"});
            while (seekToken(new String[]{"SEP_R_BRACE"}) == null) {
                Node codeChainNode = parseExpr();
                ifElseNode.addElseExpr(codeChainNode);
            }
        }
        return ifElseNode;
    }
    private Node parseCondition() {
        Node leftOperand = parseValue();
        Token operator = expect(new String[]{"COMP_LESS", "COMP_L_EQ", "COMP_MORE", "COMP_M_EQ", "COMP_EQ", "COMP_NEQ"});
        Node rightOperand = parseValue();
        return new BinOpNode(operator, leftOperand, rightOperand);
    }
    //
    //
    // OLD
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
