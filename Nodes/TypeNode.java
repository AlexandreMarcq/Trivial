package Trivial.Compiler.Nodes;

import Trivial.Compiler.Token.Token;

public class TypeNode extends Node{

    public TypeNode(Token token) {
        super("type", token);
    }

    public void print(int level) {
        System.out.println("Type(" + super.getToken().getToken() + ")");
    }
}
