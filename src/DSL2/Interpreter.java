package DSL2;

import DSL2.NODES.*;

public class Interpreter {
    RootNode rootNode;

    public Interpreter() { this.rootNode = new Parser().parseLang(); }
    public Interpreter(RootNode rootNode) { this.rootNode = rootNode; }

    // Основной метод
    public void execute() {
        for (Node node : rootNode.getCodeChain()) {
            System.out.println(node);
        }
    }
}
