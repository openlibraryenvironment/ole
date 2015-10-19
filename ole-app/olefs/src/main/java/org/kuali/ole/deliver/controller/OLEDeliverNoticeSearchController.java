package org.kuali.ole.deliver.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ojb.broker.query.Criteria;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.deliver.bo.OLEDeliverNoticeHistory;
import org.kuali.ole.deliver.bo.OLEDeliverNoticeSearchResult;
import org.kuali.ole.deliver.form.OLEDeliverNoticeSearchForm;
import org.kuali.ole.deliver.service.OleLoanDocumentDaoOjb;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by chenchulakshmig on 10/16/15.
 */
@Controller
@RequestMapping(value = "/deliverNoticeSearchController")
public class OLEDeliverNoticeSearchController extends UifControllerBase {

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
        Map<String, Object> filterFields = buildFilterFields(oleDeliverNoticeSearchForm);

        String solrQuery = buildSolrQuery(filterFields);
        List results = new SolrRequestReponseHandler().retriveResults(solrQuery);

        for (Iterator iterator = results.iterator(); iterator.hasNext(); ) {
            Map resultsMap = (Map) iterator.next();
            if (resultsMap.containsKey("patronId")) {
                String patronId = (String) resultsMap.get("patronId");
                if (StringUtils.isNotBlank(patronId)) {
                    Criteria criteria = new Criteria();
                    if (StringUtils.isNotBlank(oleDeliverNoticeSearchForm.getPatronBarcode())) {
                        criteria.addEqualTo("patronId", patronId);
                    }
                    if (StringUtils.isNotBlank(oleDeliverNoticeSearchForm.getNoticeType())) {
                        criteria.addEqualTo("noticeType", resultsMap.get("noticeName"));
                    }
                    if (null != oleDeliverNoticeSearchForm.getDateSentTo() && null != oleDeliverNoticeSearchForm.getDateSentFrom()) {
                        criteria.addBetween("noticeSentDate", oleDeliverNoticeSearchForm.getDateSentFrom(), oleDeliverNoticeSearchForm.getDateSentTo());
                    } else if (null != oleDeliverNoticeSearchForm.getDateSentTo()) {
                        criteria.addLessOrEqualThan("noticeSentDate", oleDeliverNoticeSearchForm.getDateSentTo());
                    } else if (null != oleDeliverNoticeSearchForm.getDateSentFrom()) {
                        criteria.addGreaterOrEqualThan("noticeSentDate", oleDeliverNoticeSearchForm.getDateSentFrom());
                    }

                    Collection<Object> oleDeliverNoticeHistories = getLoanDaoOjb().getDeliverNoticeHistory(criteria);
                    if (CollectionUtils.isNotEmpty(oleDeliverNoticeHistories)) {
                        oleDeliverNoticeSearchForm.getOleDeliverNoticeSearchResult().addAll(buildSearchResults(oleDeliverNoticeHistories));
                    }
                }
            }
        }

        return getUIFModelAndView(oleDeliverNoticeSearchForm);
    }

    private List<OLEDeliverNoticeSearchResult> buildSearchResults(Collection<Object> oleDeliverNoticeHistories) {
        List<OLEDeliverNoticeSearchResult> oleDeliverNoticeSearchResults = new ArrayList<>();
        for (Iterator<Object> iterator = oleDeliverNoticeHistories.iterator(); iterator.hasNext(); ) {
            OLEDeliverNoticeHistory oleDeliverNoticeHistory = (OLEDeliverNoticeHistory) iterator.next();
            byte[] noticeContent = oleDeliverNoticeHistory.getNoticeContent();
            if (null != noticeContent) {
                OLEDeliverNoticeSearchResult oleDeliverNoticeSearchResult = new OLEDeliverNoticeSearchResult();
                oleDeliverNoticeSearchResult.setNoticeContent(new String(noticeContent));
                oleDeliverNoticeSearchResult.setDateSentTo(oleDeliverNoticeHistory.getNoticeSentDate());
                oleDeliverNoticeSearchResult.setNoticeType(oleDeliverNoticeHistory.getNoticeType());
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
        } else if (StringUtils.isNotBlank(oleDeliverNoticeSearchForm.getItemBarcode())) {
            filterFields.put("itemBarcode", oleDeliverNoticeSearchForm.getItemBarcode());
        } else if (StringUtils.isNotBlank(oleDeliverNoticeSearchForm.getNoticeType())) {
            filterFields.put("noticeType", oleDeliverNoticeSearchForm.getNoticeType());
        } else if (StringUtils.isNotBlank(oleDeliverNoticeSearchForm.getDeskLocation())) {
            filterFields.put("deskLocation", oleDeliverNoticeSearchForm.getDeskLocation());
        } else if (oleDeliverNoticeSearchForm.getDateSentTo() != null) {
            filterFields.put("dateSentTo", oleDeliverNoticeSearchForm.getDateSentTo());
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
        oleDeliverNoticeSearchForm.setDeskLocation(null);
        oleDeliverNoticeSearchForm.setNoticeType(null);
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
}