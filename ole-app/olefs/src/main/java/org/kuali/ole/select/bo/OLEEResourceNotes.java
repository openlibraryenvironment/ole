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
public class OLEEResourceNotes extends PersistableBusinessObjectBase {

    private String oleERSNotesId;
    private String oleERSIdentifier;
    private String oleERSNoteText;
    private OLEEResourceRecordDocument oleeResourceRecordDocument;

    public OLEEResourceRecordDocument getOleeResourceRecordDocument() {
        return oleeResourceRecordDocument;
    }

    public void setOleeResourceRecordDocument(OLEEResourceRecordDocument oleeResourceRecordDocument) {
        this.oleeResourceRecordDocument = oleeResourceRecordDocument;
    }

    public String getOleERSNotesId() {
        return oleERSNotesId;
    }

    public void setOleERSNotesId(String oleERSNotesId) {
        this.oleERSNotesId = oleERSNotesId;
    }

    public String getOleERSIdentifier() {
        return oleERSIdentifier;
    }

    public void setOleERSIdentifier(String oleERSIdentifier) {
        this.oleERSIdentifier = oleERSIdentifier;
    }

    public String getOleERSNoteText() {
        return oleERSNoteText;
    }

    public void setOleERSNoteText(String oleERSNoteText) {
        this.oleERSNoteText = oleERSNoteText;
    }
}
