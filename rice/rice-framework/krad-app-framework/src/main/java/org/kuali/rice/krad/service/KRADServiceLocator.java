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
package org.kuali.rice.krad.service;

import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.framework.persistence.platform.DatabasePlatform;

import javax.persistence.EntityManagerFactory;

/**
 * Service locator for the KRAD App Module
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KRADServiceLocator {
    public static final String ATTACHMENT_SERVICE = "attachmentService";
    public static final String PERSISTENCE_SERVICE = "persistenceService";
    public static final String PERSISTENCE_STRUCTURE_SERVICE = "persistenceStructureService";
    public static final String NOTE_SERVICE = "noteService";
    public static final String BUSINESS_OBJECT_SERVICE = "businessObjectService";
    public static final String ENTITY_MANAGER_FACTORY = "entityManagerFactory";
    public static final String APPLICATION_ENTITY_MANAGER_FACTORY = "kradApplicationEntityManagerFactory";
    public static final String XML_OBJECT_SERIALIZER_SERVICE = "xmlObjectSerializerService";
    public static final String XML_OBJECT_SERIALIZER_IGNORE_MISSING_FIELDS_SERVICE =
            "xmlObjectSerializerIgnoreMissingFieldsService";
    public static final String SERIALIZER_SERVICE = "businessObjectSerializerService";
    public static final String SEQUENCE_ACCESSOR_SERVICE = "sequenceAccessorService";
    public static final String KEY_VALUES_SERVICE = "keyValuesService";
    public static final String MAIL_SERVICE = "mailService";
    public static final String DB_PLATFORM = "dbPlatform";
    public static final String INACTIVATEABLE_FROM_TO_SERVICE = "inactivateableFromToService";

    static <T> T getService(String serviceName) {
        return GlobalResourceLoader.<T>getService(serviceName);
    }

    public static AttachmentService getAttachmentService() {
        return getService(ATTACHMENT_SERVICE);
    }

    public static PersistenceService getPersistenceService() {
        return getService(PERSISTENCE_SERVICE);
    }

    public static PersistenceStructureService getPersistenceStructureService() {
        return getService(PERSISTENCE_STRUCTURE_SERVICE);
    }

    public static NoteService getNoteService() {
        return getService(NOTE_SERVICE);
    }

    public static BusinessObjectService getBusinessObjectService() {
        return getService(BUSINESS_OBJECT_SERVICE);
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return getService(ENTITY_MANAGER_FACTORY);
    }

    public static EntityManagerFactory getApplicationEntityManagerFactory() {
        return getService(APPLICATION_ENTITY_MANAGER_FACTORY);
    }

    public static XmlObjectSerializerService getXmlObjectSerializerService() {
        return getService(XML_OBJECT_SERIALIZER_SERVICE);
    }

    public static XmlObjectSerializerService getXmlObjectSerializerIgnoreMissingFieldsService() {
        return getService(XML_OBJECT_SERIALIZER_IGNORE_MISSING_FIELDS_SERVICE);
    }

    public static BusinessObjectSerializerService getBusinessObjectSerializerService() {
        return getService(SERIALIZER_SERVICE);
    }

    public static SequenceAccessorService getSequenceAccessorService() {
        return getService(SEQUENCE_ACCESSOR_SERVICE);
    }

    public static KeyValuesService getKeyValuesService() {
        return getService(KEY_VALUES_SERVICE);
    }

    public static final MailService getMailService() {
        return (MailService) getService(MAIL_SERVICE);
    }

    public static DatabasePlatform getDatabasePlatform() {
        return (DatabasePlatform) getService(DB_PLATFORM);
    }

    public static InactivateableFromToService getInactivateableFromToService() {
        return (InactivateableFromToService) getService(INACTIVATEABLE_FROM_TO_SERVICE);
    }

}