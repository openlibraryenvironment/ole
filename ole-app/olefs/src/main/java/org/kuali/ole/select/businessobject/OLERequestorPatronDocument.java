package org.kuali.ole.select.businessobject;

import org.kuali.ole.deliver.bo.OleBorrowerType;
import org.kuali.rice.core.api.mo.common.active.MutableInactivatable;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: syedk
 * Date: 8/29/13
 * Time: 6:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLERequestorPatronDocument extends PersistableBusinessObjectBase implements MutableInactivatable {

    private String olePatronId;
    private String barcode;
    private String firstName;
    private String lastName;
    private String borrowerType;
    private boolean activeIndicator;
    private String name;
    private OleBorrowerType oleBorrowerType;
    private String requestorPatronId;

    /**
     * Gets the olePatronId attribute.
     *
     * @return Returns the olePatronId.
     */
    public String getOlePatronId() {
        return olePatronId;
    }

    /**
     * Sets the olePatronId attribute value.
     *
     * @param olePatronId The olePatronId to set.
     */
    public void setOlePatronId(String olePatronId) {
        this.olePatronId = olePatronId;
    }

    /**
     * Gets the barcode attribute.
     *
     * @return Returns the barcode.
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * Sets the barcode attribute value.
     *
     * @param barcode The barcode to set.
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }


    /**
     * Gets the firstName attribute.
     *
     * @return Returns the firstName.
     */
    public String getFirstName() {
        return firstName;
    }


    /**
     * Sets the firstName attribute value.
     *
     * @param firstName The firstName to set.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    /**
     * Gets the lastName attribute.
     *
     * @return Returns the lastName.
     */
    public String getLastName() {
        return lastName;
    }


    /**
     * Sets the lastName attribute value.
     *
     * @param lastName The lastName to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the borrowerType attribute.
     *
     * @return Returns the borrowerType.
     */
    public String getBorrowerType() {
        return borrowerType;
    }

    /**
     * Sets the borrowerType attribute value.
     *
     * @param borrowerType The borrowerType to set.
     */
    public void setBorrowerType(String borrowerType) {
        this.borrowerType = borrowerType;
    }

    /**
     * Gets the activeIndicator attribute.
     *
     * @return Returns the activeIndicator.
     */
    public boolean isActiveIndicator() {
        return activeIndicator;
    }

    /**
     * Sets the activeIndicator attribute value.
     *
     * @param activeIndicator The activeIndicator to set.
     */
    public void setActiveIndicator(boolean activeIndicator) {
        this.activeIndicator = activeIndicator;
    }

    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("olePatronId", olePatronId);
        return toStringMap;
    }

    @Override
    public boolean isActive() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setActive(boolean active) {
        // TODO Auto-generated method stub

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OleBorrowerType getOleBorrowerType() {
        return oleBorrowerType;
    }

    public void setOleBorrowerType(OleBorrowerType oleBorrowerType) {
        this.oleBorrowerType = oleBorrowerType;
    }

    public String getRequestorPatronId() {
        return requestorPatronId;
    }

    public void setRequestorPatronId(String requestorPatronId) {
        this.requestorPatronId = requestorPatronId;
    }
}
