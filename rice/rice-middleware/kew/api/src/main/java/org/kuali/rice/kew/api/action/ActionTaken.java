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

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.CoreConstants;
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

@XmlRootElement(name = ActionTaken.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = ActionTaken.Constants.TYPE_NAME, propOrder = {
    ActionTaken.Elements.ID,
    ActionTaken.Elements.DOCUMENT_ID,
    ActionTaken.Elements.PRINCIPAL_ID,
    ActionTaken.Elements.DELEGATOR_PRINCIPAL_ID,
    ActionTaken.Elements.DELEGATOR_GROUP_ID,
    ActionTaken.Elements.ACTION_TAKEN_CODE,
    ActionTaken.Elements.ACTION_DATE,
    ActionTaken.Elements.ANNOTATION,
    ActionTaken.Elements.CURRENT,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class ActionTaken extends AbstractDataTransferObject implements ActionTakenContract {

	private static final long serialVersionUID = 8411150332911080837L;

	@XmlElement(name = Elements.ID, required = true)
    private final String id;
    
    @XmlElement(name = Elements.DOCUMENT_ID, required = true)
    private final String documentId;
    
    @XmlElement(name = Elements.PRINCIPAL_ID, required = true)
    private final String principalId;
    
    @XmlElement(name = Elements.DELEGATOR_PRINCIPAL_ID, required = false)
    private final String delegatorPrincipalId;
    
    @XmlElement(name = Elements.DELEGATOR_GROUP_ID, required = false)
    private final String delegatorGroupId;
    
    @XmlElement(name = Elements.ACTION_TAKEN_CODE, required = true)
    private final String actionTakenCode;
    
    @XmlElement(name = Elements.ACTION_DATE, required = true)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime actionDate;

    @XmlElement(name = Elements.ANNOTATION, required = false)
    private final String annotation;

    @XmlElement(name = Elements.CURRENT, required = true)
    private final boolean current;    
    
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     * 
     */
    private ActionTaken() {
        this.id = null;
        this.documentId = null;
        this.principalId = null;
        this.delegatorPrincipalId = null;
        this.delegatorGroupId = null;
        this.actionTakenCode = null;
        this.actionDate = null;
        this.annotation = null;
        this.current = false;
    }

    private ActionTaken(Builder builder) {
        this.annotation = builder.getAnnotation();
        this.id = builder.getId();
        this.documentId = builder.getDocumentId();
        this.principalId = builder.getPrincipalId();
        this.delegatorPrincipalId = builder.getDelegatorPrincipalId();
        this.delegatorGroupId = builder.getDelegatorGroupId();
        this.actionTakenCode = builder.getActionTaken().getCode();
        this.actionDate = builder.getActionDate();
        this.current = builder.isCurrent();
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getDocumentId() {
        return this.documentId;
    }

    @Override
    public String getPrincipalId() {
        return this.principalId;
    }

    @Override
    public String getDelegatorPrincipalId() {
        return this.delegatorPrincipalId;
    }

    @Override
    public String getDelegatorGroupId() {
        return this.delegatorGroupId;
    }

    @Override
    public ActionType getActionTaken() {
        return ActionType.fromCode(actionTakenCode);
    }

    @Override
    public DateTime getActionDate() {
        return this.actionDate;
    }
    
    @Override
    public String getAnnotation() {
        return this.annotation;
    }

    @Override
    public boolean isCurrent() {
        return this.current;
    }

    /**
     * A builder which can be used to construct {@link ActionTaken} instances.  Enforces the constraints of the {@link ActionTakenContract}.
     */
    public final static class Builder implements Serializable, ModelBuilder, ActionTakenContract {

		private static final long serialVersionUID = -1226075070994810756L;

		private String id;
        private String documentId;
        private String principalId;
        private String delegatorPrincipalId;
        private String delegatorGroupId;
        private ActionType actionTaken;
        private DateTime actionDate;
        private String annotation;
        private boolean current;

        private Builder(String id, String documentId, String principalId, ActionType actionTaken) {
        	setId(id);
        	setDocumentId(documentId);
        	setPrincipalId(principalId);
        	setActionTaken(actionTaken);
            setActionDate(new DateTime());
        	setCurrent(true);
        }

        public static Builder create(String id, String documentId, String principalId, ActionType actionTaken) {
            return new Builder(id, documentId, principalId, actionTaken);
        }

        public static Builder create(ActionTakenContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create(contract.getId(), contract.getDocumentId(), contract.getPrincipalId(), contract.getActionTaken());
            builder.setDelegatorPrincipalId(contract.getDelegatorPrincipalId());
            builder.setDelegatorGroupId(contract.getDelegatorGroupId());
            builder.setActionDate(contract.getActionDate());
            builder.setAnnotation(contract.getAnnotation());
            builder.setCurrent(contract.isCurrent());
            return builder;
        }

        public ActionTaken build() {
            return new ActionTaken(this);
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public String getDocumentId() {
            return this.documentId;
        }

        @Override
        public String getPrincipalId() {
            return this.principalId;
        }

        @Override
        public String getDelegatorPrincipalId() {
            return this.delegatorPrincipalId;
        }

        @Override
        public String getDelegatorGroupId() {
            return this.delegatorGroupId;
        }

        @Override
        public ActionType getActionTaken() {
            return this.actionTaken;
        }

        @Override
        public DateTime getActionDate() {
            return this.actionDate;
        }
        
        @Override
        public String getAnnotation() {
            return this.annotation;
        }

        @Override
        public boolean isCurrent() {
            return this.current;
        }

        public void setId(String id) {
            if (StringUtils.isWhitespace(id)) {
            	throw new IllegalArgumentException("id was blank");
            }
            this.id = id;
        }

        public void setDocumentId(String documentId) {
        	if (StringUtils.isBlank(documentId)) {
            	throw new IllegalArgumentException("documentId was null or blank");
            }
            this.documentId = documentId;
        }

        public void setPrincipalId(String principalId) {
        	if (StringUtils.isBlank(principalId)) {
            	throw new IllegalArgumentException("principalId was null or blank");
            }
            this.principalId = principalId;
        }

        public void setDelegatorPrincipalId(String delegatorPrincipalId) {
            this.delegatorPrincipalId = delegatorPrincipalId;
        }

        public void setDelegatorGroupId(String delegatorGroupId) {
            this.delegatorGroupId = delegatorGroupId;
        }

        public void setActionTaken(ActionType actionTaken) {
        	if (actionTaken == null) {
            	throw new IllegalArgumentException("actionTaken was null");
            }
            this.actionTaken = actionTaken;
        }

        public void setActionDate(DateTime actionDate) {
            if (actionDate == null) {
            	throw new IllegalArgumentException("actionDate was null");
            }
            this.actionDate = actionDate;
        }

        public void setAnnotation(String annotation) {
            this.annotation = annotation;
        }
        
        public void setCurrent(boolean current) {
            this.current = current;
        }

    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "actionTaken";
        final static String TYPE_NAME = "ActionTakenType";
    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String ID = "id";
        final static String DOCUMENT_ID = "documentId";
        final static String PRINCIPAL_ID = "principalId";
        final static String DELEGATOR_PRINCIPAL_ID = "delegatorPrincipalId";
        final static String DELEGATOR_GROUP_ID = "delegatorGroupId";
        final static String ACTION_TAKEN_CODE = "actionTakenCode";
        final static String ACTION_DATE = "actionDate";
        final static String ANNOTATION = "annotation";
        final static String CURRENT = "current";
    }

}
