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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.api.util.xml.XmlJotter;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.edl.impl.EDLContext;
import org.kuali.rice.edl.impl.EDLModelComponent;
import org.kuali.rice.edl.impl.EDLXmlUtils;
import org.kuali.rice.edl.impl.RequestParser;
import org.kuali.rice.edl.impl.UserAction;
import org.kuali.rice.edl.impl.service.EdlServiceLocator;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.document.node.RouteNodeInstance;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.krad.util.KRADConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Generates document state based on the workflow document in session.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class WorkflowDocumentState implements EDLModelComponent {

	private static final Logger LOG = Logger.getLogger(WorkflowDocumentState.class);

	// The order the enum values are listed determines the order the buttons appear on the screen
	private enum buttons{ACKNOWLEDGE, BLANKETAPPROVE, ROUTE, SAVE, COMPLETE, APPROVE, DISAPPROVE, 
	    RETURNTOPREVIOUS, FYI, CANCEL};
	
	public void updateDOM(Document dom, Element configElement, EDLContext edlContext) {

		try {
			Element documentState = EDLXmlUtils.getDocumentStateElement(dom);

			Element dateTime = EDLXmlUtils.getOrCreateChildElement(documentState, "dateTime", true);
			dateTime.appendChild(dom.createTextNode(RiceConstants.getDefaultDateAndTimeFormat().format(new Date())));

			Element definition = EDLXmlUtils.getOrCreateChildElement(documentState, "definition", true);
			definition.appendChild(dom.createTextNode(edlContext.getEdocLiteAssociation().getDefinition()));

			Element docType = EDLXmlUtils.getOrCreateChildElement(documentState, "docType", true);
			docType.appendChild(dom.createTextNode(edlContext.getEdocLiteAssociation().getEdlName()));

			Element style = EDLXmlUtils.getOrCreateChildElement(documentState, "style", true);
			String styleName = edlContext.getEdocLiteAssociation().getStyle();
			if (styleName == null) {
				styleName = "Default";
			}
			style.appendChild(dom.createTextNode(styleName));

			Element showAttachments = EDLXmlUtils.getOrCreateChildElement(documentState, "showAttachments", true);
			boolean showConstants = CoreFrameworkServiceLocator.getParameterService().getParameterValueAsBoolean(KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.ALL_DETAIL_TYPE, KewApiConstants.SHOW_ATTACHMENTS_IND);

			showAttachments.appendChild(dom.createTextNode(Boolean.valueOf(showConstants).toString()));

			WorkflowDocument document = (WorkflowDocument)edlContext.getRequestParser().getAttribute(RequestParser.WORKFLOW_DOCUMENT_SESSION_KEY);

			boolean documentEditable = false;
			if (document != null) {
				List<String> validActions = determineValidActions(document);
				
				documentEditable = isEditable(edlContext, validActions);
	
				edlContext.getTransformer().setParameter("readOnly", String.valueOf(documentEditable));
				addActions(dom, documentState, validActions);
				boolean isAnnotatable = isAnnotatable(validActions);
				EDLXmlUtils.createTextElementOnParent(documentState, "annotatable", String.valueOf(isAnnotatable));
				EDLXmlUtils.createTextElementOnParent(documentState, "docId", document.getDocumentId());
				Element workflowDocumentStatus = EDLXmlUtils.getOrCreateChildElement(documentState, "workflowDocumentState", true);
				EDLXmlUtils.createTextElementOnParent(workflowDocumentStatus, "status", document.getStatus().getLabel());
				EDLXmlUtils.createTextElementOnParent(workflowDocumentStatus, "createDate", RiceConstants.getDefaultDateAndTimeFormat().format(document.getDateCreated().toDate()));
				List<String> nodeNames = document.getPreviousNodeNames();
				if (nodeNames.size() > 0) {
				    Element previousNodes = EDLXmlUtils.getOrCreateChildElement(documentState, "previousNodes", true);
				    // don't include LAST node (where the document is currently...don't want to return to current location)
				    for (int i = 0; i < nodeNames.size(); i++) {
					EDLXmlUtils.createTextElementOnParent(previousNodes, "node", nodeNames.get(i));
				    }
				}
                List<RouteNodeInstance> routeNodeInstances = KewApiServiceLocator.getWorkflowDocumentService().getCurrentRouteNodeInstances(
                        document.getDocumentId());

				for (RouteNodeInstance currentNode : routeNodeInstances) {
				    EDLXmlUtils.createTextElementOnParent(documentState, "currentNodeName", currentNode.getName());
				}

			}

			Element editable = EDLXmlUtils.getOrCreateChildElement(documentState, "editable", true);
			editable.appendChild(dom.createTextNode(String.valueOf(documentEditable)));

			// display the buttons
			EDLXmlUtils.createTextElementOnParent(documentState, "actionable", "true");

			List globalErrors = (List)edlContext.getRequestParser().getAttribute(RequestParser.GLOBAL_ERRORS_KEY);
			List globalMessages = (List)edlContext.getRequestParser().getAttribute(RequestParser.GLOBAL_MESSAGES_KEY);
			Map<String, String> globalFieldErrors = (Map)edlContext.getRequestParser().getAttribute(RequestParser.GLOBAL_FIELD_ERRORS_KEY);
			EDLXmlUtils.addErrorsAndMessagesToDocument(dom, globalErrors, globalMessages, globalFieldErrors);
            if (LOG.isDebugEnabled()) {
            	LOG.debug("Transforming dom " + XmlJotter.jotNode(dom, true));
            }
		} catch (Exception e) {
			throw new WorkflowRuntimeException(e);
		}
	}

    public static List<String> determineValidActions(WorkflowDocument wfdoc) throws WorkflowException {
        String[] flags = new String[10];
        List<String> list = new ArrayList<String>();
        
        if (wfdoc == null) {
            list.add(UserAction.ACTION_CREATE);
            return list;
        }
        
        if (wfdoc.isAcknowledgeRequested()) {
            flags[buttons.ACKNOWLEDGE.ordinal()] = UserAction.ACTION_ACKNOWLEDGE;
        }
        
        if (wfdoc.isApprovalRequested()) {
            if (wfdoc.isBlanketApproveCapable()) {
                flags[buttons.BLANKETAPPROVE.ordinal()] = UserAction.ACTION_BLANKETAPPROVE;
            }
            if (!wfdoc.isSaved()) {
                flags[buttons.APPROVE.ordinal()] = UserAction.ACTION_APPROVE;
                flags[buttons.DISAPPROVE.ordinal()] = UserAction.ACTION_DISAPPROVE;
            }
            
            // should invoke WorkflowDocument.saveRoutingData(...).
            flags[buttons.SAVE.ordinal()] = UserAction.ACTION_SAVE;
            if (wfdoc.getPreviousNodeNames().size() > 0) {
                flags[buttons.RETURNTOPREVIOUS.ordinal()] = UserAction.ACTION_RETURN_TO_PREVIOUS;
            }
        }
        
        // this will never happen, but left code in case this gets figured out later
        // if allowed to execute save/approve and complete will both show
        else if (wfdoc.isCompletionRequested()) {
            flags[buttons.COMPLETE.ordinal()] = UserAction.ACTION_COMPLETE;
            if (wfdoc.isBlanketApproveCapable()) {
                flags[buttons.BLANKETAPPROVE.ordinal()] = UserAction.ACTION_BLANKETAPPROVE;
            }
        }
        
        if (wfdoc.isFYIRequested()) {
            flags[buttons.FYI.ordinal()] = UserAction.ACTION_FYI;
        }
        
        if (wfdoc.isRouteCapable()) {
            flags[buttons.ROUTE.ordinal()] = UserAction.ACTION_ROUTE;
            if (wfdoc.isBlanketApproveCapable()) {
                flags[buttons.BLANKETAPPROVE.ordinal()] = UserAction.ACTION_BLANKETAPPROVE;
            }
        }
        
        if (wfdoc.isApprovalRequested() || wfdoc.isRouteCapable()) {
            flags[buttons.SAVE.ordinal()] = UserAction.ACTION_SAVE;
        }
        
        if (wfdoc.isCompletionRequested() || wfdoc.isRouteCapable()) {
            flags[buttons.CANCEL.ordinal()] = UserAction.ACTION_CANCEL;
        }

        for (int i = 0; i < flags.length; i++) {
            if (flags[i] != null) {
                list.add(flags[i]);
            }
        }

        return list;
    }
	
	public static boolean isEditable(EDLContext edlContext, List actions) {
	    boolean editable = false;
	    editable = listContainsItems(actions, UserAction.EDITABLE_ACTIONS);
	    // reset editable flag to true if edoclite specifies <param name="alwaysEditable">true</param>
	    Document edlDom = EdlServiceLocator.getEDocLiteService().getDefinitionXml(edlContext.getEdocLiteAssociation());
	    // use xpath to check for attribute value on Config param element.
        XPath xpath = edlContext.getXpath();
        String xpathExpression = "//config/param[@name='alwaysEditable']";
	    try {
		String match = (String) xpath.evaluate(xpathExpression, edlDom, XPathConstants.STRING);
		if (!StringUtils.isBlank(match) && match.equals("true")) {
		    return true;
		}
	    } catch (XPathExpressionException e) {
		throw new WorkflowRuntimeException("Unable to evaluate xpath expression " + xpathExpression, e);
	        }

	    return editable;
	}
	

    public static void addActions(Document dom, Element documentState, List actions) {
        Element actionsPossible = EDLXmlUtils.getOrCreateChildElement(documentState, "actionsPossible", true);
        Iterator it = actions.iterator();
        while (it.hasNext()) {
            String action = it.next().toString();
            Element actionElement = dom.createElement(action);
            // if we use string.xsl we can avoid doing this here
            // (unless for some reason we decide we want different titles)
            if (!Character.isUpperCase(action.charAt(0))) {
                StringBuffer sb = new StringBuffer(action);
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
                action = sb.toString();
            }
            actionElement.setAttribute("title", action);
            actionsPossible.appendChild(actionElement);
        }

        Element annotatable = EDLXmlUtils.getOrCreateChildElement(documentState, "annotatable", true);
        annotatable.appendChild(dom.createTextNode(String.valueOf(isAnnotatable(actions))));
    }




    public static boolean listContainsItems(List list, Object[] items) {
        for (int i = 0; i < items.length; i++) {
            if (list.contains(items[i])) return true;
        }
        return false;
    }

    /**
     * Determines whether to display the annotation text box
     * Currently we will show the annotation box if ANY of the possible actions are
     * annotatable.
     * But what happens if we have an un-annotatable action?
     * Hey, why don't we just make all actions annotatable.
     * @param actions list of possible actions
     * @return whether to show the annotation text box
     */
    public static boolean isAnnotatable(List actions) {
        return listContainsItems(actions, UserAction.ANNOTATABLE_ACTIONS);
    }

}
