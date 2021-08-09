package Nodes;

import Token.Token;

public class ConstNode extends Node {

    public ConstNode(Token token) {
        super("constant", token);
    }

    public void print(int level) {
        System.out.println("Constant(" + super.getToken().getToken() + ")");
    }
}
