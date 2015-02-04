package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;


/**
 * Created by chenchulakshmig on 11/11/14.
 */
public class OLEProblemType extends PersistableBusinessObjectBase {

    private String problemTypeId;

    private String problemTypeName;

    private String problemTypeDesc;

    private boolean active;

    public String getProblemTypeId() {
        return problemTypeId;
    }

    public void setProblemTypeId(String problemTypeId) {
        this.problemTypeId = problemTypeId;
    }

    public String getProblemTypeName() {
        return problemTypeName;
    }

    public void setProblemTypeName(String problemTypeName) {
        this.problemTypeName = problemTypeName;
    }

    public String getProblemTypeDesc() {
        return problemTypeDesc;
    }

    public void setProblemTypeDesc(String problemTypeDesc) {
        this.problemTypeDesc = problemTypeDesc;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
