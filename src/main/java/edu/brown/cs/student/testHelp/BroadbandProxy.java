package edu.brown.cs.student.testHelp;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class BroadbandProxy implements List<List<String>> {
    private List<List<String>> target;

    public BroadbandProxy(List<List<String>> target) {
        this.target = target;
    }

    // Implement List interface methods
    @Override
    public int size() {
        return target.size();
    }

    @Override
    public boolean isEmpty() {
        return target.isEmpty();
    }

    // Implement other List interface methods as needed

    // Additional methods for proxy behavior
    public void addRow(List<String> row) {
        // Add additional behavior before or after delegating to the target list
        target.add(row);
    }

    public List<String> getRow(int index) {
        // Add additional behavior before or after delegating to the target list
        return target.get(index);
    }

    // Implement other custom methods as needed

}
