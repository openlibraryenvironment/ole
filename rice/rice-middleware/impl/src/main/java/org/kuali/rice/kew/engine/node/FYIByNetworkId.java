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
package org.kuali.rice.kew.engine.node;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.RouteHelper;
import org.kuali.rice.kew.exception.WorkflowServiceErrorException;
import org.kuali.rice.kew.routeheader.DocumentContent;
import org.kuali.rice.kew.routeheader.StandardDocumentContent;
import org.kuali.rice.kew.rule.NetworkIdRoleAttribute;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


/**
 * A node which will generate an FYI request to a network ID specified in the document content.
 *
 * @deprecated Use {@link NetworkIdRoleAttribute} instead
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class FYIByNetworkId extends RequestActivationNode {
    private static final Logger LOG = Logger.getLogger(FYIByNetworkId.class);

	public SimpleResult process(RouteContext context, RouteHelper helper)
			throws Exception {

        LOG.debug("processing FYIByNetworkId simple node");
        String documentId = context.getDocument().getDocumentId();
        Element rootElement = getRootElement(new StandardDocumentContent(context.getDocument().getDocContent()));
		Collection<Element> fieldElements = XmlHelper.findElements(rootElement, "field");
        Iterator<Element> elementIter = fieldElements.iterator();
        while (elementIter.hasNext()) {
        	Element field = (Element) elementIter.next();
        	Element version = field.getParentElement();
        	if (version.getAttribute("current").getValue().equals("true")) {
        		LOG.debug("Looking for networkId field:  " + field.getAttributeValue("name"));
               	if (field.getAttribute("name")!= null && field.getAttributeValue("name").equals("networkId")) {
            		LOG.debug("Should send an FYI to netID:  " + field.getChildText("value"));
               		if (field.getChildText("value") != null) {
               			Person user = KimApiServiceLocator.getPersonService().getPersonByPrincipalName(field.getChildText("value"));

               			//WorkflowDocument wfDoc = new WorkflowDocument(new NetworkIdVO(field.getChildText("value")), documentId);
               			if (!context.isSimulation()) {
                   			KEWServiceLocator.getWorkflowDocumentService().adHocRouteDocumentToPrincipal(user.getPrincipalId(), context.getDocument(), KewApiConstants.ACTION_REQUEST_FYI_REQ, null, null, "Notification Request", user.getPrincipalId(), "Notification Request", true, null);
               		}
               			//wfDoc.adHocRouteDocumentToPrincipal(KewApiConstants.ACTION_REQUEST_FYI_REQ, "Notification Request", new NetworkIdVO(field.getChildText("value")), "Notification Request", true);
                		LOG.debug("Sent FYI using the adHocRouteDocumentToPrincipal function to NetworkID:  " + user.getPrincipalName());
                               	break;
               	}
        	}
        }
        }
		return super.process(context, helper);
	}


    private static Element getRootElement(DocumentContent docContent) {
        Element rootElement = null;
        try {
            rootElement = XmlHelper.buildJDocument(docContent.getDocument()).getRootElement();
        } catch (Exception e) {
            throw new WorkflowServiceErrorException("Invalid XML submitted", new ArrayList<Object>());
        }
        return rootElement;
    }


	protected Object getService(String serviceName) {
		return KEWServiceLocator.getService(serviceName);
	}


}
