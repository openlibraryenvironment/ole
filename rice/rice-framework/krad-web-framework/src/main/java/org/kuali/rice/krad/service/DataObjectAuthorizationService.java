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

import org.kuali.rice.kim.api.identity.Person;

/**
 * Provides methods for checking authorization for actions
 * on a given data object class including the security of fields
 * within the class
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DataObjectAuthorizationService {

    /**
     * Indicates whether the given attribute of the given data object class has any
     * security defined (such as read-only, masked, ...) and therefore data for the
     * attribute should be securely passed
     *
     * @param dataObjectClass - class that contains the attribute
     * @param attributeName - name of the attribute (property) within the class
     * @return boolean true if the attribute should be secured, false if security is not needed
     */
    public boolean attributeValueNeedsToBeEncryptedOnFormsAndLinks(Class<?> dataObjectClass, String attributeName);

    /**
     * Indicates whether the given user has permission to create records of the given data
     * object class with the given document type
     *
     * @param dataObjectClass - class of data object to check authorization for
     * @param user - person requesting action
     * @param docTypeName - name of the document type that provides the action
     * @return boolean true if the user has create authorization, false if not
     */
    public boolean canCreate(Class<?> dataObjectClass, Person user, String docTypeName);

    /**
     * Indicates whether the given user has permission to maintain (edit/delete) the
     * give data object instance with the given document type
     *
     * @param dataObject - data object instance to check authorization for
     * @param user - person requesting action
     * @param docTypeName - name of the document type that provides the action
     * @return boolean true if the user has maintain authorization, false if not
     */
    public boolean canMaintain(Object dataObject, Person user, String docTypeName);
}
