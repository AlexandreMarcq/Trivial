package Generators;

import Nodes.*;
import Parsers.SemanticParser;
import SymbolTable.SymbolTable;

import java.util.List;

public class TACGenerator {

    private final SemanticParser mSemanticParser = new SemanticParser();
    private final List<Node> mNodeList = mSemanticParser.parse();
    private final SymbolTable mSymbolTable = mSemanticParser.getSymbolTable();
    private final StringBuilder mStringBuilder = new StringBuilder();

    public SymbolTable getSymbolTable() {
        return mSymbolTable;
    }

    public List<Node> generate() {
        mStringBuilder.append("BEGIN\n");
        for (Node n: mNodeList) {
            genTAC(n);
        }
        mStringBuilder.append("END\n");
        System.out.println(mStringBuilder);
        return mNodeList;
    }

    private Temporary genTAC(Node node) {
        Temporary t = null;
        switch (node.getNodeType()) {
            case ("assignation"):
                AssignNode assignNode = (AssignNode) node;
                genTAC(assignNode);
                break;
            case ("comparison"):
                CompNode compNode = (CompNode) node;
                t = genTAC(compNode);
                break;
            case ("constant"):
                ConstNode constNode = (ConstNode) node;
                t = genTAC(constNode);
                break;
            case ("id"):
                IdNode idNode = (IdNode) node;
                t = genTAC(idNode);
                break;
            case ("if"):
                IfNode ifNode = (IfNode) node;
                genTAC(ifNode);
                break;
            case ("operation"):
                OpeNode opeNode = (OpeNode) node;
                t = genTAC(opeNode);
                break;
            case ("statements"):
                StatementsNode statementsNode = (StatementsNode) node;
                genTAC(statementsNode);
                break;
            case ("while"):
                WhileNode whileNode = (WhileNode) node;
                genTAC(whileNode);
                break;
            default:
                break;
        }
        return t;
    }

    private void genTAC(AssignNode node) {
        String left = node.getId().getToken().getToken();
        Temporary t = genTAC(node.getValue());
        mStringBuilder.append('\t')
                .append(left)
                .append(" := ")
                .append(t)
                .append('\n');
    }

    private Temporary genTAC(CompNode node) {
        Temporary t1 = genTAC(node.getId());
        Temporary t2 = genTAC(node.getValue());
        Temporary t = new Temporary();
        mStringBuilder.append('\t')
                .append(t)
                .append(" := ")
                .append(t1)
                .append(" ")
                .append(node.getToken().getToken())
                .append(" ")
                .append(t2)
                .append('\n');
        return t;
    }

    private Temporary genTAC(ConstNode node) {
        Temporary t = new Temporary();
        mStringBuilder.append('\t')
                .append(t)
                .append(" := ")
                .append(node.getToken())
                .append('\n');
        return t;
    }

    private Temporary genTAC(IdNode node) {
        /*mSymbolTable.put(node.getToken().getToken(), "t" + registerCounter++);*/
        Temporary t = new Temporary();
        mStringBuilder.append('\t')
                .append(t)
                .append(" := ")
                .append(node.getToken().getToken())
                .append('\n');
        return t;
    }

    private void genTAC(IfNode node) {
        Label lAfter = new Label();
        Label lElse = null;
        Temporary t = genTAC(node.getComp());
        mStringBuilder.append("\tIfFalse ")
                .append(t)
                .append(" Goto ");
        if (node.getFalse() != null) {
            lElse = new Label();
            mStringBuilder.append(lElse);
        } else {
            mStringBuilder.append(lAfter);
        }
        mStringBuilder.append('\n');
        genTAC(node.getTrue());
        if (node.getFalse() != null) {
            mStringBuilder.append("\tGoto ")
                    .append(lAfter)
                    .append('\n');
            mStringBuilder.append(lElse)
                    .append(":\n");
            genTAC(node.getFalse());
        }
        mStringBuilder
                .append(lAfter)
                .append(": \n");
    }

    private Temporary genTAC(OpeNode node) {
        Temporary t1 = genTAC(node.getLeft());
        Temporary t2 = genTAC(node.getRight());
        Temporary t = new Temporary();
        mStringBuilder.append("\t")
                .append(t)
                .append(" := ")
                .append(t1)
                .append(" ")
                .append(node.getToken().getToken())
                .append(" ")
                .append(t2)
                .append('\n');
        return t;
    }

    private void genTAC(StatementsNode node) {
        for (Node n: node.getStatements()) {
            genTAC(n);
        }
    }

    private void genTAC(WhileNode node) {
        Label lBefore = new Label();
        Label lAfter = new Label();
        mStringBuilder.append(lBefore)
                .append(": \n");
        Temporary t = genTAC(node.getComp());
        mStringBuilder.append("\tIfFalse ")
                .append(t)
                .append(" Goto ")
                .append(lAfter)
                .append('\n');
        genTAC(node.getBranch());
        mStringBuilder.append("\tGoto ")
                .append(lBefore)
                .append('\n')
                .append(lAfter)
                .append(": \n");
    }
}
