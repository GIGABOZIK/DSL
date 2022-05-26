package DSL_Wolf.master;

import java.util.HashMap;

public class Interpreter {
    HashMap<String, String> scope = new HashMap<>();

    public String run(Node node) {
        if (node.getClass() == UnOpNode.class) {
            if (((UnOpNode) node).operator.type.typeName.equals("PRINT")) {
                System.out.println(this.run(((UnOpNode) node).value));
            }
        }
        if (node.getClass() == BinOpNode.class) {
            if (((BinOpNode) node).operator.type.typeName.equals("ASSIGN")) {
                String res = this.run(((BinOpNode) node).rightVal);
                VarNode varNode = (VarNode) (((BinOpNode) node).leftVal);
                this.scope.put(varNode.var.value, res);
                return res; // хз
            }
            else {
                int left=Integer.parseInt(this.run(((BinOpNode) node).leftVal));
                int right=Integer.parseInt(this.run(((BinOpNode) node).rightVal));
                switch (((BinOpNode) node).operator.type.typeName){
                    case "PLUS":
                        return String.valueOf(left+right);
                    case "MINUS":
                        return String.valueOf(left-right);
                    case "MULT":
                        return String.valueOf(left*right);
                    case "DIV":
                        return String.valueOf(left/right);
                }
            }
        }
        if (node.getClass() == VarNode.class) {
            return scope.get(((VarNode) node).var.value);
        }
        if (node.getClass() == NumberNode.class) {
            return ((NumberNode) node).number.value;
        }
        if (node.getClass() == WhileNode.class) {
            int left=Integer.parseInt(this.run(((WhileNode) node).leftVal));
            int right=Integer.parseInt(this.run(((WhileNode) node).rightVal));
            switch (((WhileNode) node).operator.type.typeName) {
                case "LESS":
                    while (left < right) {
                        for (int i = 0; i < ((WhileNode) node).operations.size(); i++)
                            this.run(((WhileNode) node).operations.get(i));
                        left = Integer.parseInt(this.run(((WhileNode) node).leftVal));
                        right = Integer.parseInt(this.run(((WhileNode) node).rightVal));
                    }
                    break;
                case "MORE":
                    while (left > right) {
                        for (int i = 0; i < ((WhileNode) node).operations.size(); i++)
                            this.run(((WhileNode) node).operations.get(i));
                        left = Integer.parseInt(this.run(((WhileNode) node).leftVal));
                        right = Integer.parseInt(this.run(((WhileNode) node).rightVal));
                    }
                    break;
                case "EQUAL":
                    while (left == right) {
                        for (int i = 0; i < ((WhileNode) node).operations.size(); i++)
                            this.run(((WhileNode) node).operations.get(i));
                        left = Integer.parseInt(this.run(((WhileNode) node).leftVal));
                        right = Integer.parseInt(this.run(((WhileNode) node).rightVal));
                    }
                    break;
            }
        }
        if (node.getClass() == ForNode.class) {
            int left=Integer.parseInt(this.run(((ForNode) node).leftVal));
            int right=Integer.parseInt(this.run(((ForNode) node).rightVal));
            switch (((ForNode) node).operator.type.typeName) {
                case "LESS":
                    while (left < right) {
                        for (int i = 0; i < ((ForNode) node).operations.size(); i++)
                            this.run(((ForNode) node).operations.get(i));
                        this.run(((ForNode) node).action);
                        left = Integer.parseInt(this.run(((ForNode) node).leftVal));
                        right = Integer.parseInt(this.run(((ForNode) node).rightVal));
                    }
                    break;
                case "MORE":
                    while (left > right) {
                        for (int i = 0; i < ((ForNode) node).operations.size(); i++)
                            this.run(((ForNode) node).operations.get(i));
                        this.run(((ForNode) node).action);
                        left = Integer.parseInt(this.run(((ForNode) node).leftVal));
                        right = Integer.parseInt(this.run(((ForNode) node).rightVal));
                    }
                    break;
                case "EQUAL":
                    while (left == right) {
                        for (int i = 0; i < ((ForNode) node).operations.size(); i++)
                            this.run(((ForNode) node).operations.get(i));
                        this.run(((ForNode) node).action);
                        left = Integer.parseInt(this.run(((ForNode) node).leftVal));
                        right = Integer.parseInt(this.run(((ForNode) node).rightVal));
                    }
                    break;
            }
        }
        return "";
    }
}
