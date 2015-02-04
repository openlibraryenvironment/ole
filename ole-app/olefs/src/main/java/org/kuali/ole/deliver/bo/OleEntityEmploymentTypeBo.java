package org.kuali.ole.deliver.bo;

/**
 * Created with IntelliJ IDEA.
 * User: divyaj
 * Date: 11/22/13
 * Time: 11:49 AM
 * To change this template use File | Settings | File Templates.
 */


        import javax.persistence.Column;
        import javax.persistence.Entity;
        import javax.persistence.Id;
        import javax.persistence.Table;
        import org.kuali.rice.kim.api.identity.CodedAttribute;
        import org.kuali.rice.kim.api.identity.CodedAttributeContract;
        import org.kuali.rice.kim.framework.identity.employment.EntityEmploymentTypeEbo;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;


public class OleEntityEmploymentTypeBo extends PersistableBusinessObjectBase{

    String code;

    String name;

    boolean active;

    String sortCode;

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }
}
