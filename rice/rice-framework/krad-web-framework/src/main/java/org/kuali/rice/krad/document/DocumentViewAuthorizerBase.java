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
package org.kuali.rice.krad.document;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.UserSessionUtils;
import org.kuali.rice.krad.datadictionary.AttributeSecurity;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.field.DataField;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.view.ViewAuthorizerBase;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADUtils;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.web.form.DocumentFormBase;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of {@link org.kuali.rice.krad.uif.view.ViewAuthorizer} for
 * {@link org.kuali.rice.krad.uif.view.DocumentView} instances
 *
 * <p>
 * Performs KIM permission checks for the various document actions such as save, approve, cancel
 * </p>
 *
 * <p>
 * By default delegates to the {@link DocumentAuthorizer} configured for the document in the data dictionary
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentViewAuthorizerBase extends ViewAuthorizerBase implements DocumentAuthorizer {
    private static final long serialVersionUID = 3800780934223224565L;

    protected static Log LOG = LogFactory.getLog(DocumentViewAuthorizerBase.class);

    public static final String PRE_ROUTING_ROUTE_NAME = "PreRoute";

    private DocumentAuthorizer documentAuthorizer;

    /**
     * @see org.kuali.rice.krad.uif.view.ViewAuthorizer#getActionFlags(org.kuali.rice.krad.uif.view.View,
     *      org.kuali.rice.krad.uif.view.ViewModel, org.kuali.rice.kim.api.identity.Person,
     *      java.util.Set<java.lang.String>)
     */
    @Override
    public Set<String> getActionFlags(View view, ViewModel model, Person user, Set<String> actions) {
        Document document = ((DocumentFormBase) model).getDocument();

        if (LOG.isDebugEnabled()) {
            LOG.debug("calling DocumentAuthorizerBase.getDocumentActionFlags for document '"
                    + document.getDocumentNumber()
                    + "'. user '"
                    + user.getPrincipalName()
                    + "'");
        }

        if (actions.contains(KRADConstants.KUALI_ACTION_CAN_EDIT) && !canEdit(document, user)) {
            actions.remove(KRADConstants.KUALI_ACTION_CAN_EDIT);
        }

        if (actions.contains(KRADConstants.KUALI_ACTION_CAN_COPY) && !canCopy(document, user)) {
            actions.remove(KRADConstants.KUALI_ACTION_CAN_COPY);
        }

        if (actions.contains(KRADConstants.KUALI_ACTION_CAN_CLOSE) && !canClose(document, user)) {
            actions.remove(KRADConstants.KUALI_ACTION_CAN_CLOSE);
        }

        if (actions.contains(KRADConstants.KUALI_ACTION_CAN_RELOAD) && !canReload(document, user)) {
            actions.remove(KRADConstants.KUALI_ACTION_CAN_RELOAD);
        }

        if (actions.contains(KRADConstants.KUALI_ACTION_CAN_BLANKET_APPROVE) && !canBlanketApprove(document, user)) {
            actions.remove(KRADConstants.KUALI_ACTION_CAN_BLANKET_APPROVE);
        }

        if (actions.contains(KRADConstants.KUALI_ACTION_CAN_CANCEL) && !canCancel(document, user)) {
            actions.remove(KRADConstants.KUALI_ACTION_CAN_CANCEL);
        }

        if (actions.contains(KRADConstants.KUALI_ACTION_CAN_RECALL) && !canRecall(document, user)) {
            actions.remove(KRADConstants.KUALI_ACTION_CAN_RECALL);
        }

        if (actions.contains(KRADConstants.KUALI_ACTION_CAN_SAVE) && !canSave(document, user)) {
            actions.remove(KRADConstants.KUALI_ACTION_CAN_SAVE);
        }

        if (actions.contains(KRADConstants.KUALI_ACTION_CAN_ROUTE) && !canRoute(document, user)) {
            actions.remove(KRADConstants.KUALI_ACTION_CAN_ROUTE);
        }

        if (actions.contains(KRADConstants.KUALI_ACTION_CAN_ACKNOWLEDGE) && !canAcknowledge(document, user)) {
            actions.remove(KRADConstants.KUALI_ACTION_CAN_ACKNOWLEDGE);
        }

        if (actions.contains(KRADConstants.KUALI_ACTION_CAN_FYI) && !canFyi(document, user)) {
            actions.remove(KRADConstants.KUALI_ACTION_CAN_FYI);
        }

        if (actions.contains(KRADConstants.KUALI_ACTION_CAN_APPROVE) && !canApprove(document, user)) {
            actions.remove(KRADConstants.KUALI_ACTION_CAN_APPROVE);
        }

        if (actions.contains(KRADConstants.KUALI_ACTION_CAN_DISAPPROVE) && !canDisapprove(document, user)) {
            actions.remove(KRADConstants.KUALI_ACTION_CAN_DISAPPROVE);
        }

        if (!canSendAnyTypeAdHocRequests(document, user)) {
            actions.remove(KRADConstants.KUALI_ACTION_CAN_ADD_ADHOC_REQUESTS);
            actions.remove(KRADConstants.KUALI_ACTION_CAN_SEND_ADHOC_REQUESTS);
            actions.remove(KRADConstants.KUALI_ACTION_CAN_SEND_NOTE_FYI);
        }

        if (actions.contains(KRADConstants.KUALI_ACTION_CAN_SEND_NOTE_FYI) && !canSendNoteFyi(document, user)) {
            actions.remove(KRADConstants.KUALI_ACTION_CAN_SEND_NOTE_FYI);
        }

        if (actions.contains(KRADConstants.KUALI_ACTION_CAN_ANNOTATE) && !canAnnotate(document, user)) {
            actions.remove(KRADConstants.KUALI_ACTION_CAN_ANNOTATE);
        }

        if (actions.contains(KRADConstants.KUALI_ACTION_CAN_EDIT_DOCUMENT_OVERVIEW) && !canEditDocumentOverview(
                document, user)) {
            actions.remove(KRADConstants.KUALI_ACTION_CAN_EDIT_DOCUMENT_OVERVIEW);
        }

        if (actions.contains(KRADConstants.KUALI_ACTION_PERFORM_ROUTE_REPORT) && !canPerformRouteReport(document,
                user)) {
            actions.remove(KRADConstants.KUALI_ACTION_PERFORM_ROUTE_REPORT);
        }

        return actions;
    }

    public final boolean canInitiate(String documentTypeName, Person user) {
        return getDocumentAuthorizer().canInitiate(documentTypeName, user);
    }

    public final boolean canOpen(Document document, Person user) {
        return getDocumentAuthorizer().canOpen(document, user);
    }

    @Override
    public boolean canOpenView(View view, ViewModel model, Person user) {
        DocumentFormBase documentForm = (DocumentFormBase) model;

        return super.canOpenView(view, model, user) && canOpen(documentForm.getDocument(), user);
    }

    public boolean canEdit(Document document, Person user) {
        return getDocumentAuthorizer().canEdit(document, user);
    }

    @Override
    public boolean canEditView(View view, ViewModel model, Person user) {
        DocumentFormBase documentForm = (DocumentFormBase) model;

        return super.canEditView(view, model, user) && canEdit(documentForm.getDocument(), user);
    }

    /**
     * @see org.kuali.rice.krad.uif.view.ViewAuthorizer#canUnmaskField(org.kuali.rice.krad.uif.view.View, org.kuali.rice.krad.uif.view.ViewModel,
     * org.kuali.rice.krad.uif.field.DataField, java.lang.String, org.kuali.rice.kim.api.identity.Person)
     */
    public boolean canUnmaskField(View view, ViewModel model, DataField field, String propertyName, Person user) {
        // check mask authz flag is set
        AttributeSecurity attributeSecurity = field.getDataFieldSecurity().getAttributeSecurity();
        if (attributeSecurity == null || !attributeSecurity.isMask()) {
            return true;
        }

        // don't mask empty fields when user is the initiator (allows document creation when masked field exists)
        String fieldValue = ObjectPropertyUtils.getPropertyValue(model, field.getBindingInfo().getBindingPath());
        if (StringUtils.isBlank(fieldValue) && isInitiator(model, user)) {
            return true;
        }

        return super.canUnmaskField(view, model, field, propertyName, user);
    }

    /**
     * Checks if the user is the initiator for the current document
     *
     * @param model object containing the view data
     * @param user user we are authorizing
     * @return true if user is the initiator, false otherwise
     */
    protected boolean isInitiator(ViewModel model, Person user) {
            WorkflowDocument workflowDocument = UserSessionUtils.getWorkflowDocument(GlobalVariables.getUserSession(),
                    ((DocumentFormBase) model).getDocument().getDocumentNumber());
            return StringUtils.equals(user.getPrincipalId(), workflowDocument.getInitiatorPrincipalId());
    }


    public boolean canAnnotate(Document document, Person user) {
        return getDocumentAuthorizer().canAnnotate(document, user);
    }

    public boolean canReload(Document document, Person user) {
        return getDocumentAuthorizer().canReload(document, user);
    }

    public boolean canClose(Document document, Person user) {
        return getDocumentAuthorizer().canClose(document, user);
    }

    public boolean canSave(Document document, Person user) {
        return getDocumentAuthorizer().canSave(document, user);
    }

    public boolean canRoute(Document document, Person user) {
        return getDocumentAuthorizer().canRoute(document, user);
    }

    public boolean canCancel(Document document, Person user) {
        return getDocumentAuthorizer().canCancel(document, user);
    }

    public boolean canRecall(Document document, Person user) {
        return getDocumentAuthorizer().canRecall(document, user);
    }

    public boolean canCopy(Document document, Person user) {
        return getDocumentAuthorizer().canCopy(document, user);
    }

    public boolean canPerformRouteReport(Document document, Person user) {
        return getDocumentAuthorizer().canPerformRouteReport(document, user);
    }

    public boolean canBlanketApprove(Document document, Person user) {
        return getDocumentAuthorizer().canBlanketApprove(document, user);
    }

    public boolean canApprove(Document document, Person user) {
        return getDocumentAuthorizer().canApprove(document, user);
    }

    public boolean canDisapprove(Document document, Person user) {
        return getDocumentAuthorizer().canDisapprove(document, user);
    }

    public boolean canSendNoteFyi(Document document, Person user) {
        return getDocumentAuthorizer().canSendNoteFyi(document, user);
    }

    public boolean canFyi(Document document, Person user) {
        return getDocumentAuthorizer().canFyi(document, user);
    }

    public boolean canAcknowledge(Document document, Person user) {
        return getDocumentAuthorizer().canAcknowledge(document, user);
    }

    public final boolean canReceiveAdHoc(Document document, Person user, String actionRequestCode) {
        return getDocumentAuthorizer().canReceiveAdHoc(document, user, actionRequestCode);
    }

    public final boolean canAddNoteAttachment(Document document, String attachmentTypeCode, Person user) {
        return getDocumentAuthorizer().canAddNoteAttachment(document, attachmentTypeCode, user);
    }

    public final boolean canDeleteNoteAttachment(Document document, String attachmentTypeCode,
            String authorUniversalIdentifier, Person user) {
        return getDocumentAuthorizer().canDeleteNoteAttachment(document, attachmentTypeCode, authorUniversalIdentifier,
                user);
    }

    public final boolean canViewNoteAttachment(Document document, String attachmentTypeCode,
            String authorUniversalIdentifier, Person user) {
        return getDocumentAuthorizer().canViewNoteAttachment(document, attachmentTypeCode, authorUniversalIdentifier,
                user);
    }

    public final boolean canSendAdHocRequests(Document document, String actionRequestCd, Person user) {
        return getDocumentAuthorizer().canSendAdHocRequests(document, actionRequestCd, user);
    }

    public boolean canEditDocumentOverview(Document document, Person user) {
        return getDocumentAuthorizer().canEditDocumentOverview(document, user);
    }

    public boolean canSendAnyTypeAdHocRequests(Document document, Person user) {
        return getDocumentAuthorizer().canSendAnyTypeAdHocRequests(document, user);
    }

    public boolean canTakeRequestedAction(Document document, String actionRequestCode, Person user) {
        return getDocumentAuthorizer().canTakeRequestedAction(document, actionRequestCode, user);
    }

    @Override
    protected void addPermissionDetails(Object dataObject, Map<String, String> attributes) {
        super.addPermissionDetails(dataObject, attributes);

        if (dataObject instanceof Document) {
            addStandardAttributes((Document) dataObject, attributes);
        }
    }

    @Override
    protected void addRoleQualification(Object dataObject, Map<String, String> attributes) {
        super.addRoleQualification(dataObject, attributes);

        if (dataObject instanceof Document) {
            addStandardAttributes((Document) dataObject, attributes);
        }
    }

    protected void addStandardAttributes(Document document, Map<String, String> attributes) {
        WorkflowDocument wd = document.getDocumentHeader().getWorkflowDocument();
        attributes.put(KimConstants.AttributeConstants.DOCUMENT_NUMBER, document.getDocumentNumber());
        attributes.put(KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME, wd.getDocumentTypeName());

        if (wd.isInitiated() || wd.isSaved()) {
            attributes.put(KimConstants.AttributeConstants.ROUTE_NODE_NAME, PRE_ROUTING_ROUTE_NAME);
        } else {
            attributes.put(KimConstants.AttributeConstants.ROUTE_NODE_NAME,
                    KRADServiceLocatorWeb.getWorkflowDocumentService().getCurrentRouteNodeNames(wd));
        }

        attributes.put(KimConstants.AttributeConstants.ROUTE_STATUS_CODE, wd.getStatus().getCode());
    }

    protected boolean isDocumentInitiator(Document document, Person user) {
        WorkflowDocument workflowDocument = document.getDocumentHeader().getWorkflowDocument();

        return workflowDocument.getInitiatorPrincipalId().equalsIgnoreCase(user.getPrincipalId());
    }

    public DocumentAuthorizer getDocumentAuthorizer() {
        return documentAuthorizer;
    }

    public void setDocumentAuthorizer(DocumentAuthorizer documentAuthorizer) {
        this.documentAuthorizer = documentAuthorizer;
    }

    public void setDocumentAuthorizerClass(Class<? extends DocumentAuthorizer> documentAuthorizerClass) {
        this.documentAuthorizer = ObjectUtils.newInstance(documentAuthorizerClass);
    }
}
