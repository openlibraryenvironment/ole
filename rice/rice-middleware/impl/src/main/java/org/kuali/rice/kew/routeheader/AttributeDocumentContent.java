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
package org.kuali.rice.kew.routeheader;

import org.kuali.rice.core.api.util.xml.XmlException;
import org.kuali.rice.kew.rule.WorkflowRuleAttribute;
import org.kuali.rice.kew.rule.web.RoutingReportAction;
import org.kuali.rice.kew.api.KewApiConstants;

import java.util.Iterator;
import java.util.List;


/**
 * {@link DocumentContent} which is generated from a List of attributes.
 * Used by the {@link RoutingReportAction} to aid in generation of 
 * document content when running routing reports.
 * 
 * @see org.kuali.rice.kew.rule.WorkflowRuleAttribute
 * @see RoutingReportAction
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AttributeDocumentContent extends StandardDocumentContent {
    
	private static final long serialVersionUID = 6789132279492877000L;

	public AttributeDocumentContent(List attributes) throws XmlException {
        super(generateDocContent(attributes));
    }
    
    private static String generateDocContent(List attributes) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<").append(KewApiConstants.DOCUMENT_CONTENT_ELEMENT).append(">");
        buffer.append("<").append(KewApiConstants.ATTRIBUTE_CONTENT_ELEMENT).append(">");
        for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
            WorkflowRuleAttribute attribute = (WorkflowRuleAttribute) iterator.next();
            buffer.append(attribute.getDocContent());
        }
        buffer.append("</").append(KewApiConstants.ATTRIBUTE_CONTENT_ELEMENT).append(">");
        buffer.append("</").append(KewApiConstants.DOCUMENT_CONTENT_ELEMENT).append(">");
        return buffer.toString();
    }

}
