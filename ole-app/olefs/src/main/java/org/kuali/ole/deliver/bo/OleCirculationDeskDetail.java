package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/16/12
 * Time: 9:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class OleCirculationDeskDetail extends PersistableBusinessObjectBase {
    private String circulationDeskDetailId;
    private boolean defaultLocation;
    private boolean allowedLocation;
    private String operatorId;
    private String circulationDeskId;
    private OleCirculationDesk oleCirculationDesk;

    /**
     * Gets the circulationDeskDetailId attribute.
     *
     * @return Returns the circulationDeskDetailId
     */
    public String getCirculationDeskDetailId() {
        return circulationDeskDetailId;
    }

    /**
     * Sets the circulationDeskDetailId attribute value.
     *
     * @param circulationDeskDetailId The circulationDeskDetailId to set.
     */
    public void setCirculationDeskDetailId(String circulationDeskDetailId) {
        this.circulationDeskDetailId = circulationDeskDetailId;
    }

    /**
     * Gets the defaultLocation attribute.
     *
     * @return Returns the defaultLocation
     */
    public boolean isDefaultLocation() {
        return defaultLocation;
    }

    /**
     * Sets the defaultLocation attribute value.
     *
     * @param defaultLocation The defaultLocation to set.
     */
    public void setDefaultLocation(boolean defaultLocation) {
        this.defaultLocation = defaultLocation;
    }

    /**
     * Gets the allowedLocation attribute.
     *
     * @return Returns the allowedLocation
     */
    public boolean isAllowedLocation() {
        return allowedLocation;
    }

    /**
     * Sets the allowedLocation attribute value.
     *
     * @param allowedLocation The allowedLocation to set.
     */
    public void setAllowedLocation(boolean allowedLocation) {
        this.allowedLocation = allowedLocation;
    }

    /**
     * Gets the circulationDeskId attribute.
     *
     * @return Returns the circulationDeskId
     */
    public String getCirculationDeskId() {
        return circulationDeskId;
    }

    /**
     * Sets the circulationDeskId attribute value.
     *
     * @param circulationDeskId The circulationDeskId to set.
     */
    public void setCirculationDeskId(String circulationDeskId) {
        this.circulationDeskId = circulationDeskId;
    }

    /**
     * Gets the operatorId attribute.
     *
     * @return Returns the operatorId
     */
    public String getOperatorId() {
        return operatorId;
    }

    /**
     * Sets the operatorId attribute value.
     *
     * @param operatorId The operatorId to set.
     */
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * Gets the instance of OleCirculationDesk
     *
     * @return oleCirculationDesk (OleCirculationDesk)
     */
    public OleCirculationDesk getOleCirculationDesk() {
        return oleCirculationDesk;
    }

    /**
     * Sets the value for OleCirculationDesk
     *
     * @param oleCirculationDesk (OleCirculationDesk)
     */
    public void setOleCirculationDesk(OleCirculationDesk oleCirculationDesk) {
        this.oleCirculationDesk = oleCirculationDesk;
    }
}
