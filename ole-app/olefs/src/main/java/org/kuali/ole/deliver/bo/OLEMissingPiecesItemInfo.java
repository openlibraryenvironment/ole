package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 1/9/14
 * Time: 3:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEMissingPiecesItemInfo extends PersistableBusinessObjectBase {
    private String id;
    private String olePatronId;
    private String itemId;
    private String flagType;
    private String flagNote;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOlePatronId() {
        return olePatronId;
    }

    public void setOlePatronId(String olePatronId) {
        this.olePatronId = olePatronId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getFlagType() {
        return flagType;
    }

    public void setFlagType(String flagType) {
        this.flagType = flagType;
    }

    public String getFlagNote() {
        return flagNote;
    }

    public void setFlagNote(String flagNote) {
        this.flagNote = flagNote;
    }
}
