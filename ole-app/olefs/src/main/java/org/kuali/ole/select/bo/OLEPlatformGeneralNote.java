package org.kuali.ole.select.bo;


import org.kuali.ole.select.document.OLEPlatformRecordDocument;
import org.kuali.rice.krad.bo.BusinessObjectBase;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created by chenchulakshmig on 9/10/14.
 * OLEPlatformGeneralNote provides platform general note information through getter and setter.
 */
public class OLEPlatformGeneralNote extends PersistableBusinessObjectBase{

    private String generalNoteId;

    private String generalNote;

    private String olePlatformId;

    private OLEPlatformRecordDocument olePlatformRecordDocument;

    public String getGeneralNoteId() {
        return generalNoteId;
    }

    public void setGeneralNoteId(String generalNoteId) {
        this.generalNoteId = generalNoteId;
    }

    public String getOlePlatformId() {
        return olePlatformId;
    }

    public void setOlePlatformId(String olePlatformId) {
        this.olePlatformId = olePlatformId;
    }

    public String getGeneralNote() {
        return generalNote;
    }

    public void setGeneralNote(String generalNote) {
        this.generalNote = generalNote;
    }

    public OLEPlatformRecordDocument getOlePlatformRecordDocument() {
        return olePlatformRecordDocument;
    }

    public void setOlePlatformRecordDocument(OLEPlatformRecordDocument olePlatformRecordDocument) {
        this.olePlatformRecordDocument = olePlatformRecordDocument;
    }
}
