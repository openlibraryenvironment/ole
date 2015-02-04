package org.kuali.ole.describe.form;

import org.kuali.ole.describe.bo.WorkDublinEditorField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pp7788
 * Date: 12/11/12
 * Time: 12:43 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * DublinEditorForm is the Form class for Dublin Editor
 */
public class WorkBibDublinForm extends EditorForm {
    private List<WorkDublinEditorField> dublinFieldList = new ArrayList<WorkDublinEditorField>();
    private List<WorkDublinEditorField> existingDublinFieldList = new ArrayList<WorkDublinEditorField>();
    //private String message;
    private String uuid;

    /**
     * Default Constructor.
     * The default behaviour of this object.
     */
    public WorkBibDublinForm() {
        super();
        dublinFieldList.add(new WorkDublinEditorField());
    }

    /**
     * Gets the dublinFieldList attribute.
     *
     * @return Returns the dublinFieldList.
     */
    public List<WorkDublinEditorField> getDublinFieldList() {
        return dublinFieldList;
    }

    /**
     * Sets the dublinFieldList attribute value.
     *
     * @param dublinFieldList The dublinFieldList to set.
     */
    public void setDublinFieldList(List<WorkDublinEditorField> dublinFieldList) {
        this.dublinFieldList = dublinFieldList;
    }

    /**
     * Gets the existingDublinFieldList attribute.
     *
     * @return Returns the existingDublinFieldList.
     */
    public List<WorkDublinEditorField> getExistingDublinFieldList() {
        return existingDublinFieldList;
    }

    /**
     * Sets the existingDublinFieldList attribute value.
     *
     * @param existingDublinFieldList The existingDublinFieldList.
     */
    public void setExistingDublinFieldList(List<WorkDublinEditorField> existingDublinFieldList) {
        this.existingDublinFieldList = existingDublinFieldList;
    }

    /**
     * Gets the message attribute.
     *
     * @return Returns the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message attribute value.
     *
     * @param message The message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the uuid attribute.
     *
     * @return Returns the uuid.
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets the uuid attribute value.
     *
     * @param uuid The uuid to set.
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
