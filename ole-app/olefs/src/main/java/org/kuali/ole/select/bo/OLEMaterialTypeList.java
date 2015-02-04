package org.kuali.ole.select.bo;

import org.kuali.ole.select.document.OLEEResourceRecordDocument;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: srinivasane
 * Date: 6/18/13
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEMaterialTypeList extends PersistableBusinessObjectBase {

    private String oleMaterialTypesId;
    private String oleERSIdentifier;
    private String oleMaterialTypeId;
    private OLEMaterialType oleMaterialType;
    private OLEEResourceRecordDocument oleeResourceRecordDocument;

    public String getOleMaterialTypesId() {
        return oleMaterialTypesId;
    }

    public void setOleMaterialTypesId(String oleMaterialTypesId) {
        this.oleMaterialTypesId = oleMaterialTypesId;
    }

    public String getOleERSIdentifier() {
        return oleERSIdentifier;
    }

    public void setOleERSIdentifier(String oleERSIdentifier) {
        this.oleERSIdentifier = oleERSIdentifier;
    }

    public String getOleMaterialTypeId() {
        return oleMaterialTypeId;
    }

    public void setOleMaterialTypeId(String oleMaterialTypeId) {
        this.oleMaterialTypeId = oleMaterialTypeId;
    }

    public OLEMaterialType getOleMaterialType() {
        return oleMaterialType;
    }

    public void setOleMaterialType(OLEMaterialType oleMaterialType) {
        this.oleMaterialType = oleMaterialType;
    }

    public OLEEResourceRecordDocument getOleeResourceRecordDocument() {
        return oleeResourceRecordDocument;
    }

    public void setOleeResourceRecordDocument(OLEEResourceRecordDocument oleeResourceRecordDocument) {
        this.oleeResourceRecordDocument = oleeResourceRecordDocument;
    }
}
