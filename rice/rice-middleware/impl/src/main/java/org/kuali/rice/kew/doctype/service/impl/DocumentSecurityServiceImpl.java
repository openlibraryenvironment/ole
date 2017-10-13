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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.reflect.ObjectDefinition;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.document.search.DocumentSearchResult;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.api.extension.ExtensionRepositoryService;
import org.kuali.rice.kew.doctype.DocumentTypeSecurity;
import org.kuali.rice.kew.framework.KewFrameworkServiceLocator;
import org.kuali.rice.kew.framework.document.security.DocumentSecurityDirective;
import org.kuali.rice.kew.framework.document.security.DocumentSecurityHandlerService;
import org.kuali.rice.kew.framework.document.security.DocumentSecurityAttribute;
import org.kuali.rice.kew.doctype.SecurityPermissionInfo;
import org.kuali.rice.kew.doctype.SecuritySession;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.doctype.service.DocumentSecurityService;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.user.UserUtils;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DocumentSecurityServiceImpl implements DocumentSecurityService {

    public static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            DocumentSecurityServiceImpl.class);

    private ExtensionRepositoryService extensionRepositoryService;

    @Override
    public boolean routeLogAuthorized(String principalId, DocumentRouteHeaderValue routeHeader,
            SecuritySession securitySession) {
        List<Document> documents = Collections.singletonList(DocumentRouteHeaderValue.to(routeHeader));
        Set<String> authorizationResults = checkAuthorizations(principalId, securitySession, documents);
        return authorizationResults.contains(routeHeader.getDocumentId());
    }

    @Override
    public Set<String> documentSearchResultAuthorized(String principalId, DocumentSearchResults results,
            SecuritySession securitySession) {
        List<Document> documents = new ArrayList<Document>();
        for (DocumentSearchResult result : results.getSearchResults()) {
            documents.add(result.getDocument());
        }
        return checkAuthorizations(principalId, securitySession, documents);
    }

    protected Set<String> checkAuthorizations(String principalId, SecuritySession securitySession,
            List<Document> documents) {
        Set<String> authorizations = new HashSet<String>();
        // a list of documents which need to be processed with security extension attributes after the standard set of
        // security has been attempted
        List<Document> documentsRequiringExtensionProcessing = new ArrayList<Document>();
        boolean admin = isAdmin(securitySession);
        for (Document document : documents) {
            if (admin) {
                authorizations.add(document.getDocumentId());
                continue;
            }
            DocumentTypeSecurity security = null;
            try {
                security = getDocumentTypeSecurity(document.getDocumentTypeName(), securitySession);
                if (security == null || !security.isActive() || checkStandardAuthorization(security, principalId,
                        document, securitySession)) {
                    authorizations.add(document.getDocumentId());
                } else {
                    // if we get to this point, it means we aren't authorized yet, last chance for authorization will be
                    // security extension attributes, so prepare for execution of those after the main loop is complete
                    if (CollectionUtils.isNotEmpty(security.getSecurityAttributeExtensionNames())) {
                        documentsRequiringExtensionProcessing.add(document);
                    }
                }
            } catch (Exception e) {
                LOG.warn(
                        "Not able to retrieve DocumentTypeSecurity from remote system for documentTypeName: " + document
                                .getDocumentTypeName(), e);
                continue;
            }
        }
        processDocumentRequiringExtensionProcessing(documentsRequiringExtensionProcessing, securitySession,
                authorizations);
        return authorizations;
    }

    protected void processDocumentRequiringExtensionProcessing(List<Document> documentsRequiringExtensionProcessing,
            SecuritySession securitySession, Set<String> authorizations) {
        if (CollectionUtils.isNotEmpty(documentsRequiringExtensionProcessing)) {
            LOG.info("Beginning processing of documents requiring extension processing (total: "
                    + documentsRequiringExtensionProcessing.size()
                    + " documents)");
            long start = System.currentTimeMillis();
            MultiValueMap<PartitionKey, Document> partitions = partitionDocumentsForSecurity(
                    documentsRequiringExtensionProcessing, securitySession);
            MultiValueMap<String, DocumentSecurityDirective> applicationSecurityDirectives =
                    new LinkedMultiValueMap<String, DocumentSecurityDirective>();
            for (PartitionKey partitionKey : partitions.keySet()) {
                DocumentSecurityDirective directive = DocumentSecurityDirective.create(
                        partitionKey.getDocumentSecurityAttributeNameList(), partitions.get(partitionKey));
                applicationSecurityDirectives.add(partitionKey.applicationId, directive);
            }
            for (String applicationId : applicationSecurityDirectives.keySet()) {
                List<DocumentSecurityDirective> documentSecurityDirectives = applicationSecurityDirectives.get(
                        applicationId);
                DocumentSecurityHandlerService securityHandler = loadSecurityHandler(applicationId);
                List<String> authorizedDocumentIds = securityHandler.getAuthorizedDocumentIds(
                        securitySession.getPrincipalId(), documentSecurityDirectives);
                if (CollectionUtils.isNotEmpty(authorizedDocumentIds)) {
                    authorizations.addAll(authorizedDocumentIds);
                }
            }
            long end = System.currentTimeMillis();
            LOG.info("Finished processing of documents requiring extension processing (total time: "
                    + (start - end)
                    + ")");
        }
    }

    protected MultiValueMap<PartitionKey, Document> partitionDocumentsForSecurity(List<Document> documents,
            SecuritySession securitySession) {
        MultiValueMap<PartitionKey, Document> partitions = new LinkedMultiValueMap<PartitionKey, Document>();
        for (Document document : documents) {
            DocumentTypeSecurity security = getDocumentTypeSecurity(document.getDocumentTypeName(), securitySession);
            MultiValueMap<String, ExtensionDefinition> securityAttributeExtensionDefinitions = loadExtensionDefinitions(
                    security, securitySession);
            for (String applicationId : securityAttributeExtensionDefinitions.keySet()) {
                List<ExtensionDefinition> extensionDefinitions = securityAttributeExtensionDefinitions.get(
                        applicationId);
                PartitionKey key = new PartitionKey(applicationId, extensionDefinitions);
                partitions.add(key, document);
            }
        }
        return partitions;
    }

    protected MultiValueMap<String, ExtensionDefinition> loadExtensionDefinitions(DocumentTypeSecurity security,
            SecuritySession securitySession) {
        MultiValueMap<String, ExtensionDefinition> securityAttributeExtensionDefinitions =
                new LinkedMultiValueMap<String, ExtensionDefinition>();
        List<String> securityAttributeExtensionNames = security.getSecurityAttributeExtensionNames();
        for (String securityAttributeExtensionName : securityAttributeExtensionNames) {
            ExtensionDefinition extensionDefinition = extensionRepositoryService.getExtensionByName(
                    securityAttributeExtensionName);
            securityAttributeExtensionDefinitions.add(extensionDefinition.getApplicationId(), extensionDefinition);
        }
        return securityAttributeExtensionDefinitions;
    }

    protected DocumentSecurityHandlerService loadSecurityHandler(String applicationId) {
        DocumentSecurityHandlerService service = KewFrameworkServiceLocator.getDocumentSecurityHandlerService(
                applicationId);
        if (service == null) {
            throw new WorkflowRuntimeException(
                    "Failed to locate DocumentSecurityHandlerService for applicationId: " + applicationId);
        }
        return service;
    }

    protected boolean isAdmin(SecuritySession session) {
        if (session.getPrincipalId() == null) {
            return false;
        }
        return KimApiServiceLocator.getPermissionService().isAuthorized(session.getPrincipalId(),
                KewApiConstants.KEW_NAMESPACE, KewApiConstants.PermissionNames.UNRESTRICTED_DOCUMENT_SEARCH, new HashMap<String, String>());
    }

    protected boolean checkStandardAuthorization(DocumentTypeSecurity security, String principalId, Document document,
            SecuritySession securitySession) {
        String documentId = document.getDocumentId();
        String initiatorPrincipalId = document.getInitiatorPrincipalId();

        LOG.debug("auth check user=" + principalId + " docId=" + documentId);

        // Doc Initiator Authorization
        if (security.getInitiatorOk() != null && security.getInitiatorOk()) {
            boolean isInitiator = StringUtils.equals(initiatorPrincipalId, principalId);
            if (isInitiator) {
                return true;
            }
        }

        // Permission Authorization
        List<SecurityPermissionInfo> securityPermissions = security.getPermissions();
        if (securityPermissions != null) {
            for (SecurityPermissionInfo securityPermission : securityPermissions) {
                if (isAuthenticatedByPermission(documentId, securityPermission.getPermissionNamespaceCode(),
                        securityPermission.getPermissionName(), securityPermission.getPermissionDetails(),
                        securityPermission.getQualifications(), securitySession)) {
                    return true;
                }
            }
        }

        //  Group Authorization
        List<Group> securityWorkgroups = security.getWorkgroups();
        if (securityWorkgroups != null) {
            for (Group securityWorkgroup : securityWorkgroups) {
                if (isGroupAuthenticated(securityWorkgroup.getNamespaceCode(), securityWorkgroup.getName(),
                        securitySession)) {
                    return true;
                }
            }
        }

        // Searchable Attribute Authorization
        Collection searchableAttributes = security.getSearchableAttributes();
        if (searchableAttributes != null) {
            for (Iterator iterator = searchableAttributes.iterator(); iterator.hasNext(); ) {
                KeyValue searchableAttr = (KeyValue) iterator.next();
                String attrName = searchableAttr.getKey();
                String idType = searchableAttr.getValue();
                String idValue = UserUtils.getIdValue(idType, principalId);
                if (!StringUtils.isEmpty(idValue)) {
                    if (KEWServiceLocator.getRouteHeaderService().hasSearchableAttributeValue(documentId, attrName,
                            idValue)) {
                        return true;
                    }
                }
            }
        }

        // Route Log Authorization
        if (security.getRouteLogAuthenticatedOk() != null && security.getRouteLogAuthenticatedOk()) {
            boolean isInitiator = StringUtils.equals(initiatorPrincipalId, principalId);
            if (isInitiator) {
                return true;
            }
            boolean hasTakenAction = KEWServiceLocator.getActionTakenService().hasUserTakenAction(principalId,
                    documentId);
            if (hasTakenAction) {
                return true;
            }
            boolean hasRequest = KEWServiceLocator.getActionRequestService().doesPrincipalHaveRequest(principalId,
                    documentId);
            if (hasRequest) {
                return true;
            }
        }

        // local security attribute authorization
        List<DocumentSecurityAttribute> immediateSecurityAttributes = getImmediateSecurityAttributes(document, security,
                securitySession);
        if (immediateSecurityAttributes != null) {
            for (DocumentSecurityAttribute immediateSecurityAttribute : immediateSecurityAttributes) {
                boolean isAuthorized = immediateSecurityAttribute.isAuthorizedForDocument(principalId, document);
                if (isAuthorized) {
                    return true;
                }
            }
        }

        LOG.debug("user not authorized");
        return false;
    }

    protected List<DocumentSecurityAttribute> getImmediateSecurityAttributes(Document document, DocumentTypeSecurity security,
            SecuritySession securitySession) {
        List<DocumentSecurityAttribute> securityAttributes = new ArrayList<DocumentSecurityAttribute>();
        for (String securityAttributeClassName : security.getSecurityAttributeClassNames()) {
            DocumentSecurityAttribute securityAttribute = securitySession.getSecurityAttributeForClass(
                    securityAttributeClassName);
            if (securityAttribute == null) {
                securityAttribute = GlobalResourceLoader.getObject(new ObjectDefinition(securityAttributeClassName));
                securitySession.setSecurityAttributeForClass(securityAttributeClassName, securityAttribute);
            }
            securityAttributes.add(securityAttribute);
        }
        return securityAttributes;
    }

    protected DocumentTypeSecurity getDocumentTypeSecurity(String documentTypeName, SecuritySession session) {
        DocumentTypeSecurity security = session.getDocumentTypeSecurity().get(documentTypeName);
        if (security == null) {
            DocumentType docType = KEWServiceLocator.getDocumentTypeService().findByName(documentTypeName);
            if (docType != null) {
                security = docType.getDocumentTypeSecurity();
                session.getDocumentTypeSecurity().put(documentTypeName, security);
            }
        }
        return security;
    }

    protected boolean isGroupAuthenticated(String namespace, String groupName, SecuritySession session) {
        String key = namespace.trim() + KewApiConstants.KIM_GROUP_NAMESPACE_NAME_DELIMITER_CHARACTER + groupName.trim();
        Boolean existingAuth = session.getAuthenticatedWorkgroups().get(key);
        if (existingAuth != null) {
            return existingAuth;
        }
        boolean memberOfGroup = isMemberOfGroupWithName(namespace, groupName, session.getPrincipalId());
        session.getAuthenticatedWorkgroups().put(key, memberOfGroup);
        return memberOfGroup;
    }

    private boolean isMemberOfGroupWithName(String namespace, String groupName, String principalId) {
        for (Group group : KimApiServiceLocator.getGroupService().getGroupsByPrincipalId(principalId)) {
            if (StringUtils.equals(namespace, group.getNamespaceCode()) && StringUtils.equals(groupName,
                    group.getName())) {
                return true;
            }
        }
        return false;
    }

    protected boolean isAuthenticatedByPermission(String documentId, String permissionNamespaceCode,
            String permissionName, Map<String, String> permissionDetails, Map<String, String> qualification,
            SecuritySession session) {

        Document document;
        try {
            document = KewApiServiceLocator.getWorkflowDocumentService().getDocument(documentId);

            for (String qualificationKey : qualification.keySet()) {
                String qualificationValue = qualification.get(qualificationKey);
                String replacementValue = getReplacementString(document, qualificationValue);
                qualification.put(qualificationKey, replacementValue);
            }

            for (String permissionDetailKey : permissionDetails.keySet()) {
                String detailValue = qualification.get(permissionDetailKey);
                String replacementValue = getReplacementString(document, detailValue);
                qualification.put(permissionDetailKey, replacementValue);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
        return KimApiServiceLocator.getPermissionService().isAuthorized(session.getPrincipalId(),
                permissionNamespaceCode, permissionName, qualification);
    }

    private String getReplacementString(Document document, String value) throws Exception {
        String startsWith = "${document.";
        String endsWith = "}";
        if (value.startsWith(startsWith)) {
            int tokenStart = value.indexOf(startsWith);
            int tokenEnd = value.indexOf(endsWith, tokenStart + startsWith.length());
            if (tokenEnd == -1) {
                throw new RuntimeException("No ending bracket on token in value " + value);
            }
            String token = value.substring(tokenStart + startsWith.length(), tokenEnd);

            return getRouteHeaderVariableValue(document, token);
        }
        return value;

    }

    private String getRouteHeaderVariableValue(Document document, String variableName) throws Exception {
        Field field;
        try {
            field = document.getClass().getDeclaredField(variableName);
        } catch (NoSuchFieldException nsfe) {
            LOG.error("Field '" + variableName + "' not found on Document object.");
            // instead of raising an exception, return null as a value
            // this leaves it up to proper permission configuration to fail the check if a field value
            // is required
            return null;
        }
        field.setAccessible(true);
        Object fieldValue = field.get(document);
        Class<?> clazzType = field.getType();
        if (clazzType.equals(String.class)) {
            return (String) fieldValue;
        } else if (clazzType.getName().equals("boolean") || clazzType.getName().equals("java.lang.Boolean")) {
            if ((Boolean) fieldValue) {
                return "Y";
            }
            return "N";
        } else if (clazzType.getName().equals("java.util.Calendar")) {

            DateTimeService dateTimeService = GlobalResourceLoader.getService(CoreConstants.Services.DATETIME_SERVICE);
            return dateTimeService.toDateString(((Calendar) fieldValue).getTime());
        }
        return String.valueOf(fieldValue);
    }

    public ExtensionRepositoryService getExtensionRepositoryService() {
        return extensionRepositoryService;
    }

    public void setExtensionRepositoryService(ExtensionRepositoryService extensionRepositoryService) {
        this.extensionRepositoryService = extensionRepositoryService;
    }

    /**
     * Simple class which defines the key of a partition of security attributes associated with an application id.
     *
     * <p>This class allows direct field access since it is intended for internal use only.</p>
     */
    private static final class PartitionKey {
        String applicationId;
        Set<String> documentSecurityAttributeNames;

        PartitionKey(String applicationId, Collection<ExtensionDefinition> extensionDefinitions) {
            this.applicationId = applicationId;
            this.documentSecurityAttributeNames = new HashSet<String>();
            for (ExtensionDefinition extensionDefinition : extensionDefinitions) {
                this.documentSecurityAttributeNames.add(extensionDefinition.getName());
            }
        }

        List<String> getDocumentSecurityAttributeNameList() {
            return new ArrayList<String>(documentSecurityAttributeNames);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof PartitionKey)) {
                return false;
            }
            PartitionKey key = (PartitionKey) o;
            EqualsBuilder builder = new EqualsBuilder();
            builder.append(applicationId, key.applicationId);
            builder.append(documentSecurityAttributeNames, key.documentSecurityAttributeNames);
            return builder.isEquals();
        }

        @Override
        public int hashCode() {
            HashCodeBuilder builder = new HashCodeBuilder();
            builder.append(applicationId);
            builder.append(documentSecurityAttributeNames);
            return builder.hashCode();
        }
    }

}
