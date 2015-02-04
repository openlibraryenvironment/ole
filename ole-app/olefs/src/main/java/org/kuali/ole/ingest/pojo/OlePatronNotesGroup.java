package org.kuali.ole.ingest.pojo;

import java.util.List;

/**
 * OlePatronNotesGroup is a business object class for Ole Patron Notes Group Document
 */
public class OlePatronNotesGroup {

    private List<OlePatronNote> note;

    /**
     * Gets the note attribute
     * @return  Returns the note.
     */
    public List<OlePatronNote> getNote() {
        return note;
    }

    /**
     * Sets the note attribute value
     * @param note The note to set.
     */
    public void setNote(List<OlePatronNote> note) {
        this.note = note;
    }
}
