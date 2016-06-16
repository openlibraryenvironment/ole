package org.kuali.ole.deliver.controller;

import org.apache.log4j.Logger;
import org.apache.ojb.broker.query.Criteria;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.SearchParmsBuilder;
import org.kuali.ole.deliver.bo.OLEReturnedLostItemResult;
import org.kuali.ole.deliver.bo.OleCirculationHistory;
import org.kuali.ole.deliver.form.OLEDeliverNoticeSearchForm;
import org.kuali.ole.deliver.form.OLEReturnedLostItemForm;
import org.kuali.ole.deliver.service.OleLoanDocumentDaoOjb;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.ItemOleml;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.search.*;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
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
 * Created by gopalp on 4/4/16.
 */
@Controller
@RequestMapping(value = "/returnedLostItemController")
public class OLEReturnedLostItemController extends OLEUifControllerBase {

    private OleLoanDocumentDaoOjb loanDaoOjb;
    private DocstoreClientLocator docstoreClientLocator;
    protected DateTimeService dateTimeService;


    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        return new OLEReturnedLostItemForm();
    }


    @RequestMapping(params = "methodToCall=search")
    public ModelAndView search(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) throws Exception{
        OLEReturnedLostItemForm oleReturnedLostItemForm = (OLEReturnedLostItemForm) form;
         List<OLEReturnedLostItemResult> returnedLostItemResults = new ArrayList<>();
        oleReturnedLostItemForm.reset();
        if (checkInputFields(oleReturnedLostItemForm)) {
            return getUIFModelAndView(oleReturnedLostItemForm);
        }
        Criteria criteria = new Criteria();
        criteria.addGreaterOrEqualThan("createDate", oleReturnedLostItemForm.getDateSentFrom());
        String dbVendor = getProperty("db.vendor");
        if (dbVendor.equals("mysql")) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(oleReturnedLostItemForm.getDateSentTo());
            cal.add(Calendar.DATE, 1);
            criteria.addLessOrEqualThan("createDate", cal.getTime());
        } else if (dbVendor.equals("oracle")){
            String fromdate = oleReturnedLostItemForm.getDateSentTo().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(fromdate));
            c.add(Calendar.DATE, 1);
            fromdate = sdf.format(c.getTime());
            java.sql.Date sqlDate = java.sql.Date.valueOf(fromdate);
            criteria.addLessOrEqualThan("createDate", sqlDate);
        }


        criteria.addNotNull("repaymentFeePatronBillId");
        List<OleCirculationHistory> oleCirculationHistoryList = getLoanDaoOjb().getReturnedItem(criteria);
        if (oleCirculationHistoryList.size() > 0) {
            List<String> itemBarcodeList = getItems(oleCirculationHistoryList);
            try {
                returnedLostItemResults = getItem(itemBarcodeList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        oleReturnedLostItemForm.setOleReturnedLostItemResults(returnedLostItemResults);
        if(returnedLostItemResults.size() == 0) {
            GlobalVariables.getMessageMap().putInfo(OLEConstants.NO_RESULTS_FOUND, OLEConstants.NO_RESULTS_FOUND);
        }
        return getUIFModelAndView(oleReturnedLostItemForm);

    }

    public List<String> getItems(List<OleCirculationHistory> oleCirculationHistoryList) {
        List<String> itemBarcodeList = new ArrayList<>();
        for(OleCirculationHistory oleCirculationHistory : oleCirculationHistoryList) {
            itemBarcodeList.add(oleCirculationHistory.getItemId());
        }
        return itemBarcodeList;
    }


    public List<OLEReturnedLostItemResult> getItem(List<String> itemBarcodeList)throws Exception {
        List<OLEReturnedLostItemResult> returnedLostItemResults = new ArrayList<>();
        SearchResponse searchResponse = new SearchResponse();
        SearchParams searchParams = new SearchParams();
        List<SearchCondition> searchConditions = new ArrayList<>();
        searchConditions.add(searchParams.buildSearchCondition("", searchParams.buildSearchField("item", "ItemStatus_search", "LOST"), "NOT"));
        for (String itemBarcode : itemBarcodeList) {
            searchConditions.add(searchParams.buildSearchCondition("phrase", searchParams.buildSearchField("item", "ItemBarcode_search", itemBarcode), "OR"));
        }
        SearchParmsBuilder.buildSearchParams(searchParams);
        searchParams.getSearchConditions().addAll(searchConditions);
        searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
        if (searchResponse != null) {
            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                OLEReturnedLostItemResult returnedLostItemResult = new OLEReturnedLostItemResult();
                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                        if (searchResultField.getFieldValue() != null) {
                        if (searchResultField.getFieldName().equalsIgnoreCase("itemBarcode_display")) {
                            returnedLostItemResult.setItemBarcode(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("title_display")) {
                            returnedLostItemResult.setTitle(searchResultField.getFieldValue());
                        }
                        if (searchResultField.getFieldName().equalsIgnoreCase("itemStatus_display")) {
                            returnedLostItemResult.setItemStatus(searchResultField.getFieldValue());
                        }
                    }
                }
                returnedLostItemResults.add(returnedLostItemResult);
            }
        }
        return returnedLostItemResults;
    }

    public boolean checkInputFields(OLEReturnedLostItemForm oleReturnedLostItemForm) {
        if (oleReturnedLostItemForm.getDateSentFrom() == null) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ITM_BLANK_SEARCH_ERROR_MSG);
            return true;

        } else if (oleReturnedLostItemForm.getDateSentTo() == null) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ITM_BLANK_SEARCH_ERROR_MSG);
            return true;

        }
        return false;
    }



    @RequestMapping(params = "methodToCall=clearSearch")
    public ModelAndView clearSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        OLEReturnedLostItemForm oleReturnedLostItemForm = (OLEReturnedLostItemForm) form;

        oleReturnedLostItemForm.setDateSentFrom(null);
        oleReturnedLostItemForm.setDateSentTo(null);
        oleReturnedLostItemForm.setOleReturnedLostItemResults(Collections.EMPTY_LIST);
        return getUIFModelAndView(oleReturnedLostItemForm);
    }

    @RequestMapping(params = "methodToCall=clearResults")
    public ModelAndView clearResults(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        OLEReturnedLostItemForm oleReturnedLostItemForm = (OLEReturnedLostItemForm) form;
        oleReturnedLostItemForm.setOleReturnedLostItemResults(Collections.EMPTY_LIST);
        return getUIFModelAndView(oleReturnedLostItemForm);
    }

    public OleLoanDocumentDaoOjb getLoanDaoOjb() {
        if (null == loanDaoOjb) {
            loanDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getBean("oleLoanDao");
        }
        return loanDaoOjb;
    }

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
    }

    public DateTimeService getDateTimeService() {
        if(null == dateTimeService) {
            dateTimeService = SpringContext.getBean(DateTimeService.class);
        }
        return dateTimeService;
    }

    protected String getProperty(String property) {
        return ConfigContext.getCurrentContextConfig().getProperty(property);
    }

}
