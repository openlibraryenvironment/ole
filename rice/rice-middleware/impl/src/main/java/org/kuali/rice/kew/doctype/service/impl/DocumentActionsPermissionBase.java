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
package org.kuali.rice.kew.doctype.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kns.document.authorization.DocumentAuthorizerBase;
import org.kuali.rice.krad.datadictionary.DocumentEntry;
import org.kuali.rice.krad.datadictionary.MaintenanceDocumentEntry;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADUtils;

import java.util.*;

/**
 * Base class which implements default KIM permission checks for various workflow document actions.
 * This implementation can be used as a base class for a DocumentTypePermissionService or DocumentTypeAuthorizer implementation
 * @see org.kuali.rice.kew.doctype.service.DocumentTypePermissionService
 * @see DocumentTypePermissionServiceImpl
 * @see org.kuali.rice.kew.framework.document.security.DocumentTypeAuthorizer
 * @see KimDocumentTypeAuthorizer
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 2.1.3
 */
public class DocumentActionsPermissionBase {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DocumentActionsPermissionBase.class);

    /**
     * Implements {@link org.kuali.rice.kew.doctype.service.DocumentTypePermissionService#canInitiate(String, org.kuali.rice.kew.doctype.bo.DocumentType)}
     */
    public boolean canInitiate(String principalId, DocumentType documentType) {
        validatePrincipalId(principalId);
        validateDocumentType(documentType);

        Map<String, String> permissionDetails = buildDocumentTypePermissionDetails(documentType, null, null, null);
        if (useKimPermission(KRADConstants.KUALI_RICE_SYSTEM_NAMESPACE, KewApiConstants.INITIATE_PERMISSION, permissionDetails, true)) {
            return getPermissionService().isAuthorizedByTemplate(principalId, KRADConstants.KUALI_RICE_SYSTEM_NAMESPACE,
                    KewApiConstants.INITIATE_PERMISSION, permissionDetails, new HashMap<String, String>());
        }
        return true;
    }

    /**
     * Implements {@link org.kuali.rice.kew.doctype.service.DocumentTypePermissionService#canRoute(String, org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue)}
     */
    public boolean canRoute(String principalId, DocumentRouteHeaderValue document) {
        validatePrincipalId(principalId);
        validateDocument(document);
        String documentId = document.getDocumentId();
        DocumentType documentType = document.getDocumentType();
        String documentStatus = document.getDocRouteStatus();
        String initiatorPrincipalId = document.getInitiatorWorkflowId();
        validateDocumentType(documentType);
        validateDocumentStatus(documentStatus);
        validatePrincipalId(initiatorPrincipalId);

        if (!documentType.isPolicyDefined(org.kuali.rice.kew.api.doctype.DocumentTypePolicy.INITIATOR_MUST_ROUTE)) {
            Map<String, String> permissionDetails = buildDocumentTypePermissionDetails(documentType, documentStatus, null, null);
            Map<String, String> roleQualifiers = buildDocumentRoleQualifiers(document, permissionDetails.get(KewApiConstants.ROUTE_NODE_NAME_DETAIL));

            if (LOG.isDebugEnabled()) {
                LOG.debug("Permission details values: " + permissionDetails);
                LOG.debug("Role qualifiers values: " + roleQualifiers);
            }
            if (useKimPermission(KewApiConstants.KEW_NAMESPACE, KewApiConstants.ROUTE_PERMISSION, permissionDetails, true)) {
                return getPermissionService().isAuthorizedByTemplate(principalId, KewApiConstants.KEW_NAMESPACE,
                        KewApiConstants.ROUTE_PERMISSION, permissionDetails, roleQualifiers);
            }
        }

        if (documentType.getInitiatorMustRoutePolicy().getPolicyValue()) {
            return executeInitiatorPolicyCheck(principalId, initiatorPrincipalId, documentStatus);
        }
        return true;
    }

    /**
     * Provides base implementaiton for {@link org.kuali.rice.kew.doctype.service.DocumentTypePermissionService#canSuperUserApproveSingleActionRequest(String, org.kuali.rice.kew.doctype.bo.DocumentType, java.util.List, String)}
     */
    protected boolean canSuperUserApproveSingleActionRequest(String principalId, DocumentType documentType,
                                                             Collection<String> routeNodeNames, String routeStatusCode) {
        validatePrincipalId(principalId);
        validateDocumentType(documentType);

        List<Map<String, String>> permissionDetailList = buildDocumentTypePermissionDetailsForNodes(documentType, routeNodeNames, routeStatusCode, null);
        // loop over permission details, only one of them needs to be authorized
        PermissionService permissionService = getPermissionService();
        for (Map<String, String> permissionDetails : permissionDetailList) {
            if (permissionService.isAuthorizedByTemplate(principalId, KewApiConstants.KEW_NAMESPACE,
                    KewApiConstants.SUPER_USER_APPROVE_SINGLE_ACTION_REQUEST, permissionDetails, new HashMap<String, String>())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Implements {@link org.kuali.rice.kew.doctype.service.DocumentTypePermissionService#canSuperUserApproveDocument(String, org.kuali.rice.kew.doctype.bo.DocumentType, java.util.List, String)}
     */
    protected boolean canSuperUserApproveDocument(String principalId, DocumentType documentType,
                                                  Collection<String> routeNodeNames, String routeStatusCode) {
        validatePrincipalId(principalId);
        validateDocumentType(documentType);

        List<Map<String, String>> permissionDetailList = buildDocumentTypePermissionDetailsForNodes(documentType, routeNodeNames, routeStatusCode, null);
        // loop over permission details, only one of them needs to be authorized
        PermissionService permissionService = getPermissionService();
        for (Map<String, String> permissionDetails : permissionDetailList) {
            if (permissionService.isAuthorizedByTemplate(principalId, KewApiConstants.KEW_NAMESPACE,
                    KewApiConstants.SUPER_USER_APPROVE_DOCUMENT, permissionDetails, new HashMap<String, String>())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Implements {@link org.kuali.rice.kew.doctype.service.DocumentTypePermissionService#canSuperUserDisapproveDocument(String, org.kuali.rice.kew.doctype.bo.DocumentType, java.util.List, String)}
     */
    protected boolean canSuperUserDisapproveDocument(String principalId, DocumentType documentType,
                                                     Collection<String> routeNodeNames,String routeStatusCode) {
        validatePrincipalId(principalId);
        validateDocumentType(documentType);

        List<Map<String, String>> permissionDetailList = buildDocumentTypePermissionDetailsForNodes(documentType, routeNodeNames, routeStatusCode, null);
        // loop over permission details, only one of them needs to be authorized
        PermissionService permissionService = getPermissionService();
        for (Map<String, String> permissionDetails : permissionDetailList) {
            if (permissionService.isAuthorizedByTemplate(principalId, KewApiConstants.KEW_NAMESPACE,
                    KewApiConstants.SUPER_USER_DISAPPROVE_DOCUMENT, permissionDetails, new HashMap<String, String>())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Implements {@link org.kuali.rice.kew.doctype.service.DocumentTypePermissionService#canCancel(String, org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue)}
     */
    public boolean canCancel(String principalId, DocumentRouteHeaderValue document) {
        validatePrincipalId(principalId);
        validateDocument(document);
        String documentId = document.getDocumentId();
        DocumentType documentType = document.getDocumentType();
        String documentStatus = document.getDocRouteStatus();
        String initiatorPrincipalId = document.getInitiatorWorkflowId();
        List<String> routeNodeNames = document.getCurrentNodeNames();
        validateDocumentType(documentType);
        validateRouteNodeNames(routeNodeNames);
        validateDocumentStatus(documentStatus);
        validatePrincipalId(initiatorPrincipalId);

        if (!documentType.isPolicyDefined(org.kuali.rice.kew.api.doctype.DocumentTypePolicy.INITIATOR_MUST_CANCEL)) {
            List<Map<String, String>> permissionDetailList = buildDocumentTypePermissionDetailsForNodes(documentType, routeNodeNames, documentStatus, null);

            boolean foundAtLeastOnePermission = false;
            // loop over permission details, only one of them needs to be authorized
            for (Map<String, String> permissionDetails : permissionDetailList) {
                Map<String, String> roleQualifiers = buildDocumentRoleQualifiers(document, permissionDetails.get(KewApiConstants.ROUTE_NODE_NAME_DETAIL));
                if (useKimPermission(KewApiConstants.KEW_NAMESPACE, KewApiConstants.CANCEL_PERMISSION, permissionDetails, true)) {
                    foundAtLeastOnePermission = true;
                    if (getPermissionService().isAuthorizedByTemplate(principalId, KewApiConstants.KEW_NAMESPACE,
                            KewApiConstants.CANCEL_PERMISSION, permissionDetails, roleQualifiers)) {
                        return true;
                    }
                }
            }
            // if we found defined KIM permissions, but not of them have authorized this user, return false
            if (foundAtLeastOnePermission) {
                return false;
            }
        }

        if (documentType.getInitiatorMustCancelPolicy().getPolicyValue()) {
            return executeInitiatorPolicyCheck(principalId, initiatorPrincipalId, documentStatus);
        } else {
            return true;
        }
    }

    /**
     * Implements {@link org.kuali.rice.kew.doctype.service.DocumentTypePermissionService#canRecall(String, org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue)}
     */
    public boolean canRecall(String principalId, DocumentRouteHeaderValue document) {
        validatePrincipalId(principalId);
        validateDocument(document);
        String documentId = document.getDocumentId();
        DocumentType documentType = document.getDocumentType();
        String documentStatus = document.getDocRouteStatus();
        String appDocStatus = document.getAppDocStatus();
        String initiatorPrincipalId = document.getInitiatorWorkflowId();
        List<String> routeNodeNames = document.getCurrentNodeNames();
        validateDocumentType(documentType);
        validateRouteNodeNames(routeNodeNames);
        validateDocumentStatus(documentStatus);
        // no need to validate appdocstatus, this is a free-form application defined value

        // add appDocStatus to the details
        List<Map<String, String>> permissionDetailList = buildDocumentTypePermissionDetailsForNodes(documentType, routeNodeNames, documentStatus, null);
        if (!StringUtils.isBlank(appDocStatus)) {
            for (Map<String, String> details: permissionDetailList) {
                details.put(KewApiConstants.APP_DOC_STATUS_DETAIL, appDocStatus);
            }
        }

        boolean foundAtLeastOnePermission = false;
        boolean authorizedByPermission = false;
        boolean principalIsInitiator = StringUtils.equals(initiatorPrincipalId, principalId);

        // loop over permission details, only one of them needs to be authorized
        for (Map<String, String> permissionDetails : permissionDetailList) {
            Map<String, String> roleQualifiers = buildDocumentRoleQualifiers(document, permissionDetails.get(KewApiConstants.ROUTE_NODE_NAME_DETAIL));
            if (useKimPermission(KewApiConstants.KEW_NAMESPACE, KewApiConstants.RECALL_PERMISSION, permissionDetails, false)) {
                if (getPermissionService().isPermissionDefinedByTemplate(KewApiConstants.KEW_NAMESPACE, KewApiConstants.RECALL_PERMISSION, permissionDetails)) {
                    foundAtLeastOnePermission = true;
                    if (getPermissionService().isAuthorizedByTemplate(principalId, KewApiConstants.KEW_NAMESPACE,
                            KewApiConstants.RECALL_PERMISSION, permissionDetails, roleQualifiers)) {
                        return true;
                    }
                }
            }
        }
        if (foundAtLeastOnePermission) {
            return false;
        }
        // alternative could be to only authorize initiator if the permission is omitted
        // (i.e. exclude initiator if the initiator does not have the recall permission)
        return authorizedByPermission;
    }

    /**
     * Implements {@link org.kuali.rice.kew.doctype.service.DocumentTypePermissionService#canBlanketApprove(String, org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue)}
     */
    public boolean canBlanketApprove(String principalId, DocumentRouteHeaderValue document) {
        validatePrincipalId(principalId);
        validateDocument(document);
        DocumentType documentType = document.getDocumentType();
        String initiatorPrincipalId = document.getInitiatorWorkflowId();
        String documentStatus = document.getDocRouteStatus();
        validateDocumentType(documentType);
        validateDocumentStatus(documentStatus);
        validatePrincipalId(initiatorPrincipalId);
        final Boolean result;
        if (documentType.isBlanketApproveGroupDefined()) {
            boolean initiatorAuthorized = true;
            if (documentType.getInitiatorMustBlanketApprovePolicy().getPolicyValue()) {
                initiatorAuthorized = executeInitiatorPolicyCheck(principalId, initiatorPrincipalId, documentStatus);
            }
            result = initiatorAuthorized && documentType.isBlanketApprover(principalId);
        } else {
            Map<String, String> permissionDetails = buildDocumentTypePermissionDetails(documentType, documentStatus, null, null);
            result = getPermissionService().isAuthorizedByTemplate(principalId, KewApiConstants.KEW_NAMESPACE,
                    KewApiConstants.BLANKET_APPROVE_PERMISSION, permissionDetails, new HashMap<String, String>());
        }
        return result;
    }

    /**
     * Implements {@link org.kuali.rice.kew.doctype.service.DocumentTypePermissionService#canSave(String, org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue)}
     */
    public boolean canSave(String principalId, DocumentRouteHeaderValue document) {
        validatePrincipalId(principalId);
        validateDocument(document);
        String documentId = document.getDocumentId();
        DocumentType documentType = document.getDocumentType();
        String documentStatus = document.getDocRouteStatus();
        String initiatorPrincipalId = document.getInitiatorWorkflowId();
        List<String> routeNodeNames = document.getCurrentNodeNames();
        validateDocumentType(documentType);
        validateRouteNodeNames(routeNodeNames);
        validateDocumentStatus(documentStatus);
        validatePrincipalId(initiatorPrincipalId);

        if (!documentType.isPolicyDefined(org.kuali.rice.kew.api.doctype.DocumentTypePolicy.INITIATOR_MUST_SAVE)) {
            List<Map<String, String>> permissionDetailList = buildDocumentTypePermissionDetailsForNodes(documentType, routeNodeNames, documentStatus, null);

            boolean foundAtLeastOnePermission = false;
            // loop over permission details, only one of them needs to be authorized
            for (Map<String, String> permissionDetails : permissionDetailList) {
                Map<String, String> roleQualifiers = buildDocumentRoleQualifiers(document, permissionDetails.get(KewApiConstants.ROUTE_NODE_NAME_DETAIL));
                if (useKimPermission(KewApiConstants.KEW_NAMESPACE, KewApiConstants.SAVE_PERMISSION, permissionDetails, true)) {
                    foundAtLeastOnePermission = true;
                    if (getPermissionService().isAuthorizedByTemplate(principalId, KewApiConstants.KEW_NAMESPACE,
                            KewApiConstants.SAVE_PERMISSION, permissionDetails, roleQualifiers)) {
                        return true;
                    }
                }
            }
            // if we found defined KIM permissions, but not of them have authorized this user, return false
            if (foundAtLeastOnePermission) {
                return false;
            }
        }

        if (documentType.getInitiatorMustSavePolicy().getPolicyValue()) {
            return executeInitiatorPolicyCheck(principalId, initiatorPrincipalId, documentStatus);
        }
        return true;
    }

    /**
     * Generates permission details map for KIM permission checks.  Details are derived from document type, status, action request code (if non-null)
     * and routeNode name (if non-null).  If the document status is not a routed state, "PreRoute" is used.
     * Note that this has to match the required data defined in krim_typ_attr_t for the krim_typ_t with
     * srvc_nm='documentTypeAndNodeOrStatePermissionTypeService'.
     * TODO: See KULRICE-3490, make assembly of permission details dynamic based on db config
     * @param documentType the KEW DocumentType
     * @param documentStatus the document status
     * @param actionRequestCode action request code if applicable
     * @param routeNodeName routeNode name if applicable
     * @return map of permission details for permission check
     */
    protected Map<String, String> buildDocumentTypePermissionDetails(DocumentType documentType, String documentStatus, String actionRequestCode, String routeNodeName) {
        Map<String, String> details = new HashMap<String, String>();
        if (documentType != null) {
            details.put(KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME, documentType.getName());
        }
        if (!StringUtils.isBlank(documentStatus)) {
            details.put(KimConstants.AttributeConstants.ROUTE_STATUS_CODE, documentStatus);
        }
        if (KewApiConstants.ROUTE_HEADER_INITIATED_CD.equals(documentStatus) ||
                KewApiConstants.ROUTE_HEADER_SAVED_CD.equals(documentStatus)) {
            details.put(KewApiConstants.ROUTE_NODE_NAME_DETAIL, DocumentAuthorizerBase.PRE_ROUTING_ROUTE_NAME);
        } else if (!StringUtils.isBlank(routeNodeName)) {
            details.put(KewApiConstants.ROUTE_NODE_NAME_DETAIL, routeNodeName);
        }
        if (!StringUtils.isBlank(actionRequestCode)) {
            details.put(KimConstants.AttributeConstants.ACTION_REQUEST_CD, actionRequestCode);
        }
        return details;
    }

    /**
     * This method generates the permission details for the given document with current active route nodes.
     * This method simply invokes {@link #buildDocumentTypePermissionDetails(org.kuali.rice.kew.doctype.bo.DocumentType, String, String, String)}
     * for each node (or once if no node names are provided).
     * @param documentType the DocumentType
     * @param routeNodeNames active route nodes for which to generate permission details
     * @param documentStatus document status
     * @param actionRequestCode action request code if applicable
     * @return list of permission details maps, one for each route node inspected
     */
    protected List<Map<String, String>> buildDocumentTypePermissionDetailsForNodes(DocumentType documentType,
                                                                                   Collection<String> routeNodeNames, String documentStatus, String actionRequestCode) {
        List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();

        if (!routeNodeNames.isEmpty()) {
            for (String routeNodeName : routeNodeNames) {
                detailList.add(buildDocumentTypePermissionDetails(documentType, documentStatus, actionRequestCode, routeNodeName));
            }
        } else {
            detailList.add(buildDocumentTypePermissionDetails(documentType, documentStatus, actionRequestCode, null));
        }
        return detailList;
    }

    /**
     * Generates role qualifiers for authorization check.  If the document status is a non-routed status, "PreRoute" is used.
     * The namespaceCode attribute is derived from the KRAD DataDictionary if there is a mapping for the document type.
     * @param document the document instance
     * @param routeNodeName name of the applicable routenode
     * @return map of role qualifiers
     */
    protected Map<String, String> buildDocumentRoleQualifiers(DocumentRouteHeaderValue document, String routeNodeName) {
        Map<String, String> qualifiers = new HashMap<String, String>();
        qualifiers.put(KimConstants.AttributeConstants.DOCUMENT_NUMBER, document.getDocumentId());
        if (!StringUtils.isBlank(document.getDocRouteStatus())) {
            qualifiers.put(KewApiConstants.DOCUMENT_STATUS_DETAIL, document.getDocRouteStatus());
            if (KewApiConstants.ROUTE_HEADER_INITIATED_CD.equals(document.getDocRouteStatus()) || KewApiConstants.ROUTE_HEADER_SAVED_CD.equals(document.getDocRouteStatus())) {
                qualifiers.put(KimConstants.AttributeConstants.ROUTE_NODE_NAME, DocumentAuthorizerBase.PRE_ROUTING_ROUTE_NAME);
            }
            else {
                qualifiers.put(KimConstants.AttributeConstants.ROUTE_NODE_NAME, routeNodeName);
            }
        }
        qualifiers.put(KewApiConstants.DOCUMENT_TYPE_NAME_DETAIL, document.getDocumentType().getName());

        DocumentEntry documentEntry = KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().getDocumentEntry(document.getDocumentType().getName());
        if (documentEntry != null) {
            Class<? extends Document> documentClass = documentEntry.getDocumentClass();
            String namespaceCode;
            if (MaintenanceDocument.class.isAssignableFrom(documentClass)) {
                MaintenanceDocumentEntry maintenanceDocumentEntry = (MaintenanceDocumentEntry) documentEntry;
                namespaceCode = KRADUtils.getNamespaceCode(maintenanceDocumentEntry.getDataObjectClass());
            }
            else {
                namespaceCode = KRADUtils.getNamespaceCode(documentClass);
            }
            qualifiers.put(KimConstants.AttributeConstants.NAMESPACE_CODE, namespaceCode);
        }

        return qualifiers;
    }

    /**
     * Returns whether to invoke KIM to perform permission checks.  First consults the {@link KewApiConstants#KIM_PRIORITY_ON_DOC_TYP_PERMS_IND} system parameter
     * to determine whether we should check for permission existence.  If this parameter is unset or is true, we proceed to invoke
     * {@link PermissionService#isPermissionDefinedByTemplate(String, String, java.util.Map)} to determine whether the given permission
     * is defined anywhere in the system.
     * @param namespace namespace of permission we are querying
     * @param permissionTemplateName template name of permissions we are querying
     * @param permissionDetails details of permissions we are querying
     * @param checkKimPriorityInd whether to consult the {@link KewApiConstants#KIM_PRIORITY_ON_DOC_TYP_PERMS_IND} parameter to determine whether the check for
     *                            permission definition
     * @return whether there are any permissions defined for the given permission template, or false if we are checking the kim priority indicator and
     *         the {@link KewApiConstants@KIM_PRIORITY_ON_DOC_TYP_PERMS_IND} system parameter is disabled.
     */
    protected boolean useKimPermission(String namespace, String permissionTemplateName, Map<String, String> permissionDetails, boolean checkKimPriorityInd) {
        Boolean b = true;
        if (checkKimPriorityInd) {
            b =  CoreFrameworkServiceLocator.getParameterService().getParameterValueAsBoolean(KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.ALL_DETAIL_TYPE, KewApiConstants.KIM_PRIORITY_ON_DOC_TYP_PERMS_IND);
        }
        if (b == null || b) {
            return getPermissionService().isPermissionDefinedByTemplate(namespace, permissionTemplateName,
                    permissionDetails);
        }
        return false;
    }

    private boolean executeInitiatorPolicyCheck(String principalId, String initiatorPrincipalId, String documentStatus) {
        return principalId.equals(initiatorPrincipalId) || !(KewApiConstants.ROUTE_HEADER_SAVED_CD.equals(documentStatus) || KewApiConstants.ROUTE_HEADER_INITIATED_CD.equals(documentStatus));
    }

    /**
     * Validates principal id parameter
     * @param principalId the principal id
     * @throw IllegalArgumentException if the principal is not valid (null or empty)
     */
    protected void validatePrincipalId(String principalId) {
        if (StringUtils.isBlank(principalId)) {
            throw new IllegalArgumentException("Invalid principal ID, value was empty");
        }
    }

    /**
     * Validates document parameter
     * @param document the document
     * @throw IllegalArgumentException if the document is null
     */
    protected void validateDocument(DocumentRouteHeaderValue document) {
        if (document== null) {
            throw new IllegalArgumentException("document cannot be null");
        }
    }

    /**
     * Validates documenttype parameter
     * @param documentType the document type
     * @throw IllegalArgumentException if the documenttype is null
     */
    protected void validateDocumentType(DocumentType documentType) {
        if (documentType == null) {
            throw new IllegalArgumentException("DocumentType cannot be null");
        }
    }

    /**
     * Validates routeNodeNames parameter
     * @param routeNodeNames the routeNode names
     * @throw IllegalArgumentException if any routeNode name is empty or null
     */
    protected void validateRouteNodeNames(List<String> routeNodeNames) {
        if (routeNodeNames.isEmpty()) {
            return;
            //throw new IllegalArgumentException("List of route node names was empty.");
        }
        for (String routeNodeName : routeNodeNames) {
            if (StringUtils.isBlank(routeNodeName)) {
                throw new IllegalArgumentException("List of route node names contained an invalid route node name, value was empty");
            }
        }
    }

    /**
     * Validates documentStatus parameter
     * @param documentStatus the document status
     * @throw IllegalArgumentException if document status is empty or null, or an invalid value
     */
    protected void validateDocumentStatus(String documentStatus) {
        if (StringUtils.isBlank(documentStatus)) {
            throw new IllegalArgumentException("Invalid document status, value was empty");
        }
        if (!KewApiConstants.DOCUMENT_STATUSES.containsKey(documentStatus)) {
            throw new IllegalArgumentException("Invalid document status was given, value was: " + documentStatus);
        }
    }

    // convenenience method to look up KIM PermissionService
    protected PermissionService getPermissionService() {
        return KimApiServiceLocator.getPermissionService();
    }

}