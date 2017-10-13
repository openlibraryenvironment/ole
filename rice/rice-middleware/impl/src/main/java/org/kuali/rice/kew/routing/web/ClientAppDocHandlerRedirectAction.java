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
package org.kuali.rice.kew.routing.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.doctype.SecuritySession;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.doctype.service.DocumentTypeService;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.routeheader.service.RouteHeaderService;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.web.KewKualiAction;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;


/**
 * A Struts Action for redirecting from the KEW web application to the appropriate
 * Doc Handler for a document.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ClientAppDocHandlerRedirectAction extends KewKualiAction {

    @Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DocHandlerForm docHandlerForm = (DocHandlerForm) form;

        String docHandler = null;

        if (request.getParameter(KewApiConstants.DOCUMENT_ID_PARAMETER) != null) {
            RouteHeaderService rhSrv = (RouteHeaderService) KEWServiceLocator.getService(KEWServiceLocator.DOC_ROUTE_HEADER_SRV);
            DocumentRouteHeaderValue routeHeader = rhSrv.getRouteHeader(docHandlerForm.getDocId());

            if (!KEWServiceLocator.getDocumentSecurityService().routeLogAuthorized(GlobalVariables.getUserSession().getPrincipalId(), routeHeader, new SecuritySession(GlobalVariables.getUserSession().getPrincipalId()))) {
            	return mapping.findForward("NotAuthorized");
            }
            docHandler = routeHeader.getDocumentType().getResolvedDocumentHandlerUrl();
            if (StringUtils.isBlank(docHandler)) {
                throw new WorkflowRuntimeException("Document Type '" + routeHeader.getDocumentType().getName() + "' does not have a document handler url set (attempted to open document handler url for document id " + routeHeader.getDocumentId() + ")");
            }
            if (!docHandler.contains("?")) {
                docHandler += "?";
            } else {
                docHandler += "&";
            }
            docHandler += KewApiConstants.DOCUMENT_ID_PARAMETER + "=" + docHandlerForm.getDocId();
        } else if (request.getParameter(KewApiConstants.DOCTYPE_PARAMETER) != null) {
            DocumentTypeService documentTypeService = (DocumentTypeService) KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE);
            DocumentType documentType = documentTypeService.findByName(docHandlerForm.getDocTypeName());
            docHandler = documentType.getResolvedDocumentHandlerUrl();
            if (StringUtils.isBlank(docHandler)) {
                throw new WorkflowRuntimeException("Cannot find document handler url for document type '" + documentType.getName() + "'");
            }
            if (!docHandler.contains("?")) {
                docHandler += "?";
            } else {
                docHandler += "&";
            }
            docHandler += KewApiConstants.DOCTYPE_PARAMETER + "=" + docHandlerForm.getDocTypeName();
        } else {
//TODO what should happen here if parms are missing; no proper ActionForward from here
            throw new RuntimeException ("Cannot determine document handler");
        }

        docHandler += "&" + KewApiConstants.COMMAND_PARAMETER + "=" + docHandlerForm.getCommand();
        if (getUserSession(request).isBackdoorInUse()) {
            docHandler += "&" + KewApiConstants.BACKDOOR_ID_PARAMETER + "=" + getUserSession(request).getPrincipalName();
        }
        return new ActionForward(docHandler, true);
    }

    public static UserSession getUserSession(HttpServletRequest request) {
        return GlobalVariables.getUserSession();
    }
}
