package org.kuali.ole.deliver.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.util.DateUtil;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNoticeSearchResult;
import org.kuali.ole.deliver.form.OLEDeliverNoticeSearchForm;
import org.kuali.ole.deliver.service.OleLoanDocumentDaoOjb;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.utility.DateTimeUtil;
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
import java.text.DateFormat;
import java.util.*;

/**
 * Created by chenchulakshmig on 10/16/15.
 */
@Controller
@RequestMapping(value = "/deliverNoticeSearchController")
public class OLEDeliverNoticeSearchController extends OLEUifControllerBase {

    private BusinessObjectService businessObjectService;
    private OleLoanDocumentDaoOjb loanDaoOjb;

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        return new OLEDeliverNoticeSearchForm();
    }

    @RequestMapping(params = "methodToCall=search")
    public ModelAndView search(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        OLEDeliverNoticeSearchForm oleDeliverNoticeSearchForm = (OLEDeliverNoticeSearchForm) form;
        oleDeliverNoticeSearchForm.reset();
        Map<String, Object> filterFields = buildFilterFields(oleDeliverNoticeSearchForm);

        if (filterFields.isEmpty()) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ITM_BLANK_SEARCH_ERROR_MSG);
            return getUIFModelAndView(oleDeliverNoticeSearchForm);
        }
        String solrQuery = buildSolrQuery(filterFields);
        List results = new SolrRequestReponseHandler().retrieveResultsForNotice(solrQuery);
        oleDeliverNoticeSearchForm.setTotalRecCount(results.size());
        List<OLEDeliverNoticeSearchResult> oleDeliverNoticeSearchResults = buildSearchResults(results);
        if (CollectionUtils.isEmpty(oleDeliverNoticeSearchResults)){
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.NO_RECORD_FOUND);
        }
        oleDeliverNoticeSearchForm.setOleDeliverNoticeSearchResult(oleDeliverNoticeSearchResults);

        return getUIFModelAndView(oleDeliverNoticeSearchForm);
    }


    private List<OLEDeliverNoticeSearchResult> buildSearchResults(List notices) {
        List oleDeliverNoticeSearchResults = new ArrayList<>();
        for (Iterator<Object> iterator = notices.iterator(); iterator.hasNext(); ) {
            Map noticeMap = (Map) iterator.next();
            String noticeContent = (String) noticeMap.get("noticeContent");
            if (null != noticeContent) {
                OLEDeliverNoticeSearchResult oleDeliverNoticeSearchResult = new OLEDeliverNoticeSearchResult();
                String patronBarcode = (String) noticeMap.get("patronBarcode");
                Date sentDate = (Date) noticeMap.get("dateSent");
                String noticeType = (String) noticeMap.get("noticeType");
                oleDeliverNoticeSearchResult.setPatronId(patronBarcode);
                oleDeliverNoticeSearchResult.setNoticeContent(noticeContent);
                oleDeliverNoticeSearchResult.setDateSentTo(sentDate);
                oleDeliverNoticeSearchResult.setNoticeType(noticeType);
                oleDeliverNoticeSearchResults.add(oleDeliverNoticeSearchResult);
            }
        }
        return oleDeliverNoticeSearchResults;
    }

    private String buildSolrQuery(Map<String, Object> filterFields) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Iterator<String> iterator = filterFields.keySet().iterator(); iterator.hasNext(); ) {
            String field = iterator.next();
            Object value = filterFields.get(field);
            stringBuilder.append(field).append(":").append(value);
            if (iterator.hasNext()) {
                stringBuilder.append(" AND ");
            }
        }
        return stringBuilder.toString();
    }

    private Map<String, Object> buildFilterFields(OLEDeliverNoticeSearchForm oleDeliverNoticeSearchForm) {

        Map<String, Object> filterFields = new HashMap<>();

        if (StringUtils.isNotBlank(oleDeliverNoticeSearchForm.getPatronBarcode())) {
            filterFields.put("patronBarcode", oleDeliverNoticeSearchForm.getPatronBarcode());
        }

        if (StringUtils.isNotBlank(oleDeliverNoticeSearchForm.getItemBarcode())) {
            filterFields.put("itemBarcode", oleDeliverNoticeSearchForm.getItemBarcode());
        }

        if (StringUtils.isNotBlank(oleDeliverNoticeSearchForm.getNoticeType())) {
            String noticeType = "\"" + oleDeliverNoticeSearchForm.getNoticeType() + "\"";
            filterFields.put("noticeType", noticeType);
        }

        if (oleDeliverNoticeSearchForm.getDateSentTo() != null && oleDeliverNoticeSearchForm.getDateSentFrom() != null) {
            Date dateSentFrom = oleDeliverNoticeSearchForm.getDateSentFrom();
            Date dateSentTo = oleDeliverNoticeSearchForm.getDateSentTo();
            Date dateWithStartTimeOfTheDay = DateTimeUtil.formateDateWithStartTimeOfTheDay(dateSentFrom);
            Date dateWithEndTimeOfTheDay = DateTimeUtil.formateDateWithEndTimeOfTheDay(dateSentTo);
            DateFormat solrThreadLocalDateFormat = DateUtil.getThreadLocalDateFormat();
            String rangeValues = "[" + solrThreadLocalDateFormat.format(dateWithStartTimeOfTheDay) + " TO " + solrThreadLocalDateFormat.format(dateWithEndTimeOfTheDay.getTime()) + "]";
            filterFields.put("dateSent", rangeValues);
        } else if(oleDeliverNoticeSearchForm.getDateSentTo() != null){
            Date dateSent = oleDeliverNoticeSearchForm.getDateSentTo();
            Date dateWithStartTimeOfTheDay = DateTimeUtil.formateDateWithStartTimeOfTheDay(dateSent);
            Date dateWithEndTimeOfTheDay = DateTimeUtil.formateDateWithEndTimeOfTheDay(dateSent);
            DateFormat solrThreadLocalDateFormat = DateUtil.getThreadLocalDateFormat();
            String rangeValues = "[" + solrThreadLocalDateFormat.format(dateWithStartTimeOfTheDay) + " TO " + solrThreadLocalDateFormat.format(dateWithEndTimeOfTheDay.getTime()) + "]";
            filterFields.put("dateSent", rangeValues);
        }else if(oleDeliverNoticeSearchForm.getDateSentFrom() != null){
            Date dateSent = oleDeliverNoticeSearchForm.getDateSentFrom();
            Date dateWithStartTimeOfTheDay = DateTimeUtil.formateDateWithStartTimeOfTheDay(dateSent);
            Date dateWithEndTimeOfTheDay = DateTimeUtil.formateDateWithEndTimeOfTheDay(new Date());
            DateFormat solrThreadLocalDateFormat = DateUtil.getThreadLocalDateFormat();
            String rangeValues = "[" + solrThreadLocalDateFormat.format(dateWithStartTimeOfTheDay) + " TO " + solrThreadLocalDateFormat.format(dateWithEndTimeOfTheDay.getTime()) + "]";
            filterFields.put("dateSent", rangeValues);
        }

        return filterFields;
    }


    @RequestMapping(params = "methodToCall=clearSearch")
    public ModelAndView clearSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        OLEDeliverNoticeSearchForm oleDeliverNoticeSearchForm = (OLEDeliverNoticeSearchForm) form;

        oleDeliverNoticeSearchForm.setPatronBarcode(null);
        oleDeliverNoticeSearchForm.setItemBarcode(null);
        oleDeliverNoticeSearchForm.setDateSentFrom(null);
        oleDeliverNoticeSearchForm.setDateSentTo(null);
        oleDeliverNoticeSearchForm.setNoticeType(null);
        oleDeliverNoticeSearchForm.setOleDeliverNoticeSearchResult(Collections.EMPTY_LIST);
        return getUIFModelAndView(oleDeliverNoticeSearchForm);
    }


    @RequestMapping(params = "methodToCall=showDialogWithNoticeContent")
    public ModelAndView showDialogWithNoticeContent(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                    HttpServletRequest request, HttpServletResponse response) {
        OLEDeliverNoticeSearchForm oleDeliverNoticeSearchForm = (OLEDeliverNoticeSearchForm) form;
        String contentIndex = request.getParameter("contentIndex");
        OLEDeliverNoticeSearchResult oleDeliverNoticeSearchResult = oleDeliverNoticeSearchForm.getOleDeliverNoticeSearchResult().get(Integer.valueOf(contentIndex));
        showHtmlContentToDialog(oleDeliverNoticeSearchResult.getNoticeContent(), form, "", "Notice Mail Content");
        return getUIFModelAndView(oleDeliverNoticeSearchForm);
    }

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public OleLoanDocumentDaoOjb getLoanDaoOjb() {
        if (null == loanDaoOjb) {
            loanDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getBean("oleLoanDao");
        }
        return loanDaoOjb;
    }

    @RequestMapping(params = "methodToCall=clearResults")
    public ModelAndView clearResults(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                    HttpServletRequest request, HttpServletResponse response) {
        OLEDeliverNoticeSearchForm oleDeliverNoticeSearchForm = (OLEDeliverNoticeSearchForm) form;
        oleDeliverNoticeSearchForm.setOleDeliverNoticeSearchResult(Collections.EMPTY_LIST);
        return getUIFModelAndView(oleDeliverNoticeSearchForm);
    }
}