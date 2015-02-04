package org.kuali.ole.bo.diagnostics;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 5/28/13
 * Time: 4:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUDiagnostics {
    private List<OleSRUDiagnostic> oleSRUDiagnosticList;

    public List<OleSRUDiagnostic> getOleSRUDiagnosticList() {
        return oleSRUDiagnosticList;
    }

    public void setOleSRUDiagnosticList(List<OleSRUDiagnostic> oleSRUDiagnosticList) {
        this.oleSRUDiagnosticList = oleSRUDiagnosticList;
    }

    @Override
    public String toString() {
        return "OleSRUDiagnostics{" +
                "oleSRUDiagnosticList=" + oleSRUDiagnosticList +
                '}';
    }
}
