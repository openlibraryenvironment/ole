package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: anithaa
 * Date: 2/11/14
 * Time: 6:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class CopyFormat extends PersistableBusinessObjectBase {
    private String copyFormatId;
    private String code;
    private String name;
    private boolean active;

    public String getCopyFormatId() {
        return copyFormatId;
    }

    public void setCopyFormatId(String copyFormatId) {
        this.copyFormatId = copyFormatId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the value of active property
     *
     * @return active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the value for active property
     *
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }


}
