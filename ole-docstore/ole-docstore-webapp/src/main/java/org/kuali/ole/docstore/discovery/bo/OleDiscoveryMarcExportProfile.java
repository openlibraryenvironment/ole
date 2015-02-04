package org.kuali.ole.docstore.discovery.bo;

//import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 2/7/13
 * Time: 1:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleDiscoveryMarcExportProfile {
    private String exportFormat;
    private String exportType;
    private String exportTo;
    private Integer noOfExportThreads;
    private Integer noOfRecords;
    private String dataField;
    private List<OleDiscoveryMarcMappingField> oleDiscoveryMarcMappingFields = new ArrayList<OleDiscoveryMarcMappingField>();


    /**
     * Gets the exportFormat attribute.
     *
     * @return Returns the exportFormat
     */
    public String getExportFormat() {
        return exportFormat;
    }

    /**
     * Sets the exportFormat attribute value.
     *
     * @param exportFormat The exportFormat to set.
     */
    public void setExportFormat(String exportFormat) {
        this.exportFormat = exportFormat;
    }

    /**
     * Gets the exportType attribute.
     *
     * @return Returns the exportType
     */
    public String getExportType() {
        return exportType;
    }

    /**
     * Sets the exportType attribute value.
     *
     * @param exportType The exportType to set.
     */
    public void setExportType(String exportType) {
        this.exportType = exportType;
    }

    /**
     * Gets the noOfExportThreads attribute.
     *
     * @return Returns the noOfExportThreads
     */
    public Integer getNoOfExportThreads() {
        return noOfExportThreads;
    }

    /**
     * Sets the noOfExportThreads attribute value.
     *
     * @param noOfExportThreads The noOfExportThreads to set.
     */
    public void setNoOfExportThreads(Integer noOfExportThreads) {
        this.noOfExportThreads = noOfExportThreads;
    }

    /**
     * Gets the noOfRecords attribute.
     *
     * @return Returns the noOfRecords
     */
    public Integer getNoOfRecords() {
        return noOfRecords;
    }

    /**
     * Sets the noOfRecords attribute value.
     *
     * @param noOfRecords The noOfRecords to set.
     */
    public void setNoOfRecords(Integer noOfRecords) {
        this.noOfRecords = noOfRecords;
    }

    /**
     * Gets the exportTo attribute.
     *
     * @return Returns the exportTo
     */
    public String getExportTo() {
        return exportTo;
    }

    /**
     * Sets the exportTo attribute value.
     *
     * @param exportTo The exportTo to set.
     */
    public void setExportTo(String exportTo) {
        this.exportTo = exportTo;
    }

    /**
     * Gets the dataField attribute.
     *
     * @return Returns the dataField
     */
    public String getDataField() {
        return dataField;
    }

    /**
     * Sets the dataField attribute value.
     *
     * @param dataField The dataField to set.
     */
    public void setDataField(String dataField) {
        this.dataField = dataField;
    }

    /**
     * Gets the list of type OleDiscoveryMarcMappingField
     *
     * @return oleDiscoveryMarcMappingFields
     */
    public List<OleDiscoveryMarcMappingField> getOleDiscoveryMarcMappingFields() {
        return oleDiscoveryMarcMappingFields;
    }

    /**
     * Sets the list of values of the type OleDiscoveryMarcMappingField
     *
     * @param oleDiscoveryMarcMappingFields
     */
    public void setOleDiscoveryMarcMappingFields(List<OleDiscoveryMarcMappingField> oleDiscoveryMarcMappingFields) {
        this.oleDiscoveryMarcMappingFields = oleDiscoveryMarcMappingFields;
    }
}
