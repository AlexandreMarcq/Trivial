package SymbolTable;

import java.util.HashMap;

public class SymbolTable extends HashMap<String,String> {

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (String name: this.keySet()) {
            str.append(name).append(": ").append(this.get(name)).append("\n");
        }
        return str.toString();
    }
}
