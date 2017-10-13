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
package org.kuali.rice.kew.api.doctype;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.util.collect.CollectionUtils;
import org.kuali.rice.kew.api.KewApiConstants;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@XmlRootElement(name = RoutePath.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RoutePath.Constants.TYPE_NAME, propOrder = {
        RoutePath.Elements.PROCESSES,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class RoutePath extends AbstractDataTransferObject implements RoutePathContract {

    private static final long serialVersionUID = -7177305375323986864L;

    @XmlElementWrapper(name = Elements.PROCESSES, required = false)
    @XmlElement(name = Elements.PROCESS, required = false)
    private final List<ProcessDefinition> processDefinitions;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    private RoutePath() {
        this.processDefinitions = null;
    }

    private RoutePath(Builder builder) {
        this.processDefinitions = new ArrayList<ProcessDefinition>();
        if (builder.getProcessDefinitions() != null) {
            for (ProcessDefinition.Builder processBuilder : builder.getProcessDefinitions()) {
                this.processDefinitions.add(processBuilder.build());
            }
        }
    }
    
    public ProcessDefinition getPrimaryProcess() {
        for (ProcessDefinition processDefinition : processDefinitions) {
            if (processDefinition.isInitial()) {
                return processDefinition;
            }
        }        
        return null;
    }

    @Override
    public List<ProcessDefinition> getProcessDefinitions() {
        return CollectionUtils.unmodifiableListNullSafe(this.processDefinitions);
    }

    /**
     * A builder which can be used to construct {@link RoutePath} instances. Enforces the
     * constraints of the {@link RoutePathContract}.
     */
    public final static class Builder implements Serializable, ModelBuilder, RoutePathContract {

        private static final long serialVersionUID = -6916424305298043710L;

        private List<ProcessDefinition.Builder> processes;

        private Builder() {}

        public static Builder create() {
            Builder builder = new Builder();
            builder.setProcesses(new ArrayList<ProcessDefinition.Builder>());
            return builder;
        }

        public static Builder create(RoutePathContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            List<ProcessDefinition.Builder> processBuilders = new ArrayList<ProcessDefinition.Builder>();
            for (ProcessDefinitionContract process : contract.getProcessDefinitions()) {
                processBuilders.add(ProcessDefinition.Builder.create(process));
            }
            builder.setProcesses(processBuilders);
            return builder;
        }

        public RoutePath build() {
            return new RoutePath(this);
        }

        @Override
        public List<ProcessDefinition.Builder> getProcessDefinitions() {
            return this.processes;
        }

        public void setProcesses(List<ProcessDefinition.Builder> processes) {
            this.processes = processes;
        }

    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "routePath";
        final static String TYPE_NAME = "RoutePathType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this
     * object is marshalled to XML.
     */
    static class Elements {
        final static String PROCESSES = "processDefinitions";
        final static String PROCESS = "processDefinition";
    }

    public static class Cache {
        public static final String NAME = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0 + "/" + RoutePath.Constants.TYPE_NAME;
    }

}
