package org.kuali.ole.alert.document;

import org.kuali.rice.krad.datadictionary.MaintenanceDocumentEntry;

/**
 * Created by maheswarang on 12/18/14.
 */
public class OleMaintenanceDocumentEntry extends MaintenanceDocumentEntry {

    public OleMaintenanceDocumentEntry() {
        super();
        super.documentClass=OleMaintenanceDocumentBase.class;
    }
}
