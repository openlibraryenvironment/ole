package org.kuali.ole.deliver.calendar.bo;

import org.kuali.ole.alert.bo.AlertField;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import java.util.LinkedHashMap;
/**
 * Created with IntelliJ IDEA.
 * User: dileepp
 * Date: 12/3/13
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleCalendarExceptionType extends PersistableBusinessObjectBase {
    private String exceptionTypeId;
    @AlertField
    private String exceptionTypeCode;
    private String exceptionTypeName;
    private boolean active;

    public String getExceptionTypeId() {
        return exceptionTypeId;
    }

    public void setExceptionTypeId(String exceptionTypeId) {
        this.exceptionTypeId = exceptionTypeId;
    }

    public String getExceptionTypeCode() {
        return exceptionTypeCode;
    }

    public void setExceptionTypeCode(String exceptionTypeCode) {
        this.exceptionTypeCode = exceptionTypeCode;
    }

    public String getExceptionTypeName() {
        return exceptionTypeName;
    }

    public void setExceptionTypeName(String exceptionTypeName) {
        this.exceptionTypeName = exceptionTypeName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("exceptionTypeId", exceptionTypeId);
        toStringMap.put("active", active);
        return toStringMap;
    }
}
