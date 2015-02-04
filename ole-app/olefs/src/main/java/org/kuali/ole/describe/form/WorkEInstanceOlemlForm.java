package org.kuali.ole.describe.form;

/**
 * Created with IntelliJ IDEA.
 * User: Srinivasane
 * Date: 12/11/12
 * Time: 2:57 PM
 * To change this template use File | Settings | File Templates.
 */

import org.kuali.ole.describe.bo.OleExtendedEHoldingFields;
import org.kuali.ole.docstore.common.document.content.instance.ExtentOfOwnership;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.select.document.OLEEResourceLicense;

import java.util.ArrayList;
import java.util.List;


/**
 * InstanceEditorForm is the form class for Instance Editor
 */
public class WorkEInstanceOlemlForm extends EditorForm {

    private OleHoldings selectedEHoldings;
    private OleExtendedEHoldingFields extendedEHoldingFields;

    private boolean proxiedResource = false;

    public WorkEInstanceOlemlForm() {
        getSelectedEHoldings().getExtentOfOwnership().add(new ExtentOfOwnership());
    }

    public OleHoldings getSelectedEHoldings() {
        if(selectedEHoldings == null) {
            selectedEHoldings = new OleHoldings();
        }
        return selectedEHoldings;
    }

    public void setSelectedEHoldings(OleHoldings selectedEHoldings) {
        this.selectedEHoldings = selectedEHoldings;
    }

    public OleExtendedEHoldingFields getExtendedEHoldingFields() {
        if(extendedEHoldingFields == null) {
            extendedEHoldingFields = new OleExtendedEHoldingFields();
        }
        return extendedEHoldingFields;
    }

    public void setExtendedEHoldingFields(OleExtendedEHoldingFields extendedEHoldingFields) {
        this.extendedEHoldingFields = extendedEHoldingFields;
    }

    public boolean isProxiedResource() {
        return proxiedResource;
    }

    public void setProxiedResource(boolean proxiedResource) {
        this.proxiedResource = proxiedResource;
    }
}
