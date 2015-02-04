package org.kuali.ole.deliver.maintenance;


import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.krad.maintenance.MaintainableImpl;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.maintenance.MaintenanceLock;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/11/12
 * Time: 7:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleDeliverRequestMaintenanceImpl extends MaintainableImpl {

    private DocstoreUtil docstoreUtil;
    public DocstoreUtil getDocstoreUtil() {

        if (docstoreUtil == null) {
            docstoreUtil = SpringContext.getBean(DocstoreUtil.class);

        }
        return docstoreUtil;
    }


    /**
     * This method will set the default value for the name object and also set the description when a new patron document is created
     *
     * @param document
     * @param requestParameters
     */
    @Override
    public void processAfterNew(MaintenanceDocument document,
                                Map<String, String[]> requestParameters) {
        super.processAfterNew(document, requestParameters);
        document.getDocumentHeader().setDocumentDescription(OLEConstants.NEW_REQUEST_DOC);
        OleDeliverRequestBo oleDeliverRequestBo = (OleDeliverRequestBo) document.getNewMaintainableObject().getDataObject();
        oleDeliverRequestBo.setRequestCreator(OLEConstants.OleDeliverRequest.REQUESTER_PATRON);
        oleDeliverRequestBo.setOperatorCreateId(GlobalVariables.getUserSession().getPrincipalId());
        oleDeliverRequestBo.setOperatorCreateName(GlobalVariables.getUserSession().getPrincipalName());
        oleDeliverRequestBo.setCreateDate(new Date(System.currentTimeMillis()));


    }

    /**
     * This method will set the description when copy is selected
     *
     * @param document
     * @param requestParameters
     */
    @Override
    public void processAfterCopy(MaintenanceDocument document, Map<String, String[]> requestParameters) {
        super.processAfterCopy(document, requestParameters);
        document.getDocumentHeader().setDocumentDescription(OLEConstants.COPY_REQUEST_DOC);
        OleDeliverRequestBo oleDeliverRequestBo = (OleDeliverRequestBo) document.getNewMaintainableObject().getDataObject();
        oleDeliverRequestBo.setCreateDate(new Date(System.currentTimeMillis()));
    }

    /**
     * This method will set the description for edit operation
     *
     * @param document
     * @param requestParameters
     */
    @Override
    public void processAfterEdit(MaintenanceDocument document, Map<String, String[]> requestParameters) {
        super.processAfterEdit(document, requestParameters);
        OleDeliverRequestBo oleDeliverRequestBo = (OleDeliverRequestBo) document.getNewMaintainableObject().getDataObject();
        document.getDocumentHeader().setDocumentDescription(OLEConstants.EDIT_REQUEST_DOC);
    }

    @Override
    public Object retrieveObjectForEditOrCopy(MaintenanceDocument document, Map<String, String> dataObjectKeys) {
        OleDeliverRequestBo oleDeliverRequestBo = (OleDeliverRequestBo) super.retrieveObjectForEditOrCopy(document, dataObjectKeys);
        OleDeliverRequestBo newOleDeliverRequestBo = (OleDeliverRequestBo) ObjectUtils.deepCopy(oleDeliverRequestBo);
        if (newOleDeliverRequestBo.getModifiedDate() == null) {
            newOleDeliverRequestBo.setModifiedDate(new Date(System.currentTimeMillis()));
        } //
        // if(newOleDeliverRequestBo.getOperatorModifiedId()==null || (newOleDeliverRequestBo.getOperatorModifiedId()!=null && newOleDeliverRequestBo.getOperatorModifiedId().isEmpty())){
        newOleDeliverRequestBo.setOperatorModifiedId(GlobalVariables.getUserSession().getPrincipalId());
        newOleDeliverRequestBo.setOperatorModifierName(GlobalVariables.getUserSession().getPrincipalName());
        // }
        if (newOleDeliverRequestBo.getOperatorCreateId() != null && !newOleDeliverRequestBo.getOperatorCreateId().isEmpty()) {
            newOleDeliverRequestBo.setRequestCreator(OLEConstants.OleDeliverRequest.REQUESTER_OPERATOR);
        } else if (newOleDeliverRequestBo.getProxyBorrowerId() != null && !newOleDeliverRequestBo.getProxyBorrowerId().isEmpty()) {
            newOleDeliverRequestBo.setRequestCreator(OLEConstants.OleDeliverRequest.REQUESTER_PROXY_PATRON);
        } else {
            newOleDeliverRequestBo.setRequestCreator(OLEConstants.OleDeliverRequest.REQUESTER_PATRON);
        }
        OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService = new OleDeliverRequestDocumentHelperServiceImpl();
        // newOleDeliverRequestBo = oleDeliverRequestDocumentHelperService.processItem(newOleDeliverRequestBo);
        OleDeliverRequestBo oleDeliverRequestBo1 = newOleDeliverRequestBo;

        getDocstoreUtil().isItemAvailableInDocStore(newOleDeliverRequestBo);

        newOleDeliverRequestBo.setOleItem(null);

        return newOleDeliverRequestBo;


    }

    @Override
    public List<MaintenanceLock> generateMaintenanceLocks() {
        return Collections.emptyList();
    }

}
