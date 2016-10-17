package org.kuali.ole.ncip.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleItemSearch;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.ncip.service.CirculationRestService;
import org.kuali.ole.olekrad.filter.OLELoginFilter;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sheiksalahudeenm on 25/6/15.
 */
public class CirculationRestServiceImpl implements CirculationRestService {

    private DocstoreUtil docstoreUtil;
    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreUtil getDocstoreUtil() {
        if (docstoreUtil == null) {
            docstoreUtil = SpringContext.getBean(DocstoreUtil.class);
        }
        return docstoreUtil;
    }

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    @Override
    public String renewItems(Map renewParameters) {
        return new NonSip2RenewItemService().renewItems(renewParameters);
    }

    @Override
    public String renewItemsSIP2(Map renewParameters) {
        return new Sip2RenewItemService().renewItems(renewParameters);

    }

    @Override
    public String acceptItemSIP2(Map acceptItemParameters) {
        return null;
    }

    @Override
    public String checkoutItem(Map checkoutParameters) {
        return new NonSip2CheckoutItemServiceImpl().checkoutItem(checkoutParameters);
    }

    @Override
    public String checkoutItemSIP2(Map checkoutParameters) {
        return new Sip2CheckoutItemServiceImpl().checkoutItem(checkoutParameters);
    }

    @Override
    public String checkinItem(Map checkinParameters) {
        return new NonSip2CheckinItemServiceImplImpl().checkinItem(checkinParameters);
    }

    @Override
    public String checkinItemSIP2(Map checkinParameters) {
        return new Sip2CheckinItemServiceImplImpl().checkinItem(checkinParameters);
    }

    @Override
    public String lookupUser(Map lookupUserParameters) {
        return new NonSip2LookupUserServiceImpl().lookupUser(lookupUserParameters);
    }

    @Override
    public String lookupUserSIP2(Map lookupUserParameters) {
        return new Sip2LookupUserServiceImpl().lookupUser(lookupUserParameters);
    }

    @Override
    public String patronStatusSIP2(Map lookupUserParameters) {
        return new Sip2PatronStatusServiceImpl().lookupUser(lookupUserParameters);
    }

    @Override
    public String patronBlockSIP2(Map patronBlockParameters) {
        return new Sip2PatronBlockServiceImpl().lookupUser(patronBlockParameters);
    }

    @Override
    public String patronEnableSIP2(Map patronBlockParameters) {
        return new Sip2PatronEnableServiceImpl().lookupUser(patronBlockParameters);
    }

    @Override
    public String loginSIP2(Map lookupUserParameters) {
        String loginUserId = (String) lookupUserParameters.get("loginUserId");
        String loginPassword = (String) lookupUserParameters.get("loginPassword");
        OLELoginFilter oleLoginFilter = new OLELoginFilter();
        boolean validateUser = oleLoginFilter.checkValidUserNameAndPassword(loginUserId,
                loginPassword);

        JSONObject responseJsonObject = new JSONObject();
        try {
            responseJsonObject.put("loginStatus",validateUser);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseJsonObject.toString();
    }

    @Override
    public String placeRequestSIP2(Map placeReqeustParameters) {
        return null;
    }

    @Override
    public String cancelRequestVuFind(Map cancelRequestParameters) {
        return null;
    }

    @Override
    public String itemInfoSIP2(Map itemInfoParameters) {
        String itemIdentifier = (String) itemInfoParameters.get("itemIdentifier");
        SearchParams item_search_Params = new SearchParams();
        item_search_Params.getSearchConditions().add(item_search_Params.buildSearchCondition("phrase", item_search_Params.buildSearchField(DocType.ITEM.getCode(), Item.ITEM_BARCODE, itemIdentifier), "AND"));
        getDocstoreUtil().getSearchResultFields(item_search_Params);
        SearchResponse searchResponse = null;
        JSONObject responseJsonObject = new JSONObject();
        try {
            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(item_search_Params);
            List<OleItemSearch> oleItemSearches = getDocstoreUtil().getSearchResults(searchResponse);
            if (CollectionUtils.isNotEmpty(oleItemSearches)) {
                OleItemSearch oleItemSearch = oleItemSearches.get(0);
                responseJsonObject.put("itemBarCode", oleItemSearch.getItemBarCode());
                responseJsonObject.put("title", oleItemSearch.getTitle());
                responseJsonObject.put("author", oleItemSearch.getAuthor());
                responseJsonObject.put("shelvingLocation", oleItemSearch.getShelvingLocation());
                responseJsonObject.put("itemStatus", oleItemSearch.getItemStatus());
                responseJsonObject.put("itemStatusCode", itemStatusCodeSIP2(oleItemSearch.getItemStatus()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseJsonObject.toString();
    }


    public String itemStatusCodeSIP2(String itemStatus){
        Map<String, String> itemStatusCodeMap = new HashMap<>();
        String value = ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEConstants.SIP_CIRC_STATUS);
        for(String keyValueList : value.split(",")){
            String[] keyValue = keyValueList.split(":");
            if(keyValue.length==2)
                itemStatusCodeMap.put(keyValue[0],keyValue[1]);
        }
        return itemStatusCodeMap.get(itemStatus);
    }

}
