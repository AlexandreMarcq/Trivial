package Nodes;

import Token.Token;

public class WhileNode extends Node {

    private CompNode mComp;
    private StatementsNode mBranch;

    public WhileNode(Token token, CompNode comp, StatementsNode branch) {
        super("while", token);
        mComp = comp;
        mBranch = branch;
    }

    public CompNode getComp() {
        return mComp;
    }

    public void setComp(CompNode comp) {
        mComp = comp;
    }

    public StatementsNode getBranch() {
        return mBranch;
    }

    public void setBranch(StatementsNode branch) {
        mBranch = branch;
    }

    public void print(int level) {
        System.out.println("While");
        System.out.format("%" + level * 2 + "s", "└─");
        mComp.print(level + 1);
        System.out.format("%" + level * 2 + "s", "└─");
        mBranch.print(level + 1);
    }
}
