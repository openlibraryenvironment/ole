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

import org.kuali.rice.edl.impl.EDLContext;
import org.kuali.rice.edl.impl.EDLModelComponent;
import org.kuali.rice.edl.impl.EDLXmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * This class exists solely to propagate the javascript element content into the edl
 * element of the dom destined to be transformed so the transform can include the specified
 * javascript.  This is because the EDL definition itself is no longer present in this dom.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class JavascriptEDLComponent implements EDLModelComponent {

    public void updateDOM(Document dom, Element configElement, EDLContext edlContext) {
        
        Element edlElement = EDLXmlUtils.getEDLContent(dom, false);
        Element edlSubElement = EDLXmlUtils.getOrCreateChildElement(edlElement, "edl", true);
        
        Node n = dom.importNode(configElement, true);
        edlSubElement.appendChild(n);
    }
}

