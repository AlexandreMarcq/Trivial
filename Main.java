package Trivial.Compiler;

import Trivial.Compiler.Generators.MIPSGenerator;

public class Main {

    public static void main(String[] args) {
        MIPSGenerator mipsGenerator = new MIPSGenerator();
        mipsGenerator.generate();
    }
}
