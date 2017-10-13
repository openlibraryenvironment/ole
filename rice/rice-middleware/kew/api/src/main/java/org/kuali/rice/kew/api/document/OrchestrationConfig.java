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
package org.kuali.rice.kew.api.document;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelObjectUtils;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@XmlRootElement(name = OrchestrationConfig.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = OrchestrationConfig.Constants.TYPE_NAME, propOrder = {
        OrchestrationConfig.Elements.ACTION_TAKEN_ID,
        OrchestrationConfig.Elements.NODE_NAMES,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class OrchestrationConfig extends AbstractDataTransferObject {

    @XmlElement(name = Elements.ACTION_TAKEN_ID, required = true)
    private final String actionTakenId;

    @XmlElementWrapper(name = Elements.NODE_NAMES, required = false)
    @XmlElement(name = Elements.NODE_NAME, required = false)
    private final Set<String> nodeNames;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    @SuppressWarnings("unused")
    private OrchestrationConfig() {
        this.actionTakenId = null;
        this.nodeNames = null;
    }

    private OrchestrationConfig(String actionTakenId, Set<String> nodeNames) {
        this.actionTakenId = actionTakenId;
        this.nodeNames = ModelObjectUtils.createImmutableCopy(nodeNames);
    }

    public static OrchestrationConfig create(String actionTakenId, Set<String> nodeNames) {
        return new OrchestrationConfig(actionTakenId, nodeNames);
    }

    /**
     * Returns the id of the {@link org.kuali.rice.kew.api.action.ActionTaken} against which any action
     * requests that are processed by the orchestration should be associated.  This is generally the action that was
     * submitted which triggered the orchestration process.  Should never return a null or blank value.
     *
     * @return the id of the action taken to link deactivated requests with during orchestration
     */
    public String getActionTakenId() {
        return actionTakenId;
    }

    /**
     * Returns the Set of node names to which to orchestrate the document, may be an empty set in which case the
     * document will be orchestrated from it's current position through the termination of it's entire workflow process.
     *
     * @return the set of node names to which to orchestrate the document, will never be null but may be empty
     */
    public Set<String> getNodeNames() {
        return nodeNames;
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "orchestrationConfig";
        final static String TYPE_NAME = "OrchestrationConfigType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled
     * to XML.
     */
    static class Elements {
        final static String ACTION_TAKEN_ID = "actionTakenId";
        final static String NODE_NAMES = "nodeNames";
        final static String NODE_NAME = "nodeName";
    }
    
}
