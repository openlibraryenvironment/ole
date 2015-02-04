package org.kuali.ole.describe.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;


/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 2/7/13
 * Time: 5:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleDiscoveryExportMappingFields extends PersistableBusinessObjectBase {

    private String mappingFieldId;
    private String marcField;
    private String itemField;
    private String description;
    private String exportId;
    private OleDiscoveryExportProfile oleDiscoveryExportProfile;

    /**
     * Gets the mappingFieldId attribute.
     *
     * @return Returns the mappingFieldId
     */
    public String getMappingFieldId() {
        return mappingFieldId;
    }

    /**
     * Sets the mappingFieldId attribute value.
     *
     * @param mappingFieldId The mappingFieldId to set.
     */
    public void setMappingFieldId(String mappingFieldId) {
        this.mappingFieldId = mappingFieldId;
    }

    /**
     * Gets the marcField attribute.
     *
     * @return Returns the marcField
     */
    public String getMarcField() {
        if (marcField == null && oleDiscoveryExportProfile != null) {
            return oleDiscoveryExportProfile.getOleDiscoveryExportMappingFields().get(0).getMarcField();
        }
        return marcField;
    }

    /**
     * Sets the marcField attribute value.
     *
     * @param marcField The marcField to set.
     */
    public void setMarcField(String marcField) {
        this.marcField = marcField;
    }

    /**
     * Gets the itemField attribute.
     *
     * @return Returns the itemField
     */
    public String getItemField() {
        if (itemField == null && oleDiscoveryExportProfile != null) {
            return oleDiscoveryExportProfile.getOleDiscoveryExportMappingFields().get(0).getItemField();
        }
        return itemField;
    }

    /**
     * Sets the itemField attribute value.
     *
     * @param itemField The itemField to set.
     */
    public void setItemField(String itemField) {
        this.itemField = itemField;
    }

    /**
     * Gets the description attribute.
     *
     * @return Returns the description
     */
    public String getDescription() {
        if (description == null && oleDiscoveryExportProfile != null) {
            return oleDiscoveryExportProfile.getOleDiscoveryExportMappingFields().get(0).getDescription();
        }
        return description;
    }

    /**
     * Sets the description attribute value.
     *
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the exportId attribute.
     *
     * @return Returns the exportId
     */
    public String getExportId() {
        return exportId;
    }

    /**
     * Sets the exportId attribute value.
     *
     * @param exportId The exportId to set.
     */
    public void setExportId(String exportId) {
        this.exportId = exportId;
    }

    /**
     * Gets the oleDiscoveryExportProfile instance.
     *
     * @return Return the instance of the type OleDiscoveryExportProfile
     */
    public OleDiscoveryExportProfile getOleDiscoveryExportProfile() {
        return oleDiscoveryExportProfile;
    }

    /**
     * Sets the value for oleDiscoveryExportProfile.
     *
     * @param oleDiscoveryExportProfile The oleDiscoveryExportProfile to set.
     */
    public void setOleDiscoveryExportProfile(OleDiscoveryExportProfile oleDiscoveryExportProfile) {
        this.oleDiscoveryExportProfile = oleDiscoveryExportProfile;
    }
}
