package org.kuali.ole.select.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEPropertyConstants;
import org.kuali.ole.describe.bo.SearchResultDisplayRow;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.search.*;
import org.kuali.ole.docstore.discovery.service.QueryService;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.bo.OLELinkPurapDonor;
import org.kuali.ole.select.businessobject.OleCopy;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.OlePaymentRequestDocument;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.form.OLEAnnualStewardshipReportForm;
import org.kuali.ole.service.impl.OleLicenseRequestServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.LookupService;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 12/6/13
 * Time: 7:51 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/searchAnnualStewardshipReportController")
public class OLEAnnualStewardshipReportController extends UifControllerBase {

    private QueryService queryService;
    private static final Logger LOG = Logger.getLogger(OLEEResourceSearchController.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(OLEConstants.OLEEResourceRecord.CREATED_DATE_FORMAT);
    private BusinessObjectService boService = KRADServiceLocator.getBusinessObjectService();
    private DocstoreClientLocator docstoreClientLocator;

    private LookupService getLookupService() {
        return KRADServiceLocatorWeb.getLookupService();
    }

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest httpServletRequest) {
        return new OLEAnnualStewardshipReportForm();
    }

    @RequestMapping(params = "methodToCall=search")
    public ModelAndView search(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Entering annual stewardship search");
        OLEAnnualStewardshipReportForm oleAnnualStewardshipReportForm = (OLEAnnualStewardshipReportForm) form;
        List<SearchResultDisplayRow> searchResultDisplayRowList = new ArrayList<>();
        if (StringUtils.isNotEmpty(oleAnnualStewardshipReportForm.getDonorCode())) {
            Map donorMap = new HashMap();
            donorMap.put(OLEConstants.DONOR_CODE, oleAnnualStewardshipReportForm.getDonorCode());
            List<OLEDonor> oleDonorList = (List<OLEDonor>) getLookupService().findCollectionBySearch(OLEDonor.class, donorMap);
            if (oleDonorList != null && oleDonorList.size() > 0l) {
                searchResultDisplayRowList = getSearchResultDisplayRowListFromItems(OLEConstants.DONOR_CODE_SEARCH, oleDonorList.get(0).getDonorCode());
                searchResultDisplayRowList.addAll(getSearchResultDisplayRowListFromEHoldings(OLEConstants.DONOR_CODE_SEARCH, oleDonorList.get(0).getDonorCode()));
            } else {
                oleAnnualStewardshipReportForm.setSaerSearchResultDisplayRowList(null);
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DONOR_NOT_FOUND);
                return getUIFModelAndView(oleAnnualStewardshipReportForm);
            }
        } else {
            searchResultDisplayRowList = getSearchResultDisplayRowListFromItems(OLEConstants.DONOR_CODE_SEARCH, oleAnnualStewardshipReportForm.getDonorCode());
            searchResultDisplayRowList.addAll(getSearchResultDisplayRowListFromEHoldings(OLEConstants.DONOR_CODE_SEARCH, oleAnnualStewardshipReportForm.getDonorCode()));
        }
        if (StringUtils.isNotEmpty(oleAnnualStewardshipReportForm.getStatus())) {
            searchResultDisplayRowList = getSearchResultDisplayRowListBasedOnStatus(searchResultDisplayRowList, oleAnnualStewardshipReportForm.getStatus());
        }
        if (oleAnnualStewardshipReportForm.getFromDate() != null || oleAnnualStewardshipReportForm.getToDate() != null) {
            searchResultDisplayRowList = getSearchResultDisplayRowListBasedOnDate(searchResultDisplayRowList, oleAnnualStewardshipReportForm.getFromDate(), oleAnnualStewardshipReportForm.getToDate());
        }
        if (searchResultDisplayRowList.size() == 0) {
            oleAnnualStewardshipReportForm.setSaerSearchResultDisplayRowList(null);
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.NO_RECORD_FOUND);
        } else {
            oleAnnualStewardshipReportForm.setSaerSearchResultDisplayRowList(searchResultDisplayRowList);
        }
        LOG.debug("Leaving annual stewardship search");
        return getUIFModelAndView(oleAnnualStewardshipReportForm);
    }

    @RequestMapping(params = "methodToCall=clearSearch")
    public ModelAndView clearSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        OLEAnnualStewardshipReportForm oleAnnualStewardshipReportForm = (OLEAnnualStewardshipReportForm) form;
        oleAnnualStewardshipReportForm.setDonorCode(null);
        oleAnnualStewardshipReportForm.setStatus(null);
        oleAnnualStewardshipReportForm.setFromDate(null);
        oleAnnualStewardshipReportForm.setToDate(null);
        oleAnnualStewardshipReportForm.setSaerSearchResultDisplayRowList(null);
        return getUIFModelAndView(oleAnnualStewardshipReportForm);
    }

    @RequestMapping(params = "methodToCall=cancel")
    public ModelAndView cancel(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        String baseUrl = ConfigContext.getCurrentContextConfig().getProperty(OLEPropertyConstants.OLE_URL_BASE);
        String url = baseUrl + "/portal.do";
        Properties props = new Properties();
        props.put(UifParameters.METHOD_TO_CALL, UifConstants.MethodToCallNames.REFRESH);
        if (org.apache.commons.lang.StringUtils.isNotBlank(form.getReturnFormKey())) {
            props.put(UifParameters.FORM_KEY, form.getReturnFormKey());
        }
        return performRedirect(form, url, props);
    }

    private List<SearchResultDisplayRow> getSearchResultDisplayRowListBasedOnStatus(List<SearchResultDisplayRow> searchResultDisplayRowList, String status) {
        List<SearchResultDisplayRow> searchResultDisplayRows = new ArrayList<>();
        for (SearchResultDisplayRow searchResultDisplayRow : searchResultDisplayRowList) {
            if (searchResultDisplayRow.getPoLineItemIds().size() > 0) {
                for (Integer poLineItemId : searchResultDisplayRow.getPoLineItemIds()) {
                    if (getSearchResultDisplayRowBasedOnStatus(status,searchResultDisplayRow,OLEConstants.INSTANCE_ID,searchResultDisplayRow.getInstanceIdentifier(),poLineItemId.toString())){
                        searchResultDisplayRows.add(searchResultDisplayRow);
                    }
                }
            } else {
                if (getSearchResultDisplayRowBasedOnStatus(status,searchResultDisplayRow,OLEConstants.OVERLAY_ITEMUUID,searchResultDisplayRow.getItemIdentifier(),searchResultDisplayRow.getPoLineItemId())){
                    searchResultDisplayRows.add(searchResultDisplayRow);
                }
            }
        }
        return searchResultDisplayRows;
    }

    private boolean getSearchResultDisplayRowBasedOnStatus(String status, SearchResultDisplayRow searchResultDisplayRow,String key,String value, String poLineItemId){
        boolean paidFlag = false;
        boolean recievedFlag = false;
        if (status.equals(OLEConstants.RECIEVED) || status.equals(OLEConstants.RECEIVED_PAID)) {
            Map map = new HashMap();
            map.put(OLEConstants.OLEEResourceRecord.ERESOURCE_PO_ID, poLineItemId);
            map.put(key, value);
            OleCopy oleCopy = boService.findByPrimaryKey(OleCopy.class, map);
            if (oleCopy != null && oleCopy.getReceiptStatus().equals(OLEConstants.RECIEVED)) {
                recievedFlag = true;
            }
        }
        if (status.equals(OLEConstants.PAID) || status.equals(OLEConstants.RECEIVED_PAID)) {
            Map map = new HashMap();
            map.put(OLEConstants.BIB_ITEM_ID, poLineItemId);
            OlePurchaseOrderItem olePurchaseOrderItem = boService.findByPrimaryKey(OlePurchaseOrderItem.class, map);
            if (olePurchaseOrderItem != null && olePurchaseOrderItem.getDocumentNumber() != null) {
                map.clear();
                map.put(OLEConstants.DOC_NUM, olePurchaseOrderItem.getDocumentNumber());
                OlePurchaseOrderDocument olePurchaseOrderDocument = boService.findByPrimaryKey(OlePurchaseOrderDocument.class, map);
                map.clear();
                map.put(OLEConstants.PUR_ORDER_IDENTIFIER, olePurchaseOrderDocument.getPurapDocumentIdentifier());
                OlePaymentRequestDocument olePaymentRequestDocument = boService.findByPrimaryKey(OlePaymentRequestDocument.class, map);
                if (olePaymentRequestDocument != null) {
                    paidFlag = true;
                }
            }
        }
        if ((status.equals(OLEConstants.RECIEVED) && recievedFlag == true) || (status.equals(OLEConstants.PAID) && paidFlag == true) || (status.equals(OLEConstants.RECEIVED_PAID) && recievedFlag == true && paidFlag == true)) {
            return true;
        }
        return false;
    }

    private List<SearchResultDisplayRow> getSearchResultDisplayRowListBasedOnDate(List<SearchResultDisplayRow> searchResultDisplayRowList, Date fromDate, Date toDate) {
        List<SearchResultDisplayRow> searchResultDisplayRows = new ArrayList<>();
        for (SearchResultDisplayRow searchResultDisplayRow : searchResultDisplayRowList) {
            if (searchResultDisplayRow.getPoLineItemIds().size() > 0) {
                for (Integer poLineItemId : searchResultDisplayRow.getPoLineItemIds()) {
                    if (poLineItemId != null && getSearchResultDisplayRowBasedOnDate(poLineItemId.toString(), fromDate, toDate)) {
                        searchResultDisplayRows.add(searchResultDisplayRow);
                    }
                }
            } else {
                if (getSearchResultDisplayRowBasedOnDate(searchResultDisplayRow.getPoLineItemId(), fromDate, toDate)) {
                    searchResultDisplayRows.add(searchResultDisplayRow);
                }
            }
        }
        return searchResultDisplayRows;
    }

    private boolean getSearchResultDisplayRowBasedOnDate(String poLineItemId, Date fromDate, Date toDate) {
        Map map = new HashMap();
        map.put(OLEConstants.PURAP_DOC_IDENTIFIER, poLineItemId);
       /* OlePurchaseOrderItem olePurchaseOrderItem = boService.findByPrimaryKey(OlePurchaseOrderItem.class, map);
        if (olePurchaseOrderItem != null && olePurchaseOrderItem.getDocumentNumber() != null) {
            map.clear();
            map.put(OLEConstants.DOC_NUM, olePurchaseOrderItem.getDocumentNumber());*/
            OlePurchaseOrderDocument olePurchaseOrderDocument = boService.findByPrimaryKey(OlePurchaseOrderDocument.class, map);
            if(olePurchaseOrderDocument != null){
            Date purchaseOrderDate = olePurchaseOrderDocument.getPurchaseOrderCreateTimestamp();
            try {
                String begin = null;
                if (fromDate != null) {
                    begin = dateFormat.format(fromDate);
                }
                String end = null;
                if (toDate != null) {
                    end = dateFormat.format(toDate);
                }
                boolean isValid = false;
                OleLicenseRequestServiceImpl oleLicenseRequestService = GlobalResourceLoader.getService(OLEConstants.OleLicenseRequest.LICENSE_REQUEST_SERVICE);
                isValid = oleLicenseRequestService.validateDate(purchaseOrderDate, begin, end);
                if (isValid) {
                    return true;
                }
            } catch (Exception e) {
                LOG.error("Exception while calling the licenseRequest service" + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    private List<SearchResultDisplayRow> getSearchResultDisplayRowListFromItems(String code, String value) throws Exception {
        SearchParams searchParams = new SearchParams();
        SearchField searchField = searchParams.buildSearchField(DocType.ITEM.getCode(), code, value);
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition(OLEConstants.OLEEResourceRecord.AND, searchField, OLEConstants.OLEEResourceRecord.AND));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), OLEConstants.COPY_NUM_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), OLEConstants.ENUMERATION_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), OLEConstants.CHRONOLOGY_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), OLEConstants.DONOR_CODE_SEARCH));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), OLEConstants.ITEM_STATUS_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), OLEConstants.PURCHASE_ORDER_LINE_ID_SEARCH));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), OLEConstants.ITEM_IDENTIFIER_SEARCH));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), OLEConstants.CALL_NUM_PREFIX_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), OLEConstants.CALL_NUM_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), OLEConstants.LOACTION_LEVEL_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.HOLDINGS.getCode(), OLEConstants.LOACTION_LEVEL_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.HOLDINGS.getCode(), OLEConstants.CALL_NUM_PREFIX_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.HOLDINGS.getCode(), OLEConstants.CALL_NUM_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.HOLDINGS.getCode(), OLEConstants.OVERLAY_HOLDINGS_IDENTIFIER));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.BIB.getCode(), Bib.TITLE));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.BIB.getCode(), Bib.BIBIDENTIFIER));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.BIB.getCode(), Bib.AUTHOR));
        searchParams.setPageSize(10000);
        if (LOG.isDebugEnabled()){
            LOG.debug("searchparams ==========>>>>>>>>> " + searchParams.serialize(searchParams));
        }
        SearchResponse searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
        if (LOG.isDebugEnabled()){
            LOG.debug("SearchResponse ==========>>>>>>>>> " + searchResponse.serialize(searchResponse));
        }
        List<SearchResultDisplayRow> searchResultDisplayRowList = new ArrayList<>();
        for (SearchResult searchResult : searchResponse.getSearchResults()) {
            String itemCallNumberPrefix = null;
            String itemCallNumber = null;
            String itemLocation = null;
            String holdingsLocation = null;
            String holdingsCallNumberPrefix = null;
            String holdingsCallNumber = null;
            String callNumber = null;
            String callNumberPrefix = null;
            for (SearchResultField resultField : searchResult.getSearchResultFields()) {
                if (resultField.getDocType().equalsIgnoreCase(DocType.ITEM.getCode()) && resultField.getFieldName().equalsIgnoreCase(OLEConstants.DONOR_CODE_SEARCH) && resultField.getFieldValue() != null) {
                    SearchResultDisplayRow searchResultDisplayRow = new SearchResultDisplayRow();
                    if ((StringUtils.isNotBlank(value) && resultField.getFieldValue().equals(value)) || StringUtils.isBlank(value)) {
                        searchResultDisplayRow.setDonorCode(resultField.getFieldValue());
                    }
                    if (StringUtils.isNotEmpty(searchResultDisplayRow.getDonorCode())) {
                        for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                            if (searchResultField.getDocType().equalsIgnoreCase(DocType.ITEM.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.COPY_NUM_DISPLAY)) {
                                searchResultDisplayRow.setCopyNumber(searchResultField.getFieldValue());
                            } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.ITEM.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.ENUMERATION_DISPLAY)) {
                                searchResultDisplayRow.setEnumeration(searchResultField.getFieldValue());
                            } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.ITEM.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.CHRONOLOGY_DISPLAY)) {
                                searchResultDisplayRow.setChronology(searchResultField.getFieldValue());
                            } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.ITEM.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.ITEM_STATUS_DISPLAY)) {
                                searchResultDisplayRow.setItemStatus(searchResultField.getFieldValue());
                            } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.ITEM.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.PURCHASE_ORDER_LINE_ID_SEARCH)) {
                                searchResultDisplayRow.setPoLineItemId(searchResultField.getFieldValue());
                            } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.ITEM.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.ITEM_IDENTIFIER_SEARCH)) {
                                searchResultDisplayRow.setItemIdentifier(searchResultField.getFieldValue());
                            } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.ITEM.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.CALL_NUM_PREFIX_DISPLAY)) {
                                itemCallNumberPrefix = searchResultField.getFieldValue();
                            } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.ITEM.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.CALL_NUM_DISPLAY)) {
                                itemCallNumber = searchResultField.getFieldValue();
                            } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.ITEM.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.LOACTION_LEVEL_DISPLAY)) {
                                itemLocation = searchResultField.getFieldValue();
                            } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.HOLDINGS.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.LOACTION_LEVEL_DISPLAY)) {
                                holdingsLocation = searchResultField.getFieldValue();
                            } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.HOLDINGS.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.CALL_NUM_PREFIX_DISPLAY)) {
                                holdingsCallNumberPrefix = searchResultField.getFieldValue();
                            } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.HOLDINGS.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.CALL_NUM_DISPLAY)) {
                                holdingsCallNumber = searchResultField.getFieldValue();
                            } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.BIB.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(Bib.TITLE)) {
                                searchResultDisplayRow.setTitle(searchResultField.getFieldValue());
                            } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.BIB.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(Bib.AUTHOR)) {
                                searchResultDisplayRow.setAuthor(searchResultField.getFieldValue());
                            }
                            else if (searchResultField.getDocType().equalsIgnoreCase(DocType.BIB.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(Bib.BIBIDENTIFIER)) {
                                searchResultDisplayRow.setBibIdentifier(searchResultField.getFieldValue());
                            }else if (searchResultField.getDocType().equalsIgnoreCase(DocType.HOLDINGS.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.OVERLAY_HOLDINGS_IDENTIFIER)) {
                                searchResultDisplayRow.setHoldingsIdentifier(searchResultField.getFieldValue());
                            }
                        }
                        if (StringUtils.isEmpty(itemLocation)) {
                            searchResultDisplayRow.setLocationName(holdingsLocation);
                        } else {
                            searchResultDisplayRow.setLocationName(itemLocation);
                        }
                        if (StringUtils.isEmpty(itemCallNumber)) {
                            callNumber = holdingsCallNumber;
                        } else {
                            callNumber = itemCallNumber;
                        }
                        if (StringUtils.isEmpty(itemCallNumberPrefix)) {
                            callNumberPrefix = holdingsCallNumberPrefix;
                        } else {
                            callNumberPrefix = itemCallNumberPrefix;
                        }
                        if (StringUtils.isNotEmpty(callNumberPrefix) && StringUtils.isNotEmpty(callNumber)) {
                            searchResultDisplayRow.setPrefixAndCallnumber(callNumberPrefix.concat("+").concat(callNumber));
                        } else if (StringUtils.isNotEmpty(callNumberPrefix)) {
                            searchResultDisplayRow.setPrefixAndCallnumber(callNumberPrefix);
                        } else if (StringUtils.isNotEmpty(callNumber)) {
                            searchResultDisplayRow.setPrefixAndCallnumber(callNumber);
                        }
                        if (LOG.isDebugEnabled()){
                            LOG.debug("searchResultDisplayRow ==========>>>>>>>>>  " + searchResultDisplayRow.getDonorCode());
                        }
                        searchResultDisplayRowList.add(searchResultDisplayRow);
                    }
                }
            }
        }
        return searchResultDisplayRowList;
    }

    private List<SearchResultDisplayRow> getSearchResultDisplayRowListFromEHoldings(String code, String value) throws Exception {
        SearchParams searchParams = new SearchParams();
        SearchField searchField = searchParams.buildSearchField(DocType.EHOLDINGS.getCode(), code, value);
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition(OLEConstants.OLEEResourceRecord.AND, searchField, OLEConstants.OLEEResourceRecord.AND));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.EHOLDINGS.getCode(), OLEConstants.DONOR_CODE_SEARCH));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.EHOLDINGS.getCode(), OLEConstants.CALL_NUM_PREFIX_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.EHOLDINGS.getCode(), OLEConstants.CALL_NUM_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.EHOLDINGS.getCode(), OLEConstants.LOACTION_LEVEL_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.EHOLDINGS.getCode(), OLEConstants.IMPRINT_DISPLAY));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.EHOLDINGS.getCode(), OLEConstants.OVERLAY_HOLDINGS_IDENTIFIER));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.BIB.getCode(), Bib.TITLE));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.BIB.getCode(), Bib.AUTHOR));
        searchParams.setPageSize(10000);
        if (LOG.isDebugEnabled()){
            LOG.debug("searchparams ==========>>>>>>>>> " + searchParams.serialize(searchParams));
        }
        SearchResponse searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
        if (LOG.isDebugEnabled()){
            LOG.debug("SearchResponse ==========>>>>>>>>> " + searchResponse.serialize(searchResponse));
        }
        List<SearchResultDisplayRow> searchResultDisplayRowList = new ArrayList<>();
        for (SearchResult searchResult : searchResponse.getSearchResults()) {
            String callNumber = null;
            String callNumberPrefix = null;
            for (SearchResultField resultField : searchResult.getSearchResultFields()) {
                if (resultField.getDocType().equalsIgnoreCase(DocType.EHOLDINGS.getCode()) && resultField.getFieldName().equalsIgnoreCase(OLEConstants.DONOR_CODE_SEARCH) && resultField.getFieldValue() != null) {
                    SearchResultDisplayRow searchResultDisplayRow = new SearchResultDisplayRow();
                    if ((StringUtils.isNotBlank(value) && resultField.getFieldValue().equals(value)) || StringUtils.isBlank(value)) {
                        searchResultDisplayRow.setDonorCode(resultField.getFieldValue());
                    }
                    if (StringUtils.isNotEmpty(searchResultDisplayRow.getDonorCode())) {
                        for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                            if (searchResultField.getDocType().equalsIgnoreCase(DocType.EHOLDINGS.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.LOACTION_LEVEL_DISPLAY)) {
                                searchResultDisplayRow.setLocationName(searchResultField.getFieldValue());
                            } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.EHOLDINGS.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.CALL_NUM_PREFIX_DISPLAY)) {
                                callNumberPrefix = searchResultField.getFieldValue();
                            } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.EHOLDINGS.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.CALL_NUM_DISPLAY)) {
                                callNumber = searchResultField.getFieldValue();
                            } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.EHOLDINGS.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.IMPRINT_DISPLAY)) {
                                searchResultDisplayRow.setImprint(searchResultField.getFieldValue());
                            } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.EHOLDINGS.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.OVERLAY_HOLDINGS_IDENTIFIER)) {
                                searchResultDisplayRow.setInstanceIdentifier(searchResultField.getFieldValue());
                                List<Integer> poLineItemIds=new ArrayList<>();
                                Map map=new HashMap();
                                map.put(OLEConstants.INSTANCE_ID,searchResultField.getFieldValue());
                                List<OleCopy> oleCopyList = (List<OleCopy>)boService.findMatching(OleCopy.class, map);
                                if (oleCopyList!=null && oleCopyList.size()>0){
                                    map.clear();
                                    map.put(OLEConstants.DONOR_CODE,searchResultDisplayRow.getDonorCode());
                                    List<OLELinkPurapDonor> oleLinkPurapDonorList = (List<OLELinkPurapDonor>)boService.findMatching(OLELinkPurapDonor.class, map);
                                    if (oleLinkPurapDonorList!=null && oleLinkPurapDonorList.size()>0){
                                        for (OLELinkPurapDonor oleLinkPurapDonor:oleLinkPurapDonorList){
                                            for (OleCopy oleCopy:oleCopyList){
                                                if (oleLinkPurapDonor.getPoItemId().equals(oleCopy.getPoItemId())){
                                                    poLineItemIds.add(oleLinkPurapDonor.getPoItemId());
                                                }
                                            }
                                        }
                                    }
                                }
                                searchResultDisplayRow.setPoLineItemIds(poLineItemIds);
                            } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.BIB.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(Bib.TITLE)) {
                                searchResultDisplayRow.setTitle(searchResultField.getFieldValue());
                            }else if (searchResultField.getDocType().equalsIgnoreCase(DocType.BIB.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(Bib.BIBIDENTIFIER)) {
                                searchResultDisplayRow.setBibIdentifier(searchResultField.getFieldValue());
                            } else if (searchResultField.getDocType().equalsIgnoreCase(DocType.BIB.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(Bib.AUTHOR)) {
                                searchResultDisplayRow.setAuthor(searchResultField.getFieldValue());
                            }else if (searchResultField.getDocType().equalsIgnoreCase(DocType.EHOLDINGS.getCode()) && searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.OVERLAY_HOLDINGS_IDENTIFIER)) {
                                searchResultDisplayRow.setHoldingsIdentifier(searchResultField.getFieldValue());
                            }
                        }
                        if (StringUtils.isNotEmpty(callNumberPrefix) && StringUtils.isNotEmpty(callNumber)) {
                            searchResultDisplayRow.setPrefixAndCallnumber(callNumberPrefix.concat("+").concat(callNumber));
                        } else if (StringUtils.isNotEmpty(callNumberPrefix)) {
                            searchResultDisplayRow.setPrefixAndCallnumber(callNumberPrefix);
                        } else if (StringUtils.isNotEmpty(callNumber)) {
                            searchResultDisplayRow.setPrefixAndCallnumber(callNumber);
                        }
                        if (LOG.isDebugEnabled()){
                            LOG.debug("searchResultDisplayRow ==========>>>>>>>>>  " + searchResultDisplayRow.getDonorCode());
                        }
                        searchResultDisplayRowList.add(searchResultDisplayRow);
                    }
                }
            }
        }
        return searchResultDisplayRowList;
    }

}
