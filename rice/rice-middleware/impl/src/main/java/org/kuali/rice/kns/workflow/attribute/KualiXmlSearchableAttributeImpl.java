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
package org.kuali.rice.kns.workflow.attribute;

import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.docsearch.xml.StandardGenericXMLSearchableAttribute;
import org.w3c.dom.Element;


/**
 * This class is used to define Kuali searchable attributes
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KualiXmlSearchableAttributeImpl extends StandardGenericXMLSearchableAttribute implements KualiXmlAttribute {
    private static final long serialVersionUID = -5759823164605651979L;

	/**
     * Constructs a KualiXmlRuleAttributeImpl.java.
     */
    public KualiXmlSearchableAttributeImpl() {
        super();
    }

    /**
     * This method overrides the super class and modifies the XML that it operates on to put the name and the title in the place
     * where the super class expects to see them, even though they may no longer exist in the original XML.
     */
    @Override
    public Element getConfigXML(ExtensionDefinition extensionDefinition) {
        Element root = super.getConfigXML(extensionDefinition);
        KualiXmlAttributeHelper attributeHelper = new KualiXmlAttributeHelper();
        // this adds the name and title to the xml based on the data dictionary
        return attributeHelper.processConfigXML(root);
    }

}
