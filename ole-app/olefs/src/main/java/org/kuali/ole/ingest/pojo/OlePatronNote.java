package org.kuali.ole.ingest.pojo;

/**
 * OlePatronNote is a business object class for Ole Patron Note Document
 */
public class OlePatronNote {
    private String noteType;
    private String note;
    private boolean active;
    /**
     * Gets the noteType attribute.
     * @return  Returns the note.
     */
    public String getNoteType() {
        return noteType;
    }
    /**
     * Sets the noteType attribute value.
     * @param noteType The noteType to set.
     */
    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }
    /**
     * Gets the active attribute.
     * @return  Returns the note.
     */
    public boolean isActive() {
        return active;
    }
    /**
     * Sets the active attribute value.
     * @param active The active to set.
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    /**
     * Gets the note attribute.
     * @return  Returns the note.
     */
    public String getNote() {
        return note;
    }
    /**
     * Sets the note attribute value.
     * @param note The note to set.
     */
    public void setNote(String note) {
        this.note = note;
    }
}
