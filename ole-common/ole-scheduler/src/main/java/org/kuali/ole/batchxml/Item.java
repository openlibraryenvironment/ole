package org.kuali.ole.batchxml;

public class Item {

    private int id;
    private String property;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public String toString() {
        return "Item [id=" + id + ", property=" + property + "]";
    }
}