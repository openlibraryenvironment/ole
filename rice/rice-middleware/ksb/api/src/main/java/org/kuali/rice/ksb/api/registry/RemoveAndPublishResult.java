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
package org.kuali.rice.ksb.api.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.w3c.dom.Element;

/**
 * Wraps information resulting from a "removeAndPublish" operation on the
 * registry.  Effectively contains a list of {@link ServiceEndpoint} instances
 * for services that were published and those that were removed
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@XmlRootElement(name = RemoveAndPublishResult.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RemoveAndPublishResult.Constants.TYPE_NAME, propOrder = {
		RemoveAndPublishResult.Elements.SERVICES_REMOVED,
		RemoveAndPublishResult.Elements.SERVICES_PUBLISHED,
		CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class RemoveAndPublishResult extends AbstractDataTransferObject {

	private static final long serialVersionUID = 4279564869510247725L;

	@XmlElementWrapper(name = Elements.SERVICES_REMOVED, required = false)
	@XmlElement(name = Elements.SERVICE_REMOVED, required = false)
	private final List<ServiceEndpoint> servicesRemoved;
	
	@XmlElementWrapper(name = Elements.SERVICES_PUBLISHED, required = false)
	@XmlElement(name = Elements.SERVICE_PUBLISHED, required = false)
	private final List<ServiceEndpoint> servicesPublished;
	
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

	private RemoveAndPublishResult() {
		this.servicesRemoved = null;
		this.servicesPublished = null;
	}
	
	private RemoveAndPublishResult(List<ServiceEndpoint> servicesRemoved, List<ServiceEndpoint> servicesPublished) {
		this.servicesRemoved = servicesRemoved;
		this.servicesPublished = servicesPublished;
	}
	
	/**
	 * Creates a new {@code RemoveAndPublishResult} from the given lists of services removed and published.
	 * 
	 * @param servicesRemoved the list of services removed by the operation, can be a null or empty list
	 * @param servicesPublished the list of services published by the operation, can be a null or empty list
	 * 
	 * @return the constructed {@code RemoveAndPublishResult}, should never return null
	 */
	public static RemoveAndPublishResult create(List<ServiceEndpoint> servicesRemoved, List<ServiceEndpoint> servicesPublished) {
		return new RemoveAndPublishResult(new ArrayList<ServiceEndpoint>(servicesRemoved),
				new ArrayList<ServiceEndpoint>(servicesPublished));
	}

	/**
	 * Returns an unmodifiable list of services that were removed as the result
	 * of a remove-and-publish operation.
	 * 
	 * @return an unmodifiable list of removed services, will never be null but
	 * may be empty if no services were removed
	 */
	public List<ServiceEndpoint> getServicesRemoved() {
		return Collections.unmodifiableList(servicesRemoved);
	}

	/**
	 * Returns an unmodifiable list of services that were published as the result
	 * of a remove-and-publish operation.
	 * 
	 * @return an unmodifiable list of published services, will never be null but
	 * may be empty if no services were published
	 */
	public List<ServiceEndpoint> getServicesPublished() {
		return Collections.unmodifiableList(servicesPublished);
	}
	
	/**
     * Defines some internal constants used on this class.
     */
    static class Constants {

    	final static String ROOT_ELEMENT_NAME = "removeAndPublishResult";
        final static String TYPE_NAME = "RemoveAndPublishResultType";
    }
	
	/**
     * Exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {

        final static String SERVICES_REMOVED = "servicesRemoved";
        final static String SERVICE_REMOVED = "serviceRemoved";
        final static String SERVICES_PUBLISHED = "servicesPublished";
        final static String SERVICE_PUBLISHED = "servicePublished";

    }
    
}
