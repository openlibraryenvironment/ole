/*
 * Copyright 2011 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl2.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.document;
/**  This class is to assigning Default Value (Which are the specified in OleDefaultValue Maintenance document) to the specific field based on object
 *
 */

import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OleDefaultTableColumn;
import org.kuali.ole.select.businessobject.OleDefaultValue;
import org.kuali.ole.select.constants.OleSelectPropertyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.datadictionary.AttributeDefinition;
import org.kuali.rice.krad.datadictionary.BusinessObjectEntry;
import org.kuali.rice.krad.datadictionary.DocumentEntry;
import org.kuali.rice.krad.document.TransactionalDocumentBase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.util.GlobalVariables;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

//import org.kuali.rice.kim.impl.role.RoleBo;

public class OleDefaultValueAssignment {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleDefaultValueAssignment.class);

    private Object businessObject = null;

    private Object documentObject = null;

    private static List<OleDefaultTableColumn> defaultTableColumnList = null;

    private List<Field> businessFields = null;

    private List<Field> dictionaryFields = null;

    public OleDefaultValueAssignment() {
    }

    public OleDefaultValueAssignment(String docName, Object businessObject, Object documentObject) {

        if (docName != null) {
            setDefaultTableColumnList(docName);
        }
        if (businessObject != null) {
            this.businessObject = businessObject;
            businessFields = getFields(businessObject.getClass());
            this.documentObject = null;
            setDefaultValues();
        }
        if (documentObject != null) {
            this.documentObject = documentObject;
            dictionaryFields = getFields(documentObject.getClass());
            this.businessObject = null;
            setDefaultValues();
        }


    }

    /**
     * This method to retrieve the fields(properties) according to class parameter
     *
     * @param Class type
     * @return List of fields
     */
    public List<Field> getFields(Class type) {
        LOG.debug(" getFields Mehtod-----> Start");
        List<Field> result = new ArrayList<Field>();
        Class className = type;
        while (className != PersistableBusinessObjectBase.class && className != TransactionalDocumentBase.class && className != null) {
            for (Field field : className.getDeclaredFields()) {
                result.add(field);
            }
            className = className.getSuperclass();
        }
        LOG.debug(" getFields Method-------> End");
        if (result.size() != 0)
            return result;
        else
            return null;
    }

    /**
     * This method to set the default values according to either Business Object or Document Object
     */
    public void setDefaultValues() {

        LOG.debug(" setDefaultValues method ----->  Start");
        String userId = null;
        String documentColumn = null;
        Object object = null;
        Class c = null;
        Class fieldType = null;
        Map<String, Object> defaultTableColumnIdMap = new HashMap<String, Object>();
        DataDictionaryService dataDictionaryService;
        BusinessObjectEntry businessObjectEntry = null;
        DocumentEntry dataDictionaryEntry = null;
        AttributeDefinition attributeDefinition = null;
        List<Field> tempFields = null;
        try {
            dataDictionaryService = SpringContext.getBean(DataDictionaryService.class);
            if (businessObject != null) {
                businessObjectEntry = dataDictionaryService.getDataDictionary().getBusinessObjectEntry(businessObject.getClass().getName());
                tempFields = businessFields;
            } else if (documentObject != null) {
                dataDictionaryEntry = (DocumentEntry) dataDictionaryService.getDataDictionary().getDictionaryObjectEntry(documentObject.getClass().getName());
                tempFields = dictionaryFields;
            }
            for (int j = 0; j < defaultTableColumnList.size(); j++) {
                String defaultValue = null;
                documentColumn = defaultTableColumnList.get(j).getDocumentColumn();
                Field field = null;
                for (int i = 0; i < tempFields.size(); i++) {
                    field = (Field) tempFields.get(i);
                    if (businessObjectEntry != null) {
                        attributeDefinition = businessObjectEntry.getAttributeDefinition(field.getName());
                    } else if (dataDictionaryEntry != null) {
                        attributeDefinition = dataDictionaryEntry.getAttributeDefinition(field.getName());
                    }
                    if (attributeDefinition != null) {
                        String label = attributeDefinition.getLabel();
                        String shortLabel = attributeDefinition.getShortLabel();

                        if (documentColumn.equalsIgnoreCase(label) || documentColumn.equalsIgnoreCase(shortLabel)) {
                            BigDecimal defaultTableColumnId = defaultTableColumnList.get(j).getDefaultTableColumnId();
                            defaultTableColumnIdMap.put("defaultTableColumnId", defaultTableColumnId);
                            List<OleDefaultValue> defaultValueList = (List) SpringContext.getBean(BusinessObjectService.class).findMatching(OleDefaultValue.class, defaultTableColumnIdMap);
                            if (defaultValueList.size() > 0) {
                                userId = GlobalVariables.getUserSession().getPrincipalId();
                                for (int k = 0; k < defaultValueList.size(); k++) {
                                    if (defaultValueList.get(k).getDefaultValueFor().equalsIgnoreCase(OleSelectConstant.DEFAULT_VALUE_USER)) {
                                        if (userId.equalsIgnoreCase(defaultValueList.get(k).getUserId())) {
                                            defaultValue = defaultValueList.get(k).getDefaultValue();
                                        }
                                    }
                                }
                                if (defaultValue == null) {
                                    boolean roleFlag = true;
                                    for (int k = 0; k < defaultValueList.size(); k++) {
                                        if (defaultValueList.get(k).getDefaultValueFor().equalsIgnoreCase(OleSelectConstant.DEFAULT_VALUE_ROLE)) {
                                            List<String> roleImpl = getRolesForPrincipal(userId);
                                            Iterator itr = roleImpl.iterator();
                                            while (itr.hasNext()) {
                                                String kimrole = itr.next().toString();
                                                if (kimrole.equalsIgnoreCase(defaultValueList.get(k).getRoleId()) && roleFlag) {
                                                    defaultValue = defaultValueList.get(k).getDefaultValue();
                                                }
                                            }
                                        }
                                    }
                                    if (defaultValue == null) {
                                        for (int k = 0; k < defaultValueList.size(); k++) {
                                            if (defaultValueList.get(k).getDefaultValueFor().equalsIgnoreCase(OleSelectConstant.DEFAULT_VALUE_SYSTEM)) {
                                                defaultValue = defaultValueList.get(k).getDefaultValue();
                                            }
                                        }
                                    }
                                }
                            }


                            if (businessObject != null) {
                                c = businessObject.getClass();
                                fieldType = field.getType();
                                if (fieldType != null)
                                    object = businessObject;
                            } else if (documentObject != null) {
                                c = documentObject.getClass();
                                fieldType = field.getType();
                                if (fieldType != null)
                                    object = documentObject;
                            }
                            if (fieldType != null && defaultValue != null) {
                                String[] fieldType1 = fieldType.toString().split("\\.");
                                String fieldType2 = fieldType1[fieldType1.length - 1];
                                String field1 = field.getName();
                                char initialCaps = field1.toUpperCase().charAt(0);
                                String fieldNameRemaining = field1.substring(1, field1.length());
                                String method = OleSelectPropertyConstants.SET + initialCaps + fieldNameRemaining;
                                Class[] params = new Class[]{fieldType};
                                Object[] args;
                                if (fieldType2.equalsIgnoreCase("KualiDecimal")) {
                                    args = new Object[]{new KualiDecimal(defaultValue)};
                                } else if (fieldType2.equalsIgnoreCase("BigDecimal")) {
                                    args = new Object[]{new BigDecimal(defaultValue)};
                                } else if (fieldType2.equalsIgnoreCase("String")) {
                                    args = new Object[]{new String(defaultValue)};
                                } else if (fieldType2.equalsIgnoreCase("Integer")) {
                                    args = new Object[]{new Integer(defaultValue)};
                                } else if (fieldType2.equalsIgnoreCase("Long")) {
                                    args = new Object[]{new Long(defaultValue)};
                                } else if (fieldType2.equalsIgnoreCase("double")) {
                                    args = new Object[]{new Double(defaultValue)};
                                } else {
                                    args = new Object[]{new Float(defaultValue)};
                                }

                                invoke(c, method, params, args, object);

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Exception while setting default values"+e);
            throw new RuntimeException(e);
        }
        LOG.debug(" setDefaultValues method ----->  End");

    }

    /**
     * This method performs to retrieve the roles based on the principalId
     *
     * @param principalId
     * @return List of roles
     */
    @SuppressWarnings("unchecked")
    private List<String> getRolesForPrincipal(String principalId) {
        LOG.debug(" getRolesForPrincipal method ----->  Start");
        if (principalId == null) {
            return new ArrayList<String>();
        }
        //  Map<String,String> criteria = new HashMap<String,String>( 2 );
        //  criteria.put("members.memberId", principalId);
        //  criteria.put("members.memberTypeCode", MemberType.PRINCIPAL.getCode());
        LOG.debug(" getRolesForPrincipal method ----->  End");
        // return (List<RoleBo>)SpringContext.getBean(BusinessObjectService.class).findMatching(RoleBo.class, criteria);
        return (List<String>) KimApiServiceLocator.getRoleService().getMemberParentRoleIds(MemberType.PRINCIPAL.getCode(), principalId);
    }

    /**
     * This method to set default values according to given parameters using reflections
     *
     * @param className
     * @param method
     * @param params
     * @param args
     * @param object
     */
    private void invoke(Class className, String method, Class[] params, Object[] args, Object object) {
        //Object i=null;
        LOG.debug(" invoke method ----->  Start");
        try {
            Method m = className.getMethod(method, params);
            Object r = m.invoke(object, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOG.debug(" invoke method ----->  End");
    }

    /**
     * This method to retrieve the default values(Which are in DataBase) using DocumentTypeName(as a parameter)
     *
     * @param docName
     */
    public void setDefaultTableColumnList(String docName) {
        LOG.debug(" setDefaultTableColumnList method ----->  Start");
        Map<String, Object> defaultTableColumnMap = new HashMap<String, Object>();
        Map<String, Object> documentTypes = new HashMap<String, Object>();
        documentTypes.put("name", docName);
        Collection<DocumentType> documentTypeList = SpringContext.getBean(BusinessObjectService.class).findMatching(DocumentType.class, documentTypes);
        String documentTypeId = documentTypeList.iterator().next().getDocumentTypeId();
        defaultTableColumnMap.put("documentTypeId", documentTypeId);
        defaultTableColumnList = (List) SpringContext.getBean(BusinessObjectService.class).findMatching(OleDefaultTableColumn.class, defaultTableColumnMap);
        LOG.debug(" setDefaultTableColumnList method ----->  End");
    }


}


