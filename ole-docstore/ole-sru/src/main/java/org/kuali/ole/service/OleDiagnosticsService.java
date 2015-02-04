package org.kuali.ole.service;

import org.kuali.ole.bo.diagnostics.OleSRUDiagnostic;
import org.kuali.ole.bo.diagnostics.OleSRUDiagnostics;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/9/12
 * Time: 6:13 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OleDiagnosticsService {

    public OleSRUDiagnostics getDiagnosticResponse(String errorMessage);
}
