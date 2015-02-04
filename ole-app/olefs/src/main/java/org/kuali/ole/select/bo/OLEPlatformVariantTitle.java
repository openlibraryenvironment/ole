package org.kuali.ole.select.bo;

import org.kuali.ole.select.document.OLEPlatformRecordDocument;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.*;

/**
 * Created by chenchulakshmig on 11/5/14.
 *  * OLEPlatformVariantTitle provides platform variant title information through getter and setter.
 */

public class OLEPlatformVariantTitle extends PersistableBusinessObjectBase {

    private String variantTitleId;

    private String variantTitle;

    private String olePlatformId;

    private OLEPlatformRecordDocument olePlatformRecordDocument;

    public String getVariantTitleId() {
        return variantTitleId;
    }

    public void setVariantTitleId(String variantTitleId) {
        this.variantTitleId = variantTitleId;
    }

    public String getVariantTitle() {
        return variantTitle;
    }

    public void setVariantTitle(String variantTitle) {
        this.variantTitle = variantTitle;
    }

    public String getOlePlatformId() {
        return olePlatformId;
    }

    public void setOlePlatformId(String olePlatformId) {
        this.olePlatformId = olePlatformId;
    }

    public OLEPlatformRecordDocument getOlePlatformRecordDocument() {
        return olePlatformRecordDocument;
    }

    public void setOlePlatformRecordDocument(OLEPlatformRecordDocument olePlatformRecordDocument) {
        this.olePlatformRecordDocument = olePlatformRecordDocument;
    }
}
