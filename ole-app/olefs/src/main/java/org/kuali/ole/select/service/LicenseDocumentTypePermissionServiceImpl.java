package org.kuali.ole.select.service;

import org.kuali.rice.kew.doctype.service.impl.DocumentTypePermissionServiceImpl;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;

/**
 * LicenseDocumentTypePermissionServiceImpl will check the permission for LicenseRequestDocument
 */
public class LicenseDocumentTypePermissionServiceImpl extends DocumentTypePermissionServiceImpl {
    @Override
    public boolean canRoute(String principalId, DocumentRouteHeaderValue documentRouteHeaderValue) {
        if (documentRouteHeaderValue.getDocumentTypeName().equalsIgnoreCase("LicenseRequestDocument")) {
            return true;
        } else {
            return super.canRoute(principalId, documentRouteHeaderValue);
        }
    }
}
