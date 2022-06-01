package DSL;

import DSL.NODES.*;
import DSL.TOKENS.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Parser {
    ArrayList<Token> tokenList;
    int position = 0; // Позиция в списке токенов

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
            // Выброс ошибки => Остановка выполнения
            throw new Error('\n' +
                    "\nCODE:" + tokenList.get(position).line() + ':' +
                    "\n " + Arrays.toString(expected) + "\n expected at position " + position + ' ' +
                    "(after " + tokenList.get(position - 1).insteadWas() + ")" +
                    ",\n instead was [" + tokenList.get(position).insteadWas() + "]!\n");
            // Или Исключение (НЕТ)
//            throw new ParseException('\n' +
//                    "\nCODE:" + tokenList.get(position).line() + ':' +
//                    "\n " + Arrays.toString(expected) + "\n expected at position " + position + ' ' +
//                    "(after " + tokenList.get(position - 1).insteadWas() + ")" +
//                    ",\n instead was [" + tokenList.get(position).insteadWas() + "]!\n"
//            );
            //
        }
        return seekToken;
    }
    // Основной метод
    public RootNode parseLang() {
        // Основной метод обработки грамматики
        // lang -> expr+
        RootNode rootNode = new RootNode();
        while (position < tokenList.size()) {
            Node codeChainNode = parseExpr();
//            System.out.println(codeChainNode);
            rootNode.addNode(codeChainNode);
        }
        // Если ошибок не было выявлено
        return rootNode;
    }
    //
    private Node parseExpr() {
        // expr -> assign_expr | loop_for | loop_while | stmt_if | io_console
        Token expectToken = expect(new String[]{"IDENT", "KW_WRITE", "KW_FOR", "KW_WHILE", "KW_IF"});//, "OL_COMMENT"});
        switch (expectToken.type()) {
            case "IDENT" -> { // assign_expr -> init_expr ';3'
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
            case "KW_WRITE" -> { // io_console -> KW_WRITE value ';3'
                Node valueToPrint = parseValue();
                expect(new String[]{"SEP_END_LINE"});
                return new UnOpNode(expectToken, valueToPrint);
            } // + KW_READ ???
//            default -> { // case "OL_COMMENT" -> {
//                return parseExpr(); // сделано для пропуска OL_COMMENT
//                // Еще сделано в seekToken(), поэтому тут не нужно
//            }
        }
        return null; // недостижимое
    }
    //
    private Node parseInit() {
        // init_expr -> IDENT ASSIGN_OP value
        IdNode idNode = new IdNode(expect(new String[]{"IDENT"}));
        Token assign = expect(new String[]{"ASSIGN_OP"});
        Node asValue = parseValue();
        return new BinOpNode(assign, idNode, asValue);
    }
    private Node parseValue(String select) {
        String[] operations = new String[] {"Compare", "AddSub", "MulDiv", "Brackets", "UnVal", "Condition"};
        return parseValue(operations, Arrays.asList(operations).indexOf(select));
    }
    private Node parseValue() {
        // value -> Compare | Condition | AddSub | MulDiv | Brackets | UnValue
        //
        // Организовано в порядке приоритета операций:
        // * Возвращает операцию сравнения,
        // * * либо операцию слож-выч,
        // * * * либо операцию умн-дел,
        // * * * * либо узел в скобках,
        // * * * * * либо одиночное значение
        return parseValue(new String[]{"Compare", "AddSub", "MulDiv", "Brackets", "UnVal", "Condition"}, 0);
    }
    HashMap<String, String[]> operators = new HashMap<>();
    {
        operators.put("Compare", new String[]{"COMP_LESS", "COMP_L_EQ", "COMP_MORE", "COMP_M_EQ", "COMP_EQ", "COMP_NEQ"});
//        operators.put("Condition", new String[]{"COMP_LESS", "COMP_L_EQ", "COMP_MORE", "COMP_M_EQ", "COMP_EQ", "COMP_NEQ"});
        operators.put("Condition", operators.get("Compare"));
        operators.put("AddSub", new String[]{"ADD_OP", "SUB_OP"});
        operators.put("MulDiv", new String[]{"MUL_OP", "DIV_OP"});
//        operators.put("Brackets", new String[]{"SEP_L_BRACKET", "SEP_R_BRACKET"});
        operators.put("UnVal", new String[]{"INT", "IDENT", "STRING"});
    }
    private Node parseValue(String[] expected, int select) {
        String op = expected[select];
        switch (op) {
            case "AddSub", "MulDiv" -> { // Compare ?? a > b > c > d...
                Node leftOperand = parseValue(expected, select + 1);
                Token operator = seekToken(operators.get(op));
                while (operator != null) {
                    Node rightOperand = parseValue(expected, select + 1);
                    leftOperand = new BinOpNode(operator, leftOperand, rightOperand);
                    operator = seekToken(operators.get(op));
                }
                return leftOperand;
            }
            case "Compare" -> { // Compare ?? a > b > c > d...
                Node leftOperand = parseValue(expected, select + 1);
                Token operator = seekToken(operators.get(op));
                if (operator != null) {
                    Node rightOperand = parseValue(expected, select + 1);
                    leftOperand = new BinOpNode(operator, leftOperand, rightOperand);
//                    operator = seekToken(operators.get(op));
                }
                return leftOperand;
            }
            case "Brackets" -> {
                if (seekToken(new String[]{"SEP_L_BRACKET"}) != null) {
                    Node inner = parseValue();
                    expect(new String[]{"SEP_R_BRACKET"});
                    return inner;
                }
                return parseValue(expected, select + 1);
            }
            case "UnVal" -> {
                Token expectToken = expect(operators.get(op));
                switch (expectToken.type()) {
                    case "INT" -> { return new IntNode(expectToken); }
                    case "IDENT" -> { return new IdNode(expectToken); }
                    case "STRING" -> { return new StringNode(expectToken); }
                }
            }
            case "Condition" -> {
                Node leftOperand = parseValue("AddSub");
                Token operator = expect(operators.get(op));
                Node rightOperand = parseValue("AddSub");
                return new BinOpNode(operator, leftOperand, rightOperand);
            }
            case "CompareOLD" -> {
                Node leftOperand = parseValue(expected, select + 1);
                Token operator = seekToken(operators.get(op));
                if (operator != null) {
                    Node rightOperand = parseValue(expected, select + 1);
                    leftOperand = new BinOpNode(operator, leftOperand, rightOperand);
                }
                return leftOperand;
            }
        }
        return null;
    }
//    private Node parseValueOLD() {
        // Организовано в порядке приоритета операций:
        // * Возвращает операцию сравнения,
        // * * либо операцию слож-выч,
        // * * * либо операцию умн-дел,
        // * * * * либо узел в скобках,
        // * * * * * либо одиночное значение
//        Node leftOperand = parseOpAddSub();
//        Token operator = seekToken(new String[]{"COMP_LESS", "COMP_L_EQ", "COMP_MORE", "COMP_M_EQ", "COMP_EQ", "COMP_NEQ"});
//        while (operator != null) {
//            Node rightOperand = parseOpAddSub();
//            leftOperand = new BinOpNode(operator, leftOperand, rightOperand);
//            operator = seekToken(new String[]{"COMP_LESS", "COMP_L_EQ", "COMP_MORE", "COMP_M_EQ", "COMP_EQ", "COMP_NEQ"});
//        }
//        return leftOperand;
//    }
//    private Node parseOpAddSub() {
        // * Возвращает операцию слож-выч,
        // * * либо операцию умн-дел,
        // * * * либо узел в скобках,
        // * * * * либо одиночное значение
//        Node leftOperand = parseOpMulDiv();
//        Token operator = seekToken(new String[]{"ADD_OP", "SUB_OP"});
//        while (operator != null) {
//            Node rightOperand = parseOpMulDiv();
//            leftOperand = new BinOpNode(operator, leftOperand, rightOperand);
//            operator = seekToken(new String[]{"ADD_OP", "SUB_OP"});
//        }
//        return leftOperand;
//    }
//    private Node parseOpMulDiv() {
        // * Возвращает операцию умн-дел,
        // * * либо узел в скобках,
        // * * * либо одиночное значение
//        Node leftOperand = parseBracketsOp();
//        Token operator = seekToken(new String[]{"MUL_OP", "DIV_OP"});
//        while (operator != null) {
//            Node rightOperand = parseBracketsOp();
//            leftOperand = new BinOpNode(operator, leftOperand, rightOperand);
//            operator = seekToken(new String[]{"MUL_OP", "DIV_OP"});
//        }
//        return leftOperand;
//    }
//    private Node parseBracketsOp() {
        // * Возвращает узел в скобках,
        // * * либо одиночное значение
//        if (seekToken(new String[]{"SEP_L_BRACKET"}) != null) {
//            Node inner = parseValue();
//            expect(new String[]{"SEP_R_BRACKET"});
//            return inner;
//        }
//        return parseUnValue();
//    }
//    private Node parseUnValue() { // ТОЛЬКО INT, IDENT ??
        // * Возвращает одиночное значение
//        Token expectToken = expect(new String[]{"INT", "IDENT", "STRING"});
//        if (expectToken.type().equals("STRING")) System.out.println("String: " + expectToken.value());
//        switch (expectToken.type()) {
//            case "INT" -> { return new IntNode(expectToken); }
//            case "IDENT" -> { return new IdNode(expectToken); }
//            case "STRING" -> { return new StringNode(expectToken); }
//        }
//        return null; // недостижимое
//    }
    //
    private Node parseLoopFor() {
        // stmt_loop_for -> KW_FOR '(' Init ';' Condition ';' expr ')' stmt_body
        expect(new String[]{"SEP_L_BRACKET"});
        Node init = parseInit();
        expect(new String[]{"SEP_SEMICOLON"});
//        Node condition = parseCondition();
        Node condition = parseValue("Condition");
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
        // stmt_loop_while -> KW_WHILE '(' condition ')' stmt_body
        expect(new String[]{"SEP_L_BRACKET"});
//        Node condition = parseCondition();
        Node condition = parseValue("Condition");
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
        // stmt_if -> KW_IF '(' condition ')' stmt_body stmt_else?
        // stmt_else -> KW_ELSE stmt_body
        expect(new String[]{"SEP_L_BRACKET"});
        Node condition = parseValue("Condition");
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
//    private Node parseCondition() {
//        Node leftOperand = parseValue(); // OLD
//        Node leftOperand = parseOpAddSub();
//        Token operator = expect(new String[]{"COMP_LESS", "COMP_L_EQ", "COMP_MORE", "COMP_M_EQ", "COMP_EQ", "COMP_NEQ"});
//        Node rightOperand = parseValue(); // OLD
//        Node rightOperand = parseOpAddSub();
//        return new BinOpNode(operator, leftOperand, rightOperand);
//    }
    //
}
