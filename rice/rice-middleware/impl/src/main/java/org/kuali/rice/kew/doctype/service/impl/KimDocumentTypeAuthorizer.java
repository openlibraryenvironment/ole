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

import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.doctype.DocumentType;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.framework.document.security.AuthorizableAction;
import org.kuali.rice.kew.framework.document.security.Authorization;
import org.kuali.rice.kew.framework.document.security.DocumentTypeAuthorizer;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;

import java.util.Collection;
import java.util.Map;

/**
 * Default DocumentTypeAuthorizer implementation which performs KIM checks for authorizable actions.
 * @since 2.1.3
 */
public class KimDocumentTypeAuthorizer extends DocumentActionsPermissionBase implements DocumentTypeAuthorizer {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(KimDocumentTypeAuthorizer.class);

    @Override
    public Authorization isActionAuthorized(AuthorizableAction action, String principalId, DocumentType documentType, Document document, Map<ActionArgument, Object> actionParameters) {
        org.kuali.rice.kew.doctype.bo.DocumentType documentTypeBo = org.kuali.rice.kew.doctype.bo.DocumentType.from(documentType);
        boolean success = false;
        switch (action.type) {
            case INITIATION:
                success = canInitiate(principalId, documentTypeBo);
                break;
            case SU_APPROVE_ACTION_REQUEST:
                success = canSuperUserApproveSingleActionRequest(principalId, documentTypeBo, (Collection<String>) actionParameters.get(ActionArgument.ROUTENODE_NAMES), (String) actionParameters.get(ActionArgument.DOCSTATUS));
                break;
            case ACTION:
                switch (action.actionType) {
                    case BLANKET_APPROVE:
                        success = canBlanketApprove(principalId, DocumentRouteHeaderValue.from(document));
                        break;
                    case SU_APPROVE:
                        success = canSuperUserApproveDocument(principalId, documentTypeBo, (Collection<String>) actionParameters.get(ActionArgument.ROUTENODE_NAMES), (String) actionParameters.get(ActionArgument.DOCSTATUS));
                        break;
                    case SU_DISAPPROVE:
                        success = canSuperUserDisapproveDocument(principalId, documentTypeBo, (Collection<String>) actionParameters.get(ActionArgument.ROUTENODE_NAMES), (String) actionParameters.get(ActionArgument.DOCSTATUS));
                        break;
                    case CANCEL:
                        success = canCancel(principalId, DocumentRouteHeaderValue.from(document));
                        break;
                    case RECALL:
                        success = canRecall(principalId, DocumentRouteHeaderValue.from(document));
                        break;
                    case ROUTE :
                        success = canRoute(principalId, DocumentRouteHeaderValue.from(document));
                        break;
                    case SAVE:
                        success = canSave(principalId, DocumentRouteHeaderValue.from(document));
                        break;
                    default:
                        throw new RuntimeException("Unknown document action check");
                }
                break;
            default:
                throw new RuntimeException("Unknown authorization check");
        }
        return new Authorization(success);
    }
}