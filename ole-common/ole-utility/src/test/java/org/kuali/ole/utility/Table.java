package org.kuali.ole.utility;

public class Table implements Comparable<Table> {
    String name;
    int rowCount;

    @Override
    public int compareTo(Table other) {
        return this.name.compareTo(other.getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }
}
