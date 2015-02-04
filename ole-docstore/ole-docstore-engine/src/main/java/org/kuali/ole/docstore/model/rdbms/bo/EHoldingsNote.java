package org.kuali.ole.docstore.model.rdbms.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: srinivasane
 * Date: 7/20/13
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class EHoldingsNote extends PersistableBusinessObjectBase implements Serializable {

    private String eHoldingsNoteId;
    private String eHoldingsIdentifier;
    private String eHoldingsNoteTypeId;
    private String eHoldingsNoteText;
    private EHoldingsNoteType eHoldingsNoteType;
    private EHoldingsRecord eHoldingsRecord;

    public String geteHoldingsNoteId() {
        return eHoldingsNoteId;
    }

    public void seteHoldingsNoteId(String eHoldingsNoteId) {
        this.eHoldingsNoteId = eHoldingsNoteId;
    }

    public String geteHoldingsIdentifier() {
        return eHoldingsIdentifier;
    }

    public void seteHoldingsIdentifier(String eHoldingsIdentifier) {
        this.eHoldingsIdentifier = eHoldingsIdentifier;
    }

    public String geteHoldingsNoteTypeId() {
        return eHoldingsNoteTypeId;
    }

    public void seteHoldingsNoteTypeId(String eHoldingsNoteTypeId) {
        this.eHoldingsNoteTypeId = eHoldingsNoteTypeId;
    }

    public String geteHoldingsNoteText() {
        return eHoldingsNoteText;
    }

    public void seteHoldingsNoteText(String eHoldingsNoteText) {
        this.eHoldingsNoteText = eHoldingsNoteText;
    }

    public EHoldingsNoteType geteHoldingsNoteType() {
        return eHoldingsNoteType;
    }

    public void seteHoldingsNoteType(EHoldingsNoteType eHoldingsNoteType) {
        this.eHoldingsNoteType = eHoldingsNoteType;
    }

    public EHoldingsRecord geteHoldingsRecord() {
        return eHoldingsRecord;
    }

    public void seteHoldingsRecord(EHoldingsRecord eHoldingsRecord) {
        this.eHoldingsRecord = eHoldingsRecord;
    }
}
