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
package org.kuali.rice.edl.impl.components;

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.util.xml.XmlJotter;
import org.kuali.rice.edl.impl.EDLContext;
import org.kuali.rice.edl.impl.EDLModelComponent;
import org.kuali.rice.edl.impl.EDLXmlUtils;
import org.kuali.rice.edl.impl.RequestParser;
import org.kuali.rice.edl.impl.UserAction;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Used as a pre processor and post processor. As a pre processor this creates/fetches the workflow
 * document and sets it on request. As a post processor this takes appropriate user action on the
 * document if the document is not in error.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public class WorkflowDocumentActions implements EDLModelComponent {

    private static final Logger LOG = Logger.getLogger(WorkflowDocumentActions.class);

    public static final String ACTION_TAKEN = "actionTaken";

    boolean isPreProcessor;

    public void updateDOM(Document dom, Element configElement, EDLContext edlContext) {

        try {
            isPreProcessor = configElement.getTagName().equals("preProcessor");
            if (isPreProcessor) {
                doPreProcessWork(edlContext);
            } else {
                doPostProcessWork(dom, edlContext);
            }
        } catch (Exception e) {
            throw new WorkflowRuntimeException(e);
        }

    }

    private void doPreProcessWork(EDLContext edlContext) throws Exception {
        RequestParser requestParser = edlContext.getRequestParser();

        UserAction userAction = edlContext.getUserAction();
        WorkflowDocument document = null;
        if (UserAction.ACTION_CREATE.equals(userAction.getAction())) {
            document = WorkflowDocumentFactory.createDocument(edlContext.getUserSession().getPrincipalId(), edlContext
                    .getEdocLiteAssociation().getEdlName());
            document.setTitle("Routing Document Type '" + document.getDocumentTypeName() + "'");
            document.getDocumentId();
            LOG.info("Created document " + document.getDocumentId());
        } else {
            document = (WorkflowDocument) requestParser.getAttribute(RequestParser.WORKFLOW_DOCUMENT_SESSION_KEY);
            if (document == null) {
                String docId = (String) requestParser.getAttribute("docId");
                if (docId == null) {
                    LOG.info("no document found for edl " + edlContext.getEdocLiteAssociation().getEdlName());
                    return;
                } else {
                    document = WorkflowDocumentFactory
                            .loadDocument(edlContext.getUserSession().getPrincipalId(), docId);
                }
            }
        }

        requestParser.setAttribute(RequestParser.WORKFLOW_DOCUMENT_SESSION_KEY, document);
    }

    private void doPostProcessWork(Document dom, EDLContext edlContext) throws Exception {
        RequestParser requestParser = edlContext.getRequestParser();
        // if the document is in error then we don't want to execute the action!
        if (edlContext.isInError()) {
            return;
        }
        WorkflowDocument document = (WorkflowDocument) edlContext.getRequestParser().getAttribute(
                RequestParser.WORKFLOW_DOCUMENT_SESSION_KEY);
        if (document == null) {
            return;
        }
        //strip out the data element
        Element dataElement = (Element) dom.getElementsByTagName(EDLXmlUtils.DATA_E).item(0);
        String docContent = XmlJotter.jotNode(dataElement);//use the transformer on edlcontext
        document.setApplicationContent(docContent);
        takeAction(document, dom, edlContext);
    }

    public static void takeAction(WorkflowDocument document, Document dom, EDLContext edlContext)
            throws WorkflowException {
        RequestParser requestParser = edlContext.getRequestParser();
        UserAction userAction = edlContext.getUserAction();
        String annotation = requestParser.getParameterValue("annotation");
        String action = userAction.getAction();
        String previousNodeName = requestParser.getParameterValue("previousNode");

        if (!userAction.isValidatableAction()) {
            // if the action's not validatable, clear the attribute definitions because we don't want to end up executing validateClientRoutingData()
            // TODO the problem here is that the XML is still updated on a cancel so we end up without any attribute content in the document content
            document.clearAttributeDefinitions();
        }

        boolean actionTaken = true;

        if (UserAction.ACTION_ROUTE.equals(action)) {
            document.route(annotation);
        }else if(UserAction.ACTION_CREATE.equals(action)){
               document.saveDocumentData();
        }
        else if (UserAction.ACTION_APPROVE.equals(action)) {
            document.approve(annotation);
        } else if (UserAction.ACTION_DISAPPROVE.equals(action)) {
            document.disapprove(annotation);
        } else if (UserAction.ACTION_CANCEL.equals(action)) {
            document.cancel(annotation);
        } else if (UserAction.ACTION_BLANKETAPPROVE.equals(action)) {
            document.blanketApprove(annotation);
        } else if (UserAction.ACTION_FYI.equals(action)) {
            document.fyi();
        } else if (UserAction.ACTION_ACKNOWLEDGE.equals(action)) {
            document.acknowledge(annotation);
        } else if (UserAction.ACTION_SAVE.equals(action)) {
            if (document.getStatus().equals(DocumentStatus.INITIATED)) {
                document.saveDocument(annotation);
            } else {
                document.saveDocumentData();
            }
        } else if (UserAction.ACTION_COMPLETE.equals(action)) {
            document.complete(annotation);
        } else if (UserAction.ACTION_DELETE.equals(action)) {
            document.delete();
        } else if (UserAction.ACTION_RETURN_TO_PREVIOUS.equals(action)) {
            document.returnToPreviousNode(annotation, previousNodeName);
        } else {
            actionTaken = false;
        }

        if (actionTaken) {
            Element actionTakenElement = EDLXmlUtils.getOrCreateChildElement(dom.getDocumentElement(), ACTION_TAKEN,
                    true);
            actionTakenElement.appendChild(dom.createTextNode(action));
        }
    }

}
