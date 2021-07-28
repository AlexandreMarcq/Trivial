package Trivial.Compiler.Nodes;

import Trivial.Compiler.Token.Token;

public class CompNode extends Node{

    private IdNode mId;
    private Node mValue;
    private final String mOpe;

    public CompNode(Token token, IdNode id, Node value) {
        super("comparison", token);
        mId = id;
        mValue = value;
        mOpe = token.getToken();
    }

    public IdNode getId() {
        return mId;
    }

    public void setId(IdNode id) {
        mId = id;
    }

    public Node getValue() {
        return mValue;
    }

    public void setValue(Node value) {
        mValue = value;
    }

    public void print(int level) {
        System.out.println("Comparison(" + mOpe + ")");
        System.out.format("%" + level * 2 + "s", "└─");
        mId.print(level + 1);
        System.out.format("%" + level * 2 + "s", "└─");
        mValue.print(level + 1);
    }
}
