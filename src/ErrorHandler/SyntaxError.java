package ErrorHandler;

import Token.Token;

public class SyntaxError extends ErrorHandler{

    public SyntaxError(String expectation, Token token) {
        super.printError("Syntax", "expected " + expectation + ", got '" + token.getToken() + "'");
        System.out.print(" at line " + token.getLine());
        System.exit(1);
    }

    public SyntaxError(String message, int line) {
        super.printError("Syntax", message);
        System.out.println(" at line " + line);
        System.exit(1);
    }
}
