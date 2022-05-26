package DSL_Wolf.master;

import java.util.ArrayList;

public class Parser {
    ArrayList<Token> tokens;
    int pos=0;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }
    public Token receive(String[] need) {
        Token curToken;
        if (pos<tokens.size()) {
            curToken = tokens.get(pos);
            for (String tokenTypeName : need)
                if (tokenTypeName.equals(curToken.type.typeName)) {
                    pos++;
                    return curToken;
                }
        }
        return null;
    }
    public void need(String[] expected) {
        Token token = receive(expected);
        if (token == null) {
            throw new Error("\nНа позииции ("+pos+") ожидается "+expected[0]);
        }
    }
    public Node parseVarNum() { // ->Value
        if (tokens.get(pos).type.typeName.equals("NUM")) {
            pos++;
            return new NumberNode(tokens.get(pos-1));
        }
        if (tokens.get(pos).type.typeName.equals("VAR")) {
            pos++;
            return new VarNode(tokens.get(pos-1));
        }
         
         throw new Error("\nОжидается переменная или число на позиции ("+pos+")\n");
    }
    public Node parsePar(){
        if (tokens.get(pos).type.typeName.equals("LPAR")){
            pos++;
            Node node = parseFormula();
            need(new String[]{"RPAR"});
            return node;
        }
        else
            return parseVarNum();
    }
    public Node parseMultDiv(){
        Node leftVal = parsePar();
        Token operator = receive(new String[]{"MULT", "DIV"});
        // Парсер проходиться по "дереву"
        while (operator != null){
            Node rightVal = parsePar();
            leftVal = new BinOpNode(operator, leftVal, rightVal);
            operator = receive(new String[]{"MULT", "DIV"});
        }
        return leftVal;
    }
    public Node parseFormula() {
        Node leftVal = parseMultDiv();
        Token operator = receive(new String[]{"PLUS","MINUS"});
        while (operator != null) {
            Node rightVal = parseMultDiv();
            leftVal = new BinOpNode(operator, leftVal, rightVal);
            operator = receive(new String[]{"PLUS","MINUS"});
        }
        return leftVal;
    }
    public Node parseString() {
        switch (tokens.get(pos).type.typeName) {
            case "VAR" -> {
                Node varNode = parseVarNum(); // ->Value
                Token assign = receive(new String[]{"ASSIGN"});
                if (assign != null) {
                    Node rightVal = parseFormula(); // ->Value
                    return new BinOpNode(assign, varNode, rightVal);
                }
                throw new Error("\nПосле переменной ожидается = на позиции (" + pos + ")\n");
            }
            case "PRINT" -> {
                pos++;
                return new UnOpNode(tokens.get(pos - 1), this.parseFormula());
            }
            case "WHILE" -> {
                pos++;
                return parseWhile();
            }
            case "FOR" -> {
                pos++;
                return parseFor();
            }
        }
       throw new Error("\nОшибка на позиции ("+pos+"). Ожидалось действие или переменная");
   }
    public Node parseFor(){
       Node leftVal=parseFormula();
       Token operator=receive(new String[]{"LESS","MORE","EQUAL"});
       Node rightVal=parseFormula();

       need(new String[]{"END"});

       Node varNode = parseVarNum();
       Token assign = receive(new String[]{"ASSIGN"});
       Node rightActVal = parseFormula();
       BinOpNode action = new BinOpNode(assign, varNode, rightActVal);
       if (assign == null)
           throw new Error("\nПосле переменной ожидается = на позиции ("+pos+")\n");
       ForNode forNode = new ForNode(operator, leftVal, rightVal, action);
       need(new String[]{"LRectPar"});
       while(!tokens.get(pos).type.typeName.equals("RRectPAR")) {
           forNode.addOperations(getOperations());
           if (pos==tokens.size())
               throw new Error("\nОшибка, ожидалось }");
       }
       pos++;
       return forNode;
   }
    public Node parseWhile() {
        Node leftVal=parseFormula();
        Token operator=receive(new String[]{"LESS", "MORE", "EQUAL"});
        Node rightVal=parseFormula();
        WhileNode whileNode=new WhileNode(operator, leftVal, rightVal);
        need(new String[]{"LRectPar"}); // BRACE
        while(!tokens.get(pos).type.typeName.equals("RRectPAR")) {
            whileNode.addOperations(getOperations());
            if (pos==tokens.size())
                throw new Error("\nОшибка, ожидалось }");
        }
        pos++;
        return whileNode;
    }
    public Node getOperations() {
        Node codeStringNode=parseString();
        need(new String[]{"END"});
        return codeStringNode;
    }
    public RootNode parseTokens() {
        RootNode root=new RootNode();
        while (pos<tokens.size()) {
            Node codeStringNode = parseString();
            need(new String[]{"END"});
            root.addNode(codeStringNode);
        }
        return root;
   }
}
