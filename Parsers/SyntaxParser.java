package Trivial.Compiler.Parsers;

import Trivial.Compiler.ErrorHandler.SyntaxError;
import Trivial.Compiler.Nodes.*;
import Trivial.Compiler.Scanner.Scanner;
import Trivial.Compiler.SymbolTable.SymbolTable;
import Trivial.Compiler.Token.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SyntaxParser {

    private final Scanner mScanner = new Scanner("src\\Trivial\\Compiler\\code.fr");
    private final List<List<Token>> mLineList = mScanner.scan();
    private final SymbolTable mSymbolTable = mScanner.getSymTab();
    private int mCounter;
    private final Stack<Node> mNodeStack = new Stack<>();
    private final Stack<Node> mWorkingStack = new Stack<>();
    private final List<Node> mNodeList = new ArrayList<>();

    public List<Node> parse() {
        //For each line
        for (List<Token> line: mLineList) {
            mCounter = 0;
            s0(line);
            mNodeList.add(mNodeStack.pop());
        }
        for (Node node: mNodeList) {
            node.print(1);
            System.out.println();
        }
        return mNodeList;
    }

    private int scopeCount = 0;
    private final Stack<Character> loopStack = new Stack<>();
    private boolean sinon = false;
    private boolean declaration = false;

    public SymbolTable getSymbolTable() {
        return mSymbolTable;
    }

    private void s0(List<Token> line) {
        Token token = line.get(mCounter);
        mCounter++;
        switch (token.getCategory()) {
            case ("Type"):
                mNodeStack.push(new TypeNode(token));
                s1(line);
                break;
            case ("Identifier"):
                mNodeStack.push(new IdNode(token));
                s2(line);
                break;
            case ("Keyword"):
                switch (token.getToken()) {
                    case ("tantque"):
                        mNodeStack.push(new WhileNode(token, null, null));
                        scopeCount++;
                        loopStack.push('w');
                        s6(line);
                        break;
                    case ("si"):
                        mNodeStack.push(new IfNode(token, null, null, null));
                        scopeCount++;
                        loopStack.push('i');
                        s13(line);
                        break;
                }
                break;
            case ("Delimiter"):
                if ("EOF".equals(token.getToken())) {
                    break;
                } else if ("}".equals(token.getToken())) {
                    if (scopeCount >= 0) {
                        scopeCount--;
                        while (!"{".equals(mNodeStack.peek().getToken().getToken())) {
                            mWorkingStack.push(mNodeStack.pop());
                        }
                        mNodeStack.pop();
                        mNodeStack.push(buildStmtsTree(mWorkingStack));
                        if (loopStack.peek().equals('w')) {
                            loopStack.pop();
                            StatementsNode stmts = (StatementsNode) mNodeStack.pop();
                            CompNode comp = (CompNode) mNodeStack.pop();
                            WhileNode tantque = (WhileNode) mNodeStack.pop();
                            tantque.setBranch(stmts);
                            tantque.setComp(comp);
                            mNodeStack.push(tantque);
                        } else if (loopStack.peek().equals('i')) {
                            if (!sinon) {
                                s20(line);
                                if (sinon) {
                                    break;
                                }
                            }
                            loopStack.pop();
                            StatementsNode fStmts;
                            if (sinon) {
                                fStmts = (StatementsNode) mNodeStack.pop();
                            } else {
                                fStmts = null;
                            }
                            StatementsNode tStms = (StatementsNode) mNodeStack.pop();
                            CompNode comp = (CompNode) mNodeStack.pop();
                            IfNode si = (IfNode) mNodeStack.pop();
                            si.setTrue(tStms);
                            si.setFalse(fStmts);
                            si.setComp(comp);
                            mNodeStack.push(si);
                        }
                        s0(line);
                        break;
                    }
                }
            default:
                checkEOF(token);
                new SyntaxError("type, identifier, 'tantque' or 'si'", token);
        }
   }

    private void s1(List<Token> line) {
        Token token = line.get(mCounter);
        if ("Identifier".equals(token.getCategory())) {
            mNodeStack.push(new IdNode(token));
            mCounter++;
            s3(line);
        } else {
            checkEOF(token);
            new SyntaxError("identifier", token);
        }
    }

    private void s2(List<Token> line) {
        Token token = line.get(mCounter);
        if ("=".equals(token.getToken())) {
            mNodeStack.push(new AssignNode(token,null, null));
            mCounter++;
            s4(line);
        } else {
            checkEOF(token);
            new SyntaxError("'='", token);
        }
    }

    private void s3(List<Token> line) {
        Token token = line.get(mCounter);
        mCounter++;
        switch (token.getToken()) {
            case (","):
                s1(line);
                break;
            case ("="):
                mNodeStack.push(new AssignNode(token, null, null));
                declaration = true;
                s4(line);
                break;
            case(";"):
                mNodeStack.push(buildDeclTree(mNodeStack));
                if (scopeCount > 0) {
                    s0(line);
                }
                break;
            default:
                checkEOF(token);
                new SyntaxError("',', '=' or ';'", token);
        }
    }

    private void s4(List<Token> line) {
        Token token = line.get(mCounter);
        mCounter++;
        if ("Identifier".equals(token.getCategory())) {
            mNodeStack.push(new IdNode(token));
            s5(line);
        } else if ("Constant".equals(token.getCategory())) {
            mNodeStack.push(new ConstNode(token));
            s5(line);
        } else if ("(".equals(token.getToken())) {
            mNodeStack.push(new Node(token.getToken(), token));
            s4(line);
        } else {
            checkEOF(token);
            new SyntaxError("identifier or constant", token);
        }
    }

    private void s5(List<Token> line) {
        Token token = line.get(mCounter);
        mCounter++;
        switch (token.getCategory()) {
            case ("Operator"):
                if ("=".equals(token.getToken())) {
                    new SyntaxError("arithmetic operator or ';'", token);
                } else {
                    mNodeStack.push(new OpeNode(token, null, null));
                    s4(line);
                }
                break;
            case ("Delimiter"):
                if (")".equals(token.getToken())) {
                    mNodeStack.push(new Node(token.getToken(), token));
                    s5(line);
                } else if (";".equals(token.getToken())
                            && scopeCount >= 0) {
                    while (!"=".equals(mNodeStack.peek().getToken().getToken())) {
                        mWorkingStack.push(mNodeStack.pop());
                    }
                    mWorkingStack.push(mNodeStack.pop());
                    mWorkingStack.push(mNodeStack.pop());
                    mNodeStack.push(buildAssignTree(mWorkingStack));
                    if (declaration) {
                        mNodeStack.push(buildDeclTree(mNodeStack));
                        declaration = false;
                    }
                    s0(line);
                } else {
                    new SyntaxError("arithmetic operator or ';'", token);
                }
                break;
            default:
                checkEOF(token);
                new SyntaxError("arithmetic operator or ';'", token);
        }
    }

    private void s6(List<Token> line) {
        Token token = line.get(mCounter);
        if ("(".equals(token.getToken())) {
            mNodeStack.push(new Node(token.getToken(), token));
            mCounter++;
            s7(line);
        } else {
            checkEOF(token);
            new SyntaxError("'('", token);
        }
    }

    private void s7(List<Token> line) {
        Token token = line.get(mCounter);
        if ("Identifier".equals(token.getCategory())) {
            mNodeStack.push(new IdNode(token));
            mCounter++;
            s8(line);
        } else {
            checkEOF(token);
            new SyntaxError("identifier", token);
        }
    }

    private void s8(List<Token> line) {
        Token token = line.get(mCounter);
        if ("Comparison Operator".equals(token.getCategory())) {
            mNodeStack.push(new CompNode(token, null, null));
            mCounter++;
            s9(line);
        } else {
            checkEOF(token);
            new SyntaxError("comparison operator", token);
        }
    }

    private void s9(List<Token> line) {
        Token token = line.get(mCounter);
        mCounter++;
        if ("Identifier".equals(token.getCategory())) {
            mNodeStack.push(new IdNode(token));
            s10(line);
        } else if ("Constant".equals(token.getCategory())) {
            mNodeStack.push(new ConstNode(token));
            s10(line);
        } else {
            checkEOF(token);
            new SyntaxError("identifier or constant", token);
        }
    }

    private void s10(List<Token> line) {
        Token token = line.get(mCounter);
        if (")".equals(token.getToken())) {
            while (!"(".equals(mNodeStack.peek().getToken().getToken())) {
                mWorkingStack.push(mNodeStack.pop());
            }
            mNodeStack.pop();
            mNodeStack.push(buildCompTree(mWorkingStack));
            mCounter++;
            s11(line);
        } else {
            checkEOF(token);
            new SyntaxError("')'", token);
        }
    }

    private void s11(List<Token> line) {
        Token token = line.get(mCounter);
        if ("{".equals(token.getToken())) {
            mNodeStack.push(new Node(token.getToken(), token));
            mCounter++;
            s0(line);
        } else {
            checkEOF(token);
            new SyntaxError("'{'", token);
        }
    }

    private void s13(List<Token> line) {
        Token token = line.get(mCounter);
        if ("(".equals(token.getToken())) {
            mNodeStack.push(new Node(token.getToken(), token));
            mCounter++;
            s14(line);
        } else {
            checkEOF(token);
            new SyntaxError("'('", token);
        }
    }

    private void s14(List<Token> line) {
        Token token = line.get(mCounter);
        if ("Identifier".equals(token.getCategory())) {
            mNodeStack.push(new IdNode(token));
            mCounter++;
            s15(line);
        } else {
            checkEOF(token);
            new SyntaxError("identifier", token);
        }
    }

    private void s15(List<Token> line) {
        Token token = line.get(mCounter);
        if ("Comparison Operator".equals(token.getCategory())) {
            mNodeStack.push(new CompNode(token, null, null));
            mCounter++;
            s16(line);
        } else {
            checkEOF(token);
            new SyntaxError("comparison operator", token);
        }
    }

    private void s16(List<Token> line) {
        Token token = line.get(mCounter);
        mCounter++;
        if ("Identifier".equals(token.getCategory())) {
            mNodeStack.push(new IdNode(token));
            s17(line);
        } else if ("Constant".equals(token.getCategory())) {
            mNodeStack.push(new ConstNode(token));
            s17(line);
        } else {
            checkEOF(token);
            new SyntaxError("identifier or constant", token);
        }
    }

    private void s17(List<Token> line) {
        Token token = line.get(mCounter);
        if (")".equals(token.getToken())) {
            while (!"(".equals(mNodeStack.peek().getToken().getToken())) {
                mWorkingStack.push(mNodeStack.pop());
            }
            mNodeStack.pop();
            mNodeStack.push(buildCompTree(mWorkingStack));
            mCounter++;
            s11(line);
        } else {
            checkEOF(token);
            new SyntaxError("')'", token);
        }
    }

    private void s20(List<Token> line) {
        Token token = line.get(mCounter);
        if ("sinon".equals(token.getToken())) {
            sinon = true;
            mCounter++;
            s11(line);
        }
    }

    private void checkEOF(Token token) {
        if ("EOF".equals(token.getToken())) {
            new SyntaxError("unexpected EOF", token.getLine());
        }
    }

    private Node buildAssignTree(Stack<Node> workingStack) {
        List<Node> infix = new ArrayList<>();
        while (!workingStack.empty()) {
            infix.add(workingStack.pop());
        }
        List<Node> postfix = convertPostfix(infix);
        Stack<Node> stack = new Stack<>();
        for (Node node: postfix) {
            if ("Identifier".equals(node.getToken().getCategory())
                || "Constant".equals(node.getToken().getCategory())) {
                stack.push(node);
            } else if ("Operator".equals(node.getToken().getCategory())) {
                if ("=".equals(node.getToken().getToken())) {
                    AssignNode temp = (AssignNode) node;
                    temp.setValue(stack.pop());
                    temp.setId((IdNode) stack.pop());
                    stack.push(temp);
                } else {
                    OpeNode temp = (OpeNode) node;
                    temp.setRight(stack.pop());
                    temp.setLeft(stack.pop());
                    stack.push(temp);
                }
            }
        }
        return stack.pop();
    }

    private List<Node> convertPostfix(List<Node> infixExpression) {
        List<Node> postfix = new ArrayList<>();
        Stack<Node> stack = new Stack<>();
        for (Node node: infixExpression) {
            //If it is an identifier or a constant, push it onto the stack
            if ("Identifier".equals(node.getToken().getCategory())
                    || "Constant".equals(node.getToken().getCategory())) {
                postfix.add(node);
            } else if ("Operator".equals(node.getToken().getCategory())) {
                //If the operator is not an "=" (lowest precedence)
                if (!"=".equals(node.getToken().getToken())) {
                    if (!stack.empty() && stack.peek().getToken().hasHigherPrecedence(node.getToken())) {
                        //Add every operator to the postfix expression
                        //until an operator of lowest or same precedence is found
                        while (stack.peek().getToken().hasHigherPrecedence(node.getToken())) {
                            postfix.add(stack.pop());
                        }
                    }
                }
                //Otherwise push it onto the stack
                stack.push(node);
            } else if ("(".equals(node.getToken().getToken())) {
                stack.push(node);
            } else if (")".equals(node.getToken().getToken())) {
                while (!"(".equals(stack.peek().getToken().getToken())) {
                    postfix.add(stack.pop());
                }
                stack.pop();
            }
        }
        //Add the remaining operator to the postfix expression
        while (!stack.empty()) {
            postfix.add(stack.pop());
        }
        return postfix;
    }

    private Node buildDeclTree(Stack <Node> workingStack) {
        DeclarNode decl = new DeclarNode(new Token("declaration", "declaration", 0));
        while (!"Type".equals(workingStack.peek().getToken().getCategory())) {
            decl.addId(workingStack.pop());
        }
        decl.setType((TypeNode) workingStack.pop());
        return decl;
    }

    private Node buildCompTree(Stack<Node> workingStack) {
        IdNode id = (IdNode) workingStack.pop();
        CompNode comp = (CompNode) workingStack.pop();
        ConstNode cons = (ConstNode) workingStack.pop();
        comp.setId(id);
        comp.setValue(cons);
        return comp;
    }

    private Node buildStmtsTree(Stack<Node> workingStack) {
        StatementsNode stmts = new StatementsNode();
        while (!workingStack.empty()) {
            stmts.addStatement(workingStack.pop());
        }
        return stmts;
    }
}
