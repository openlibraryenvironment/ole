package org.kuali.ole.batchannotation;

public class Dummy {

    private int dummyProperty;

    public Dummy() {
    }
    
    public Dummy(int dummyProperty) {
        this.dummyProperty = dummyProperty;
    }

    public int getDummyProperty() {
        return dummyProperty;
    }

    public void setDummyProperty(int dummyProperty) {
        this.dummyProperty = dummyProperty;
    }
    
    @Override
    public String toString() {
        return "I am a Dummy with property " + dummyProperty;
    }

}
