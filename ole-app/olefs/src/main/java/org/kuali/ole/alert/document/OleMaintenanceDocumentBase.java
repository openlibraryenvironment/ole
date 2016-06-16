package org.kuali.ole.alert.document;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.alert.bo.AlertBo;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.doctype.DocumentType;
import org.kuali.rice.krad.maintenance.Maintainable;
import org.kuali.rice.krad.maintenance.MaintenanceDocumentBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.KRADConstants;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Arrays;
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


    public Object getDataObjectFromXML(String maintainableTagName,String xmlDocumentContents) {
        String maintXml = StringUtils.substringBetween(xmlDocumentContents, "<" + maintainableTagName + ">",
                "</" + maintainableTagName + ">");

        boolean ignoreMissingFields = false;
        String classAndDocTypeNames = ConfigContext.getCurrentContextConfig().getProperty(KRADConstants.Config.IGNORE_MISSIONG_FIELDS_ON_DESERIALIZE);
        if (!StringUtils.isEmpty(classAndDocTypeNames)) {
            String classNameOnXML = StringUtils.substringBetween(xmlDocumentContents, "<" + maintainableTagName + "><", ">");
            String classNamesNoSpaces = removeSpacesAround(classAndDocTypeNames);
            List<String> classAndDocTypeNamesList = Arrays.asList(org.apache.commons.lang.StringUtils.split(classNamesNoSpaces, ","));
            String originalDocTypeId = getDocumentHeader().getWorkflowDocument().getDocumentTypeId();
            DocumentType docType = KewApiServiceLocator.getDocumentTypeService().getDocumentTypeById(originalDocTypeId);

            while (docType != null && !ignoreMissingFields) {
                for(String classNameOrDocTypeName : classAndDocTypeNamesList){
                    if (docType.getName().equalsIgnoreCase(classNameOrDocTypeName) ||
                            classNameOnXML.equalsIgnoreCase(classNameOrDocTypeName)) {
                        ignoreMissingFields = true;
                        break;
                    }
                }
                if (!StringUtils.isEmpty(docType.getParentId())) {
                    docType = KewApiServiceLocator.getDocumentTypeService().getDocumentTypeById(docType.getParentId());
                } else {
                    docType = null;
                }
            }
        }
        if (!ignoreMissingFields) {
            return KRADServiceLocator.getXmlObjectSerializerService().fromXml(maintXml);
        } else {
            return KRADServiceLocator.getXmlObjectSerializerIgnoreMissingFieldsService().fromXml(maintXml);
        }
    }

    private String removeSpacesAround(String csv) {
        if (csv == null) {
            return null;
        }

        final StringBuilder result = new StringBuilder();
        for (final String value : csv.split(",")) {
            if (!"".equals(value.trim())) {
                result.append(value.trim());
                result.append(",");
            }
        }

        //remove trailing comma
        int i = result.lastIndexOf(",");
        if (i != -1) {
            result.deleteCharAt(i);
        }

        return result.toString();
    }


}
