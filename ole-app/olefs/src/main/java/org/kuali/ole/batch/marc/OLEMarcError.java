package org.kuali.ole.batch.marc;

import org.marc4j.ErrorHandler;

import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: meenrajd
 * Date: 9/2/13
 * Time: 11:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class OLEMarcError extends ErrorHandler.Error {
    private static final String COMMA =",";
    private OLEMarcErrorHandler errorHandler;

    protected OLEMarcError(ErrorHandler errorHandler, String recordID,
                           String field, String subfield, int severity, String message) {
        errorHandler.super(recordID, field, subfield, severity, message);
        this.errorHandler = (OLEMarcErrorHandler)errorHandler;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Record").append(COMMA).append(curRecordID).append(COMMA).append("Time Stamp").append(COMMA).append(new Date().toString()).append(COMMA);

        for(Map.Entry<String,String>entry : errorHandler.getErrorMap().entrySet()){
            str.append(entry.getKey()).append(COMMA).append(entry.getValue()).append(COMMA);
        }
        return (str.toString());
    }


}