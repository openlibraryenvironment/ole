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
import org.kuali.rice.core.api.util.xml.XmlJotter;
import org.kuali.rice.edl.impl.EDLContext;
import org.kuali.rice.edl.impl.EDLModelComponent;
import org.kuali.rice.edl.impl.EDLXmlUtils;
import org.kuali.rice.edl.impl.RequestParser;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.action.ActionTaken;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

/**
 * EDL pipeline component that exposes annotations from the previous array of taken actions in the
 * EDL to render.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AnnotationComponent implements EDLModelComponent {
    private static final Logger LOG = Logger.getLogger(AnnotationComponent.class);

    public void updateDOM(Document dom, Element configElement, EDLContext edlContext) {
        WorkflowDocument document = (WorkflowDocument) edlContext.getRequestParser().getAttribute(
                RequestParser.WORKFLOW_DOCUMENT_SESSION_KEY);

        // insert current annotation into docContent
        Element currentVersion = VersioningPreprocessor.findCurrentVersion(dom);
        String annotation = edlContext.getRequestParser().getParameterValue("annotation");
        if (!StringUtils.isEmpty(annotation)) {
            EDLXmlUtils.createTextElementOnParent(currentVersion, "currentAnnotation", annotation);
        }
        LOG.debug("Inserting annotation: " + annotation);

        List<ActionTaken> actionsTaken = document.getActionsTaken();
        if (actionsTaken != null) {
            // get the current version of data
            // Element currentVersion = VersioningPreprocessor.findCurrentVersion(dom);
            // for every ActionTaken, append every annotation as a child element of EDL data element
            for (ActionTaken actionTaken : actionsTaken) {
                if (actionTaken != null) {
                    annotation = actionTaken.getAnnotation();
                    if (annotation != null) {
                        LOG.debug("Adding annotation: " + annotation);
                        Person person = KimApiServiceLocator.getPersonService().getPerson(actionTaken.getPrincipalId());
                        EDLXmlUtils.createTextElementOnParent(currentVersion, "annotation", person.getName() + ": "
                                + annotation);
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("dom: " + XmlJotter.jotNode(dom));
                        }
                    }
                }
            }
        }
    }
}
