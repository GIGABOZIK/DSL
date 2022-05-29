package DSL2.NODES;

import DSL2.TOKENS.Token;

import java.util.ArrayList;

public class ForNode extends Node {
    Node init;
    Node condition;
    Node expr;
//    Token operator;
//    Node leftOperand;
//    Node rightOperand;
//    Node step;
    ArrayList<Node> exprs = new ArrayList<>();

//    public ForNode(Node init, Token operator, Node leftOperand, Node rightOperand, Node step) {
    public ForNode(Node init, Node condition, Node expr) {
        this.init = init;
        this.condition = condition;
        this.expr = expr;
    }
    //
    public void addExpr(Node expr) { exprs.add(expr); }
}
