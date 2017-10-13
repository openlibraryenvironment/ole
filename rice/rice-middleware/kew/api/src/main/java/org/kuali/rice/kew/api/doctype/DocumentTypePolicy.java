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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.kuali.rice.core.api.mo.common.Coded;

/**
 * TODO...
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = "documentTypePolicy")
@XmlType(name = "DocumentTypePolicyType")
@XmlEnum
public enum DocumentTypePolicy implements Coded {
    
	/**
     * FIXME: needs docs
     */
    @XmlEnumValue(Codes.DISAPPROVE) DISAPPROVE(Codes.DISAPPROVE),
    
    /**
     * This policy determines whether to use the internal KEW Super User document handler URL when opening a document from
     * super user search. If set to false the client must implement a custom super user screen to be used when the doc
     * handler URL has a post variable of the name defined by {@link KewApiConstants#COMMAND_PARAMETER} and a value of
     * {@link KewApiConstants#SUPERUSER_COMMAND}. The default is 'true'.
     */
    @XmlEnumValue(Codes.USE_KEW_SUPERUSER_DOCHANDLER) USE_KEW_SUPERUSER_DOCHANDLER(Codes.USE_KEW_SUPERUSER_DOCHANDLER),

    /**
     * determines how to handle the situation where the user has initiated an action but there is not a corresponding pending request. This policy has a default of true.
     * If set to false an exception should be thrown from ApproveAction, CompleteAction, AcknowledgeAction
     * and ClearFYIAction classes when there does not exist a corresponding pending request for the user who is submitting the action. 
     * When set to false, this will result in one of the users getting an error message if 2 users attempt to submit the same action
     * at the same time (this can happen in workgroup situtations). 
     */
    @XmlEnumValue(Codes.ALLOW_UNREQUESTED_ACTION) ALLOW_UNREQUESTED_ACTION(Codes.ALLOW_UNREQUESTED_ACTION),
    
    /**
     * determines whether a document will go processed without any approval requests.  If
     * a document has this policy set to false and doesn't generate and approval requests the document will
     * be put in exception routing, which is the exception workgroup associated with the last route node or the
     * workgroup defined in the 'defaultExceptionGroupName'.  This policy if not defined in this or a parent
     * document type defaults to true
     */
    @XmlEnumValue(Codes.DEFAULT_APPROVE) DEFAULT_APPROVE(Codes.DEFAULT_APPROVE),
    
    /**
     * determines if the user that initiated a document must 'route' the document when it is
     * in the initiated state.  Defaults to true.
     */
    @XmlEnumValue(Codes.INITIATOR_MUST_ROUTE) INITIATOR_MUST_ROUTE(Codes.INITIATOR_MUST_ROUTE),
    
    /**
     * determines if the user that initiated a document must 'route' the document when it is
     * in the initiated state.  Defaults to true.
     */
    @XmlEnumValue(Codes.INITIATOR_MUST_SAVE) INITIATOR_MUST_SAVE(Codes.INITIATOR_MUST_SAVE),
    
    @XmlEnumValue(Codes.INITIATOR_MUST_CANCEL) INITIATOR_MUST_CANCEL(Codes.INITIATOR_MUST_CANCEL),
    
    @XmlEnumValue(Codes.INITIATOR_MUST_BLANKET_APPROVE) INITIATOR_MUST_BLANKET_APPROVE(Codes.INITIATOR_MUST_BLANKET_APPROVE),

    /**
     * determines whether the document can be brought into a simulated route from the route log.  A
     * simulation of where the document would end up if it where routed to completion now.  Defaults to false.
     * 
     * determines if route log will show the look into the future link
     */
    @XmlEnumValue(Codes.LOOK_FUTURE) LOOK_FUTURE(Codes.LOOK_FUTURE),

    @XmlEnumValue(Codes.SEND_NOTIFICATION_ON_SU_APPROVE) SEND_NOTIFICATION_ON_SU_APPROVE(Codes.SEND_NOTIFICATION_ON_SU_APPROVE),

    @XmlEnumValue(Codes.SUPPORTS_QUICK_INITIATE) SUPPORTS_QUICK_INITIATE(Codes.SUPPORTS_QUICK_INITIATE),

    @XmlEnumValue(Codes.NOTIFY_ON_SAVE) NOTIFY_ON_SAVE(Codes.NOTIFY_ON_SAVE),
    
    /**
     * The Document Status Policy determines whether the KEW Route Status or the Application Document Status (or both) 
     * are to be used for a specific document type.
     */
    @XmlEnumValue(Codes.DOCUMENT_STATUS_POLICY) DOCUMENT_STATUS_POLICY(Codes.DOCUMENT_STATUS_POLICY),

    /**
     * This document type policy allows us to configure if the "Perform Post Processor Logic" for the super user action on action requests is displayed.  
     * KULRICE-3584
     */
    @XmlEnumValue(Codes.ALLOW_SU_POSTPROCESSOR_OVERRIDE) ALLOW_SU_POSTPROCESSOR_OVERRIDE(Codes.ALLOW_SU_POSTPROCESSOR_OVERRIDE),

    @XmlEnumValue(Codes.FAIL_ON_INACTIVE_GROUP) FAIL_ON_INACTIVE_GROUP(Codes.FAIL_ON_INACTIVE_GROUP),

    @XmlEnumValue(Codes.ENROUTE_ERROR_SUPPRESSION) ENROUTE_ERROR_SUPPRESSION(Codes.ENROUTE_ERROR_SUPPRESSION),

    @XmlEnumValue(Codes.REGENERATE_ACTION_REQUESTS_ON_CHANGE) REGENERATE_ACTION_REQUESTS_ON_CHANGE(Codes.REGENERATE_ACTION_REQUESTS_ON_CHANGE),

    /**
     * Governs whether FYIs should be sent on *pending* (not completed) action requests when returning to a previous node
     * @since 2.1
     * @see https://jira.kuali.org/browse/KULRICE-5931
     */
    @XmlEnumValue(Codes.NOTIFY_PENDING_ON_RETURN) NOTIFY_PENDING_ON_RETURN(Codes.NOTIFY_PENDING_ON_RETURN),

    /**
     * Governs whether FYIs should be sent on *completed* (not pending) action requests when returning to a previous node (for use with Recall)
     * @since 2.1
     * @see https://jira.kuali.org/browse/KULRICE-5931
     */
    @XmlEnumValue(Codes.NOTIFY_COMPLETED_ON_RETURN) NOTIFY_COMPLETED_ON_RETURN(Codes.NOTIFY_COMPLETED_ON_RETURN),
    /**
     * Specifies additional recipients of Recall notifications.  This configuration is supplied as an additional recipients element
     * in the document policy element, which conforms to the Rule:ResponsibilityIdentifiers group schema.
     * @since 2.1
     * @see https://jira.kuali.org/browse/KULRICE-5931
     */
    @XmlEnumValue(Codes.RECALL_NOTIFICATION) RECALL_NOTIFICATION(Codes.RECALL_NOTIFICATION),
    /**
     * Specifies list of prior actions taken for which a subsequent Recall action will be valid.
     * @since 2.1
     * @see https://jira.kuali.org/browse/KULRICE-7798
     */
    @XmlEnumValue(Codes.RECALL_VALID_ACTIONSTAKEN) RECALL_VALID_ACTIONSTAKEN(Codes.RECALL_VALID_ACTIONSTAKEN),
    /**
     * Specifies whether to send acknowledgements on a super user disapprove action
     * @since 2.1
     * @see https://jira.kuali.org/browse/KULRICE-7056
     */
    @XmlEnumValue(Codes.SEND_NOTIFICATION_ON_SU_DISAPPROVE) SEND_NOTIFICATION_ON_SU_DISAPPROVE(Codes.SEND_NOTIFICATION_ON_SU_DISAPPROVE),
    /**
     * Specifies whether to disallow super user approval on the final route node
     * @since 2.1
     * @see https://jira.kuali.org/browse/KULRICE-7057
     */
    @XmlEnumValue(Codes.ALLOW_SU_FINAL_APPROVAL) ALLOW_SU_FINAL_APPROVAL(Codes.ALLOW_SU_FINAL_APPROVAL),
    /**
     * Specifies whether immediate emails should be suppressed after a superuser action is taken
     * @since 2.1.3
     * @see https://jira.kuali.org/browse/KULRICE-8289
     */
    @XmlEnumValue(Codes.SUPPRESS_IMMEDIATE_EMAILS_ON_SU_ACTION) SUPPRESS_IMMEDIATE_EMAILS_ON_SU_ACTION(Codes.SUPPRESS_IMMEDIATE_EMAILS_ON_SU_ACTION);

    private final String code;

    private DocumentTypePolicy(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    public static DocumentTypePolicy fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (DocumentTypePolicy policy : values()) {
            if (policy.code.equalsIgnoreCase(code)) {
                return policy;
            }
        }
        throw new IllegalArgumentException("Failed to locate the DocumentTypePolicy with the given code: " + code);
    }
        
    private static final class Codes {
        private static final String DISAPPROVE = "DISAPPROVE";
        private static final String USE_KEW_SUPERUSER_DOCHANDLER = "USE_KEW_SUPERUSER_DOCHANDLER";
        private static final String ALLOW_UNREQUESTED_ACTION = "ALLOW_UNREQUESTED_ACTION";
        private static final String DEFAULT_APPROVE = "DEFAULT_APPROVE";
        private static final String INITIATOR_MUST_ROUTE = "INITIATOR_MUST_ROUTE";
        private static final String INITIATOR_MUST_SAVE = "INITIATOR_MUST_SAVE";
        private static final String INITIATOR_MUST_CANCEL = "INITIATOR_MUST_CANCEL";
        private static final String INITIATOR_MUST_BLANKET_APPROVE = "INITIATOR_MUST_BLANKET_APPROVE";
        private static final String LOOK_FUTURE = "LOOK_FUTURE";
        private static final String SEND_NOTIFICATION_ON_SU_APPROVE = "SEND_NOTIFICATION_ON_SU_APPROVE";
        private static final String SUPPORTS_QUICK_INITIATE = "SUPPORTS_QUICK_INITIATE";
        private static final String NOTIFY_ON_SAVE = "NOTIFY_ON_SAVE";
        private static final String DOCUMENT_STATUS_POLICY = "DOCUMENT_STATUS_POLICY";
        private static final String ALLOW_SU_POSTPROCESSOR_OVERRIDE = "ALLOW_SU_POSTPROCESSOR_OVERRIDE";
        private static final String FAIL_ON_INACTIVE_GROUP = "FAIL_ON_INACTIVE_GROUP";
        private static final String REGENERATE_ACTION_REQUESTS_ON_CHANGE = "REGENERATE_ACTION_REQUESTS_ON_CHANGE";       
        private static final String ENROUTE_ERROR_SUPPRESSION = "ENROUTE_ERROR_SUPPRESSION";
        private static final String NOTIFY_PENDING_ON_RETURN = "NOTIFY_PENDING_ON_RETURN";
        private static final String NOTIFY_COMPLETED_ON_RETURN = "NOTIFY_COMPLETED_ON_RETURN";
        private static final String RECALL_NOTIFICATION = "RECALL_NOTIFICATION";
        private static final String RECALL_VALID_ACTIONSTAKEN = "RECALL_VALID_ACTIONSTAKEN";
        private static final String SEND_NOTIFICATION_ON_SU_DISAPPROVE = "SEND_NOTIFICATION_ON_SU_DISAPPROVE";
        private static final String ALLOW_SU_FINAL_APPROVAL = "ALLOW_SU_FINAL_APPROVAL";
        private static final String SUPPRESS_IMMEDIATE_EMAILS_ON_SU_ACTION = "SUPPRESS_IMMEDIATE_EMAILS_ON_SU_ACTION";
    }
    
}
