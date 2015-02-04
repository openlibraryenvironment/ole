package org.kuali.ole.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 4/17/14
 * Time: 1:07 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="oleSruItem")
public class OLESruItem {

    private String itemDueDate;
    private boolean renewable;
    private String shelvingLocation;
    private String localLocation;

    public String getItemDueDate() {
        return itemDueDate;
    }

    public void setItemDueDate(String itemDueDate) {
        this.itemDueDate = itemDueDate;
    }

    public boolean isRenewable() {
        return renewable;
    }

    public void setRenewable(boolean renewable) {
        this.renewable = renewable;
    }

    public String getShelvingLocation() {
        return shelvingLocation;
    }

    public void setShelvingLocation(String shelvingLocation) {
        this.shelvingLocation = shelvingLocation;
    }

    public String getLocalLocation() {
        return localLocation;
    }

    public void setLocalLocation(String localLocation) {
        this.localLocation = localLocation;
    }
}
