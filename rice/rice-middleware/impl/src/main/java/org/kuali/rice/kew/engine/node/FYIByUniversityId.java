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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.RouteHelper;
import org.kuali.rice.kew.exception.WorkflowServiceErrorException;
import org.kuali.rice.kew.routeheader.DocumentContent;
import org.kuali.rice.kew.routeheader.StandardDocumentContent;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


/**
 * A node which will generate an FYI request to a university ID specified in the document content.
 *
 * @deprecated Use {@link org.kuali.rice.kew.rule.UniversityIdRoleAttribute} instead
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class FYIByUniversityId extends RequestActivationNode {
    private static final Logger LOG = Logger.getLogger(FYIByUniversityId.class);

	public SimpleResult process(RouteContext context, RouteHelper helper)
			throws Exception {

        LOG.debug("processing FYIByUniversityId node");
        Element rootElement = getRootElement(new StandardDocumentContent(context.getDocument().getDocContent()));
 		Collection<Element> fieldElements = XmlHelper.findElements(rootElement, "field");
        Iterator<Element> elementIter = fieldElements.iterator();
        while (elementIter.hasNext()) {
        	Element field = (Element) elementIter.next();
        	Element version = field.getParentElement();
        	if (version.getAttribute("current").getValue().equals("true")) {
        		LOG.debug("Looking for studentUid field:  " + field.getAttributeValue("name"));
               	if (field.getAttribute("name")!= null && field.getAttributeValue("name").equals("studentUid")) {
               		String employeeId = field.getChildText("value");
            		LOG.debug("Should send an FYI to employee ID:  " + employeeId);
               		if (!StringUtils.isBlank(employeeId)) {
               			Person person = KimApiServiceLocator.getPersonService().getPerson(employeeId);

               			if (person == null) {
               				throw new WorkflowRuntimeException("Failed to locate a Person with the given employee ID: " + employeeId);
               			}
               			if (!context.isSimulation()) {
               				KEWServiceLocator.getWorkflowDocumentService().adHocRouteDocumentToPrincipal(person.getPrincipalId(), context.getDocument(), KewApiConstants.ACTION_REQUEST_FYI_REQ, null, null, "Notification Request", person.getPrincipalId(), "Notification Request", true, null);
               			}
               			//wfDoc.adHocRouteDocumentToPrincipal(KewApiConstants.ACTION_REQUEST_FYI_REQ, "Notification Request", new EmplIdVO(field.getChildText("value")), "Notification Request", true);
                		LOG.debug("Sent FYI using the adHocRouteDocumentToPrincipal function to UniversityID:  " + person.getEmployeeId());
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


