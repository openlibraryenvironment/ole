package org.kuali.ole.batch.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.batch.form.OLEClaimedReturnedItemsForm;
import org.kuali.ole.deliver.bo.OLEClaimedReturnedItemResult;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleCirculationDeskLocation;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.controller.checkin.CheckInAPIController;
import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.deliver.drools.DroolsExchange;
import org.kuali.ole.deliver.form.OLEForm;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.service.LostNoticesExecutor;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.search.*;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
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
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by chenchulakshmig on 3/16/16.
 */
@Controller
@RequestMapping(value = "/claimedReturnedItemsController")
public class OLEClaimedReturnedItemsController extends UifControllerBase {

    private static final Logger LOG = Logger.getLogger(OLEClaimedReturnedItemsController.class);

    private DocstoreClientLocator docstoreClientLocator;
    private BusinessObjectService businessObjectService;
    private ParameterValueResolver parameterValueResolver;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public ParameterValueResolver getParameterValueResolver() {
        if (null == parameterValueResolver) {
            parameterValueResolver = ParameterValueResolver.getInstance();
        }
        return parameterValueResolver;
    }

    public void setParameterValueResolver(ParameterValueResolver parameterValueResolver) {
        this.parameterValueResolver = parameterValueResolver;
    }

    @Override
    protected OLEClaimedReturnedItemsForm createInitialForm(HttpServletRequest request) {
        return new OLEClaimedReturnedItemsForm();
    }

    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        OLEClaimedReturnedItemsForm oleClaimedReturnedItemsForm = (OLEClaimedReturnedItemsForm) form;
        return getUIFModelAndView(oleClaimedReturnedItemsForm, "OLEClaimedReturnedItemsViewPage");
    }

    @RequestMapping(params = "methodToCall=search")
    public ModelAndView search(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        OLEClaimedReturnedItemsForm oleClaimedReturnedItemsForm = (OLEClaimedReturnedItemsForm) form;
        if (StringUtils.isNotBlank(oleClaimedReturnedItemsForm.getCirculationDeskId())) {
            OleCirculationDesk oleCirculationDesk = getBusinessObjectService().findBySinglePrimaryKey(OleCirculationDesk.class, oleClaimedReturnedItemsForm.getCirculationDeskId());
            if (oleCirculationDesk != null) {
                List<OleCirculationDeskLocation> oleCirculationDeskLocations = oleCirculationDesk.getOleCirculationDeskLocations();
                if (CollectionUtils.isNotEmpty(oleCirculationDeskLocations)) {
                    List<String> locations = new ArrayList<>();
                    for (OleCirculationDeskLocation oleCirculationDeskLocation : oleCirculationDeskLocations) {
                        if (StringUtils.isEmpty(oleCirculationDeskLocation.getCirculationPickUpDeskLocation())) {
                            OleLocation oleLocation = oleCirculationDeskLocation.getLocation();
                            if (oleLocation != null) {
                                locations.add(oleLocation.getFullLocationPath());
                            }
                        }
                    }
                    if (CollectionUtils.isNotEmpty(locations)) {
                        try {
                            SearchResponse searchResponse = getDocstoreClientLocator().getDocstoreClient().search(buildSearchParamsForClaimsReturned());
                            List<OLEClaimedReturnedItemResult> claimedReturnedItemResults = buildClaimedReturnedItemResults(searchResponse, locations);
                            if (CollectionUtils.isEmpty(claimedReturnedItemResults)) {
                                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.NO_RECORD_FOUND);
                            }
                            oleClaimedReturnedItemsForm.setClaimedReturnedItemResults(claimedReturnedItemResults);
                        } catch (Exception e) {
                            LOG.error("Exception " + e);
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_CIRC_DESK_REQUIRED);
        }
        return getUIFModelAndView(oleClaimedReturnedItemsForm, "OLEClaimedReturnedItemsViewPage");
    }

    @RequestMapping(params = "methodToCall=searchedNotFound")
    public ModelAndView searchedNotFound(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                         HttpServletRequest request, HttpServletResponse response) {
        OLEClaimedReturnedItemsForm oleClaimedReturnedItemsForm = (OLEClaimedReturnedItemsForm) form;
        oleClaimedReturnedItemsForm.setBillForItem(false);
        List<OLEClaimedReturnedItemResult> selectedClaimedReturnedItemResults = getSelectedClaimedReturnedItemResults(oleClaimedReturnedItemsForm);
        if (CollectionUtils.isEmpty(selectedClaimedReturnedItemResults)) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_SELECT_ANY_ITEM);
        } else {
            String claimsReturnedSearchCount = ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID_OLE,
                    OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CR_ITEM_SEARCH_COUNT_BEFORE_ITEM_BILLED);
            int claimsSearchCountBeforeItemBilled = 0;
            if (StringUtils.isNotBlank(claimsReturnedSearchCount) && StringUtils.isNumeric(claimsReturnedSearchCount)) {
                claimsSearchCountBeforeItemBilled = Integer.parseInt(claimsReturnedSearchCount);
            }
            for (OLEClaimedReturnedItemResult oleClaimedReturnedItemResult : selectedClaimedReturnedItemResults) {
                OleLoanDocument oleLoanDocument = oleClaimedReturnedItemResult.getOleLoanDocument();
                int claimsSearchCount = oleLoanDocument.getClaimsSearchCount();
                if (claimsSearchCount < claimsSearchCountBeforeItemBilled) {
                    boolean isNotifyClaimsReturnedToPatron = getParameterValueResolver().getParameterAsBoolean(OLEConstants.APPL_ID_OLE,
                            OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.NOTIFY_CLAIMS_RETURNED_TO_PATRON);
                    oleLoanDocument.setLastClaimsReturnedSearchedDate(new Timestamp(new Date().getTime()));
                    oleLoanDocument.setClaimsSearchCount(claimsSearchCount + 1);
                    if (isNotifyClaimsReturnedToPatron) {
                        new CircUtilController().sendClaimReturnedNotice(oleLoanDocument, OLEConstants.CLAIMS_RETURNED_NOT_FOUND_NOTICE, OLEParameterConstants.CLAIMS_RETURNED_NOT_FOUND_NOTICE_TITLE, OLEConstants.OleDeliverRequest.CLAIMS_RETURNED_NOT_FOUND_NOTICE_CONTENT);
                        oleLoanDocument.setNoOfClaimsReturnedNoticesSent(oleLoanDocument.getNoOfClaimsReturnedNoticesSent() + 1);
                    }
                    getBusinessObjectService().save(oleLoanDocument);
                    oleClaimedReturnedItemResult.setSelect(false);
                    GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_MESSAGES, OLEConstants.RECORD_UPDATED_SUCCESSFULLY);
                } else {
                    showDialog("ClaimsReturnPopupDialog", oleClaimedReturnedItemsForm, request, response);
                }
            }
        }
        return getUIFModelAndView(oleClaimedReturnedItemsForm, "OLEClaimedReturnedItemsViewPage");
    }

    @RequestMapping(params = "methodToCall=processBillForItem")
    public ModelAndView processBillForItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) {
        OLEClaimedReturnedItemsForm oleClaimedReturnedItemsForm = (OLEClaimedReturnedItemsForm) form;
        String isBillForItem = request.getParameter("isBillForItem");
        if (StringUtils.isNotBlank(isBillForItem)) {
            oleClaimedReturnedItemsForm.setBillForItem(Boolean.valueOf(isBillForItem));
        }
        List<OLEClaimedReturnedItemResult> selectedClaimedReturnedItemResults = getSelectedClaimedReturnedItemResults(oleClaimedReturnedItemsForm);
        if (CollectionUtils.isEmpty(selectedClaimedReturnedItemResults)) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_SELECT_ANY_ITEM);
        } else {
            boolean isNotifyClaimsReturnedToPatron = getParameterValueResolver().getParameterAsBoolean(OLEConstants.APPL_ID_OLE,
                    OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.NOTIFY_CLAIMS_RETURNED_TO_PATRON);
            for (OLEClaimedReturnedItemResult oleClaimedReturnedItemResult : selectedClaimedReturnedItemResults) {
                OleLoanDocument oleLoanDocument = oleClaimedReturnedItemResult.getOleLoanDocument();
                if (oleClaimedReturnedItemsForm.isBillForItem()) {
                    createOrUpdateBillForItem(isNotifyClaimsReturnedToPatron, oleLoanDocument);
                } else {
                    forgiveClaimProcess(isNotifyClaimsReturnedToPatron, oleLoanDocument);
                }
                oleClaimedReturnedItemsForm.getClaimedReturnedItemResults().remove(oleClaimedReturnedItemResult);
                GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_MESSAGES, OLEConstants.RECORD_UPDATED_SUCCESSFULLY);
            }
        }
        return getUIFModelAndView(oleClaimedReturnedItemsForm, "OLEClaimedReturnedItemsViewPage");
    }

    private void createOrUpdateBillForItem(boolean isNotifyClaimsReturnedToPatron, OleLoanDocument oleLoanDocument) {
        CircUtilController circUtilController = new CircUtilController();
        oleLoanDocument.setLastClaimsReturnedSearchedDate(null);
        oleLoanDocument.setClaimsSearchCount(0);
        oleLoanDocument.setNoOfClaimsReturnedNoticesSent(0);
        Map parameterMap = new HashMap();
        parameterMap.put("deleteClaimsReturn", null);
        circUtilController.deleteItemInfoInSolr(parameterMap, oleLoanDocument.getItemUuid());
        boolean billExists = circUtilController.updatePaymentStatusToOutstanding(oleLoanDocument.getItemId(), oleLoanDocument.getOlePatron().getOlePatronId());
        if (!billExists) {
            oleLoanDocument.setIsManualBill(true);
            Map lostMap = new HashMap();
            lostMap.put(OLEConstants.NOTICE_CONTENT_CONFIG_NAME, OLEConstants.LOST_NOTICE);
            lostMap.put(OLEConstants.LOAN_DOCUMENTS, Arrays.asList(oleLoanDocument));
            Runnable deliverLostNoticesExecutor = new LostNoticesExecutor(lostMap);
            int threadPoolSize = OLEConstants.DEFAULT_NOTICE_THREAD_POOL_SIZE;
            ExecutorService lostNoticesExecutorService = Executors.newFixedThreadPool(threadPoolSize);
            lostNoticesExecutorService.execute(deliverLostNoticesExecutor);
        } else {
            getBusinessObjectService().save(oleLoanDocument);
        }
        if (isNotifyClaimsReturnedToPatron) {
            circUtilController.sendClaimReturnedNotice(oleLoanDocument, OLEConstants.CLAIMS_RETURNED_NOT_FOUND_FINES_OWED_NOTICE_TITLE, OLEParameterConstants.CLAIMS_RETURNED_NOT_FOUND_FINES_OWED_NOTICE_TITLE, OLEConstants.OleDeliverRequest.CLAIMS_RETURNED_NOT_FOUND_FINES_OWED_NOTICE_CONTENT);
        }
    }

    private void forgiveClaimProcess(boolean isNotifyClaimsReturnedToPatron, OleLoanDocument oleLoanDocument) {
        CircUtilController circUtilController = new CircUtilController();
        String operatorId = GlobalVariables.getUserSession().getPrincipalId();
        if (StringUtils.isNotBlank(operatorId)) {
            OleCirculationDesk oleCirculationDesk = new CircDeskLocationResolver().getCircDeskForOpertorId(operatorId);
            if (oleCirculationDesk != null) {
                DroolsExchange droolsExchange = new DroolsExchange();
                droolsExchange.addToContext("itemBarcode", oleLoanDocument.getItemId());
                droolsExchange.addToContext("selectedCirculationDesk", oleCirculationDesk.getCirculationDeskId());
                droolsExchange.addToContext("operatorId", operatorId);
                OLEForm oleAPIForm = new OLEForm();
                oleAPIForm.setDroolsExchange(droolsExchange);
                new CheckInAPIController().claimsReturnedCheckInProcess(oleLoanDocument, oleAPIForm, "MISSING");

                circUtilController.updatePaymentStatusToForgive(oleLoanDocument.getItemId(), oleLoanDocument.getOlePatron().getOlePatronId(), new Timestamp(new Date().getTime()), operatorId, "Claimed item was forgiven by staff on ");
                if (isNotifyClaimsReturnedToPatron) {
                    circUtilController.sendClaimReturnedNotice(oleLoanDocument, OLEConstants.CLAIMS_RETURNED_NOT_FOUND_NO_FEES_NOTICE, OLEParameterConstants.CLAIMS_RETURNED_NOT_FOUND_NO_FEES_NOTICE_TITLE, OLEConstants.OleDeliverRequest.CLAIMS_RETURNED_NOT_FOUND_NO_FEES_NOTICE_CONTENT);
                }
            }
        }
    }

    private List<OLEClaimedReturnedItemResult> getSelectedClaimedReturnedItemResults(OLEClaimedReturnedItemsForm oleClaimedReturnedItemsForm) {
        List<OLEClaimedReturnedItemResult> oleClaimedReturnedItemResults = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(oleClaimedReturnedItemsForm.getClaimedReturnedItemResults())) {
            for (OLEClaimedReturnedItemResult oleClaimedReturnedItemResult : oleClaimedReturnedItemsForm.getClaimedReturnedItemResults()) {
                if (oleClaimedReturnedItemResult.isSelect()) {
                    oleClaimedReturnedItemResults.add(oleClaimedReturnedItemResult);
                }
            }
        }
        return oleClaimedReturnedItemResults;
    }

    private SearchParams buildSearchParamsForClaimsReturned() {
        SearchParams searchParams = new SearchParams();
        SearchField searchField = searchParams.buildSearchField(DocType.ITEM.getCode(), DocstoreConstants.CLAIMS_RETURNED_FLAG_SEARCH, Boolean.TRUE.toString());
        SearchCondition searchCondition = searchParams.buildSearchCondition("AND", searchField, "AND");
        searchParams.getSearchConditions().add(searchCondition);
        searchParams.getSearchResultFields().addAll(buildSearchResultFieldsForClaimsReturned(searchParams));
        searchParams.setPageSize(Integer.parseInt(OLEConstants.MAX_PAGE_SIZE_FOR_LOAN));
        return searchParams;
    }

    private List<SearchResultField> buildSearchResultFieldsForClaimsReturned(SearchParams searchParams) {
        List<SearchResultField> searchResultFields = new ArrayList<>();
        searchResultFields.add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), Item.ITEMIDENTIFIER));
        searchResultFields.add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), Item.CALL_NUMBER));
        searchResultFields.add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), Item.COPY_NUMBER));
        searchResultFields.add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), Item.ENUMERATION));
        searchResultFields.add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), Item.CHRONOLOGY));
        searchResultFields.add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), DocstoreConstants.LOCATION_LEVEL_DISPLAY));
        searchResultFields.add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), DocstoreConstants.CLMS_RET_FLAG_CRE_DATE));
        searchResultFields.add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), DocstoreConstants.CLMS_RET_NOTE));
        searchResultFields.add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), Bib.TITLE));
        searchResultFields.add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), Item.HOLDINGS_CALL_NUMBER));
        searchResultFields.add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), Item.HOLDINGS_COPY_NUMBER));
        searchResultFields.add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), DocstoreConstants.HOLDINGS_LOCATION_DISPLAY));
        return searchResultFields;
    }

    private List<OLEClaimedReturnedItemResult> buildClaimedReturnedItemResults(SearchResponse searchResponse, List<String> locations) {
        List<OLEClaimedReturnedItemResult> claimedReturnedItemResults = new ArrayList<>();
        Set<String> itemIds = new HashSet<>();
        if (searchResponse != null) {
            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                String itemCallNumber = null;
                String holdingsCallNumber = null;
                String itemCopyNumber = null;
                String holdingsCopyNumber = null;
                String itemLocation = null;
                String holdingsLocation = null;
                OLEClaimedReturnedItemResult claimedReturnedItemResult = new OLEClaimedReturnedItemResult();
                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                    if (StringUtils.isNotBlank(searchResultField.getFieldValue())) {
                        if (searchResultField.getFieldName().equalsIgnoreCase(Item.ITEMIDENTIFIER)) {
                            String itemId = searchResultField.getFieldValue();
                            itemIds.add(itemId);
                            claimedReturnedItemResult.setItemId(itemId);
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(Item.CALL_NUMBER)) {
                            itemCallNumber = searchResultField.getFieldValue();
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(Item.HOLDINGS_CALL_NUMBER)) {
                            holdingsCallNumber = searchResultField.getFieldValue();
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(Item.COPY_NUMBER)) {
                            itemCopyNumber = searchResultField.getFieldValue();
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(Item.HOLDINGS_COPY_NUMBER)) {
                            holdingsCopyNumber = searchResultField.getFieldValue();
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(DocstoreConstants.LOCATION_LEVEL_DISPLAY)) {
                            itemLocation = searchResultField.getFieldValue();
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(DocstoreConstants.HOLDINGS_LOCATION_DISPLAY)) {
                            holdingsLocation = searchResultField.getFieldValue();
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(Item.ENUMERATION)) {
                            claimedReturnedItemResult.setEnumeration(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(Item.CHRONOLOGY)) {
                            claimedReturnedItemResult.setChronology(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(DocstoreConstants.CLMS_RET_FLAG_CRE_DATE)) {
                            claimedReturnedItemResult.setDateOfClaim(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(DocstoreConstants.CLMS_RET_NOTE)) {
                            claimedReturnedItemResult.setClaimReturnNote(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(Bib.TITLE)) {
                            claimedReturnedItemResult.setTitle(searchResultField.getFieldValue());
                        }
                    }
                }
                if (StringUtils.isNotBlank(itemCallNumber)) {
                    claimedReturnedItemResult.setCallNumber(itemCallNumber);
                } else {
                    claimedReturnedItemResult.setCallNumber(holdingsCallNumber);
                }
                if (StringUtils.isNotBlank(itemCopyNumber)) {
                    claimedReturnedItemResult.setCopyNumber(itemCopyNumber);
                } else {
                    claimedReturnedItemResult.setCopyNumber(holdingsCopyNumber);
                }
                if (StringUtils.isNotBlank(itemLocation)) {
                    claimedReturnedItemResult.setLocation(itemLocation);
                } else {
                    claimedReturnedItemResult.setLocation(holdingsLocation);
                }
                if (StringUtils.isNotBlank(claimedReturnedItemResult.getLocation()) && locations.contains(claimedReturnedItemResult.getLocation())) {
                    claimedReturnedItemResults.add(claimedReturnedItemResult);
                }
            }
        }
        List<OLEClaimedReturnedItemResult> claimedReturnedItemResultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(itemIds)) {
            Map loanMap = new HashMap();
            loanMap.put(OLEConstants.ITEM_UUID, itemIds);
            List<OleLoanDocument> oleLoanDocuments = (List<OleLoanDocument>) getBusinessObjectService().findMatching(OleLoanDocument.class, loanMap);
            if (CollectionUtils.isNotEmpty(oleLoanDocuments) && CollectionUtils.isNotEmpty(claimedReturnedItemResults)) {
                for (OleLoanDocument oleLoanDocument : oleLoanDocuments) {
                    for (OLEClaimedReturnedItemResult claimedReturnedItemResult : claimedReturnedItemResults) {
                        if (oleLoanDocument.getItemUuid().equalsIgnoreCase(claimedReturnedItemResult.getItemId())) {
                            oleLoanDocument.setItemFullLocation(claimedReturnedItemResult.getLocation());
                            claimedReturnedItemResult.setOleLoanDocument(oleLoanDocument);
                            claimedReturnedItemResultList.add(claimedReturnedItemResult);
                            break;
                        }
                    }
                }
            }
        }
        return claimedReturnedItemResultList;
    }
}
