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

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.bo.OLESerialReceivingDocument;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.service.OleUuidCheckWebService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.service.BusinessObjectService;

import java.util.*;

public class OleUuidCheckWebServiceImpl implements OleUuidCheckWebService {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleUuidCheckWebServiceImpl.class);
    private BusinessObjectService businessObjectService;

    /**
     * @see org.kuali.ole.select.service.OleUuidCheckWebService#checkUuidExsistence(java.lang.String)
     */
    @Override
    public String checkUuidExsistence(String uuids) {
        // TODO Auto-generated method stub        
        String[] uuidArr = StringUtils.split(uuids, ",");
        List<String> uuidList = new ArrayList(Arrays.asList(uuidArr));
        StringBuilder linksSb = new StringBuilder();
        StringBuilder uuidsSb = new StringBuilder();
        String responseString = null;
        uuidList = checkUuidInRequisitionItem(uuidList, linksSb);
        if (uuidList.size() > 0)
            uuidList = checkUuidInPOItem(uuidList, linksSb);
        if (uuidList.size() > 0)
            uuidList = checkUuidInPaymentRequestItem(uuidList, linksSb);
        if (uuidList.size() > 0)
            uuidList = checkUuidInReceivingLineItem(uuidList, linksSb);
        if (uuidList.size() > 0)
            uuidList = checkUuidInCopies(uuidList, linksSb);
        if (uuidList.size() > 0)
            uuidList = checkUuidInRequest(uuidList, linksSb);
        if (uuidList.size() > 0)
            uuidList = checkUuidInLoan(uuidList, linksSb);
        if (uuidList.size() > 0)
            uuidList = checkUuidInSerialReceiving(uuidList, linksSb);

        for (String titleId : uuidList) {
            uuidsSb.append(titleId);
            uuidsSb.append(",");
        }
        if (uuidsSb.length() > 0 || linksSb.length() > 0) {
            responseString = (uuidsSb.length() > 0 ? uuidsSb.substring(0, uuidsSb.length() - 1) : "") + (linksSb.length() > 0 ? "|" + linksSb.substring(0, linksSb.length() - 2) : "");
            return responseString;
        } else {
            return "";
        }
    }

    /**
     * This method will search in the Requisition Item table whether the passed uuid exist or not, if exist returns List of those exsisted uuids
     *
     * @param uuid
     * @return List<String>
     */
    public List<String> checkUuidInRequisitionItem(List<String> uuid, StringBuilder linksSb) {
        boolean isLinked = false;
        Map<String, List> uuidMap = new HashMap<String, List>();
        uuidMap.put(OleSelectConstant.ITEMTITLEID, uuid);
        List<OleRequisitionItem> requisitionItems = (List<OleRequisitionItem>) getBusinessObjectService().findMatching(OleRequisitionItem.class, uuidMap);
        if (requisitionItems.size() > 0) {
            for (OleRequisitionItem requisitionItem : requisitionItems) {
                for (int i = 0; i < uuid.size(); i++) {
                    if (uuid.get(i).equalsIgnoreCase(requisitionItem.getItemTitleId())) {
                        uuid.remove(i);
                        isLinked = true;
                    }
                }
            }
        }
        if (isLinked) {
            linksSb.append(OLEConstants.REQUISITION).append(OLEConstants.COMMA).append(OLEConstants.SPACE);
        }
        return uuid;
    }

    /**
     * This method will search in the PurchaseOrder Item table whether the passed uuid exist or not, if exist returns List of those exsisted uuids
     *
     * @param uuid
     * @return List<String>
     */
    public List<String> checkUuidInPOItem(List<String> uuid, StringBuilder linksSb) {
        boolean isLinked = false;
        Map<String, List> uuidMap = new HashMap<String, List>();
        uuidMap.put(OleSelectConstant.ITEMTITLEID, uuid);
        List<OlePurchaseOrderItem> poItems = (List<OlePurchaseOrderItem>) getBusinessObjectService().findMatching(OlePurchaseOrderItem.class, uuidMap);
        if (poItems.size() > 0) {
            for (OlePurchaseOrderItem purchaseOrderItem : poItems) {
                for (int i = 0; i < uuid.size(); i++) {
                    if (uuid.get(i).equalsIgnoreCase(purchaseOrderItem.getItemTitleId())) {
                        uuid.remove(i);
                        isLinked = true;
                    }
                }
            }
        }
        if (isLinked) {
            linksSb.append(OLEConstants.PURCHASE_ORDER).append(OLEConstants.COMMA).append(OLEConstants.SPACE);
        }
        return uuid;
    }

    /**
     * This method will search in the Payment Request Item table whether the passed uuid exist or not, if exist returns List of those exsisted uuids
     *
     * @param uuid
     * @return List<String>
     */
    public List<String> checkUuidInPaymentRequestItem(List<String> uuid, StringBuilder linksSb) {
        boolean isLinked = false;
        Map<String, List> uuidMap = new HashMap<String, List>();
        uuidMap.put(OleSelectConstant.ITEMTITLEID, uuid);
        List<OlePaymentRequestItem> paymentRequestItems = (List<OlePaymentRequestItem>) getBusinessObjectService().findMatching(OlePaymentRequestItem.class, uuidMap);
        if (paymentRequestItems.size() > 0) {
            for (OlePaymentRequestItem paymentRequestItem : paymentRequestItems) {
                for (int i = 0; i < uuid.size(); i++) {
                    if (uuid.get(i).equalsIgnoreCase(paymentRequestItem.getItemTitleId())) {
                        uuid.remove(i);
                        isLinked = true;
                    }
                }
            }
        }
        if (isLinked) {
            linksSb.append(OLEConstants.PAYMENT_REQUEST).append(OLEConstants.COMMA).append(OLEConstants.SPACE);
        }
        return uuid;
    }

    /**
     * This method will search in the Receiving Line Item table whether the passed uuid exist or not, if exist returns List of those exsisted uuids
     *
     * @param uuid
     * @return List<String>
     */
    public List<String> checkUuidInReceivingLineItem(List<String> uuid, StringBuilder linksSb) {
        boolean isLinked = false;
        Map<String, List> uuidMap = new HashMap<String, List>();
        uuidMap.put(OleSelectConstant.ITEMTITLEID, uuid);
        List<OleLineItemReceivingDoc> lineItemReceivingItems = (List<OleLineItemReceivingDoc>) getBusinessObjectService().findMatching(OleLineItemReceivingDoc.class, uuidMap);
        if (lineItemReceivingItems.size() > 0) {
            for (OleLineItemReceivingDoc linItemReceivingDocItem : lineItemReceivingItems) {
                for (int i = 0; i < uuid.size(); i++) {
                    if (uuid.get(i).equalsIgnoreCase(linItemReceivingDocItem.getItemTitleId())) {
                        uuid.remove(i);
                        isLinked = true;
                    }
                }
            }
        }
        if (isLinked) {
            linksSb.append(OLEConstants.LINE_ITEM).append(OLEConstants.COMMA).append(OLEConstants.SPACE);
        }
        return uuid;
    }


    /**
     * This method will search in copies table whether the passed uuids exist or not, if exists returns List of those non existing uuids
     *
     * @param uuids
     * @return List<String>
     */
    public List<String> checkUuidInCopies(List<String> uuids, StringBuilder linksSb) {
        boolean isLinked = false;
        Set<OleCopy> copies = new TreeSet<>(new Comparator<OleCopy>() {
            @Override
            public int compare(OleCopy copy1, OleCopy copy2) {
                if (copy1.getCopyId().equals(copy2.getCopyId())) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        Map<String, List> uuidMap = new HashMap<String, List>();
        uuidMap.put(OLEConstants.OleDeliverRequest.ITEM_UUID, uuids);
        copies.addAll(getBusinessObjectService().findMatching(OleCopy.class, uuidMap));
        uuidMap.clear();
        uuidMap.put(OLEConstants.INSTANCE_ID, uuids);
        copies.addAll(getBusinessObjectService().findMatching(OleCopy.class, uuidMap));
        uuidMap.clear();
        uuidMap.put(OLEConstants.BIB_ID, uuids);
        copies.addAll(getBusinessObjectService().findMatching(OleCopy.class, uuidMap));
        if (copies.size() > 0) {
            for (OleCopy oleCopy : copies) {
                for (int i = 0; i < uuids.size(); i++) {
                    if (uuids.get(i).equalsIgnoreCase(oleCopy.getItemUUID()) || uuids.get(i).equalsIgnoreCase(oleCopy.getInstanceId()) || uuids.get(i).equalsIgnoreCase(oleCopy.getBibId())) {
                        uuids.remove(i);
                        isLinked = true;
                    }
                }
            }
        }
        if (isLinked) {
            linksSb.append(OLEConstants.COPY).append(OLEConstants.COMMA).append(OLEConstants.SPACE);
        }
        return uuids;
    }

    /**
     * This method will search in deliver request table whether the passed uuids exist or not, if exists returns List of those non existing uuids
     *
     * @param uuids
     * @return List<String>
     */
    public List<String> checkUuidInRequest(List<String> uuids, StringBuilder linksSb) {
        boolean isLinked = false;
        Map<String, List> uuidMap = new HashMap<String, List>();
        uuidMap.put(OLEConstants.ITEM_UUID, uuids);
        List<OleDeliverRequestBo> oleDeliverRequestBos = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, uuidMap);
        if (oleDeliverRequestBos.size() > 0) {
            for (OleDeliverRequestBo oleDeliverRequestBo : oleDeliverRequestBos) {
                for (int i = 0; i < uuids.size(); i++) {
                    if (uuids.get(i).equalsIgnoreCase(oleDeliverRequestBo.getItemUuid())) {
                        uuids.remove(i);
                        isLinked = true;
                    }
                }
            }
        }
        if (isLinked) {
            linksSb.append(OLEConstants.REQUEST).append(OLEConstants.COMMA).append(OLEConstants.SPACE);
        }
        return uuids;
    }

    /**
     * This method will search in loan table whether the passed uuids exist or not, if exists returns List of those non existing uuids
     *
     * @param uuids
     * @return List<String>
     */
    public List<String> checkUuidInLoan(List<String> uuids, StringBuilder linksSb) {
        boolean isLinked = false;
        Map<String, List> uuidMap = new HashMap<String, List>();
        uuidMap.put(OLEConstants.ITEM_UUID, uuids);
        List<OleLoanDocument> oleLoanDocuments = (List<OleLoanDocument>) getBusinessObjectService().findMatching(OleLoanDocument.class, uuidMap);
        if (oleLoanDocuments.size() > 0) {
            for (OleLoanDocument oleLoanDocument : oleLoanDocuments) {
                for (int i = 0; i < uuids.size(); i++) {
                    if (uuids.get(i).equalsIgnoreCase(oleLoanDocument.getItemUuid())) {
                        uuids.remove(i);
                        isLinked = true;
                    }
                }
            }
        }
        if (isLinked) {
            linksSb.append(OLEConstants.LOAN).append(OLEConstants.COMMA).append(OLEConstants.SPACE);
        }
        return uuids;
    }

    /**
     * This method will search in serial receiving table whether the passed uuids exist or not, if exists returns List of those non existing uuids
     *
     * @param uuids
     * @return List<String>
     */
    public List<String> checkUuidInSerialReceiving(List<String> uuids, StringBuilder linksSb) {
        boolean isLinked = false;
        Map<String, List> uuidMap = new HashMap<String, List>();
        uuidMap.put(OLEConstants.INSTANCE_ID, uuids);
        List<OLESerialReceivingDocument> oleSerialReceivingDocuments = (List<OLESerialReceivingDocument>) getBusinessObjectService().findMatching(OLESerialReceivingDocument.class, uuidMap);
        if (oleSerialReceivingDocuments.size() > 0) {
            for (OLESerialReceivingDocument oleSerialReceivingDocument : oleSerialReceivingDocuments) {
                for (int i = 0; i < uuids.size(); i++) {
                    if (uuids.get(i).equalsIgnoreCase(oleSerialReceivingDocument.getInstanceId()) && oleSerialReceivingDocument.isActive()) {
                        uuids.remove(i);
                        isLinked = true;
                    }
                }
            }
        }
        if (isLinked) {
            linksSb.append(OLEConstants.SERIAL_RECEIVING).append(OLEConstants.COMMA).append(OLEConstants.SPACE);
        }
        return uuids;
    }


    /**
     * Gets the businessObjectService attribute.
     *
     * @return Returns the businessObjectService.
     */
    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null)
            businessObjectService = SpringContext.getBean(org.kuali.rice.krad.service.BusinessObjectService.class);
        return businessObjectService;
    }

}
