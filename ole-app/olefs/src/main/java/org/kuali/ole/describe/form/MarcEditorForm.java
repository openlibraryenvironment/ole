package org.kuali.ole.describe.form;

import org.kuali.ole.describe.bo.MarcEditorControlField;
import org.kuali.ole.describe.bo.MarcEditorDataField;

import java.util.ArrayList;
import java.util.List;

/**
 * MarcEditorForm is the form class for Marc Editor
 */
public class MarcEditorForm extends EditorForm {

    //Leader and Control Fields
    private String leader;
    private String message;
    private String uuid;
    private List<MarcEditorControlField> controlFields = new ArrayList<MarcEditorControlField>();
    private List<MarcEditorDataField> dataFields = new ArrayList<MarcEditorDataField>();

    /**
     * Default Constructor.
     * The default behaviour of this object.
     */
    public MarcEditorForm() {
        super();
        controlFields.add(new MarcEditorControlField());
        dataFields.add(new MarcEditorDataField());
    }

    /**
     * Gets the leader attribute.
     *
     * @return Returns leader.
     */
    public String getLeader() {
        return leader;
    }

    /**
     * Sets the leader attribute value.
     *
     * @param leader The leader to set.
     */
    public void setLeader(String leader) {
        this.leader = leader;
    }

    /**
     * Gets the  dataFields attribute.
     *
     * @return Returns dataFields.
     */
    public List<MarcEditorDataField> getDataFields() {
        return dataFields;
    }

    /**
     * Sets the dataFields attribute value.
     *
     * @param dataFields The dataFields to set.
     */
    public void setDataFields(List<MarcEditorDataField> dataFields) {
        this.dataFields = dataFields;
    }

    /**
     * Gets the  controlFields attribute.
     *
     * @return Returns controlFields.
     */
    public List<MarcEditorControlField> getControlFields() {
        return controlFields;
    }

    /**
     * Sets the controlFields attribute value.
     *
     * @param controlFields The controlFields to set.
     */
    public void setControlFields(List<MarcEditorControlField> controlFields) {
        this.controlFields = controlFields;
    }

    /**
     * Gets the  message attribute.
     *
     * @return Returns message.
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
     * Gets the  uuid attribute.
     *
     * @return Returns uuid.
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
