package org.kuali.ole.bo.serachRetrieve;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/17/12
 * Time: 4:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUInstanceVolume {

    public String enumeration;
    public String chronology;
    public String enumAndChron;

    public String getEnumeration() {
        return enumeration;
    }

    public void setEnumeration(String enumeration) {
        this.enumeration = enumeration;
    }

    public String getChronology() {
        return chronology;
    }

    public void setChronology(String chronology) {
        this.chronology = chronology;
    }

    public String getEnumAndChron() {
        return enumAndChron;
    }

    public void setEnumAndChron(String enumAndChron) {
        this.enumAndChron = enumAndChron;
    }

    @Override
    public String toString() {
        return "Volumes {" +
                "enumeration='" + enumeration + '\'' +
                ", chronology='" + chronology + '\'' +
                ", enumAndChron='" + enumAndChron + '\'' +
                '}';
    }
}
