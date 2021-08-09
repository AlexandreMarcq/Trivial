package Nodes;

import Token.Token;

import java.util.ArrayList;
import java.util.List;

public class StatementsNode extends Node {
    private final List<Node> mStatements = new ArrayList<>();

    public StatementsNode() {
        super("statements", new Token("", null, 0));
    }

    public List<Node> getStatements() {
        return mStatements;
    }

    public void addStatement(Node n) {
        mStatements.add(n);
    }

    public void print(int level) {
        System.out.println("Statements");
        for (Node node: mStatements) {
            System.out.format("%" + level * 2 + "s", "└─");
            node.print(level);
        }
    }
}
