/*
 * Copyright 2012 The Kuali Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.service.impl;

import org.kuali.ole.OleEditorResponseHandler;
import org.kuali.ole.OleOrderRecordHandler;
import org.kuali.ole.OleOrderRecords;
import org.kuali.ole.batch.bo.OLEBatchProcessJobDetailsBo;
import org.kuali.ole.pojo.OleEditorResponse;
import org.kuali.ole.select.businessobject.OleDocstoreResponse;
import org.kuali.ole.select.businessobject.OlePaymentMethod;
import org.kuali.ole.select.service.OleExposedWebService;
import org.kuali.ole.select.service.OleReqPOLoadTransactionsService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.service.BusinessObjectService;

import java.util.HashMap;
import java.util.List;

public class OleExposedWebServiceImpl implements OleExposedWebService {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleExposedWebService.class);
    private OleReqPOLoadTransactionsService oleReqPOLoadTransactionsService;

    @Override
    public void createReqAndPO(String oleOrderRecordXMLContent, OLEBatchProcessJobDetailsBo job) {
        try {
            OleOrderRecordHandler oleOrderRecordHandler = new OleOrderRecordHandler();
            if (LOG.isDebugEnabled()) {
                LOG.debug("oleOrderRecordXMLContent----------->" + oleOrderRecordXMLContent);
            }
            OleOrderRecords oleOrderRecords = oleOrderRecordHandler.fromXML(oleOrderRecordXMLContent);
            OleReqPOLoadTransactionsService oleReqPOLoadTransactionsService = getOleReqPOLoadTransactionsService();
            List reqList = oleReqPOLoadTransactionsService.saveRequisitionDocument(oleOrderRecords, job);
            oleReqPOLoadTransactionsService.createAcquisitionDocument(reqList, oleOrderRecords, job);
        } catch (Exception e) {
            LOG.error("Exception while creating Req & PO"+e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public OleReqPOLoadTransactionsService getOleReqPOLoadTransactionsService() {
        return oleReqPOLoadTransactionsService;
    }

    public void setOleReqPOLoadTransactionsService(OleReqPOLoadTransactionsService oleReqPOLoadTransactionsService) {
        this.oleReqPOLoadTransactionsService = oleReqPOLoadTransactionsService;
    }

    @Override
    public void addDoctoreResponse(String docstoreResponse) {
        HashMap<String, OleEditorResponse> docstoreResponses = new HashMap<String, OleEditorResponse>();
        OleEditorResponseHandler oleEditorResponseHandler = new OleEditorResponseHandler();
        OleEditorResponse oleEditorResponse = oleEditorResponseHandler.fromXML(docstoreResponse);
        docstoreResponses.put(oleEditorResponse.getTokenId(), oleEditorResponse);

        OleDocstoreResponse.getInstance().setDocstoreResponse(docstoreResponses);
        if (LOG.isDebugEnabled()) {
            LOG.debug("#########OleDocstoreResponse##########" + OleDocstoreResponse.getInstance().getDocstoreResponse().toString());
        }
    }

    @Override
    public String getPaymentMethod() {

        StringBuffer paymentMethodbuffer = new StringBuffer();
        BusinessObjectService businessObjectService = SpringContext.getBean(BusinessObjectService.class);
        List<OlePaymentMethod> olePaymentMethods = (List<OlePaymentMethod>) businessObjectService.findAll(OlePaymentMethod.class);
        if (olePaymentMethods != null && olePaymentMethods.size() > 0) {
            for (int i = 0; i < olePaymentMethods.size(); i++) {
                if (olePaymentMethods.get(i).isActive()) {
                    paymentMethodbuffer.append(olePaymentMethods.get(i).getPaymentMethod() + ",");
                }
            }

        }
        return paymentMethodbuffer.toString();
    }

}
