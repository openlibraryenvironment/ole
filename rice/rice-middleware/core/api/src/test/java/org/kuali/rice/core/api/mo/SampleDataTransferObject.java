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
package org.kuali.rice.core.api.mo;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * A sample {@code AbstractDataTransferObject} use in tests.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = "sampleDataTransferObject", namespace = CoreConstants.Namespaces.CORE_NAMESPACE_2_0)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "SampleDataTransferObjectType", namespace = CoreConstants.Namespaces.CORE_NAMESPACE_2_0, propOrder = {
        "name", "values", "attributes", CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class SampleDataTransferObject extends AbstractDataTransferObject {

    @XmlElement(name = "name", required = false)
    private final String name;

    @XmlElementWrapper(name = "values", required = false)
    @XmlElement(name = "value", required = false)
    private final List<String> values;

    @XmlElement(name = "attributes", required = false)
    @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
    private final Map<String, String> attributes;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * This constructor should never be called.
     * It is only present for use during JAXB unmarshalling.
     */
    private SampleDataTransferObject() {
    	this.name = null;
        this.values = null;
        this.attributes = null;
    }

    public SampleDataTransferObject(String name, List<String> values, Map<String, String> attributes) {
        this.name = name;
        this.values = values;
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public List<String> getValues() {
        return values;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }
    
}
