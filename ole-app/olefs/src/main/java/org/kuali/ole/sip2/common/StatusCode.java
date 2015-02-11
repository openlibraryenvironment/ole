package org.kuali.ole.sip2.common;


/**
 * This enum represents the status of the SC unit.
 *
 * @author Gayathri A
 */
public enum StatusCode {

    OK("0"), PRINTER_OUT_OF_PAPER("1"), SHUT_DOWN("2");

    private String value;

    private StatusCode(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
