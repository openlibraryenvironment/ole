/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.kew.impl.service;

import org.joda.time.DateTime;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.PredicateUtils;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.doctype.DocumentTypeService;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.docsearch.DocumentSearchCriteriaEbo;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.doctype.bo.DocumentTypeEBO;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.group.GroupContract;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.role.RoleContract;
import org.kuali.rice.kim.framework.group.GroupEbo;
import org.kuali.rice.kim.framework.role.RoleEbo;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;
import org.kuali.rice.krad.service.impl.RemoteModuleServiceBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class KewRemoteModuleService extends RemoteModuleServiceBase {

    protected DocumentTypeService docTypeService = null;

    /**
     * This overridden method calls the DocumentTypeService instead of the underlying
     * KNS service.  Allows you to search on name and docTypeId
     *
     * @see org.kuali.rice.krad.service.impl.ModuleServiceBase#getExternalizableBusinessObject(java.lang.Class, java.util.Map)
     */
    @Override
    public <T extends ExternalizableBusinessObject> T getExternalizableBusinessObject(
            Class<T> businessObjectClass, Map<String, Object> fieldValues) {
        if(DocumentTypeEBO.class.isAssignableFrom(businessObjectClass)){
            if ( fieldValues.containsKey( "name" ) ) {
                return (T) DocumentType.from(getDocumentTypeService().getDocumentTypeByName((String) fieldValues.get(
                        "name")));
            } else if( fieldValues.containsKey( "documentTypeId" ) ){
                return (T) DocumentType.from(getDocumentTypeService().getDocumentTypeById(fieldValues.get("documentTypeId").toString()));
            } else if (fieldValues.containsKey( "id" ) ) {
                // assume it's a string and convert it to a long.
                return (T) DocumentType.from(getDocumentTypeService().getDocumentTypeById(fieldValues.get("id").toString()));
            }

        } else if(DocumentSearchCriteriaEbo.class.isAssignableFrom( businessObjectClass )) {
            if ( fieldValues.containsKey( "documentId" ) ) {
                return (T)createDocumentSearchEbo(KewApiServiceLocator.getWorkflowDocumentService().getDocument(
                        fieldValues.get("documentId").toString()));
            }

        }

        return null;
    }

    private DocumentSearchCriteriaEbo createDocumentSearchEbo(final Document doc){
        return new DocumentSearchCriteriaEbo(){

            @Override
            public String getApplicationDocumentId() {
                return doc.getApplicationDocumentId();
            }

            @Override
            public DocumentStatus getStatus() {
                return doc.getStatus();
            }

            @Override
            public String getApplicationDocumentStatus() {
                return doc.getApplicationDocumentStatus();
            }

            @Override
            public String getTitle() {
                return doc.getTitle();
            }

            @Override
            public String getDocumentTypeName() {
                return doc.getDocumentTypeName();
            }

            @Override
            public String getInitiatorPrincipalId() {
                return doc.getInitiatorPrincipalId();
            }

            @Override
            public String getDocumentId() {
                return doc.getDocumentId();
            }

            @Override
            public DateTime getDateCreated() {
                return doc.getDateCreated();
            }

            @Override
            public void refresh() {
                // do nothing
            }

        };
    }

    @Override
    public <T extends ExternalizableBusinessObject> List<T> getExternalizableBusinessObjectsList(
            Class<T> businessObjectClass, Map<String, Object> fieldValues) {
        if (!fieldValues.isEmpty()) {
            throw new IllegalStateException("fieldValues must be empty");
        }
        List<T> ebos = new ArrayList<T>();
        if(DocumentTypeEBO.class.isAssignableFrom(businessObjectClass)){
            List<org.kuali.rice.kew.api.doctype.DocumentType> docTypes = getDocumentTypeService().findAllDocumentTypes();

            for (org.kuali.rice.kew.api.doctype.DocumentType docType : docTypes) {
                ebos.add((T)DocumentType.from(docType));
            }
            return ebos;
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isExternalizableBusinessObjectLookupable(Class boClass) {
        return isExternalizable(boClass);
    }

    @Override
    public boolean isExternalizableBusinessObjectInquirable(Class boClass) {
        return isExternalizable(boClass);
    }

    @Override
    public boolean isExternalizable(Class boClazz) {
        if (boClazz == null) {
            return false;
        }
        if(DocumentTypeEBO.class.isAssignableFrom(boClazz)) {
            return true;
        } else if(DocumentSearchCriteriaEbo.class.isAssignableFrom(boClazz)) {
            return true;
        }
        return ExternalizableBusinessObject.class.isAssignableFrom(boClazz);
    }

    @Override
    public List<String> listPrimaryKeyFieldNames(Class boClass) {
        if ( DocumentTypeEBO.class.isAssignableFrom( boClass ) ) {
            List<String> pkFields = new ArrayList<String>( 1 );
            pkFields.add( "documentTypeId" );
            return pkFields;
        }else if(DocumentSearchCriteriaEbo.class.isAssignableFrom( boClass )){
            List<String> pkFields = new ArrayList<String>( 1 );
            pkFields.add( "documentId" );
            return pkFields;
        }
        return Collections.emptyList();
    }

    /**
     * We want to be able to use name as an alternate key
     *
     * @see org.kuali.rice.krad.service.ModuleService#listAlternatePrimaryKeyFieldNames(java.lang.Class)
     */
    public List<List<String>> listAlternatePrimaryKeyFieldNames(
            Class businessObjectInterfaceClass) {
        if ( DocumentTypeEBO.class.isAssignableFrom( businessObjectInterfaceClass ) ) {
            ArrayList<List<String>> retList = new ArrayList<List<String>>();
            ArrayList<String> keyList = new ArrayList<String>();

            keyList.add("name");
            retList.add(keyList);
            return retList;
        }else{
            return null;
        }

    }

    /**
     * @return the docTypeService
     */
    protected synchronized DocumentTypeService getDocumentTypeService() {
        if(this.docTypeService == null){
            // the default
            this.docTypeService = KewApiServiceLocator.getDocumentTypeService();
        }
        return this.docTypeService;
    }
}
