package Nodes;

import Token.Token;

public class IfNode extends Node {

    private CompNode mComp;
    private StatementsNode mTrue;
    private StatementsNode mFalse;

    public IfNode(Token token, CompNode comp, StatementsNode aTrue, StatementsNode aFalse) {
        super("if", token);
        mComp = comp;
        mTrue = aTrue;
        mFalse = aFalse;
    }

    public CompNode getComp() {
        return mComp;
    }

    public void setComp(CompNode comp) {
        mComp = comp;
    }

    public StatementsNode getTrue() {
        return mTrue;
    }

    public void setTrue(StatementsNode aTrue) {
        mTrue = aTrue;
    }

    public StatementsNode getFalse() {
        return mFalse;
    }

    public void setFalse(StatementsNode aFalse) {
        mFalse = aFalse;
    }

    public void print(int level) {
        System.out.println("If");
        System.out.format("%" + ( level + 1 ) * 2 + "s", "└─");
        mComp.print(level + 2);
        System.out.format("%" + ( level + 2 ) * 2 + "s", "└─True\n");
        System.out.format("%" + ( level + 1 ) * 2 + "s", "└─");
        mTrue.print(level + 2);
        if (mFalse != null) {
            System.out.format("%" + ( level + 2 ) * 2 + "s", "└─False\n");
            System.out.format("%" + ( level + 1 ) * 2 + "s", "└─");
            mFalse.print(level + 2);
        }
    }

}
