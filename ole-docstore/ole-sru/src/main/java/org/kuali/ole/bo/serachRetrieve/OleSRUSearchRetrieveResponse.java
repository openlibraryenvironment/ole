package org.kuali.ole.bo.serachRetrieve;

import org.kuali.ole.bo.diagnostics.OleSRUDiagnostics;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 5/27/13
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUSearchRetrieveResponse {
    private String version;
    private Long numberOfRecords;
    private OleSRUResponseRecords oleSRUResponseRecords;
    private OleSRUDiagnostics oleSRUDiagnostics;


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Long getNumberOfRecords() {
        return numberOfRecords;
    }

    public void setNumberOfRecords(Long numberOfRecords) {
        this.numberOfRecords = numberOfRecords;
    }

    public OleSRUResponseRecords getOleSRUResponseRecords() {
        return oleSRUResponseRecords;
    }

    public void setOleSRUResponseRecords(OleSRUResponseRecords oleSRUResponseRecords) {
        this.oleSRUResponseRecords = oleSRUResponseRecords;
    }

    public OleSRUDiagnostics getOleSRUDiagnostics() {
        return oleSRUDiagnostics;
    }

    public void setOleSRUDiagnostics(OleSRUDiagnostics oleSRUDiagnostics) {
        this.oleSRUDiagnostics = oleSRUDiagnostics;
    }

    @Override
    public String toString() {
        return "OleSRUSearchRetrieveResponse{" +
                "version='" + version + '\'' +
                ", numberOfRecords=" + numberOfRecords +
                ", oleSRUResponseRecords=" + oleSRUResponseRecords +
                ", oleSRUDiagnostics=" + oleSRUDiagnostics +
                '}';
    }
}

