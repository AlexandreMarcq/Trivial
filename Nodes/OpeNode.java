package Trivial.Compiler.Nodes;

import Trivial.Compiler.Token.Token;

public class OpeNode extends Node {

    private Node mLeft, mRight;
    private final String mOpe;

    public OpeNode(Token token, Node left, Node right) {
        super("operation", token);
        mLeft = left;
        mRight = right;
        mOpe = token.getToken();
    }

    public Node getLeft() {
        return mLeft;
    }

    public void setLeft(Node left) {
        mLeft = left;
    }

    public Node getRight() {
        return mRight;
    }

    public void setRight(Node right) {
        mRight = right;
    }

    public void print(int level) {
        System.out.println("Operation(" + mOpe + ")");
        System.out.format("%" + level * 4 + "s", "└─");
        mLeft.print(level + 1);
        System.out.format("%" + level * 4 + "s", "└─");
        mRight.print(level + 1);
    }
}
