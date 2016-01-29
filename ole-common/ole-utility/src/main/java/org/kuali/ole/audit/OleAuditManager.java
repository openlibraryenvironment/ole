package org.kuali.ole.audit;

import org.javers.core.diff.changetype.ValueChange;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by pvsubrah on 11/5/15.
 */
public class OleAuditManager {
    private static OleAuditManager oleAuditManager;
    private static Map<String,List<String>> auditFieldMap ;
    private BusinessObjectService businessObjectService;

    private OleAuditManager() {

    }

    public static OleAuditManager getInstance() {
        synchronized (OleAuditManager.class) {
            if (null == oleAuditManager) {
                oleAuditManager = new OleAuditManager();
                auditFieldMap = new HashMap<String,List<String>>();
            }
        }
        return oleAuditManager;
    }


    public List<Audit> audit(Class tClass, Object existing, Object updated, String foreignKeyRefId, String actor) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        List<Audit> audits = new ArrayList();
        List<ValueChange> diff = ObjectDiffer.getInstance().diff(existing, updated);
        List<String> auditFields;
        if(!auditFieldMap.containsKey(existing.getClass().getName())){
            auditFields = getAuditFields(existing);
            auditFieldMap.put(existing.getClass().getCanonicalName(),auditFields);
        }
        auditFields = auditFieldMap.get(existing.getClass().getCanonicalName());

        for (Iterator<ValueChange> iterator = diff.iterator(); iterator.hasNext(); ) {
            ValueChange valueChange = iterator.next();
            String propertyName = valueChange.getPropertyName();
            if(auditFields.contains(propertyName)){
                Object left = valueChange.getLeft();
                Object right = valueChange.getRight();
                Class<?> aClass = Class.forName(tClass.getName());
                Audit audit = (Audit) aClass.newInstance();
                audit.setActor(actor);
                audit.setForeignKeyRef(foreignKeyRefId);
                audit.setColumnUpdated(propertyName);
                if(right!=null && right.toString()!=null){
                audit.setColumnValue(right.toString().getBytes());
                }
                audit.setUpdateDate(new Timestamp(System.currentTimeMillis()));
                audits.add(audit);
            }

            getBusinessObjectService().save(audits);
        }


        return audits;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public  List<String> getAuditFields(Object object){
        List<String> auditFields = new ArrayList<String>();
        Field[] fields = object.getClass().getDeclaredFields();
        Annotation[] annotations;
        for(Field field :fields ){
            annotations = field.getDeclaredAnnotations();
            for(Annotation annotation : annotations){
                if(annotation instanceof AuditField){
                    auditFields.add(field.getName());
                }
            }
        }

        return auditFields;
    }
}
