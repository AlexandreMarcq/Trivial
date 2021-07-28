package Trivial.Compiler.Generators;

public class Temporary {
    private static int counter = 0;
    private int mCount;

    public Temporary() {
        mCount = counter++;
    }

    public void resetCounter() { counter = 0; }

    @Override
    public String toString() {
        return "t" + mCount;
    }
}
