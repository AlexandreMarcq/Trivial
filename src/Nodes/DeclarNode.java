package Nodes;

import Token.Token;

import java.util.ArrayList;
import java.util.List;

public class DeclarNode extends Node {

    private TypeNode mType;
    private List<Node> mIdList = new ArrayList<>();

    public DeclarNode(Token token) {
        super("declaration", token);
    }

    public TypeNode getType() {
        return mType;
    }

    public void setType(TypeNode type) {
        mType = type;
    }

    public List<Node> getIdList() {
        return mIdList;
    }

    public void setIdList(List<Node> idList) {
        mIdList = idList;
    }

    public void addId(Node n) { mIdList.add(0, n); }

    public void print(int level) {
        System.out.println("Declaration");
        System.out.format("%" + ( level + 1 ) * 2 + "s", "└─");
        mType.print(level + 1);
        for (Node node: mIdList) {
            System.out.format("%" + ( level + 1 ) * 2 + "s", "└─");
            node.print(level + 1);
        }
    }
}
