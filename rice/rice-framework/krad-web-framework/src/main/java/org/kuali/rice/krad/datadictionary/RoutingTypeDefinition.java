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
package org.kuali.rice.krad.datadictionary;

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;

import java.util.List;

/**
 * This is a description of what this class does - mpham don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "routingTypeDefinition-bean")
public class RoutingTypeDefinition extends DataDictionaryDefinitionBase {
    private static final long serialVersionUID = -5455042765223753531L;

    private List<RoutingAttribute> routingAttributes;
    private List<DocumentValuePathGroup> documentValuePathGroups;

    /**
     * @return the routingAttributes
     */
    @BeanTagAttribute(name = "routingAttributes", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<RoutingAttribute> getRoutingAttributes() {
        return this.routingAttributes;
    }

    /**
     * @return the documentValuePathGroups
     */
    @BeanTagAttribute(name = "documentValuePathGroups", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<DocumentValuePathGroup> getDocumentValuePathGroups() {
        return this.documentValuePathGroups;
    }

    /**
     * @param routingAttributes the routingAttributes to set
     */
    public void setRoutingAttributes(List<RoutingAttribute> routingAttributes) {
        this.routingAttributes = routingAttributes;
    }

    /**
     * @param documentValuePathGroups the documentValuePathGroups to set
     */
    public void setDocumentValuePathGroups(List<DocumentValuePathGroup> documentValuePathGroups) {
        this.documentValuePathGroups = documentValuePathGroups;
    }

    /**
     * This overridden method ...
     *
     * @see org.kuali.rice.krad.datadictionary.DataDictionaryDefinition#completeValidation(java.lang.Class,
     *      java.lang.Class)
     */
    public void completeValidation(Class rootBusinessObjectClass, Class otherBusinessObjectClass) {
        // TODO wliang - THIS METHOD NEEDS JAVADOCS

    }

}
