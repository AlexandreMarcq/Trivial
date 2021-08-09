package ErrorHandler;


public class InputError extends ErrorHandler {

    public InputError (String message) {
        this.printError("Input", message);
        System.exit(1);
    }
}
