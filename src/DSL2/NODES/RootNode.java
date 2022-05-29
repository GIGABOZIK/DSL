package DSL2.NODES;

import java.util.ArrayList;

public class RootNode extends Node {
    ArrayList<Node> codeChain = new ArrayList<>();
    //
    public void addNode(Node node) { codeChain.add(node); }
    public ArrayList<Node> getCodeChain() { return codeChain; }
}
