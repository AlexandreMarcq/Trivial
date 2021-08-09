package ErrorHandler;


public class SemanticError extends ErrorHandler{

    public SemanticError(int flag, String type, int line) {
        if (flag == 0) {
            super.printError("Semantic", "type mismatch in " + type);
        } else {
            super.printError("Semantic", type);
        }
        System.out.print(" at line " + line);
        System.exit(1);
    }

}
