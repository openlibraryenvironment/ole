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
package org.kuali.rice.kew.api.peopleflow;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.mo.ModelObjectUtils;
import org.kuali.rice.kew.api.action.ActionRequestPolicy;
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

@XmlRootElement(name = PeopleFlowMember.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = PeopleFlowMember.Constants.TYPE_NAME, propOrder = {
        PeopleFlowMember.Elements.MEMBER_ID,
        PeopleFlowMember.Elements.MEMBER_TYPE,
        PeopleFlowMember.Elements.ACTION_REQUEST_POLICY,
        PeopleFlowMember.Elements.RESPONSIBILITY_ID,
        PeopleFlowMember.Elements.PRIORITY,
        PeopleFlowMember.Elements.DELEGATES,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class PeopleFlowMember extends AbstractDataTransferObject implements PeopleFlowMemberContract {

    private static final int STARTING_PRIORITY = 1;

    @XmlElement(name = Elements.MEMBER_ID, required = true)
    private final String memberId;

    @XmlElement(name = Elements.MEMBER_TYPE, required = true)
    private final MemberType memberType;

    @XmlElement(name = Elements.ACTION_REQUEST_POLICY, required = false)
    private final ActionRequestPolicy actionRequestPolicy;

    @XmlElement(name = Elements.RESPONSIBILITY_ID, required = false)
    private final String responsibilityId;

    @XmlElement(name = Elements.PRIORITY, required = true)
    private final int priority;

    @XmlElementWrapper(name = Elements.DELEGATES, required = false)
    @XmlElement(name = Elements.DELEGATE, required = false)
    private final List<PeopleFlowDelegate> delegates;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    private PeopleFlowMember() {
        this.memberId = null;
        this.memberType = null;
        this.actionRequestPolicy = null;
        this.responsibilityId = null;
        this.priority = STARTING_PRIORITY;
        this.delegates = null;
    }

    private PeopleFlowMember(Builder builder) {
        this.memberId = builder.getMemberId();
        this.memberType = builder.getMemberType();
        this.actionRequestPolicy = builder.getActionRequestPolicy();
        this.responsibilityId = builder.getResponsibilityId();
        this.priority = builder.getPriority();
        this.delegates = ModelObjectUtils.buildImmutableCopy(builder.getDelegates());
    }

    @Override
    public String getMemberId() {
        return this.memberId;
    }

    @Override
    public MemberType getMemberType() {
        return this.memberType;
    }

    @Override
    public ActionRequestPolicy getActionRequestPolicy() {
        return this.actionRequestPolicy;
    }

    @Override
    public String getResponsibilityId() {
        return this.responsibilityId;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    @Override
    public List<PeopleFlowDelegate> getDelegates() {
        return this.delegates;
    }

    /**
     * A builder which can be used to construct {@link PeopleFlowMember} instances.  Enforces the constraints of the
     * {@link PeopleFlowMemberContract}.
     */
    public final static class Builder implements Serializable, ModelBuilder, PeopleFlowMemberContract {

        private String memberId;
        private MemberType memberType;
        private ActionRequestPolicy actionRequestPolicy;
        private String responsibilityId;
        private int priority;
        private List<PeopleFlowDelegate.Builder> delegates;

        private Builder(String memberId, MemberType memberType) {
            setMemberId(memberId);
            setMemberType(memberType);
            setPriority(STARTING_PRIORITY);
            setDelegates(new ArrayList<PeopleFlowDelegate.Builder>());
        }

        public static Builder create(String memberId, MemberType memberType) {
            return new Builder(memberId, memberType);
        }

        public static Builder create(PeopleFlowMemberContract contract) {
            Builder builder = createCopy(contract);

            builder.setResponsibilityId(contract.getResponsibilityId());
            return builder;
        }

        public static Builder createCopy(PeopleFlowMemberContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create(contract.getMemberId(), contract.getMemberType());
            builder.setActionRequestPolicy(contract.getActionRequestPolicy());
            builder.setPriority(contract.getPriority());
            if (CollectionUtils.isNotEmpty(contract.getDelegates())) {
                for (PeopleFlowDelegateContract delegate : contract.getDelegates()) {
                    builder.getDelegates().add(PeopleFlowDelegate.Builder.create(delegate));
                }
            }
            return builder;
        }

        public PeopleFlowMember build() {
            return new PeopleFlowMember(this);
        }

        @Override
        public String getMemberId() {
            return this.memberId;
        }

        @Override
        public MemberType getMemberType() {
            return this.memberType;
        }

        @Override
        public ActionRequestPolicy getActionRequestPolicy() {
            return this.actionRequestPolicy;
        }

        @Override
        public String getResponsibilityId() {
            return this.responsibilityId;
        }

        @Override
        public int getPriority() {
            return this.priority;
        }

        @Override
        public List<PeopleFlowDelegate.Builder> getDelegates() {
            return delegates;
        }

        public void setMemberId(String memberId) {
            if (StringUtils.isBlank(memberId)) {
                throw new IllegalArgumentException("memberId was null or blank");
            }
            this.memberId = memberId;
        }

        public void setMemberType(MemberType memberType) {
            if (memberType == null) {
                throw new IllegalArgumentException("memberType was null");
            }
            this.memberType = memberType;
        }

        public void setActionRequestPolicy(ActionRequestPolicy actionRequestPolicy) {
            if (this.memberType.equals(MemberType.ROLE)) {
                if (actionRequestPolicy == null) {
                    throw new IllegalArgumentException("actionRequestPolicy was null");
                }
                this.actionRequestPolicy = actionRequestPolicy;
            }
        }

        public void setResponsibilityId(String responsibilityId) {
            this.responsibilityId = responsibilityId;
        }

        public void setPriority(int priority) {
            if (priority < STARTING_PRIORITY) {
                throw new IllegalArgumentException("Given priority was smaller than the minimum prior value of " + STARTING_PRIORITY);
            }
            this.priority = priority;
        }

        public void setDelegates(List<PeopleFlowDelegate.Builder> delegates) {
            this.delegates = delegates;
        }
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "peopleFlowMember";
        final static String TYPE_NAME = "PeopleFlowMemberType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String MEMBER_ID = "memberId";
        final static String MEMBER_TYPE = "memberType";
        final static String ACTION_REQUEST_POLICY = "actionRequestPolicy";
        final static String RESPONSIBILITY_ID = "responsibilityId";
        final static String PRIORITY = "priority";
        final static String DELEGATES = "delegates";
        final static String DELEGATE = "delegate";
    }

}
