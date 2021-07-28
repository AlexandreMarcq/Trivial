package Trivial.Compiler.Generators;

import Trivial.Compiler.Nodes.*;

import java.util.List;

public class MIPSGenerator {

    private final TACGenerator mTACGenerator = new TACGenerator();
    private final List<Node> mNodeList = mTACGenerator.generate();
    private final StringBuilder mStringBuilder = new StringBuilder().append(".text\n.globl main\n\nmain:\n");
    private final StringBuilder mData = new StringBuilder().append("\n\n.data\n");

    public void generate() {
        for (Node n: mNodeList) {
            new Temporary().resetCounter();
            new Label().resetCounter();
            genMIPS(n);
        }
        mStringBuilder.append("\tjr\t$ra");
        mStringBuilder.append(mData);
        System.out.println(mStringBuilder);
    }

    private Temporary genMIPS(Node node) {
        Temporary t = null;
        switch (node.getNodeType()) {
            case ("assignation"):
                AssignNode assignNode = (AssignNode) node;
                genMIPS(assignNode);
                break;
            case ("comparison"):
                CompNode compNode = (CompNode) node;
                genMIPS(compNode);
                break;
            case ("constant"):
                ConstNode constNode = (ConstNode) node;
                t = genMIPS(constNode);
                break;
            case ("declaration"):
                DeclarNode declarNode = (DeclarNode) node;
                genMIPS(declarNode);
                break;
            case ("id"):
                IdNode idNode = (IdNode) node;
                t = genMIPS(idNode);
                break;
            case ("if"):
                IfNode ifNode = (IfNode) node;
                genMIPS(ifNode);
                break;
            case ("operation"):
                OpeNode opeNode = (OpeNode) node;
                t = genMIPS(opeNode);
                break;
            case ("statements"):
                StatementsNode statementsNode = (StatementsNode) node;
                genMIPS(statementsNode);
                break;
            case ("while"):
                WhileNode whileNode = (WhileNode) node;
                genMIPS(whileNode);
                break;
            default:
                break;
        }
        return t;
    }

    private void genMIPS(AssignNode node) {
        Temporary t = genMIPS(node.getValue());
        mStringBuilder.append("\tsw\t$")
                .append(t)
                .append(", ")
                .append((node.getId().getToken().getToken()).toUpperCase())
                .append('\n');
    }

    private void genMIPS(CompNode node) {
        Temporary t = genMIPS(node.getId());
        Temporary t1 = genMIPS(node.getValue());
        String comp = "";
        switch (node.getToken().getToken()) {
            case ("~"):
                comp = "bne";
                break;
            case ("!"):
                comp = "beq";
                break;
            case ("<"):
                comp = "bgt";
                break;
            case (">"):
                comp = "blt";
                break;
        }
        mStringBuilder.append('\t')
                .append(comp)
                .append("\t$")
                .append(t)
                .append(", $")
                .append(t1)
                .append(", ");
    }

    private Temporary genMIPS(ConstNode node) {
        Temporary t = new Temporary();
        mStringBuilder.append("\tli\t$")
                .append(t)
                .append(", ")
                .append(node.getToken().getToken())
                .append('\n');
        return t;
    }

    private void genMIPS(DeclarNode node) {
        for (Node n: node.getIdList()) {
            if ("id".equals(n.getNodeType())) {
                IdNode id = (IdNode) n;
                mData.append('\t')
                        .append(id.getToken().getToken().toUpperCase())
                        .append(":\n");
            } else {
                AssignNode assi = (AssignNode) n;
                if ("constant".equals(assi.getValue().getNodeType())) {
                    mData.append('\t')
                            .append(assi.getId().getToken().getToken().toUpperCase())
                            .append(": .word ")
                            .append(assi.getValue().getToken())
                            .append('\n');
                }
            }
        }
    }

    private Temporary genMIPS(IdNode node) {
        Temporary t = new Temporary();
        String id = node.getToken().getToken().toUpperCase();
        mStringBuilder.append("\tlw\t$")
                .append(t)
                .append(", ")
                .append(id)
                .append('\n');
        return t;
    }

    private void genMIPS(IfNode node) {
        genMIPS(node.getComp());
        Label lEnd, lElse = null;
        if (node.getFalse() != null) {
            lElse = new Label();
            mStringBuilder.append(lElse);
            lEnd = new Label();
        } else {
            lEnd = new Label();
            mStringBuilder.append(lEnd);
        }
        mStringBuilder.append('\n');
        genMIPS(node.getTrue());
        if (node.getFalse() != null) {
            mStringBuilder.append("\tj\t")
                    .append(lEnd)
                    .append('\n')
                    .append(lElse)
                    .append(": \n");
            genMIPS(node.getFalse());
        }
        mStringBuilder.append(lEnd)
                .append(": \n");
    }

    private Temporary genMIPS(OpeNode node) {
        Temporary t1 = genMIPS(node.getLeft());
        Temporary t2 = genMIPS(node.getRight());
        Temporary t = new Temporary();
        String ope = "";
        switch (node.getToken().getToken()) {
            case ("+"):
                ope = "add";
                break;
            case ("-"):
                ope = "sub";
                break;
            case ("*"):
                ope = "mul";
                break;
            case ("/"):
                ope = "div";
                break;
        }
        mStringBuilder.append('\t')
                .append(ope)
                .append("\t$")
                .append(t)
                .append(", $")
                .append(t1)
                .append(" ,$")
                .append(t2)
                .append('\n');
        return t;
    }

    private void genMIPS(StatementsNode node) {
        for (Node n: node.getStatements()) {
            genMIPS(n);
        }
    }

    private void genMIPS(WhileNode node) {
        Label lBefore = new Label();
        Label lEnd = new Label();
        mStringBuilder.append(lBefore)
                .append(": \n");
        genMIPS(node.getComp());
        mStringBuilder.append(lEnd)
                .append('\n');
        genMIPS(node.getBranch());
        mStringBuilder.append("\tj\t")
                .append(lBefore)
                .append('\n')
                .append(lEnd)
                .append(": \n");
    }
}
