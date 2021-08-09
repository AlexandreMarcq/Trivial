package Parsers;

import ErrorHandler.SemanticError;
import Nodes.*;
import SymbolTable.SymbolTable;

import java.util.List;

public class SemanticParser {

    private final SyntaxParser mSyntaxParser = new SyntaxParser();
    private final List<Node> mNodeList = mSyntaxParser.parse();
    private final SymbolTable mSymbolTable = mSyntaxParser.getSymbolTable();

    public SymbolTable getSymbolTable() {
        return mSymbolTable;
    }

    public List<Node> parse() {
        for (Node node: mNodeList) {
            checkSem(node);
        }
        return mNodeList;
    }

    private String checkSem(Node node) {
        String s = "";
        switch (node.getNodeType()) {
            case ("assignation"):
                AssignNode assignNode = (AssignNode) node;
                s = checkSem(assignNode);
                break;
            case ("comparison"):
                CompNode compNode = (CompNode) node;
                checkSem(compNode);
                break;
            case ("constant"):
                ConstNode constNode = (ConstNode) node;
                s = checkSem(constNode);
                break;
            case ("declaration"):
                DeclarNode declarNode = (DeclarNode) node;
                checkSem(declarNode);
                break;
            case ("id"):
                IdNode idNode = (IdNode) node;
                s = checkSem(idNode);
                break;
            case ("if"):
                IfNode ifNode = (IfNode) node;
                checkSem(ifNode);
                break;
            case ("operation"):
                OpeNode opeNode = (OpeNode) node;
                s = checkSem(opeNode);
                break;
            case ("statements"):
                StatementsNode statementsNode = (StatementsNode) node;
                checkSem(statementsNode);
                break;
            case ("type"):
                TypeNode typeNode = (TypeNode) node;
                s = checkSem(typeNode);
                break;
            case ("while"):
                WhileNode whileNode = (WhileNode) node;
                checkSem(whileNode);
                break;
        }
        return s;
    }

    private String checkSem(AssignNode node) {
        String left = checkSem(node.getId());
        String right = checkSem(node.getValue());
        if (!left.equals(right)) {
            new SemanticError(0, "assignation", node.getToken().getLine());
        }
        return left;
    }

    private void checkSem(CompNode node) {
        String left = checkSem(node.getId());
        String right = checkSem(node.getValue());
        if (!left.equals(right)) {
            new SemanticError(0, "comparison", node.getToken().getLine());
        }
    }

    private String checkSem(ConstNode node) {
        return type(node.getToken().getToken());
    }

    private void checkSem(DeclarNode node) {
        String left = checkSem(node.getType());
        for (Node n: node.getIdList()) {
            String right = checkSem(n);
            if (!left.equals(right)) {
                new SemanticError(0, "declaration", node.getToken().getLine());
            }
        }
    }

    private String checkSem(IdNode node) {
        String symbol = node.getToken().getToken();
        if (!isDeclared(symbol)) {
            new SemanticError(1, "variable " + symbol + " not declared", node.getToken().getLine());
        }
        return mSymbolTable.get(symbol);
    }

    private void checkSem(IfNode node) {
        checkSem(node.getComp());
        checkSem(node.getTrue());
        if (node.getFalse() != null) {
            checkSem(node.getFalse());
        }
    }

    private String checkSem(OpeNode node) {
        String left = checkSem(node.getLeft());
        String right = checkSem(node.getRight());
        if (!left.equals(right)) {
            new SemanticError(0, "operation", node.getToken().getLine());
        }
        return left;
    }

    private void checkSem(StatementsNode node) {
        for (Node n: node.getStatements()) {
            checkSem(n);
        }
    }

    private String checkSem(TypeNode node) {
        return node.getToken().getToken();
    }

    private void checkSem(WhileNode node) {
        checkSem(node.getComp());
        checkSem(node.getBranch());
    }

    private boolean isDeclared(String var) {
        return mSymbolTable.containsKey(var);
    }

    private String type(String value) {
        return value.contains(".") ? "decimal" : "entier";
    }
}
