package Nodes;

import Token.Token;

public class AssignNode extends Node{

    private IdNode mId;
    private Node mValue;

    public AssignNode(Token token, IdNode id, Node value) {
        super("assignation", token);
        mId = id;
        mValue = value;
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
        System.out.println("Assignation(=)");
        System.out.format("%" + ( level + 1 ) * 2 + "s", "└─");
        mId.print(level + 1);
        System.out.format("%" + ( level + 1 ) * 2 + "s", "└─");
        mValue.print(level + 1);
    }
}
