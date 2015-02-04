package org.kuali.ole.alert.document;

import org.kuali.ole.alert.bo.AlertBo;
import org.kuali.rice.krad.maintenance.Maintainable;
import org.kuali.rice.krad.maintenance.MaintenanceDocumentBase;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maheswarang on 12/18/14.
 */
@MappedSuperclass
@Entity
@Table(name = "KRNS_MAINT_DOC_T")
public class OleMaintenanceDocumentBase extends MaintenanceDocumentBase {
    @Transient
    public List<AlertBo> alertBoList = new ArrayList();

    public List<AlertBo> getAlertBoList() {
        return alertBoList;
    }

    public void setAlertBoList(List<AlertBo> alertBoList) {
        this.alertBoList = alertBoList;
    }

    public OleMaintenanceDocumentBase() {
        super();
        fieldsClearedOnCopy = false;
        alertBoList=new ArrayList<>();
    }

    public OleMaintenanceDocumentBase(String documentTypeName) {
        this();
        Class clazz = getDocumentDictionaryService().getMaintainableClass(documentTypeName);
        try {
            oldMaintainableObject = (Maintainable) clazz.newInstance();
            newMaintainableObject = (Maintainable) clazz.newInstance();

            // initialize maintainable with a data object
            Class<?> dataObjectClazz = getDocumentDictionaryService().getMaintenanceDataObjectClass(documentTypeName);
            oldMaintainableObject.setDataObject(dataObjectClazz.newInstance());
            oldMaintainableObject.setDataObjectClass(dataObjectClazz);
            newMaintainableObject.setDataObject(dataObjectClazz.newInstance());
            newMaintainableObject.setDataObjectClass(dataObjectClazz);
        } catch (InstantiationException e) {
           // LOG.error("Unable to initialize maintainables of type " + clazz.getName());
            throw new RuntimeException("Unable to initialize maintainables of type " + clazz.getName());
        } catch (IllegalAccessException e) {
        //    LOG.error("Unable to initialize maintainables of type " + clazz.getName());
            throw new RuntimeException("Unable to initialize maintainables of type " + clazz.getName());
        }
    }
}
