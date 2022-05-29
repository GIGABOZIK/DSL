package DSL;

import DSL.NODES.*;

import java.util.HashMap;

public class Interpreter {
    RootNode rootNode;
    HashMap<String, String> scope = new HashMap<>();

    public Interpreter() { this.rootNode = new Parser().parseLang(); }
    public Interpreter(RootNode rootNode) { this.rootNode = rootNode; }

    // Основной метод
    public void execute() {
        for (Node node : rootNode.getCodeChain()) {
            executeNode(node);
//            printNode(node); // Прост
        }
    }

    private void printNode(Node node) {
        // Просто вывести все узлы
    }

    private String executeNode(Node node) {
        System.out.println('\n' + node.getClass().getName());
        //
        if (UnOpNode.class.equals(node.getClass())) {
            if (((UnOpNode) node).getOperator().type().equals("KW_WRITE")) {
                System.out.println(executeNode(((UnOpNode) node).getArg()));
            }
        }
        if (BinOpNode.class.equals(node.getClass())) {
            // Commit here (:
        }
        return null;
    }
}
