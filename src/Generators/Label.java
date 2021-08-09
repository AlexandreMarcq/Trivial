package Generators;

public class Label {
    private static int counter = 0;
    private int mCount;

    public Label() {
        mCount = counter++;
    }

    public void resetCounter() { counter = 0; }

    @Override
    public String toString() {
        return "L" + mCount;
    }
}
