package Notes.DSL_Wolf.master;

public class UnOpNode extends Node {
    Token operator;
    Node value;

    public UnOpNode(Token operator, Node value) {
        this.operator = operator;
        this.value = value;
    }
}
