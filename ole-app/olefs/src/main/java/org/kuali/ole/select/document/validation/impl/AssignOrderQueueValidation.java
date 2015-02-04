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
package org.kuali.ole.select.document.validation.impl;

import org.kuali.ole.select.document.OleOrderQueueDocument;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.document.validation.GenericValidation;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.krad.util.GlobalVariables;

public class AssignOrderQueueValidation extends GenericValidation {

    private OleOrderQueueDocument orderQueueForValidation;

    public OleOrderQueueDocument getOrderQueueForValidation() {
        return orderQueueForValidation;
    }

    public void setOrderQueueForValidation(OleOrderQueueDocument orderQueueForValidation) {
        this.orderQueueForValidation = orderQueueForValidation;
    }

    @Override
    public boolean validate(AttributedDocumentEvent event) {
        boolean isValid = true;
        if (orderQueueForValidation.getPrincipalName() == null)
            isValid = false;
        else
            isValid = true;

        if (!isValid)
            GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.PRINCIPAL_NAME, OLEKeyConstants.ERROR_REQUIRED, "User Id");

        return isValid;

    }
    
    /*private boolean isAuthorizedToApprove(OrderQueueDocument document, String principalId){
        List<OleRequisitionItem> requisitions = document.getRequisitions();
        List<OleRequisitionItem> authorizedRequisitions = new ArrayList<OleRequisitionItem>();
        boolean flag = false;
        OleRequisitionDocument requsitionDocument;
        for(OleRequisitionItem item:requisitions){
            if(item.isItemAdded()){
                Long requisitionDocumentId = new Long(item.getRequisition().getPurapDocumentIdentifier());
                requsitionDocument = (OleRequisitionDocument) KNSServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OleRequisitionDocument.class,requisitionDocumentId);
                requsitionDocument.setDocumentHeader(KNSServiceLocator.getDocumentHeaderService().getDocumentHeaderById(item.getRequisition().getDocumentNumber()));
                Person principalPerson = SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId());
                try{
                    requsitionDocument.getDocumentHeader().setWorkflowDocument(KNSServiceLocator.getWorkflowDocumentService().createWorkflowDocument(new Long(item.getRequisition().getDocumentNumber()), principalPerson));
                }catch(WorkflowException we){
                   we.printStackTrace();
                }
                if (!SpringContext.getBean(DocumentHelperService.class).getDocumentAuthorizer(requsitionDocument).isAuthorizedByTemplate(requsitionDocument, KRADConstants.KRAD_NAMESPACE, KimConstants.PermissionTemplateNames.APPROVE_DOCUMENT, principalId)) {
                    GlobalVariables.getMessageMap().putError("requisitions", "error.requisition.unauthorizedToApprove", new String[]{document.getSelectorUserId(),item.getRequisition().getDocumentNumber()});
                    item.setItemAdded(false);
                    authorizedRequisitions.add(item);
                }else{
                    flag = true;
                }
            }
            authorizedRequisitions.add(item);
        }
        document.setRequisitions(authorizedRequisitions);
        if(flag)
            return true;
        else
            return false;
    }*/

}
