package Nodes;

import Token.Token;

public class IdNode extends Node {

    public IdNode(Token token) {
        super("id", token);
    }

    public void print(int level) {
        System.out.println("Id(" + super.getToken().getToken() + ")");
    }
}
