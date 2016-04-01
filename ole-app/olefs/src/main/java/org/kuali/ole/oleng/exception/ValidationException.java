package org.kuali.ole.oleng.exception;

/**
 * Created by SheikS on 3/30/2016.
 */
public class ValidationException extends Exception {

    public ValidationException(String message) {
        super(message);
    }


    @Override
    public String toString() {
        return getMessage();
    }
}
