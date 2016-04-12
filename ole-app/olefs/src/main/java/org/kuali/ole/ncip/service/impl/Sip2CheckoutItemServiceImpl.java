package org.kuali.ole.ncip.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.asr.handler.CheckoutItemResponseHandler;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.deliver.util.DroolsResponse;
import org.kuali.ole.deliver.util.ItemInfoUtil;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.utility.OleStopWatch;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenchulakshmig on 8/24/15.
 */
public class Sip2CheckoutItemServiceImpl extends CheckoutItemServiceImpl {

    @Override
    public String prepareResponse() {

        switch (responseFormatType) {
            case ("XML"):
                response = ((CheckoutItemResponseHandler) getResponseHandler()).marshalObjectToSIP2Xml(getOleCheckOutItem());
                break;
            case ("JSON"):
                response = getResponseHandler().marshalObjectToJson(getOleCheckOutItem());
                break;
        }

        return response;
    }

    @Override
    public String getOperatorId(String operatorId) {
        return ParameterValueResolver.getInstance().getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, operatorId);
    }

    @Override
    protected String preProcess(Map checkoutParameters) {
        String itemBarcode = (String) checkoutParameters.get("itemBarcode");
        ItemRecord itemRecord = ItemInfoUtil.getInstance().getItemRecordByBarcode(itemBarcode);
        if (itemRecord !=null && itemRecord.getItemStatusRecord()!=null && OLEConstants.ITEM_STATUS_LOST.equalsIgnoreCase(itemRecord.getItemStatusRecord().getCode())){
            getOleCheckOutItem().setCode("500");
            getOleCheckOutItem().setMessage("Cannot loan item, item is 'lost'.Please go to desk.");
            return prepareResponse();
        }
        Map map = new HashMap();
        map.put("itemId", itemBarcode);
        map.put("patronId", getOlePatronDocument().getOlePatronId());
        List<OleLoanDocument> oleLoanDocuments = (List<OleLoanDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleLoanDocument.class, map);
        if (CollectionUtils.isNotEmpty(oleLoanDocuments)) {
            List<String> items = new ArrayList<>();
            items.add(itemBarcode);
            checkoutParameters.put("itemBarcodes", items);
            return new Sip2RenewItemService().renewItems(checkoutParameters);
        }
        return null;
    }

    @Override
    protected String fireRules() {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        ArrayList<Object> facts = new ArrayList<>();
        facts.add(getOlePatronDocument());
        DroolsResponse droolsResponse = new DroolsResponse();
        facts.add(droolsResponse);
        new CircUtilController().fireRules(facts, null, "lookup-user-sip2");
        return droolsResponse.getErrorMessage().getErrorMessage();
    }
}
