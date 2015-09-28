package org.kuali.ole.ncip.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.extensiblecatalog.ncip.v2.service.*;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleItemSearch;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.deliver.drools.LoanPeriodUtil;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.util.DroolsResponse;
import org.kuali.ole.deliver.util.OlePatronRecordUtil;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.ncip.bo.OLENCIPConstants;
import org.kuali.ole.ncip.service.OLESIAPIHelperService;
import org.kuali.ole.ncip.service.impl.OLECirculationHelperServiceImpl;
import org.kuali.ole.select.document.service.OleDocstoreHelperService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.ole.utility.OleStopWatch;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pvsubrah on 8/18/15.
 */
public class OLENCIPUtil {

    private static final Logger LOG = Logger.getLogger(OLENCIPUtil.class);

    private OlePatronRecordUtil olePatronRecordUtil;
    private OLESIAPIHelperService oleSIAPIHelperService;
    private OLECirculationHelperServiceImpl oleCirculationHelperService;
    private CircDeskLocationResolver circDeskLocationResolver;
    private OLENCIPAcceptItemUtil olencipAcceptItemUtil;
    private OleDocstoreHelperService oleDocstoreHelperService;
    private DocstoreUtil docstoreUtil;

    public HashMap<String, String> agencyPropertyMap = new HashMap<>();

    private OlePatronRecordUtil getOlePatronRecordUtil() {
        if (null == olePatronRecordUtil) {
            olePatronRecordUtil = (OlePatronRecordUtil) SpringContext.getBean("olePatronRecordUtil");
        }
        return olePatronRecordUtil;
    }

    public void setOlePatronRecordUtil(OlePatronRecordUtil olePatronRecordUtil) {
        this.olePatronRecordUtil = olePatronRecordUtil;
    }

    public OLESIAPIHelperService getOleSIAPIHelperService() {
        if (oleSIAPIHelperService == null) {
            oleSIAPIHelperService = (OLESIAPIHelperService) SpringContext.getService("oleSIAPIHelperService");
        }
        return oleSIAPIHelperService;
    }

    public void setOleSIAPIHelperService(OLESIAPIHelperService oleSIAPIHelperService) {
        this.oleSIAPIHelperService = oleSIAPIHelperService;
    }

    public OLECirculationHelperServiceImpl getOleCirculationHelperService() {
        if (null == oleCirculationHelperService) {
            oleCirculationHelperService = GlobalResourceLoader.getService(OLENCIPConstants.CIRCULATION_HELPER_SERVICE);
        }
        return oleCirculationHelperService;
    }

    public void setOleCirculationHelperService(OLECirculationHelperServiceImpl oleCirculationHelperService) {
        this.oleCirculationHelperService = oleCirculationHelperService;
    }

    private CircDeskLocationResolver getCircDeskLocationResolver() {
        if (circDeskLocationResolver == null) {
            circDeskLocationResolver = new CircDeskLocationResolver();
        }
        return circDeskLocationResolver;
    }

    public void setCircDeskLocationResolver(CircDeskLocationResolver circDeskLocationResolver) {
        this.circDeskLocationResolver = circDeskLocationResolver;
    }

    public OLENCIPAcceptItemUtil getOlencipAcceptItemUtil() {
        if (null == olencipAcceptItemUtil) {
            olencipAcceptItemUtil = new OLENCIPAcceptItemUtil();
        }
        return olencipAcceptItemUtil;
    }

    public void setOlencipAcceptItemUtil(OLENCIPAcceptItemUtil olencipAcceptItemUtil) {
        this.olencipAcceptItemUtil = olencipAcceptItemUtil;
    }

    public OleDocstoreHelperService getOleDocstoreHelperService() {
        if (oleDocstoreHelperService == null) {
            oleDocstoreHelperService = SpringContext.getBean(OleDocstoreHelperService.class);
        }
        return oleDocstoreHelperService;
    }

    public void setOleDocstoreHelperService(OleDocstoreHelperService oleDocstoreHelperService) {
        this.oleDocstoreHelperService = oleDocstoreHelperService;
    }

    public DocstoreUtil getDocstoreUtil() {
        if (null == docstoreUtil) {
            docstoreUtil = new DocstoreUtil();
        }
        return docstoreUtil;
    }

    public void setDocstoreUtil(DocstoreUtil docstoreUtil) {
        this.docstoreUtil = docstoreUtil;
    }


    public AgencyId validateAgency(InitiationHeader initiationHeader, NCIPResponseData ncipResponseData) {

        AgencyId agencyId = getAgencyId(initiationHeader);

        agencyPropertyMap = getAgencyPropertyMap(agencyId);

        if (agencyPropertyMap.size() == 0) {
            processProblems(ncipResponseData, agencyId.getValue(), ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.INVALID_AGENCY_ID), OLENCIPConstants.AGENCY_ID);
            return null;
        }
        return agencyId;
    }

    public void processProblems(NCIPResponseData ncipResponseData, String problemValue, String problemDetail, String problemElement) {
        List<Problem> problems = new ArrayList<Problem>();
        Problem problem = new Problem();
        ProblemType problemType = new ProblemType("");
        problem.setProblemDetail(problemDetail);
        problem.setProblemElement(problemElement);
        problem.setProblemType(problemType);
        problem.setProblemValue(problemValue);
        problems.add(problem);
        setProblems(ncipResponseData, problems);
    }

    private void setProblems(NCIPResponseData ncipResponseData, List<Problem> problems) {
        if (ncipResponseData instanceof AcceptItemResponseData) {
            AcceptItemResponseData acceptItemResponseData = (AcceptItemResponseData) ncipResponseData;
            acceptItemResponseData.setProblems(problems);
        } else if (ncipResponseData instanceof LookupUserResponseData) {
            LookupUserResponseData lookupUserResponseData = (LookupUserResponseData) ncipResponseData;
            lookupUserResponseData.setProblems(problems);
        } else if (ncipResponseData instanceof CheckOutItemResponseData) {
            CheckOutItemResponseData checkOutItemResponseData = (CheckOutItemResponseData) ncipResponseData;
            checkOutItemResponseData.setProblems(problems);
        } else if (ncipResponseData instanceof CheckInItemResponseData) {
            CheckInItemResponseData checkInItemResponseData = (CheckInItemResponseData) ncipResponseData;
            checkInItemResponseData.setProblems(problems);
        }
    }

    public HashMap<String, String> getAgencyPropertyMap(AgencyId agencyId) {
        agencyPropertyMap = getOleSIAPIHelperService().getAgencyPropertyMap(OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLENCIPConstants.NCIPAPI_PARAMETER_NAME, agencyId.getValue(), agencyPropertyMap);
        return agencyPropertyMap;
    }

    public boolean validateUser(UserId userId, NCIPResponseData ncipResponseData) {
        if (userId == null) {
            processProblems(ncipResponseData, "", ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.USER_DOES_NOT_EXIST), OLENCIPConstants.USER);
            return false;
        }
        return true;
    }

    public OlePatronDocument validPatron(UserId userId, NCIPResponseData ncipResponseData) {
        OlePatronDocument olePatronDocument = null;
        String patronBarcode = userId.getUserIdentifierValue();

        if (StringUtils.isBlank(patronBarcode)) {
            processProblems(ncipResponseData, "", ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.USER_IDENTIFIER_VALUE_DOES_NOT_EXIST), OLENCIPConstants.USER);
            return null;
        }
        try {
            olePatronDocument = getOlePatronRecordUtil().getPatronRecordByBarcode(patronBarcode);
        } catch (Exception e) {
            LOG.error("Exception " + e);
        }

        if (olePatronDocument == null) {
            processProblems(ncipResponseData, patronBarcode, ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_PATRON_INFO), OLENCIPConstants.USER);
            return null;
        }

        String errorMessage = fireLookupUserRules(olePatronDocument);
        if (StringUtils.isNotBlank(errorMessage)) {
            processProblems(ncipResponseData, patronBarcode, errorMessage, OLENCIPConstants.USER);
            return null;
        }

        return olePatronDocument;
    }

    public OleCirculationDesk validOperator(NCIPResponseData ncipResponseData, String operatorId) {
        OleCirculationDesk oleCirculationDesk = null;
        if (StringUtils.isNotBlank(operatorId)) {
            oleCirculationDesk = getCircDeskLocationResolver().getCircDeskForOpertorId(operatorId);
        }
        if (oleCirculationDesk == null) {
            processProblems(ncipResponseData, operatorId, ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.CIRCULATION_DESK_NOT_MAPPED_OPERATOR), OLENCIPConstants.ITEM);
        }
        return oleCirculationDesk;
    }

    public String validItemType(NCIPResponseData ncipResponseData) {
        String itemType = agencyPropertyMap.get(OLENCIPConstants.ITEM_TYPE);

        if (!getOlencipAcceptItemUtil().isValidItemType(itemType)) {
            processProblems(ncipResponseData, itemType, ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_ITM_TYP), OLENCIPConstants.ITEM);
            return null;
        }
        return itemType;
    }

    public boolean validLocation(NCIPResponseData ncipResponseData, String location) {
        boolean validLocation = false;
        if (StringUtils.isNotBlank(location)) {
            OleStopWatch oleStopWatch = new OleStopWatch();
            oleStopWatch.start();
            validLocation = getOleDocstoreHelperService().isValidLocation(location);
            oleStopWatch.end();
            LOG.info("Time taken to validate location : " + oleStopWatch.getTotalTime());
        }
        if (!validLocation) {
            processProblems(ncipResponseData, location, ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVAL_LOC), OLENCIPConstants.ITEM);
        }
        return validLocation;
    }

    public AgencyId getAgencyId(InitiationHeader initiationHeader) {
        AgencyId agencyId = null;
        if (initiationHeader != null && initiationHeader.getFromAgencyId() != null && initiationHeader.getFromAgencyId().getAgencyId() != null
                && StringUtils.isNotBlank(initiationHeader.getFromAgencyId().getAgencyId().getValue())) {
            agencyId = new AgencyId(initiationHeader.getFromAgencyId().getAgencyId().getValue());
        } else if (initiationHeader != null && initiationHeader.getApplicationProfileType() != null
                && StringUtils.isNotBlank(initiationHeader.getApplicationProfileType().getValue())) {
            agencyId = new AgencyId(initiationHeader.getApplicationProfileType().getValue());
        } else {
            agencyId = new AgencyId(new LoanPeriodUtil().getParameter(OLENCIPConstants.AGENCY_ID_PARAMETER));
        }
        return agencyId;
    }

    public String fireLookupUserRules(OlePatronDocument olePatronDocument) {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        ArrayList<Object> facts = new ArrayList<>();
        facts.add(olePatronDocument);
        DroolsResponse droolsResponse = new DroolsResponse();
        facts.add(droolsResponse);
        new CircUtilController().fireRules(facts, null, "lookup-user-ncip");
        oleStopWatch.end();
        LOG.info("Time taken for fire rules to validate patron : " + oleStopWatch.getTotalTime());
        return droolsResponse.getErrorMessage().getErrorMessage();
    }

    public OleItemSearch getOleItemSearch(String itemBarcode) {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        OleItemSearch oleItemSearch = new OleItemSearch();
        try {
            org.kuali.ole.docstore.common.search.SearchParams search_Params = new org.kuali.ole.docstore.common.search.SearchParams();
            search_Params.getSearchConditions().add(search_Params.buildSearchCondition("phrase", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "ITEMBARCODE", itemBarcode), ""));
            search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), OLEConstants.ID));
            search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), OLEConstants.TITLE));
            search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), OLEConstants.AUTHOR));
            search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), OLEConstants.ITEM_TYPE));
            search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), OLEConstants.BIB_IDENTIFIER));
            SearchResponse searchResponse = getDocstoreUtil().getDocstoreClientLocator().getDocstoreClient().search(search_Params);
            if (searchResponse.getSearchResults() != null && searchResponse.getSearchResults().size() > 0) {
                for (SearchResult searchResult : searchResponse.getSearchResults()) {
                    for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                        if (searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.TITLE)) {
                            oleItemSearch.setTitle(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.AUTHOR)) {
                            oleItemSearch.setAuthor(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.ID)) {
                            oleItemSearch.setItemUUID(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.ITEM_TYPE)) {
                            oleItemSearch.setItemType(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase(OLEConstants.BIB_IDENTIFIER)) {
                            oleItemSearch.setBibUUID(searchResultField.getFieldValue());
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Exception " + e);
        }
        oleStopWatch.end();
        LOG.info("Time taken to getOleItemSearch : " + oleStopWatch.getTotalTime());
        return oleItemSearch;
    }

}
