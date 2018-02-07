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
package org.kuali.rice.kew.api.action;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Collection;


/**
 * A transport object representing an action a user might take
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = RoutingReportActionToTake.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RoutingReportActionToTake.Constants.TYPE_NAME, propOrder = {
    RoutingReportActionToTake.Elements.ACTION_TO_PERFORM,
    RoutingReportActionToTake.Elements.PRINCIPAL_ID,
    RoutingReportActionToTake.Elements.NODE_NAME,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class RoutingReportActionToTake
    extends AbstractDataTransferObject
    implements RoutingReportActionToTakeContract
{

    @XmlElement(name = Elements.ACTION_TO_PERFORM, required = false)
    private final String actionToPerform;
    @XmlElement(name = Elements.PRINCIPAL_ID, required = false)
    private final String principalId;
    @XmlElement(name = Elements.NODE_NAME, required = false)
    private final String nodeName;
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     * 
     */
    private RoutingReportActionToTake() {
        this.actionToPerform = null;
        this.principalId = null;
        this.nodeName = null;
    }

    private RoutingReportActionToTake(Builder builder) {
        this.actionToPerform = builder.getActionToPerform();
        this.principalId = builder.getPrincipalId();
        this.nodeName = builder.getNodeName();
    }

    @Override
    public String getActionToPerform() {
        return this.actionToPerform;
    }

    @Override
    public String getPrincipalId() {
        return this.principalId;
    }

    @Override
    public String getNodeName() {
        return this.nodeName;
    }


    /**
     * A builder which can be used to construct {@link RoutingReportActionToTake} instances.  Enforces the constraints of the {@link RoutingReportActionToTakeContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, RoutingReportActionToTakeContract
    {

        private String actionToPerform;
        private String principalId;
        private String nodeName;

        private Builder(String actionToPerform, String principalId, String nodeName) {
            this.setActionToPerform(actionToPerform);
            this.setPrincipalId(principalId);
            this.setNodeName(nodeName);
        }

        public static Builder create(String actionToPerform, String principalId, String nodeName) {
            return new Builder(actionToPerform, principalId, nodeName);
        }

        public static Builder create(RoutingReportActionToTakeContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            return create(contract.getActionToPerform(), contract.getPrincipalId(), contract.getNodeName());
        }

        public RoutingReportActionToTake build() {
            return new RoutingReportActionToTake(this);
        }

        @Override
        public String getActionToPerform() {
            return this.actionToPerform;
        }

        @Override
        public String getPrincipalId() {
            return this.principalId;
        }

        @Override
        public String getNodeName() {
            return this.nodeName;
        }

        public void setActionToPerform(String actionToPerform) {
            this.actionToPerform = actionToPerform;
        }

        public void setPrincipalId(String principalId) {
            this.principalId = principalId;
        }

        public void setNodeName(String nodeName) {
            this.nodeName = nodeName;
        }

    }


    /**
     * Defines some internal constants used on this class.
     * 
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "routingReportActionToTake";
        final static String TYPE_NAME = "RoutingReportActionToTakeType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String ACTION_TO_PERFORM = "actionToPerform";
        final static String PRINCIPAL_ID = "principalId";
        final static String NODE_NAME = "nodeName";

    }

}