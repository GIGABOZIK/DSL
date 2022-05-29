package DSL2.NODES;

import java.util.ArrayList;

public class WhileNode extends Node {
    Node condition;
    ArrayList<Node> exprs = new ArrayList<>();

    public WhileNode(Node condition) {
        this.condition = condition;
    }

    public void addExpr(Node expr) { exprs.add(expr); }

    public Node getCondition() {
        return condition;
    }

    public ArrayList<Node> getExprs() {
        return exprs;
    }
}
