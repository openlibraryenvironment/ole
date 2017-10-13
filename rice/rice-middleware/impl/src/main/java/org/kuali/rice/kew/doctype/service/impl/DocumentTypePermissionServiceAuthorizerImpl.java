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
import org.apache.log4j.Logger;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.api.extension.ExtensionUtils;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.doctype.service.DocumentTypePermissionService;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.framework.document.security.AuthorizableAction;
import org.kuali.rice.kew.framework.document.security.DocumentTypeAuthorizer;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;

import java.util.*;

/**
 * Implementation of {@link DocumentTypePermissionService} that delegates all calls (based on the
 * {@link DocumentType} or {@link DocumentRouteHeaderValue} parameter to the method being called)
 * <ul>
 *     <li>to the DocumentTypeAuthorizer configured on the {@link org.kuali.rice.kew.doctype.bo.DocumentType} if there is one</li>
 *     <li>otherwise, to the default {@link DocumentTypeAuthorizer} implementation</li>
 * </ul>
 */
public class DocumentTypePermissionServiceAuthorizerImpl extends DocumentTypePermissionServiceImpl {
    private static final Logger LOG = Logger.getLogger(DocumentTypePermissionServiceAuthorizerImpl.class);

    /**
     * Arbitrary placeholder strings for Extension lookup.  We are simply using the Extension loading convention
     * to instantiate the DocumentTypeAuthorizor, so we do not need meaningful name or type parameters
     */
    private static final String PLACEHOLDER_EXTENSION_NAME = "DocumenTypeAuthorizer";
    private static final String PLACEHOLDER_EXTENSION_TYPE = "DocumenTypeAuthorizer";

    /**
     * The default DocumentTypeAuthorizer implementation.  Kept as a singleton for performance.
     */
    protected DocumentTypeAuthorizer defaultDocumentTypeAuthorizer = new KimDocumentTypeAuthorizer();

    /**
     * Load the KimDocumentTypeAuthorizer for the specified document, or default impl if custom impl is not specified
     * @param documentType the document type whose DocumentTypeAuthorizer to load
     * @return a DocumentTypeAuthorizer impl
     */
    protected DocumentTypeAuthorizer getDocumentTypeAuthorizer(DocumentType documentType) {
        DocumentTypeAuthorizer delegate = defaultDocumentTypeAuthorizer;

        if (documentType == null) {
            LOG.warn("DocumentType is null, using default DocumentTypeAuthorizer impl: " + delegate.getClass());
        } else {
            String documentTypeAuthorizer = documentType.getAuthorizer();

            if (StringUtils.isNotBlank(documentTypeAuthorizer)) {
                ExtensionDefinition extensionDef = ExtensionDefinition.Builder.create(PLACEHOLDER_EXTENSION_NAME, PLACEHOLDER_EXTENSION_TYPE, documentTypeAuthorizer).build();
                Object extension = ExtensionUtils.loadExtension(extensionDef);

                if (extension == null) {
                    throw new WorkflowRuntimeException("Could not load DocumentTypeAuthorizer: " + documentTypeAuthorizer);
                }

                if (!DocumentTypeAuthorizer.class.isAssignableFrom(extension.getClass())) {
                    throw new WorkflowRuntimeException("DocumentType Authorizer '" + documentTypeAuthorizer + "' configured for document type '" + documentType.getName() + " does not implement " + DocumentTypeAuthorizer.class.getName());
                }

                delegate = (DocumentTypeAuthorizer) extension;
            }
        }

        return delegate;
    }

    @Override
    public boolean canInitiate(String principalId, DocumentType documentType) {
        return getDocumentTypeAuthorizer(documentType).isActionAuthorized(new AuthorizableAction(AuthorizableAction.CheckType.INITIATION), principalId, org.kuali.rice.kew.api.doctype.DocumentType.Builder.create(documentType).build(), null, Collections.EMPTY_MAP).isAuthorized();
    }

    @Override
    public boolean canBlanketApprove(String principalId, DocumentRouteHeaderValue document) {
        validateDocument(document);
        return getDocumentTypeAuthorizer(document.getDocumentType()).isActionAuthorized(new AuthorizableAction(ActionType.BLANKET_APPROVE), principalId, org.kuali.rice.kew.api.doctype.DocumentType.Builder.create(document.getDocumentType()).build(), Document.Builder.create(document).build(), Collections.EMPTY_MAP).isAuthorized();
    }

    @Override
    public boolean canCancel(String principalId, DocumentRouteHeaderValue document) {
        validateDocument(document);
        return getDocumentTypeAuthorizer(document.getDocumentType()).isActionAuthorized(new AuthorizableAction(ActionType.CANCEL), principalId, org.kuali.rice.kew.api.doctype.DocumentType.Builder.create(document.getDocumentType()).build(), Document.Builder.create(document).build(), Collections.EMPTY_MAP).isAuthorized();
    }

    @Override
    public boolean canRecall(String principalId, DocumentRouteHeaderValue document) {
        validateDocument(document);
        return getDocumentTypeAuthorizer(document.getDocumentType()).isActionAuthorized(new AuthorizableAction(ActionType.RECALL), principalId, org.kuali.rice.kew.api.doctype.DocumentType.Builder.create(document.getDocumentType()).build(), Document.Builder.create(document).build(), Collections.EMPTY_MAP).isAuthorized();
    }

    @Override
    public boolean canSave(String principalId, DocumentRouteHeaderValue document) {
        validateDocument(document);
        return getDocumentTypeAuthorizer(document.getDocumentType()).isActionAuthorized(new AuthorizableAction(ActionType.SAVE), principalId, org.kuali.rice.kew.api.doctype.DocumentType.Builder.create(document.getDocumentType()).build(), Document.Builder.create(document).build(), Collections.EMPTY_MAP).isAuthorized();
    }

    @Override
    public boolean canRoute(String principalId, DocumentRouteHeaderValue document) {
        validateDocument(document);
        return getDocumentTypeAuthorizer(document.getDocumentType()).isActionAuthorized(new AuthorizableAction(ActionType.ROUTE), principalId, org.kuali.rice.kew.api.doctype.DocumentType.Builder.create(document.getDocumentType()).build(), Document.Builder.create(document).build(), Collections.EMPTY_MAP).isAuthorized();
    }

    @Override
    public boolean canSuperUserApproveDocument(String principalId, DocumentType documentType, Collection<String> routeNodeNames, String routeStatusCode) {
        Map<DocumentTypeAuthorizer.ActionArgument, Object> actionParams = new HashMap<DocumentTypeAuthorizer.ActionArgument, Object>();
        actionParams.put(DocumentTypeAuthorizer.ActionArgument.ROUTENODE_NAMES, routeNodeNames);
        actionParams.put(DocumentTypeAuthorizer.ActionArgument.DOCSTATUS, routeStatusCode);
        return getDocumentTypeAuthorizer(documentType).isActionAuthorized(new AuthorizableAction(ActionType.SU_APPROVE), principalId, org.kuali.rice.kew.api.doctype.DocumentType.Builder.create(documentType).build(), null, actionParams).isAuthorized();
    }

    @Override
    public boolean canSuperUserDisapproveDocument(String principalId, DocumentType documentType, Collection<String> routeNodeNames, String routeStatusCode) {
        Map<DocumentTypeAuthorizer.ActionArgument, Object> actionParams = new HashMap<DocumentTypeAuthorizer.ActionArgument, Object>();
        actionParams.put(DocumentTypeAuthorizer.ActionArgument.ROUTENODE_NAMES, routeNodeNames);
        actionParams.put(DocumentTypeAuthorizer.ActionArgument.DOCSTATUS, routeStatusCode);
        return getDocumentTypeAuthorizer(documentType).isActionAuthorized(new AuthorizableAction(ActionType.SU_DISAPPROVE), principalId, org.kuali.rice.kew.api.doctype.DocumentType.Builder.create(documentType).build(), null, actionParams).isAuthorized();
    }

    @Override
    protected boolean canSuperUserApproveSingleActionRequest(String principalId, DocumentType documentType, Collection<String> routeNodeNames, String routeStatusCode) {
        Map<DocumentTypeAuthorizer.ActionArgument, Object> actionParams = new HashMap<DocumentTypeAuthorizer.ActionArgument, Object>();
        actionParams.put(DocumentTypeAuthorizer.ActionArgument.ROUTENODE_NAMES, routeNodeNames);
        actionParams.put(DocumentTypeAuthorizer.ActionArgument.DOCSTATUS, routeStatusCode);
        return getDocumentTypeAuthorizer(documentType).isActionAuthorized(new AuthorizableAction(AuthorizableAction.CheckType.SU_APPROVE_ACTION_REQUEST), principalId, org.kuali.rice.kew.api.doctype.DocumentType.Builder.create(documentType).build(), null, actionParams).isAuthorized();
    }
}
