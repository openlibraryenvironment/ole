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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.util.jaxb.DateTimeAdapter;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

@XmlRootElement(name = ActionItem.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = ActionItem.Constants.TYPE_NAME, propOrder = {
    ActionItem.Elements.ID,
    ActionItem.Elements.DATE_TIME_ASSIGNED,
    ActionItem.Elements.ACTION_REQUEST_CD,
    ActionItem.Elements.ACTION_REQUEST_ID,
    ActionItem.Elements.DOCUMENT_ID,
    ActionItem.Elements.DOC_TITLE,
    ActionItem.Elements.DOC_LABEL,
    ActionItem.Elements.DOC_HANDLER_U_R_L,
    ActionItem.Elements.DOC_NAME,
    ActionItem.Elements.RESPONSIBILITY_ID,
    ActionItem.Elements.ROLE_NAME,
    ActionItem.Elements.DELEGATION_TYPE,
    ActionItem.Elements.GROUP_ID,
    ActionItem.Elements.PRINCIPAL_ID,
    ActionItem.Elements.DELEGATOR_GROUP_ID,
    ActionItem.Elements.DELEGATOR_PRINCIPAL_ID,
    ActionItem.Elements.DATE_TIME_ASSIGNED_VALUE,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class ActionItem
    extends AbstractDataTransferObject
    implements ActionItemContract
{

    @XmlElement(name = Elements.ID, required = false)
    private final String id;
    @Deprecated
    @XmlElement(name = Elements.DATE_TIME_ASSIGNED, required = false)
    private final DateTime dateTimeAssigned;
    @XmlElement(name = Elements.ACTION_REQUEST_CD, required = true)
    private final String actionRequestCd;
    @XmlElement(name = Elements.ACTION_REQUEST_ID, required = true)
    private final String actionRequestId;
    @XmlElement(name = Elements.DOCUMENT_ID, required = true)
    private final String documentId;
    @XmlElement(name = Elements.DOC_TITLE, required = false)
    private final String docTitle;
    @XmlElement(name = Elements.DOC_LABEL, required = true)
    private final String docLabel;
    @XmlElement(name = Elements.DOC_HANDLER_U_R_L, required = true)
    private final String docHandlerURL;
    @XmlElement(name = Elements.DOC_NAME, required = true)
    private final String docName;
    @XmlElement(name = Elements.RESPONSIBILITY_ID, required = true)
    private final String responsibilityId;
    @XmlElement(name = Elements.ROLE_NAME, required = false)
    private final String roleName;
    @XmlElement(name = Elements.DELEGATION_TYPE, required = false)
    private final String delegationType;
    @XmlElement(name = Elements.GROUP_ID, required = false)
    private final String groupId;
    @XmlElement(name = Elements.PRINCIPAL_ID, required = true)
    private final String principalId;
    @XmlElement(name = Elements.DELEGATOR_GROUP_ID, required = false)
    private final String delegatorGroupId;
    @XmlElement(name = Elements.DELEGATOR_PRINCIPAL_ID, required = false)
    private final String delegatorPrincipalId;
    @XmlElement(name = Elements.DATE_TIME_ASSIGNED_VALUE, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime dateTimeAssignedValue;
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     * 
     */
    private ActionItem() {
        this.id = null;
        this.dateTimeAssigned = null;
        this.dateTimeAssignedValue = null;
        this.actionRequestCd = null;
        this.actionRequestId = null;
        this.documentId = null;
        this.docTitle = null;
        this.docLabel = null;
        this.docHandlerURL = null;
        this.docName = null;
        this.responsibilityId = null;
        this.roleName = null;
        this.delegationType = null;
        this.groupId = null;
        this.principalId = null;
        this.delegatorGroupId = null;
        this.delegatorPrincipalId = null;
    }

    private ActionItem(Builder builder) {
        this.id = builder.getId();
        this.dateTimeAssigned = builder.getDateTimeAssigned();
        this.dateTimeAssignedValue = builder.getDateTimeAssigned();
        this.actionRequestCd = builder.getActionRequestCd();
        this.actionRequestId = builder.getActionRequestId();
        this.documentId = builder.getDocumentId();
        this.docTitle = builder.getDocTitle();
        this.docLabel = builder.getDocLabel();
        this.docHandlerURL = builder.getDocHandlerURL();
        this.docName = builder.getDocName();
        this.responsibilityId = builder.getResponsibilityId();
        this.roleName = builder.getRoleName();
        this.delegationType = builder.getDelegationType() != null ? builder.getDelegationType().getCode() : null;
        this.groupId = builder.getGroupId();
        this.principalId = builder.getPrincipalId();
        this.delegatorGroupId = builder.getDelegatorGroupId();
        this.delegatorPrincipalId = builder.getDelegatorPrincipalId();
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public DateTime getDateTimeAssigned() {
        return this.dateTimeAssignedValue == null ? this.dateTimeAssigned : this.dateTimeAssignedValue;
    }

    @Override
    public String getActionRequestCd() {
        return this.actionRequestCd;
    }

    @Override
    public String getActionRequestId() {
        return this.actionRequestId;
    }

    @Override
    public String getDocumentId() {
        return this.documentId;
    }

    @Override
    public String getDocTitle() {
        return this.docTitle;
    }

    @Override
    public String getDocLabel() {
        return this.docLabel;
    }

    @Override
    public String getDocHandlerURL() {
        return this.docHandlerURL;
    }

    @Override
    public String getDocName() {
        return this.docName;
    }

    @Override
    public String getResponsibilityId() {
        return this.responsibilityId;
    }

    @Override
    public String getRoleName() {
        return this.roleName;
    }

    @Deprecated
    @Override
    public String getDateAssignedString() {
        // deprecated, always return null (see the contract javadoc for more details)
        return null;
    }

    @Deprecated
    @Override
    public String getActionToTake() {
        // deprecated, always return null (see the contract javadoc for more details)
        return null;
    }

    @Deprecated
    @Override
    public Integer getActionItemIndex() {
        // deprecated, always return null (see the contract javadoc for more details)
        return null;
    }

    @Override
    public DelegationType getDelegationType() {
        return DelegationType.fromCode(this.delegationType);
    }



    @Override
    public String getGroupId() {
        return this.groupId;
    }

    @Override
    public String getPrincipalId() {
        return this.principalId;
    }

    @Override
    public String getDelegatorGroupId() {
        return this.delegatorGroupId;
    }

    @Override
    public String getDelegatorPrincipalId() {
        return this.delegatorPrincipalId;
    }


    /**
     * A builder which can be used to construct {@link ActionItem} instances.  Enforces the constraints of the {@link ActionItemContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, ActionItemContract
    {

        private String id;
        private DateTime dateTimeAssigned;
        private String actionRequestCd;
        private String actionRequestId;
        private String documentId;
        private String docTitle;
        private String docLabel;
        private String docHandlerURL;
        private String docName;
        private String responsibilityId;
        private String roleName;
        private DelegationType delegationType;
        private String groupId;
        private String principalId;
        private String delegatorGroupId;
        private String delegatorPrincipalId;

        private Builder(String documentId, String actionRequestCd, String actionRequestId,
                DateTime dateTimeAssigned, String docLabel, String docHanderlURL,
                String docName, String responsibilityId, String principalId) {
            setDocumentId(documentId);
            setActionRequestCd(actionRequestCd);
            setActionRequestId(actionRequestId);
            setDateTimeAssigned(dateTimeAssigned);
            setDocLabel(docLabel);
            setDocHandlerURL(docHanderlURL);
            setDocName(docName);
            setResponsibilityId(responsibilityId);
            setPrincipalId(principalId);
        }

        public static Builder create(String documentId, String actionRequestCd, String actionRequestId,
                DateTime dateTimeAssigned, String docLabel, String docHanderlURL,
                String docName, String responsibilityId, String principalId) {
            return new Builder(documentId, actionRequestCd, actionRequestId, dateTimeAssigned, docLabel,
                    docHanderlURL, docName, responsibilityId, principalId);
        }

        public static Builder create(ActionItemContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create(contract.getDocumentId(), contract.getActionRequestCd(), contract.getActionRequestId(),
                    contract.getDateTimeAssigned(), contract.getDocLabel(), contract.getDocHandlerURL(), contract.getDocName(),
                    contract.getResponsibilityId(), contract.getPrincipalId());
            builder.setId(contract.getId());
            builder.setRoleName(contract.getRoleName());
            builder.setDocTitle(contract.getDocTitle());
            builder.setDelegationType(contract.getDelegationType());
            builder.setGroupId(contract.getGroupId());
            builder.setPrincipalId(contract.getPrincipalId());
            builder.setDelegatorGroupId(contract.getDelegatorGroupId());
            builder.setDelegatorPrincipalId(contract.getDelegatorPrincipalId());
            return builder;
        }

        public ActionItem build() {
            return new ActionItem(this);
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public DateTime getDateTimeAssigned() {
            return this.dateTimeAssigned;
        }

        @Override
        public String getActionRequestCd() {
            return this.actionRequestCd;
        }

        @Override
        public String getActionRequestId() {
            return this.actionRequestId;
        }

        @Override
        public String getDocumentId() {
            return this.documentId;
        }

        @Override
        public String getDocTitle() {
            return this.docTitle;
        }

        @Override
        public String getDocLabel() {
            return this.docLabel;
        }

        @Override
        public String getDocHandlerURL() {
            return this.docHandlerURL;
        }

        @Override
        public String getDocName() {
            return this.docName;
        }

        @Override
        public String getResponsibilityId() {
            return this.responsibilityId;
        }

        @Override
        public String getRoleName() {
            return this.roleName;
        }

        @Deprecated
        @Override
        public String getDateAssignedString() {
            // deprecated, always return null (see the contract javadoc for more details)
            return null;
        }

        @Deprecated
        @Override
        public String getActionToTake() {
            // deprecated, always return null (see the contract javadoc for more details)
            return null;
        }

        @Deprecated
        @Override
        public Integer getActionItemIndex() {
            // deprecated, always return null (see the contract javadoc for more details)
            return null;
        }

        @Override
        public DelegationType getDelegationType() {
            return this.delegationType;
        }

        @Override
        public String getGroupId() {
            return this.groupId;
        }

        @Override
        public String getPrincipalId() {
            return this.principalId;
        }

        @Override
        public String getDelegatorGroupId() {
            return this.delegatorGroupId;
        }

        @Override
        public String getDelegatorPrincipalId() {
            return this.delegatorPrincipalId;
        }

        public void setId(String id) {
            if (StringUtils.isWhitespace(id)) {
                throw new IllegalArgumentException("id is blank");
            }
            this.id = id;
        }

        public void setDateTimeAssigned(DateTime dateTimeAssigned) {
            if (dateTimeAssigned == null) {
                throw new IllegalArgumentException("dateTimeAssigned is null");
            }
            this.dateTimeAssigned = dateTimeAssigned;
        }

        public void setActionRequestCd(String actionRequestCd) {
            if (StringUtils.isBlank(actionRequestCd)) {
                throw new IllegalArgumentException("actionRequestCd is blank");
            }
            this.actionRequestCd = actionRequestCd;
        }

        public void setActionRequestId(String actionRequestId) {
            if (StringUtils.isBlank(actionRequestId)) {
                throw new IllegalArgumentException("actionRequestId is blank");
            }
            this.actionRequestId = actionRequestId;
        }

        public void setDocumentId(String documentId) {
            if (StringUtils.isBlank(documentId)) {
                throw new IllegalArgumentException("documentId is blank");
            }
            this.documentId = documentId;
        }

        public void setDocTitle(String docTitle) {
            this.docTitle = docTitle;
        }

        public void setDocLabel(String docLabel) {
            if (StringUtils.isBlank(docLabel)) {
                throw new IllegalArgumentException("docLabel is blank");
            }
            this.docLabel = docLabel;
        }

        public void setDocHandlerURL(String docHandlerURL) {
            // can be empty, but not null
            if (docHandlerURL == null) {
                throw new IllegalArgumentException("docHandlerURL is null");
            }
            this.docHandlerURL = docHandlerURL;
        }

        public void setDocName(String docName) {
            if (StringUtils.isBlank(docName)) {
                throw new IllegalArgumentException("docName is blank");
            }
            this.docName = docName;
        }

        public void setResponsibilityId(String responsibilityId) {
            if (StringUtils.isBlank(responsibilityId)) {
                throw new IllegalArgumentException("responsibilityId is blank");
            }
            this.responsibilityId = responsibilityId;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        /**
         * This method has been deprecated and should not be called. If it is, then it will have no effect.
         * It was mistakenly added to this interface when it was created and has been left here for compatibility
         * purposes.
         *
         * @deprecated dateAssignedString is never used
         */
        @Deprecated
        public void setDateAssignedString(String dateAssignedString) {
            // deprecated, does nothing
        }

        /**
         * This method has been deprecated and should not be called. If it is, then it will have no effect.
         * It was mistakenly added to this interface when it was created and has been left here for compatibility
         * purposes.
         *
         * @deprecated actionToTake is never used
         */
        @Deprecated
        public void setActionToTake(String actionToTake) {
            // deprecated, does nothing
        }

        public void setDelegationType(DelegationType delegationType) {
            this.delegationType = delegationType;
        }

        /**
         * This method has been deprecated and should not be called. If it is, then it will have no effect.
         * It was mistakenly added to this interface when it was created and has been left here for compatibility
         * purposes.
         *
         * @deprecated actionItemIndex is never used
         */
        @Deprecated
        public void setActionItemIndex(Integer actionItemIndex) {
            // deprecated, does nothing
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public void setPrincipalId(String principalId) {
            if (StringUtils.isBlank(principalId)) {
                throw new IllegalArgumentException("principalId is blank");
            }
            this.principalId = principalId;
        }

        public void setDelegatorGroupId(String delegatorGroupId) {
            this.delegatorGroupId = delegatorGroupId;
        }

        public void setDelegatorPrincipalId(String delegatorPrincipalId) {
            this.delegatorPrincipalId = delegatorPrincipalId;
        }

    }

    @Override
    public boolean equals(Object obj) {
        return equalsExcludeFields(obj, Constants.excludeFields);
    }

    @Override
    public int hashCode() {
        return hashCodeExcludeFields(Constants.excludeFields);
    }

    /**
     * Defines some internal constants used on this class.
     * 
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "actionItem";
        final static String TYPE_NAME = "ActionItemType";


        final static Collection<String> excludeFields;

        static {
            excludeFields = Collections.unmodifiableCollection( (Collection<String>)
                    CollectionUtils.union(
                            Collections.singletonList("dateTimeAssigned"),
                            getDefaultHashCodeEqualsExcludeFields()
                    )
            );
        }
    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String ID = "id";
        final static String DATE_TIME_ASSIGNED = "dateTimeAssigned";
        final static String DATE_TIME_ASSIGNED_VALUE = "dateTimeAssignedValue";
        final static String ACTION_REQUEST_CD = "actionRequestCd";
        final static String ACTION_REQUEST_ID = "actionRequestId";
        final static String DOCUMENT_ID = "documentId";
        final static String DOC_TITLE = "docTitle";
        final static String DOC_LABEL = "docLabel";
        final static String DOC_HANDLER_U_R_L = "docHandlerURL";
        final static String DOC_NAME = "docName";
        final static String RESPONSIBILITY_ID = "responsibilityId";
        final static String ROLE_NAME = "roleName";
        final static String DELEGATION_TYPE = "delegationType";
        final static String GROUP_ID = "groupId";
        final static String PRINCIPAL_ID = "principalId";
        final static String DELEGATOR_GROUP_ID = "delegatorGroupId";
        final static String DELEGATOR_PRINCIPAL_ID = "delegatorPrincipalId";

    }

}