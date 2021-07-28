package Trivial.Compiler.Scanner;

import Trivial.Compiler.Dictionary.Dictionary;
import Trivial.Compiler.ErrorHandler.InputError;
import Trivial.Compiler.ErrorHandler.ScannerError;
import Trivial.Compiler.SymbolTable.SymbolTable;
import Trivial.Compiler.Token.Token;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Scanner {
    private final String mInput;
    private final String mTypes;
    private final String mKeywords;
    private final String mOperators;
    private final String mCompOperators;
    private final String mDelimiters;
    private final List<Token> mTokenList = new ArrayList<>();
    private final SymbolTable mSymTab = new SymbolTable();

    public SymbolTable getSymTab() {
        return mSymTab;
    }

// HashMaps and Arrays instead of using enums to simplify the code

    //Types
    final String[] mTypeList = {"entier", "booleen", "phrase", "decimal", "tableau"};

    //Operators
    final HashMap<String, String> mOpeMap = new HashMap<String, String>() {{
        put("+", "ADDITION");
        put("-", "SOUSTRACTION");
        put("*", "MULTIPLICATION");
        put("/", "DIVISION");
        put("=", "EGAL");
    }};

    //Comparison operators
    final HashMap<String, String> mCompOpeMap = new HashMap<String, String>() {{
        put("~", "EGAL");
        put("!", "DIFFERENT");
        put("<", "INFERIEUR");
        put(">", "SUPERIEUR");
        //put("<=", "INFEGAL");
        //put(">=", "SUPEGAL");
    }};

    //Delimiters
    final private HashMap<String, String> mDelMap = new HashMap<String, String>() {{
        put(";", "PVIRGULE");
        put(",", "VIRGULE");
        put("\"", "GUILLEMET");
        put(":", "DEUXPOINTS");
        put("//", "DOUBLESLASH");
        put("/*", "SLASHETOILE");
        put("*/", "ETOILESLASH");
        put("'", "APOSTROPHE");
        put("(", "PARGAUCHE");
        put(")", "PARDROITE");
        put("{", "ACCGAUCHE");
        put("}", "ACCDROITE");
    }};

    public Scanner(String input) {
        this.mInput = input;
        this.mTypes = generateTypes();
        this.mKeywords = generateKeywords();
        this.mOperators = generateOperators();
        this.mCompOperators = generateCompOperators();
        this.mDelimiters = generateDelimiters();
    }

    private String generateTypes() {
        StringBuilder types = new StringBuilder();
        for (String type: mTypeList) {
            types.append(type).append("|");
        }
        return types.substring(0, types.length()-1);
    }

    private String generateKeywords() {
        StringBuilder keywords = new StringBuilder();
        for (Dictionary word: Dictionary.values()) {
            keywords.append(word).append("|");
        }
        return keywords.substring(0, keywords.length()-1);
    }

    private String generateOperators() {
        StringBuilder operators = new StringBuilder();
        for (String operator: mOpeMap.keySet()) {
            if (operator.equals("*") || operator.equals("+")) {
                operator = "\\" + operator;
            }
            operators.append(operator).append("|");
        }
        return operators.substring(0, operators.length()-1);
    }

    private String generateCompOperators() {
        StringBuilder operators = new StringBuilder();
        for (String operator: mCompOpeMap.keySet()) {
            operators.append(operator).append("|");
        }
        return operators.substring(0, operators.length()-1);
    }

    private String generateDelimiters() {
        StringBuilder delimiters = new StringBuilder();
        for (String delimiter : mDelMap.keySet()) {
            if (delimiter.equals("*/") || delimiter.equals("(") || delimiter.equals(")") || delimiter.equals("{") || delimiter.equals("}")) {
                delimiter = "\\" + delimiter;
            }
            delimiters.append(delimiter).append("|");
        }
        return delimiters.substring(0, delimiters.length()-1);
    }


    public List<List<Token>> scan() {
        //The list that is going to be sent to the parser
        List<List<Token>> lineList = new ArrayList<>();
        //Readers used to read the input file
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;

        //Checking if the extension of the input file is 'fr'. If not, creates an InputError.
        String ext = mInput.split("\\.")[1];
        if (!ext.equals("fr")) {
            new InputError("wrong file extension, got '." + ext + "' instead of '.fr'");
        }

        try {
            fileReader = new FileReader(mInput);
            bufferedReader = new BufferedReader(fileReader);
            String currentLine = new String(Files.readAllBytes(Paths.get(mInput)));
            int lineNum = 1;
            //System.out.println(currentLine.chars().filter(ch -> ch == '\n').count());
            for (String token: currentLine.split("[ \r]+|(?=([\n;(){}+\\-*/=,<>!~]))|(?<=[\n;(){}+\\-*/=,<>!~])")) {
                    if ("\n".equals(token)) {
                        lineNum++;
                    } else if (!"".equals(token)) {
                        //Identifies the token
                        identifyToken(token, lineNum);
                    }
            }
            int scopeCount = 0;
            ArrayList<Token> line = new ArrayList<>();
            for (Token token: mTokenList) {
                line.add(token);
                if (";".equals(token.getToken()) && scopeCount == 0) {
                    line.add(new Token("EOF", "Delimiter", lineNum));
                    lineList.add(new ArrayList<>(line));
                    line.clear();
                } else if ("{".equals(token.getToken())) {
                    scopeCount++;
                } else if ("}".equals(token.getToken())) {
                    scopeCount--;
                    if (!(mTokenList.indexOf(token) + 1 > mTokenList.size() - 1)) {
                        if (!"sinon".equals(mTokenList.get(mTokenList.indexOf(token) + 1).getToken())) {
                            if (scopeCount == 0) {
                                line.add(new Token("EOF", "Delimiter", lineNum));
                                lineList.add(new ArrayList<>(line));
                                line.clear();
                            }
                        }
                    } else {
                        line.add(new Token("EOF", "Delimiter", lineNum));
                        lineList.add(new ArrayList<>(line));
                        line.clear();
                    }
                }
            }
            for (List<Token> tokenLine : lineList) {
                checkInsert(tokenLine, lineNum);
            }
        } catch (Exception e) {
            //If we get an exception due to readers, create a new InputError
            new InputError(e.getMessage());
        } finally {
            try {
                //Tries to close all the readers
                if (bufferedReader != null)
                    bufferedReader.close();
                if (fileReader != null)
                    fileReader.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.exit(0);
            }
            System.out.println(mSymTab);
        }
        return lineList;
    }

    private void identifyToken(String token, int lineNum) {
        //Creates a new Token and adds it to the mTokenList
        String identifier = "(^([a-zA-Z]+[a-zA-Z0-9_|-]*))";
        String constant = "(^([0-9]+\\.?[0-9]*))$";
        if (token.matches(mTypes)) {
            mTokenList.add(new Token(token, "Type", lineNum));
        } else if (token.matches(mOperators)) {
            mTokenList.add(new Token(token, "Operator", lineNum));
        } else if (token.matches(mDelimiters)) {
            mTokenList.add(new Token(token, "Delimiter", lineNum));
        } else if (token.matches(mKeywords)) {
            mTokenList.add(new Token(token, "Keyword", lineNum));
        } else if (token.matches(identifier)) {
            mTokenList.add(new Token(token, "Identifier", lineNum));
        } else if (token.matches(constant)) {
            mTokenList.add(new Token(token, "Constant", lineNum));
        } else if (token.matches(mCompOperators)) {
            mTokenList.add(new Token(token, "Comparison Operator", lineNum));
        } else {
            new ScannerError("invalid token " + token , lineNum);
        }
    }

    private void checkInsert(List<Token> line, int lineNum) {
        String type = "";
        boolean flag = false;
        for (Token token: line) {
            if ("Type".equals(token.getCategory())) {
                type = token.getToken();
                flag = true;
            } else if ("Identifier".equals(token.getCategory()) && flag) {
                insert(token, type, lineNum);
            } else if (";".equals(token.getToken()) || "=".equals(token.getToken())) {
                flag = false;
            }
        }
    }

    private void insert(Token token, String type, int lineNum) {
        //Insert the variable into the symbol table if it doesn't already exists
        if (!mSymTab.isEmpty()) {
            if (!mSymTab.containsKey(token.getToken())) {
                mSymTab.put(token.getToken(), type);
            } else {
                new ScannerError("variable " + token.getToken() + " already exists", lineNum);
            }
        } else {
            mSymTab.put(token.getToken(), type);
        }
    }
}
