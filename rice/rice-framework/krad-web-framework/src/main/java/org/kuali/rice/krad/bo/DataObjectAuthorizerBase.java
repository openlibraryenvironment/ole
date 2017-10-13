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
package org.kuali.rice.krad.bo;

import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.util.KRADUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DataObjectAuthorizerBase implements DataObjectAuthorizer, Serializable {
    private static final long serialVersionUID = 3987953326458974964L;

    /**
     * @see DataObjectAuthorizer#isAuthorized(java.lang.Object, java.lang.String, java.lang.String, java.lang.String)
     */
    public final boolean isAuthorized(Object dataObject, String namespaceCode, String permissionName,
            String principalId) {
        return getPermissionService().isAuthorized(principalId, namespaceCode, permissionName,
                new HashMap<String, String>(getRoleQualification(dataObject, principalId)));
    }

    /**
     * @see DataObjectAuthorizer#isAuthorizedByTemplate(java.lang.Object, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    public final boolean isAuthorizedByTemplate(Object dataObject, String namespaceCode, String permissionTemplateName,
            String principalId) {
        return getPermissionService().isAuthorizedByTemplate(principalId, namespaceCode, permissionTemplateName,
                new HashMap<String, String>(getPermissionDetailValues(dataObject)), new HashMap<String, String>(
                (getRoleQualification(dataObject, principalId))));
    }

    /**
     * @see DataObjectAuthorizer#isAuthorized(java.lang.Object, java.lang.String, java.lang.String, java.lang.String)
     */
    public final boolean isAuthorized(Object dataObject, String namespaceCode, String permissionName,
            String principalId, Map<String, String> collectionOrFieldLevelPermissionDetails,
            Map<String, String> collectionOrFieldLevelRoleQualification) {
        Map<String, String> roleQualifiers;
        Map<String, String> permissionDetails;
        if (collectionOrFieldLevelRoleQualification != null) {
            roleQualifiers = new HashMap<String, String>(getRoleQualification(dataObject, principalId));
            roleQualifiers.putAll(collectionOrFieldLevelRoleQualification);
        } else {
            roleQualifiers = new HashMap<String, String>(getRoleQualification(dataObject, principalId));
        }

        if (collectionOrFieldLevelPermissionDetails != null) {
            permissionDetails = new HashMap<String, String>(getPermissionDetailValues(dataObject));
            permissionDetails.putAll(collectionOrFieldLevelPermissionDetails);
        } else {
            permissionDetails = new HashMap<String, String>(getPermissionDetailValues(dataObject));
        }

        return getPermissionService().isAuthorized(principalId, namespaceCode, permissionName, roleQualifiers);
    }

    /**
     * @see DataObjectAuthorizer#isAuthorizedByTemplate(java.lang.Object, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    public final boolean isAuthorizedByTemplate(Object dataObject, String namespaceCode, String permissionTemplateName,
            String principalId, Map<String, String> collectionOrFieldLevelPermissionDetails,
            Map<String, String> collectionOrFieldLevelRoleQualification) {
        Map<String, String> roleQualifiers = new HashMap<String, String>(getRoleQualification(dataObject, principalId));
        Map<String, String> permissionDetails = new HashMap<String, String>(getPermissionDetailValues(dataObject));

        if (collectionOrFieldLevelRoleQualification != null) {
            roleQualifiers.putAll(collectionOrFieldLevelRoleQualification);
        }

        if (collectionOrFieldLevelPermissionDetails != null) {
            permissionDetails.putAll(collectionOrFieldLevelPermissionDetails);
        }

        return getPermissionService().isAuthorizedByTemplate(principalId, namespaceCode, permissionTemplateName,
                permissionDetails, roleQualifiers);
    }

    /**
     * Override this method to populate the role qualifier attributes from the
     * primary data object or document. This will only be called once per
     * request.
     *
     * @param primaryDataObjectOrDocument - the primary data object (i.e. the main object instance
     * behind the lookup result row or inquiry) or the document
     * @param attributes - role qualifiers will be added to this map
     */
    protected void addRoleQualification(Object primaryDataObjectOrDocument, Map<String, String> attributes) {
        addStandardAttributes(primaryDataObjectOrDocument, attributes);
    }

    /**
     * Override this method to populate the permission details from the primary
     * data object or document. This will only be called once per request.
     *
     * @param primaryDataObjectOrDocument - the primary data object (i.e. the main object instance
     * behind the lookup result row or inquiry) or the document
     * @param attributes - permission details will be added to this map
     */
    protected void addPermissionDetails(Object primaryDataObjectOrDocument, Map<String, String> attributes) {
        addStandardAttributes(primaryDataObjectOrDocument, attributes);
    }

    /**
     * @param primaryDataObjectOrDocument - the primary data object (i.e. the main object instance
     * behind the lookup result row or inquiry) or the document
     * @param attributes - attributes (i.e. role qualifications or permission details)
     * will be added to this map
     */
    private void addStandardAttributes(Object primaryDataObjectOrDocument, Map<String, String> attributes) {
        attributes.putAll(KRADUtils.getNamespaceAndComponentSimpleName(primaryDataObjectOrDocument.getClass()));
    }

    protected final boolean permissionExistsByTemplate(Object dataObject, String namespaceCode,
            String permissionTemplateName) {
        return getPermissionService().isPermissionDefinedByTemplate(namespaceCode, permissionTemplateName,
                new HashMap<String, String>(getPermissionDetailValues(dataObject)));
    }

    protected final boolean permissionExistsByTemplate(String namespaceCode, String permissionTemplateName,
            Map<String, String> permissionDetails) {
        return getPermissionService().isPermissionDefinedByTemplate(namespaceCode, permissionTemplateName,
                new HashMap<String, String>(permissionDetails));
    }

    protected final boolean permissionExistsByTemplate(Object dataObject, String namespaceCode,
            String permissionTemplateName, Map<String, String> permissionDetails) {
        Map<String, String> combinedPermissionDetails = new HashMap<String, String>(getPermissionDetailValues(
                dataObject));
        combinedPermissionDetails.putAll(permissionDetails);

        return getPermissionService().isPermissionDefinedByTemplate(namespaceCode, permissionTemplateName,
                combinedPermissionDetails);
    }

    /**
     * Returns a role qualification map based off data from the primary business
     * object or the document. DO NOT MODIFY THE MAP RETURNED BY THIS METHOD
     *
     * @param primaryDataObjectOrDocument the primary data object (i.e. the main object instance behind
     * the lookup result row or inquiry) or the document
     * @return a Map containing role qualifications
     */
    protected final Map<String, String> getRoleQualification(Object primaryDataObjectOrDocument, String principalId) {
        Map<String, String> roleQualification = new HashMap<String, String>();
        addRoleQualification(primaryDataObjectOrDocument, roleQualification);
        roleQualification.put(KimConstants.AttributeConstants.PRINCIPAL_ID, principalId);

        return roleQualification;
    }

    /**
     * Returns a permission details map based off data from the primary business
     * object or the document. DO NOT MODIFY THE MAP RETURNED BY THIS METHOD
     *
     * @param primaryDataObjectOrDocument the primary data object (i.e. the main object instance behind
     * the lookup result row or inquiry) or the document
     * @return a Map containing permission details
     */
    protected final Map<String, String> getPermissionDetailValues(Object primaryDataObjectOrDocument) {
        Map<String, String> permissionDetails = new HashMap<String, String>();
        addPermissionDetails(primaryDataObjectOrDocument, permissionDetails);

        return permissionDetails;
    }

    protected static PermissionService getPermissionService() {
        return KimApiServiceLocator.getPermissionService();
    }

    protected static PersonService getPersonService() {
        return KimApiServiceLocator.getPersonService();
    }
}
