package Trivial.Compiler.Nodes;

import Trivial.Compiler.Token.Token;

public class Node {

    private final String mNodeType;
    private Token mToken;

    public Node(String type, Token token) {
        mNodeType = type;
        mToken = token;
    }

    public Token getToken() {
        return mToken;
    }

    public void setToken(Token token) {
        mToken = token;
    }

    public String getNodeType() {
        return mNodeType;
    }

    public void print(int level) {}
}
