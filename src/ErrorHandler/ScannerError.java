package ErrorHandler;

public class ScannerError extends ErrorHandler {

    public ScannerError(String message, int line) {
        super.printError("Lexical", message);
        System.out.print(" at line " + line);
        System.exit(1);
    }
}
