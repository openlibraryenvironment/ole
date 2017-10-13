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
package org.kuali.rice.kim.api.common.assignee;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kim.api.common.delegate.DelegateType;
import org.kuali.rice.kim.api.common.delegate.DelegateTypeContract;
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
import java.util.Collections;
import java.util.List;


@XmlRootElement(name = Assignee.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = Assignee.Constants.TYPE_NAME, propOrder = {
        Assignee.Elements.PRINCIPAL_ID,
        Assignee.Elements.GROUP_ID,
        Assignee.Elements.DELEGATES,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class Assignee extends AbstractDataTransferObject implements AssigneeContract {
    @XmlElement(name = Elements.PRINCIPAL_ID, required = false)
    private final String principalId;

    @XmlElement(name = Elements.GROUP_ID, required = true)
    private final String groupId;

    @XmlElementWrapper(name = Elements.DELEGATES, required = false)
    @XmlElement(name = Elements.DELEGATE, required = false)
    private final List<DelegateType> delegates;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
	 *  A constructor to be used only by JAXB unmarshalling.
	 *  
	 */
    private Assignee() {
        this.principalId = null;
        this.groupId = null;
        this.delegates = null;
    }
 
    /**
	 * A constructor using the Builder.
	 * 
	 * @param builder
	 */
    public Assignee(Builder builder) {
        this.principalId = builder.getPrincipalId();
        this.groupId = builder.getGroupId();
        final List<DelegateType> temp = new ArrayList<DelegateType>();
        if (!CollectionUtils.isEmpty(builder.getDelegates())) {
            for (DelegateType.Builder delegate: builder.getDelegates()) {
                temp.add(delegate.build());
            }
        }
        this.delegates = Collections.unmodifiableList(temp);
    }

	/**
	 * @see AssigneeContract#getPrincipalId()
	 */
	@Override
	public String getPrincipalId() {
		return this.principalId;
	}

	/**
	 * @see AssigneeContract#getGroupId()
	 */
	@Override
	public String getGroupId() {
		return this.groupId;
	}

	/**
	 * @see AssigneeContract#getDelegates()
	 */
	@Override
	public List<DelegateType> getDelegates() {
		return this.delegates;
	}

    /**
     * This builder constructs a PermissionAssignee enforcing the constraints of the {@link AssigneeContract}.
     */
    public static final class Builder implements AssigneeContract, ModelBuilder, Serializable {
        private String principalId;
        private String groupId;
        private List<DelegateType.Builder> delegates;

        private Builder(String principalId, String groupId, List<DelegateType.Builder> delegates) {
            setPrincipalId(principalId);
            setGroupId(groupId);
            setDelegates(delegates);
        }

        /**
         * Creates a KimAttributeData with the required fields.
         */
        public static Builder create(String principalId, String groupId, List<DelegateType.Builder> delegates) {
            return new Builder(principalId, groupId, delegates);
        }

        /**
         * creates a KimAttributeData from an existing {@link org.kuali.rice.kim.api.common.attribute.KimAttributeDataContract}.
         */
        public static Builder create(AssigneeContract contract) {
            final List<DelegateType.Builder> builders = new ArrayList<DelegateType.Builder>();
            for (DelegateTypeContract d : contract.getDelegates()) {
                builders.add(DelegateType.Builder.create(d));
            }

            Builder builder = new Builder(contract.getPrincipalId(), contract.getGroupId(), builders);
            return builder;
        }

        @Override
        public String getPrincipalId() {
            return principalId;
        }

        public void setPrincipalId(final String principalId) {
            this.principalId = principalId;
        }

        @Override
        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(final String groupId) {
        	this.groupId = groupId;
        }

		@Override
		public List<DelegateType.Builder> getDelegates() {
			return delegates;
		}
		
        public void setDelegates(final List<DelegateType.Builder> delegates) {
        	this.delegates = Collections.unmodifiableList(new ArrayList<DelegateType.Builder>(delegates));
        }		
		
		@Override
		public Assignee build() {
		    //validate required fields
            final boolean requiredSet = (groupId != null ^ principalId != null) && delegates != null;
            if (!requiredSet) {
                throw new IllegalStateException("all the required fields are not set");
            }

			return new Assignee(this);
		}
       
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "assignee";
        final static String TYPE_NAME = "assigneeType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use
     * when this object is marshalled to XML.
     */
    static class Elements {
        final static String PRINCIPAL_ID = "principalId";
        final static String GROUP_ID = "groupId";
        final static String DELEGATES = "delegates";
        final static String DELEGATE = "delegate";
    }
}
