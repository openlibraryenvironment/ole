package org.kuali.ole.deliver.notice.bo;

import org.kuali.ole.alert.document.OlePersistableBusinessObjectBase;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created by maheswarang on 7/7/15.
 */
public class OleNoticeFieldLabelMapping extends OlePersistableBusinessObjectBase {

    private String oleNoticeFieldLabelMappingId;
    private String fieldName;
    private String fieldLabel;
    private String belongsTo;
    private String oleNoticeContentConfigurationId;
    private OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo;

    public String getOleNoticeFieldLabelMappingId() {
        return oleNoticeFieldLabelMappingId;
    }

    public void setOleNoticeFieldLabelMappingId(String oleNoticeFieldLabelMappingId) {
        this.oleNoticeFieldLabelMappingId = oleNoticeFieldLabelMappingId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldLabel() {
        return fieldLabel;
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public String getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(String belongsTo) {
        this.belongsTo = belongsTo;
    }

    public String getOleNoticeContentConfigurationId() {
        return oleNoticeContentConfigurationId;
    }

    public void setOleNoticeContentConfigurationId(String oleNoticeContentConfigurationId) {
        this.oleNoticeContentConfigurationId = oleNoticeContentConfigurationId;
    }

    public OleNoticeContentConfigurationBo getOleNoticeContentConfigurationBo() {
        return oleNoticeContentConfigurationBo;
    }

    public void setOleNoticeContentConfigurationBo(OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo) {
        this.oleNoticeContentConfigurationBo = oleNoticeContentConfigurationBo;
    }
}
