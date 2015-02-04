/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.document.web.struts;

import org.apache.struts.action.ActionMapping;
import org.kuali.ole.select.businessobject.OleRequisitionItem;
import org.kuali.ole.select.document.OleOrderQueueDocument;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.kim.api.services.IdentityManagementService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kns.web.struts.form.KualiTransactionalDocumentFormBase;
import org.kuali.rice.kns.web.ui.ExtraButton;
import org.kuali.rice.krad.util.GlobalVariables;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is the actionform class for Order Holding Queue.
 */
public class OleOrderQueueForm extends KualiTransactionalDocumentFormBase {


    private String selectedUserId;

    private String selectorUserId;

    private String principalName;

    private Timestamp createdDate;

    private Timestamp modifiedDate;

    private String statusCode;

    private Integer totalPrice;

    private String active;

    private List<OleRequisitionItem> requisitions = new ArrayList<OleRequisitionItem>(0);

    protected boolean selectAllCheckbox;

    private String url;

    private String selectorRoleName = OLEConstants.OleRequisitionItem.ORDER_HOLD_QUEUE_ROLE;
    private String selectorRoleNamespace = OLEConstants.OleRequisitionItem.ORDER_HOLD_QUEUE_ROLE_NAMESPACE;

    public String getUrl() {
        return OLEConstants.DOC_HANDLER_URL;
    }

    public String getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(String selectedUserId) {
        this.selectedUserId = selectedUserId;
    }

    public String getSelectorUserId() {
        return selectorUserId;
    }

    public void setSelectorUserId(String selectorUserId) {
        this.selectorUserId = selectorUserId;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    /**
     * Gets the principalName attribute.
     *
     * @return Returns the principalName.
     */
    public String getPrincipalName() {
        return principalName;
    }

    /**
     * Sets the principalName attribute value.
     *
     * @param principalName The principalName to set.
     */
    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    /**
     * Gets the requisitions attribute.
     *
     * @return Returns the requisitions.
     */
    public List<OleRequisitionItem> getRequisitions() {
        return requisitions;
    }

    /**
     * Sets the requisitions attribute value.
     *
     * @param requisitions The requisitions to set.
     */
    public void setRequisitions(List<OleRequisitionItem> requisitions) {
        this.requisitions = requisitions;
    }

    public void addRequisitions(OleRequisitionItem data) {
        this.requisitions.add(data);
    }

    public boolean isRequisitionAdded() {
        return this.requisitions.size() > 0;
    }


    private String lookupResultsSequenceNumber;

    /**
     * Gets the lookupResultsSequenceNumber attribute.
     *
     * @return Returns the lookupResultsSequenceNumber.
     */
    public String getLookupResultsSequenceNumber() {
        return lookupResultsSequenceNumber;
    }

    /**
     * Sets the lookupResultsSequenceNumber attribute value.
     *
     * @param lookupResultsSequenceNumber The lookupResultsSequenceNumber to set.
     */
    public void setLookupResultsSequenceNumber(String lookupResultsSequenceNumber) {
        this.lookupResultsSequenceNumber = lookupResultsSequenceNumber;
    }

    public boolean isSelectAllCheckbox() {
        return selectAllCheckbox;
    }

    public void setSelectAllCheckbox(boolean selectAllCheckbox) {
        this.selectAllCheckbox = selectAllCheckbox;
    }

    public String getSelectorRoleName() {
        return selectorRoleName;
    }

    public void setSelectorRoleName(String selectorRoleName) {
        this.selectorRoleName = selectorRoleName;
    }

    public String getSelectorRoleNamespace() {
        return selectorRoleNamespace;
    }

    public void setSelectorRoleNamespace(String selectorRoleNamespace) {
        this.selectorRoleNamespace = selectorRoleNamespace;
    }

    @Override
    public String getRefreshCaller() {
        return "refreshCaller";
    }

    public OleOrderQueueForm() {
        super();
        setDocument(new OleOrderQueueDocument());
        setDocTypeName("OLE_ORDQU");
    }

    @Override
    public void populate(HttpServletRequest req) {
        super.populate(req);
    }

    @Override
    public void addRequiredNonEditableProperties() {
        super.addRequiredNonEditableProperties();
        registerRequiredNonEditableProperty("lookupResultsSequenceNumber");
    }

    protected boolean canApprove() {

        String nameSpaceCode = OLEConstants.OleRequisitionItem.ORDER_HOLD_QUEUE_ROLE_NAMESPACE;

        boolean canApprove = KimApiServiceLocator.getPermissionService().hasPermission(
                GlobalVariables.getUserSession().getPerson().getPrincipalId(), nameSpaceCode,
                OLEConstants.OleRequisitionItem.ORDER_HOLD_QUEUE_APPROVE_REQ);
        return canApprove;
    }

    protected boolean canDelete() {

        String nameSpaceCode = OLEConstants.OleRequisitionItem.ORDER_HOLD_QUEUE_ROLE_NAMESPACE;

        boolean canDelete = KimApiServiceLocator.getPermissionService().hasPermission(
                GlobalVariables.getUserSession().getPerson().getPrincipalId(), nameSpaceCode,
                OLEConstants.OleRequisitionItem.ORDER_HOLD_QUEUE_APPROVE_REQ);
        return canDelete;
    }

    protected boolean canTotal() {
        return true;
    }

    protected boolean canAssign() {
        String documentTypeName = OLEConstants.OrderQueue.DOCUMENT_TYPE;
        String nameSpaceCode = OLEConstants.OleRequisitionItem.ORDER_HOLD_QUEUE_ROLE_NAMESPACE;

        boolean canAssign = SpringContext.getBean(IdentityManagementService.class).hasPermission(GlobalVariables.getUserSession().getPerson().getPrincipalId(), nameSpaceCode,
                OLEConstants.OleRequisitionItem.ORDER_HOLD_QUEUE_ASSIGN_PERMISSION);
        return canAssign;
    }

    protected boolean canCreateNewList() {
        return true;
    }

    protected boolean canTotalSelectedItems() {
        return true;
    }

    @Override
    public List<ExtraButton> getExtraButtons() {
        super.getExtraButtons();
        Map buttonsMap = createButtonsMap();

        if (canApprove()) {
            extraButtons.add((ExtraButton) buttonsMap.get("methodToCall.massApprove"));
        }
        if (canDelete()) {
            extraButtons.add((ExtraButton) buttonsMap.get("methodToCall.delete"));
        }
        /*if (canTotal()) {
            extraButtons.add((ExtraButton) buttonsMap.get("methodToCall.total"));
        }*/
        if (canAssign()) {
            extraButtons.add((ExtraButton) buttonsMap.get("methodToCall.assign"));
        }
        /*if (canCreateNewList()) {
            extraButtons.add((ExtraButton) buttonsMap.get("methodToCall.createNewList"));
        }*/
        if (canTotalSelectedItems()) {
            extraButtons.add((ExtraButton) buttonsMap.get("methodToCall.totalSelectedItems"));
        }

        return extraButtons;
    }

    /**
     * Creates a MAP for all the buttons to appear on the Purchase Order Form, and sets the attributes of these buttons.
     *
     * @return the button map created.
     */
    protected Map<String, ExtraButton> createButtonsMap() {
        HashMap<String, ExtraButton> result = new HashMap<String, ExtraButton>();

        // Retransmit button


        // Amend button
        ExtraButton approveButton = new ExtraButton();
        approveButton.setExtraButtonProperty("methodToCall.massApprove");
        approveButton.setExtraButtonSource("${" + OLEConstants.EXTERNALIZABLE_IMAGES_URL_KEY + "}approve.gif");
        approveButton.setExtraButtonAltText("Approve");

        ExtraButton deleteButton = new ExtraButton();
        deleteButton.setExtraButtonProperty("methodToCall.delete");
        deleteButton.setExtraButtonSource("${" + OLEConstants.EXTERNALIZABLE_IMAGES_URL_KEY + "}cancel requistion.gif");
        deleteButton.setExtraButtonAltText("Cancel Requisition");

        ExtraButton totalButton = new ExtraButton();
        totalButton.setExtraButtonProperty("methodToCall.total");
        totalButton.setExtraButtonSource("${" + OLEConstants.EXTERNALIZABLE_IMAGES_URL_KEY + "}tinybutton-add1.gif");
        totalButton.setExtraButtonAltText("Total");

        ExtraButton assignButton = new ExtraButton();
        assignButton.setExtraButtonProperty("methodToCall.assign");
        assignButton.setExtraButtonSource("${" + OLEConstants.EXTERNALIZABLE_IMAGES_URL_KEY + "}assign.gif");
        assignButton.setExtraButtonAltText("Assign");

        ExtraButton createNewListButton = new ExtraButton();
        createNewListButton.setExtraButtonProperty("methodToCall.createNewList");
        createNewListButton.setExtraButtonSource("${" + OLEConstants.EXTERNALIZABLE_IMAGES_URL_KEY + "}buttonsmall_amend.gif");
        createNewListButton.setExtraButtonAltText("Create New List");

        ExtraButton totalSelectedItemsButton = new ExtraButton();
        totalSelectedItemsButton.setExtraButtonProperty("methodToCall.totalSelectedItems");
        totalSelectedItemsButton.setExtraButtonSource("${" + OLEConstants.EXTERNALIZABLE_IMAGES_URL_KEY + "}totalselected_items.gif");
        totalSelectedItemsButton.setExtraButtonAltText("Total Selected Items");

        result.put(approveButton.getExtraButtonProperty(), approveButton);
        result.put(deleteButton.getExtraButtonProperty(), deleteButton);
        result.put(totalButton.getExtraButtonProperty(), totalButton);
        result.put(assignButton.getExtraButtonProperty(), assignButton);
        result.put(createNewListButton.getExtraButtonProperty(), createNewListButton);
        result.put(totalSelectedItemsButton.getExtraButtonProperty(), totalSelectedItemsButton);

        return result;

    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        List<OleRequisitionItem> refreshItems = new ArrayList<OleRequisitionItem>(0);
        List<OleRequisitionItem> orderQueueRequisitions = ((OleOrderQueueDocument) getDocument()).getRequisitionItems();
        if (orderQueueRequisitions != null) {
            for (OleRequisitionItem item : orderQueueRequisitions) {
                item.setItemAdded(false);
                refreshItems.add(item);
            }

            ((OleOrderQueueDocument) getDocument()).setRequisitionItems(refreshItems);
        }
        if (getLookupResultsSequenceNumber() != null) {
            setLookupResultsSequenceNumber(null);
        }
        super.reset(mapping, request);
    }
}
