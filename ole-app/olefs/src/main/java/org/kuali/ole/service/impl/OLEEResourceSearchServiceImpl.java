package org.kuali.ole.service.impl;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.coa.businessobject.*;
import org.kuali.ole.describe.form.WorkEInstanceOlemlForm;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.ids.BibId;
import org.kuali.ole.docstore.common.document.ids.HoldingsId;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsUriRecord;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderType;
import org.kuali.ole.module.purap.businessobject.RequisitionAccount;
import org.kuali.ole.module.purap.businessobject.RequisitionItem;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.ole.module.purap.document.service.OlePurapService;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.bo.*;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.*;
import org.kuali.ole.select.bo.OLECreatePO;
import org.kuali.ole.select.form.OLEEResourceRecordForm;
import org.kuali.ole.select.gokb.*;
import org.kuali.ole.select.service.OLESelectDaoOjb;
import org.kuali.ole.select.service.OleReqPOCreateDocumentService;
import org.kuali.ole.service.OLEEResourceSearchService;
import org.kuali.ole.sys.businessobject.Building;
import org.kuali.ole.sys.businessobject.FinancialSystemDocumentHeader;
import org.kuali.ole.sys.businessobject.Room;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.FinancialSystemMaintenanceDocument;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.ole.vnd.VendorConstants;
import org.kuali.ole.vnd.businessobject.*;
import org.kuali.ole.vnd.document.service.VendorService;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.coreservice.impl.parameter.ParameterBo;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResult;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.krad.bo.DocumentHeader;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentHeaderService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 7/10/13
 * Time: 12:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEEResourceSearchServiceImpl implements OLEEResourceSearchService {

    private static final Logger LOG = Logger.getLogger(OLEEResourceSearchServiceImpl.class);
    private BusinessObjectService businessObjectService;
    private ConfigurationService kualiConfigurationService;
    private OleReqPOCreateDocumentService oleReqPOCreateDocumentService;
    private OlePurapService olePurapService;
    protected VendorService vendorService;
    private final String calendarYearAgo = getParameter(OLEConstants.OLEEResourceRecord.CALENDAR_OR_YEAR_AGO);
    private final String calendarYearsAgo = getParameter(OLEConstants.OLEEResourceRecord.CALENDAR_OR_YEARS_AGO);
    private final String monthAgo = getParameter(OLEConstants.OLEEResourceRecord.MONTH_AGO);
    private final String monthsAgo = getParameter(OLEConstants.OLEEResourceRecord.MONTHS_AGO);
    private final String weekAgo = getParameter(OLEConstants.OLEEResourceRecord.WEEK_AGO);
    private final String weeksAgo = getParameter(OLEConstants.OLEEResourceRecord.WEEKS_AGO);
    private final String dayAgo = getParameter(OLEConstants.OLEEResourceRecord.DAY_AGO);
    private final String daysAgo = getParameter(OLEConstants.OLEEResourceRecord.DAYS_AGO);
    private final String firstDayOfYear = getParameter(OLEConstants.OLEEResourceRecord.FIRST_DAY_OF_YEAR);
    private final String lastDayOfYear = getParameter(OLEConstants.OLEEResourceRecord.LAST_DAY_OF_YEAR);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(OLEConstants.OLEEResourceRecord.CREATED_DATE_FORMAT);
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT);
    private static final String PLATFORM_URL_START = "platformRecordController?viewId=OLEPlatformRecordView&methodToCall=docHandler&docId=";
    private static final String PLATFORM_URL_END = "&command=displayDocSearchView&pageId=OLEPlatformRecordView-EventLogTab";
    private static final String HREF_START = "<a  href='";
    private static final String HREF_END = "' target='_blank'>Click</a>";
    private UniversityDateService universityDateService;

    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    public ConfigurationService getConfigurationService() {
        if (kualiConfigurationService == null) {
            kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
        }
        return kualiConfigurationService;
    }

    public OleReqPOCreateDocumentService getOleReqPOCreateDocumentService() {
        if (oleReqPOCreateDocumentService == null) {
            oleReqPOCreateDocumentService = SpringContext.getBean(OleReqPOCreateDocumentService.class);
        }
        return oleReqPOCreateDocumentService;
    }

    public OlePurapService getOlePurapService() {
        if (olePurapService == null) {
            olePurapService = SpringContext.getBean(OlePurapService.class);
        }
        return olePurapService;
    }

    public VendorService getVendorService() {
        if (vendorService == null) {
            vendorService = SpringContext.getBean(VendorService.class);
        }
        return vendorService;
    }

    private ConfigurationService getKualiConfigurationService() {
        return GlobalResourceLoader.getService("kualiConfigurationService");
    }

    @Override
    public List<OLEEResourceRecordDocument> findMatching(Map<String, List<String>> map, DocumentSearchCriteria.Builder docSearchCriteria) {
        Map<String, List<String>> attributes = new HashMap<String, List<String>>();
        if (docSearchCriteria != null) {
            if (!map.isEmpty()) {
                for (String propertyField : map.keySet()) {
                    if (map.get(propertyField) != null) {
                        attributes.put(propertyField, map.get(propertyField));
                    }
                }
            }
        }
        docSearchCriteria.setDocumentAttributeValues(attributes);
        Date currentDate = new Date();
        docSearchCriteria.setDateCreatedTo(new DateTime(currentDate));
        DocumentSearchCriteria docSearchCriteriaDTO = docSearchCriteria.build();
        DocumentSearchResults components = null;
        components = KEWServiceLocator.getDocumentSearchService().lookupDocuments(GlobalVariables.getUserSession().getPrincipalId(), docSearchCriteriaDTO);
        List<DocumentSearchResult> docSearchResults = components.getSearchResults();
        OLEEResourceRecordDocument oleeResourceRecordDocument;
        List<OLEEResourceRecordDocument> oleeResourceRecordDocumentnew = new ArrayList<OLEEResourceRecordDocument>();
        if (!docSearchResults.isEmpty()) {
            for (DocumentSearchResult searchResult : docSearchResults) {
                oleeResourceRecordDocument = new OLEEResourceRecordDocument();
                oleeResourceRecordDocument.setResultDetails(searchResult, new ArrayList());
                if (oleeResourceRecordDocument != null) {
                    oleeResourceRecordDocumentnew.add(oleeResourceRecordDocument);
                }
            }
        }
        return oleeResourceRecordDocumentnew;
    }

    @Override
    public List<OLEEResourceRecordDocument> statusNotNull(List<OLEEResourceRecordDocument> eresourceList, List<String> status) {
        List<OLEEResourceRecordDocument> eresourceStatusList = new ArrayList<OLEEResourceRecordDocument>();
        List<String> listOfStatuses = new ArrayList<>();
        listOfStatuses.addAll(status);
        for (OLEEResourceRecordDocument oleeResourceRecordDocument : eresourceList) {
            if (listOfStatuses.contains(oleeResourceRecordDocument.getStatusId())) {
                eresourceStatusList.add(oleeResourceRecordDocument);
            }
        }
        return eresourceStatusList;
    }

    @Override
    public List<OLEEResourceRecordDocument> performSearch(List<OLESearchCondition> oleSearchConditionsList) throws Exception {
        boolean flag = true;
        Map<String, List<String>> searchCriteriaMap = new HashMap<String, List<String>>();
        List<OLEEResourceRecordDocument> eresourceList = new ArrayList<OLEEResourceRecordDocument>();
        List<OLEEResourceRecordDocument> eresourceDocumentList = new ArrayList<OLEEResourceRecordDocument>();
        DocumentSearchCriteria.Builder docSearchCriteria = DocumentSearchCriteria.Builder.create();
        docSearchCriteria.setDocumentTypeName(OLEConstants.OLEEResourceRecord.OLE_ERS_DOC);
        for (int searchCriteriaCnt = 0; searchCriteriaCnt < oleSearchConditionsList.size(); searchCriteriaCnt++) {
            if (StringUtils.isNotBlank(oleSearchConditionsList.get(searchCriteriaCnt).getSearchBy()) && StringUtils.isBlank(oleSearchConditionsList.get(searchCriteriaCnt).getSearchCriteria())) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.SERACH_CRITERIA_REQUIRED);
            } else if (StringUtils.isBlank(oleSearchConditionsList.get(searchCriteriaCnt).getSearchBy()) && StringUtils.isNotBlank(oleSearchConditionsList.get(searchCriteriaCnt).getSearchCriteria())) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.SERACH_BY_REQUIRED);
            } else if (!GlobalVariables.getMessageMap().hasMessages() && StringUtils.isNotBlank(oleSearchConditionsList.get(searchCriteriaCnt).getSearchBy()) && StringUtils.isNotBlank(oleSearchConditionsList.get(searchCriteriaCnt).getSearchCriteria())
                    && (OLEConstants.OLEEResourceRecord.AND.equals(oleSearchConditionsList.get(searchCriteriaCnt).getOperator()) || OLEConstants.OLEEResourceRecord.OR.equals(oleSearchConditionsList.get(searchCriteriaCnt).getOperator()))) {
                flag = false;
                if (searchCriteriaCnt != 0 && OLEConstants.OLEEResourceRecord.AND.equals(oleSearchConditionsList.get(searchCriteriaCnt).getOperator())) {
                    if (!searchCriteriaMap.containsKey(oleSearchConditionsList.get(searchCriteriaCnt).getSearchBy())) {
                        searchCriteriaMap = getSearchCriteriaMap(oleSearchConditionsList.get(searchCriteriaCnt).getSearchBy(), oleSearchConditionsList.get(searchCriteriaCnt).getSearchCriteria(), searchCriteriaMap);
                    } else {
                        searchCriteriaMap.clear();
                        break;
                    }
                } else {
                    searchCriteriaMap = getSearchCriteriaMap(oleSearchConditionsList.get(searchCriteriaCnt).getSearchBy(), oleSearchConditionsList.get(searchCriteriaCnt).getSearchCriteria(), searchCriteriaMap);
                }
                if (searchCriteriaCnt < oleSearchConditionsList.size() - 1) {
                    if (StringUtils.isNotBlank(oleSearchConditionsList.get(searchCriteriaCnt + 1).getSearchBy()) && !oleSearchConditionsList.get(searchCriteriaCnt + 1).getSearchCriteria().isEmpty()) {
                        if (OLEConstants.OLEEResourceRecord.AND.equals(oleSearchConditionsList.get(searchCriteriaCnt).getOperator())) {
                            if (!searchCriteriaMap.containsKey(oleSearchConditionsList.get(searchCriteriaCnt + 1).getSearchBy())) {
                                searchCriteriaMap = getSearchCriteriaMap(oleSearchConditionsList.get(searchCriteriaCnt + 1).getSearchBy(), oleSearchConditionsList.get(searchCriteriaCnt + 1).getSearchCriteria(), searchCriteriaMap);
                                if (searchCriteriaCnt < oleSearchConditionsList.size() - 2 && oleSearchConditionsList.get(searchCriteriaCnt + 2).getSearchBy() != null && !oleSearchConditionsList.get(searchCriteriaCnt + 2).getSearchCriteria().isEmpty()) {
                                    if (OLEConstants.OLEEResourceRecord.AND.equals(oleSearchConditionsList.get(searchCriteriaCnt + 1).getOperator())) {
                                        searchCriteriaCnt++;
                                    }
                                } else {
                                    if (searchCriteriaMap.size() > 0) {
                                        eresourceList = findMatching(searchCriteriaMap, docSearchCriteria);
                                    }
                                    break;
                                }
                            } else {
                                searchCriteriaMap.clear();
                                break;
                            }
                        } else if (OLEConstants.OLEEResourceRecord.OR.equals(oleSearchConditionsList.get(searchCriteriaCnt).getOperator())) {
                            if (searchCriteriaMap.size() > 0) {
                                eresourceDocumentList = findMatching(searchCriteriaMap, docSearchCriteria);
                                eresourceList.addAll(eresourceDocumentList);
                                searchCriteriaMap.clear();
                            }
                            if (searchCriteriaCnt == oleSearchConditionsList.size() - 2) {
                                searchCriteriaMap = getSearchCriteriaMap(oleSearchConditionsList.get(searchCriteriaCnt + 1).getSearchBy(), oleSearchConditionsList.get(searchCriteriaCnt + 1).getSearchCriteria(), searchCriteriaMap);
                                if (searchCriteriaMap.size() > 0) {
                                    eresourceDocumentList = findMatching(searchCriteriaMap, docSearchCriteria);
                                    eresourceList.addAll(eresourceDocumentList);
                                    searchCriteriaMap.clear();
                                }
                                break;
                            }
                            if (OLEConstants.OLEEResourceRecord.AND.equals(oleSearchConditionsList.get(searchCriteriaCnt + 1).getOperator())) {
                                searchCriteriaMap = getSearchCriteriaMap(oleSearchConditionsList.get(searchCriteriaCnt + 1).getSearchBy(), oleSearchConditionsList.get(searchCriteriaCnt + 1).getSearchCriteria(), searchCriteriaMap);
                                if (searchCriteriaMap.size() > 0) {
                                    eresourceDocumentList = findMatching(searchCriteriaMap, docSearchCriteria);
                                    eresourceList.addAll(eresourceDocumentList);
                                }
                            }
                        }
                    } else {
                        if (!searchCriteriaMap.isEmpty()) {
                            if (searchCriteriaMap.size() > 0) {
                                eresourceDocumentList = findMatching(searchCriteriaMap, docSearchCriteria);
                                eresourceList.addAll(eresourceDocumentList);
                                searchCriteriaMap.clear();
                            }
                        }
                    }
                }
                if (searchCriteriaCnt == oleSearchConditionsList.size() - 1) {
                    if (searchCriteriaMap.size() > 0) {
                        eresourceDocumentList = findMatching(searchCriteriaMap, docSearchCriteria);
                        eresourceList.addAll(eresourceDocumentList);
                        searchCriteriaMap.clear();
                    }
                }
            }
        }
        if (flag) {
            if (!GlobalVariables.getMessageMap().hasMessages()) {
                eresourceList = findMatching(searchCriteriaMap, docSearchCriteria);
            }
        }
        if (eresourceList.size() > 0) {
            for (int searchCriteriaCnt = 0; searchCriteriaCnt < oleSearchConditionsList.size(); searchCriteriaCnt++) {
                if (oleSearchConditionsList.get(searchCriteriaCnt).getSearchBy() != null && StringUtils.isNotEmpty(oleSearchConditionsList.get(searchCriteriaCnt).getSearchBy()) && !oleSearchConditionsList.get(searchCriteriaCnt).getSearchCriteria().isEmpty() &&
                        (OLEConstants.OLEEResourceRecord.NOT.equals(oleSearchConditionsList.get(searchCriteriaCnt).getOperator()))) {
                    searchCriteriaMap.clear();
                    searchCriteriaMap = getSearchCriteriaMap(oleSearchConditionsList.get(searchCriteriaCnt).getSearchBy(), oleSearchConditionsList.get(searchCriteriaCnt).getSearchCriteria(), searchCriteriaMap);
                    if (searchCriteriaMap.size() > 0) {
                        eresourceDocumentList = findMatching(searchCriteriaMap, docSearchCriteria);
                    }
                    List<String> list = new ArrayList<String>();
                    for (OLEEResourceRecordDocument oleEResourceRecordDocument : eresourceDocumentList) {
                        int count = 0;
                        for (OLEEResourceRecordDocument eResourceRecordDocument : eresourceList) {
                            if (oleEResourceRecordDocument.getDocumentNumber().toString().equalsIgnoreCase(eResourceRecordDocument.getDocumentNumber().toString())) {
                                list.add(count + "");
                            }
                            count++;
                        }
                    }
                    for (String str : list) {
                        eresourceList.remove(Integer.parseInt(str));
                    }
                }
            }
        }
        return eresourceList;
    }

    public final String getParameter(String parameterName) {
        ParameterKey parameterKey = ParameterKey.create(OLEConstants.APPL_ID, OLEConstants.SELECT_NMSPC, OLEConstants.SELECT_CMPNT, parameterName);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        return parameter != null ? parameter.getValue() : null;
    }

    public void getEResourcesFields(String eResourceId, OleHoldings eHoldings, WorkEInstanceOlemlForm olemlForm) {
        ExtentOfOwnership extentOfOwnership = null;
        if (eHoldings.getExtentOfOwnership().size() > 0) {
            extentOfOwnership = eHoldings.getExtentOfOwnership().get(0);
        } else if (extentOfOwnership == null) {
            extentOfOwnership = new ExtentOfOwnership();
            eHoldings.getExtentOfOwnership().add(extentOfOwnership);
        }
        Coverages coverages = extentOfOwnership.getCoverages();
        if (coverages == null) {
            coverages = new Coverages();
            eHoldings.getExtentOfOwnership().get(0).setCoverages(coverages);
        }
        PerpetualAccesses perpetualAccesses = extentOfOwnership.getPerpetualAccesses();
        if (perpetualAccesses == null) {
            perpetualAccesses = new PerpetualAccesses();
            eHoldings.getExtentOfOwnership().get(0).setPerpetualAccesses(perpetualAccesses);
        }
        List<Coverage> coverage = coverages.getCoverage();
        List<PerpetualAccess> perpetualAccess = perpetualAccesses.getPerpetualAccess();
        //TODO: set the invoice and eResource information
//        Invoice invoice = eHoldings.getInvoice();
        Map ersIdMap = new HashMap();
        ersIdMap.put(OLEConstants.OLEEResourceRecord.ERESOURCE_IDENTIFIER, eResourceId);
        OLEEResourceRecordDocument eResourceDocument = getBusinessObjectService().findByPrimaryKey(OLEEResourceRecordDocument.class, ersIdMap);
        if (eResourceDocument != null && olemlForm.geteResourceId() != null && !olemlForm.geteResourceId().isEmpty()) {
            olemlForm.setTokenId(eResourceDocument.getDocumentNumber());
            eHoldings.setEResourceId(eResourceDocument.getOleERSIdentifier());
            olemlForm.seteResourceTitle(eResourceDocument.getTitle());
            getAccessLocationFromERS(eResourceDocument, eHoldings, olemlForm);
            if (eHoldings.getStatisticalSearchingCode() == null) {
                StatisticalSearchingCode statisticalSearchingCode = new StatisticalSearchingCode();
                statisticalSearchingCode.setCodeValue(eResourceDocument.getOleStatisticalCode() != null ? eResourceDocument.getOleStatisticalCode().getStatisticalSearchingCode() : "");
                eHoldings.setStatisticalSearchingCode(statisticalSearchingCode);
            }
//            If subscription fields are blank, then get from the corresponding e-resource screen.
            if (StringUtils.isBlank(eHoldings.getSubscriptionStatus()) && StringUtils.isNotBlank(eResourceDocument.getSubscriptionStatus())){
                eHoldings.setSubscriptionStatus(eResourceDocument.getSubscriptionStatus());
                eHoldings.seteResourceSubscriptionStatus(eResourceDocument.getSubscriptionStatus());}

            if (eHoldings.getInitialSubscriptionStartDate()==null && eResourceDocument.getInitialSubscriptionStartDate()!=null){
                eHoldings.seteResourceInitialSubscriptionStartDate(eResourceDocument.getInitialSubscriptionStartDate());
                eHoldings.setInitialSubscriptionStartDate(new SimpleDateFormat("MM/dd/yyyy").format(eResourceDocument.getInitialSubscriptionStartDate()));}

            if(eHoldings.getCurrentSubscriptionEndDate()==null && eResourceDocument.getCurrentSubscriptionEndDate()!=null){
                eHoldings.seteResourceCurrentSubscriptionEndDate(eResourceDocument.getCurrentSubscriptionEndDate());
                eHoldings.setCurrentSubscriptionEndDate(new SimpleDateFormat("MM/dd/yyyy").format(eResourceDocument.getCurrentSubscriptionEndDate()));}

            if(eHoldings.getCurrentSubscriptionStartDate()==null && eResourceDocument.getCurrentSubscriptionStartDate()!=null){
                eHoldings.setCurrentSubscriptionStartDate(new SimpleDateFormat("MM/dd/yyyy").format(eResourceDocument.getCurrentSubscriptionStartDate()));
                eHoldings.seteResourceCurrentSubscriptionStartDate(eResourceDocument.getCurrentSubscriptionStartDate());}

            if(eHoldings.getCancellationDecisionDate()==null && eResourceDocument.getCancellationDecisionDate()!=null){
                eHoldings.setCancellationDecisionDate(new SimpleDateFormat("MM/dd/yyyy").format(eResourceDocument.getCancellationDecisionDate()));
                eHoldings.seteResourceCancellationDecisionDate(eResourceDocument.getCancellationDecisionDate());}

            if(eHoldings.getCancellationEffectiveDate()==null && eResourceDocument.getCancellationEffectiveDate()!=null){
                eHoldings.setCancellationEffectiveDate(new SimpleDateFormat("MM/dd/yyyy").format(eResourceDocument.getCancellationEffectiveDate()));
                eHoldings.seteResourceCancellationEffectiveDate(eResourceDocument.getCancellationEffectiveDate());}

            if(StringUtils.isBlank(eHoldings.getCancellationReason()) && StringUtils.isNotBlank(eResourceDocument.getCancellationReason())){
                eHoldings.setCancellationReason(eResourceDocument.getCancellationReason());
                eHoldings.seteResourceCancellationReason(eResourceDocument.getCancellationReason());}

            if (coverage.size() != 0) {
                if (eResourceDocument.getDefaultCoverage() != null && !eResourceDocument.getDefaultCoverage().isEmpty()) {
                    if (eResourceDocument.getDefaultCoverage().contains("-")) {
                        String[] defaultCoverageStart = eResourceDocument.getDefaultCoverage().split(getParameter(OLEConstants.OLEEResourceRecord.COVERAGE_DATE_SEPARATOR));
                        String[] covStartSeparator = defaultCoverageStart[0].split(getParameter(OLEConstants.OLEEResourceRecord.COMMA_SEPARATOR));
                        String[] covEndSeparator = defaultCoverageStart[1].split(getParameter(OLEConstants.OLEEResourceRecord.COMMA_SEPARATOR));
                        for (Coverage coverageList : coverage) {
                            if (StringUtils.isBlank(coverageList.getCoverageEndDate())) {
                                if (covEndSeparator.length > 2 && !covEndSeparator[2].isEmpty()) {
                                    if (covEndSeparator[2].contains("/")) {
                                        coverageList.setCoverageEndDateFormat(covEndSeparator[2]);
                                    } else {
                                        coverageList.setCoverageEndDateString(covEndSeparator[2]);
                                    }
                                    coverageList.setCoverageEndDate(covEndSeparator[2]);
                                }
                                if (covEndSeparator.length > 0 && !covEndSeparator[0].isEmpty()) {
                                    coverageList.setCoverageEndVolume(covEndSeparator[0]);
                                }
                                if (covEndSeparator.length > 1 && !covEndSeparator[1].isEmpty()) {
                                    coverageList.setCoverageEndIssue(covEndSeparator[1]);
                                }
                            }
                            if (StringUtils.isBlank(coverageList.getCoverageStartDate())) {
                                if (covStartSeparator.length > 2 && !covStartSeparator[2].isEmpty()) {
                                    if (covStartSeparator[2].contains("/")) {
                                        coverageList.setCoverageStartDateFormat(covStartSeparator[2]);
                                    } else {
                                        coverageList.setCoverageStartDateString(covStartSeparator[2]);
                                    }
                                    coverageList.setCoverageStartDate(covStartSeparator[2]);
                                }
                                if (covStartSeparator.length > 0 && !covStartSeparator[0].isEmpty()) {
                                    coverageList.setCoverageStartVolume(covStartSeparator[0]);
                                }
                                if (covStartSeparator.length > 1 && !covStartSeparator[1].isEmpty()) {
                                    coverageList.setCoverageStartIssue(covStartSeparator[1]);
                                }
                            }
                        }
                    }
                }
            }
            if (perpetualAccess.size() != 0) {
                if (eResourceDocument.getDefaultPerpetualAccess() != null && !eResourceDocument.getDefaultPerpetualAccess().isEmpty()) {
                    if (eResourceDocument.getDefaultPerpetualAccess().contains("-")) {
                        String[] defaultPerAccStart = eResourceDocument.getDefaultPerpetualAccess().split(getParameter(OLEConstants.OLEEResourceRecord.PERPETUAL_ACCESS_DATE_SEPARATOR));
                        String[] perAccStartSeparator = defaultPerAccStart[0].split(getParameter(OLEConstants.OLEEResourceRecord.COMMA_SEPARATOR));
                        String[] perAccEndSeparator = defaultPerAccStart[1].split(getParameter(OLEConstants.OLEEResourceRecord.COMMA_SEPARATOR));
                        for (PerpetualAccess perpetualAccessList : perpetualAccess) {
                            if (perpetualAccessList.getPerpetualAccessEndDate() == null) {
                                if (perAccEndSeparator.length > 2 && !perAccEndSeparator[2].isEmpty()) {
                                    if (perAccEndSeparator[2].contains("/")) {
                                        perpetualAccessList.setPerpetualAccessEndDateFormat(perAccEndSeparator[2]);
                                    } else {
                                        perpetualAccessList.setPerpetualAccessEndDateString(perAccEndSeparator[2]);
                                    }
                                    perpetualAccessList.setPerpetualAccessEndDate(perAccEndSeparator[2]);
                                }
                                if (perAccEndSeparator.length > 0 && !perAccEndSeparator[0].isEmpty()) {
                                    perpetualAccessList.setPerpetualAccessEndVolume(perAccEndSeparator[0]);
                                }
                                if (perAccEndSeparator.length > 1 && !perAccEndSeparator[1].isEmpty()) {
                                    perpetualAccessList.setPerpetualAccessEndIssue(perAccEndSeparator[1]);
                                }
                            }
                            if (perpetualAccessList.getPerpetualAccessStartDate() == null) {
                                if (perAccStartSeparator.length > 2 && !perAccStartSeparator[2].isEmpty()) {
                                    if (perAccStartSeparator[2].contains("/")) {
                                        perpetualAccessList.setPerpetualAccessStartDateFormat(perAccStartSeparator[2]);
                                    } else {
                                        perpetualAccessList.setPerpetualAccessStartDateString(perAccStartSeparator[2]);
                                    }
                                    perpetualAccessList.setPerpetualAccessStartDate(perAccStartSeparator[2]);
                                }
                                if (perAccStartSeparator.length > 0 && !perAccStartSeparator[0].isEmpty()) {
                                    perpetualAccessList.setPerpetualAccessStartVolume(perAccStartSeparator[0]);
                                }
                                if (perAccStartSeparator.length > 1 && !perAccStartSeparator[1].isEmpty()) {
                                    perpetualAccessList.setPerpetualAccessStartIssue(perAccStartSeparator[1]);
                                }
                            }
                        }
                    }
                }
            }
            if (coverage.size() == 0) {
                boolean coverageFlag = false;
                Coverage cov = new Coverage();
                if (eResourceDocument.getDefaultCoverage() != null && !eResourceDocument.getDefaultCoverage().isEmpty()) {
                    if (eResourceDocument.getDefaultCoverage().contains("-")) {
                        String[] defaultCoverageStart = eResourceDocument.getDefaultCoverage().split(getParameter(OLEConstants.OLEEResourceRecord.COVERAGE_DATE_SEPARATOR));
                        String[] covStartSeparator = defaultCoverageStart[0].split(getParameter(OLEConstants.OLEEResourceRecord.COMMA_SEPARATOR));
                        if (covStartSeparator.length > 2 && !covStartSeparator[2].isEmpty()) {
                            if(covStartSeparator[2].contains("/")) {
                                cov.setCoverageStartDateFormat(covStartSeparator[2]);
                            } else {
                                cov.setCoverageStartDateString(covStartSeparator[2]);
                            }
                            cov.setCoverageStartDate(covStartSeparator[2]);
                            coverageFlag = true;
                        }
                        if (covStartSeparator.length > 0 && !covStartSeparator[0].isEmpty()) {
                            cov.setCoverageStartVolume(covStartSeparator[0]);
                            coverageFlag = true;
                        }
                        if (covStartSeparator.length > 1 && !covStartSeparator[1].isEmpty()) {
                            cov.setCoverageStartIssue(covStartSeparator[1]);
                            coverageFlag = true;
                        }
                        String[] covEndSeparator = defaultCoverageStart[1].split(getParameter(OLEConstants.OLEEResourceRecord.COMMA_SEPARATOR));
                        if (covEndSeparator.length > 2 && !covEndSeparator[2].isEmpty()) {
                            if(covEndSeparator[2].contains("/")) {
                                cov.setCoverageEndDateFormat(covEndSeparator[2]);
                            } else {
                                cov.setCoverageEndDateString(covEndSeparator[2]);
                            }
                            cov.setCoverageEndDate(covEndSeparator[2]);
                            coverageFlag = true;
                        }
                        if (covEndSeparator.length > 0 && !covEndSeparator[0].isEmpty()) {
                            cov.setCoverageEndVolume(covEndSeparator[0]);
                            coverageFlag = true;
                        }
                        if (covEndSeparator.length > 1 && !covEndSeparator[1].isEmpty()) {
                            cov.setCoverageEndIssue(covEndSeparator[1]);
                            coverageFlag = true;
                        }
                        if (coverageFlag) {
                            eHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage().add(cov);
                        }
                    }
                }
            }
            if (perpetualAccess.size() == 0) {
                boolean perpetualAccFlag = false;
                PerpetualAccess perpetualAcc = new PerpetualAccess();
                if (eResourceDocument.getDefaultPerpetualAccess() != null && !eResourceDocument.getDefaultPerpetualAccess().isEmpty()) {
                    if (eResourceDocument.getDefaultPerpetualAccess().contains("-")) {
                        String[] defaultPerAccStart = eResourceDocument.getDefaultPerpetualAccess().split(getParameter(OLEConstants.OLEEResourceRecord.PERPETUAL_ACCESS_DATE_SEPARATOR));
                        String[] perAccStartSeparator = defaultPerAccStart[0].split(getParameter(OLEConstants.OLEEResourceRecord.COMMA_SEPARATOR));
                        if (perAccStartSeparator.length > 2 && !perAccStartSeparator[2].isEmpty()) {
                            if(perAccStartSeparator[2].contains("/")) {
                                perpetualAcc.setPerpetualAccessStartDateFormat(perAccStartSeparator[2]);
                            } else {
                                perpetualAcc.setPerpetualAccessStartDateString(perAccStartSeparator[2]);
                            }
                            perpetualAcc.setPerpetualAccessStartDate(perAccStartSeparator[2]);
                            perpetualAccFlag = true;
                        }
                        if (perAccStartSeparator.length > 0 && !perAccStartSeparator[0].isEmpty()) {
                            perpetualAcc.setPerpetualAccessStartVolume(perAccStartSeparator[0]);
                            perpetualAccFlag = true;
                        }
                        if (perAccStartSeparator.length > 1 && !perAccStartSeparator[1].isEmpty()) {
                            perpetualAcc.setPerpetualAccessStartIssue(perAccStartSeparator[1]);
                            perpetualAccFlag = true;
                        }
                        String[] perAccEndSeparator = defaultPerAccStart[1].split(getParameter(OLEConstants.OLEEResourceRecord.COMMA_SEPARATOR));
                        if (perAccEndSeparator.length > 2 && !perAccEndSeparator[2].isEmpty()) {
                            if(perAccEndSeparator[2].contains("/")) {
                                perpetualAcc.setPerpetualAccessEndDateFormat(perAccEndSeparator[2]);
                            } else {
                                perpetualAcc.setPerpetualAccessEndDateString(perAccEndSeparator[2]);
                            }
                            perpetualAcc.setPerpetualAccessEndDate(perAccEndSeparator[2]);
                            perpetualAccFlag = true;
                        }
                        if (perAccEndSeparator.length > 0 && !perAccEndSeparator[0].isEmpty()) {
                            perpetualAcc.setPerpetualAccessEndVolume(perAccEndSeparator[0]);
                            perpetualAccFlag = true;
                        }
                        if (perAccEndSeparator.length > 1 && !perAccEndSeparator[1].isEmpty()) {
                            perpetualAcc.setPerpetualAccessEndIssue(perAccEndSeparator[1]);
                            perpetualAccFlag = true;
                        }
                        if (perpetualAccFlag) {
                            eHoldings.getExtentOfOwnership().get(0).getPerpetualAccesses().getPerpetualAccess().add(perpetualAcc);
                        }
                    }
                }
            }
        } else {
            getAccessLocationFromEInstance(eHoldings, olemlForm);
        }
    }

    public void removeEResourcesFields(String eResourceId, OleHoldings eHoldings, WorkEInstanceOlemlForm olemlForm) {
        ExtentOfOwnership extentOfOwnership = null;
        if (eHoldings.getExtentOfOwnership().size() > 0) {
            extentOfOwnership = eHoldings.getExtentOfOwnership().get(0);
        } else if (extentOfOwnership == null) {
            extentOfOwnership = new ExtentOfOwnership();
            eHoldings.getExtentOfOwnership().add(extentOfOwnership);
        }
        Coverages coverages = extentOfOwnership.getCoverages();
        if (coverages == null) {
            coverages = new Coverages();
            eHoldings.getExtentOfOwnership().get(0).setCoverages(coverages);
        }
        List<Coverage> coverage = coverages.getCoverage();
        Map ersIdMap = new HashMap();
        ersIdMap.put(OLEConstants.OLEEResourceRecord.ERESOURCE_IDENTIFIER, eResourceId);
        OLEEResourceRecordDocument eResourceDocument = getBusinessObjectService().findByPrimaryKey(OLEEResourceRecordDocument.class, ersIdMap);
        if (eResourceDocument != null && olemlForm.geteResourceId() != null && !olemlForm.geteResourceId().isEmpty()) {
            olemlForm.setTokenId(eResourceDocument.getDocumentNumber());
            eHoldings.setEResourceId(eResourceDocument.getOleERSIdentifier());
            olemlForm.seteResourceTitle(eResourceDocument.getTitle());
            if (coverage.size() != 0) {
                if (eResourceDocument.getDefaultCoverage() != null && !eResourceDocument.getDefaultCoverage().isEmpty()) {
                    if (eResourceDocument.getDefaultCoverage().contains("-")) {
                        String[] eResDefaultCoverageStart = eResourceDocument.getDefaultCoverage().split(getParameter(OLEConstants.OLEEResourceRecord.COVERAGE_DATE_SEPARATOR));
                        String[] eResCovStartSeparator = eResDefaultCoverageStart[0].split(getParameter(OLEConstants.OLEEResourceRecord.COMMA_SEPARATOR));
                        String[] eResCovEndSeparator = eResDefaultCoverageStart[1].split(getParameter(OLEConstants.OLEEResourceRecord.COMMA_SEPARATOR));
                        for (Coverage cov : coverage) {
                            Coverage tempCov = new Coverage();
                            boolean coverageFlag = false;
                            if (eResCovStartSeparator.length > 2 && !eResCovStartSeparator[2].isEmpty()) {
                                if (eResCovStartSeparator[2].contains("/")) {
                                    tempCov.setCoverageStartDateFormat(eResCovStartSeparator[2]);
                                } else {
                                    tempCov.setCoverageStartDateString(eResCovStartSeparator[2]);
                                }
                                tempCov.setCoverageStartDate(eResCovStartSeparator[2]);
                                coverageFlag = true;
                            }
                            if (eResCovStartSeparator.length > 0 && !eResCovStartSeparator[0].isEmpty()) {
                                tempCov.setCoverageStartVolume(eResCovStartSeparator[0]);
                                coverageFlag = true;
                            }
                            if (eResCovStartSeparator.length > 1 && !eResCovStartSeparator[1].isEmpty()) {
                                tempCov.setCoverageStartIssue(eResCovStartSeparator[1]);
                                coverageFlag = true;
                            }
                            if (eResCovEndSeparator.length > 2 && !eResCovEndSeparator[2].isEmpty()) {
                                if (eResCovEndSeparator[2].contains("/")) {
                                    tempCov.setCoverageEndDateFormat(eResCovEndSeparator[2]);
                                } else {
                                    tempCov.setCoverageEndDateString(eResCovEndSeparator[2]);
                                }
                                tempCov.setCoverageEndDate(eResCovEndSeparator[2]);
                                coverageFlag = true;
                            }
                            if (eResCovEndSeparator.length > 0 && !eResCovEndSeparator[0].isEmpty()) {
                                tempCov.setCoverageEndVolume(eResCovEndSeparator[0]);
                                coverageFlag = true;
                            }
                            if (eResCovEndSeparator.length > 1 && !eResCovEndSeparator[1].isEmpty()) {
                                tempCov.setCoverageEndIssue(eResCovEndSeparator[1]);
                                coverageFlag = true;
                            }
                            if (coverageFlag) {
                                if (StringUtils.isNotBlank(tempCov.getCoverageStartDateFormat()) && StringUtils.isNotBlank(cov.getCoverageStartDateFormat()) && tempCov.getCoverageStartDateFormat().equals(cov.getCoverageStartDateFormat())) {
                                    cov.setCoverageStartDateFormat(null);
                                }
                                if (StringUtils.isNotBlank(tempCov.getCoverageStartDateString()) && StringUtils.isNotBlank(cov.getCoverageStartDateString()) && tempCov.getCoverageStartDateString().equals(cov.getCoverageStartDateString())) {
                                    cov.setCoverageStartDateString(null);
                                }
                                if (StringUtils.isNotBlank(tempCov.getCoverageStartDate()) && StringUtils.isNotBlank(cov.getCoverageStartDate()) && tempCov.getCoverageStartDate().equals(cov.getCoverageStartDate())) {
                                    cov.setCoverageStartDate(null);
                                }
                                if (StringUtils.isNotBlank(tempCov.getCoverageStartVolume()) && StringUtils.isNotBlank(cov.getCoverageStartVolume()) && tempCov.getCoverageStartVolume().equals(cov.getCoverageStartVolume())) {
                                    cov.setCoverageStartVolume(null);
                                }
                                if (StringUtils.isNotBlank(tempCov.getCoverageStartIssue()) && StringUtils.isNotBlank(cov.getCoverageStartIssue()) && tempCov.getCoverageStartIssue().equals(cov.getCoverageStartIssue())) {
                                    cov.setCoverageStartIssue(null);
                                }
                                if (StringUtils.isNotBlank(tempCov.getCoverageEndDateFormat()) && StringUtils.isNotBlank(cov.getCoverageEndDateFormat()) && tempCov.getCoverageEndDateFormat().equals(cov.getCoverageEndDateFormat())) {
                                    cov.setCoverageEndDateFormat(null);
                                }
                                if (StringUtils.isNotBlank(tempCov.getCoverageEndDateString()) && StringUtils.isNotBlank(cov.getCoverageEndDateString()) && tempCov.getCoverageEndDateString().equals(cov.getCoverageEndDateString())) {
                                    cov.setCoverageEndDateString(null);
                                }
                                if (StringUtils.isNotBlank(tempCov.getCoverageEndDate()) && StringUtils.isNotBlank(cov.getCoverageEndDate()) && tempCov.getCoverageEndDate().equals(cov.getCoverageEndDate())) {
                                    cov.setCoverageEndDate(null);
                                }
                                if (StringUtils.isNotBlank(tempCov.getCoverageEndVolume()) && StringUtils.isNotBlank(cov.getCoverageEndVolume()) && tempCov.getCoverageEndVolume().equals(cov.getCoverageEndVolume())) {
                                    cov.setCoverageEndVolume(null);
                                }
                                if (StringUtils.isNotBlank(tempCov.getCoverageEndIssue()) && StringUtils.isNotBlank(cov.getCoverageEndIssue()) && tempCov.getCoverageEndIssue().equals(cov.getCoverageEndIssue())) {
                                    cov.setCoverageEndIssue(null);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void getAccessLocationFromERS(OLEEResourceRecordDocument eResourceDocument, OleHoldings eHoldings, WorkEInstanceOlemlForm olemlForm) {
        if (eResourceDocument != null) {
            HoldingsAccessInformation accessInformation = eHoldings.getHoldingsAccessInformation();
            if (accessInformation != null&&accessInformation.getAccessLocation()!=null) {
                if (olemlForm.getExtendedEHoldingFields().getAccessLocation() != null && olemlForm.getExtendedEHoldingFields().getAccessLocation().size() > 0) {
                    String accessId = "";
                    for (String accessLoc : olemlForm.getExtendedEHoldingFields().getAccessLocation()) {
                        accessId += accessLoc;
                        accessId += OLEConstants.OLEEResourceRecord.ACCESS_LOCATION_SEPARATOR;
                    }
                    accessInformation.setAccessLocation(accessId.substring(0, (accessId.lastIndexOf(OLEConstants.OLEEResourceRecord.ACCESS_LOCATION_SEPARATOR))));
                    eHoldings.setHoldingsAccessInformation(accessInformation);
                }
                else{
                    String accessLocationId = accessInformation.getAccessLocation();
                    String accessId = "";
                    if (accessLocationId != null && !accessLocationId.isEmpty()) {
                        String[] accessLocation = accessLocationId.split(OLEConstants.OLEEResourceRecord.ACCESS_LOCATION_SEPARATOR);
                        List<String> accessLocations = new ArrayList<>();
                        for (String accessLocId : accessLocation) {
                            olemlForm.getExtendedEHoldingFields().getAccessLocation().add(accessLocId);
                            accessLocations.add(accessLocId);
                        }
                        for (String accessLoc : accessLocations) {
                            accessId += accessLoc;
                            accessId += OLEConstants.OLEEResourceRecord.ACCESS_LOCATION_SEPARATOR;
                        }
                        accessInformation.setAccessLocation(accessId.substring(0, (accessId.lastIndexOf(OLEConstants.OLEEResourceRecord.ACCESS_LOCATION_SEPARATOR))));
                    }
                }
            }else {
                accessInformation = new HoldingsAccessInformation();
                String accessLocationId = eResourceDocument.getAccessLocationId();
                String accessId = "";
                if (accessLocationId != null && !accessLocationId.isEmpty()) {
                    String[] accessLocation = accessLocationId.split(OLEConstants.OLEEResourceRecord.ACCESS_LOCATION_SEPARATOR);
                    List<String> accessLocations = new ArrayList<>();
                    for (String accessLocId : accessLocation) {
                        olemlForm.getExtendedEHoldingFields().getAccessLocation().add(accessLocId);
                        accessLocations.add(accessLocId);
                    }
                    for (String accessLoc : accessLocations) {
                        accessId += accessLoc;
                        accessId += OLEConstants.OLEEResourceRecord.ACCESS_LOCATION_SEPARATOR;
                    }
                    accessInformation.setAccessLocation(accessId.substring(0, (accessId.lastIndexOf(OLEConstants.OLEEResourceRecord.ACCESS_LOCATION_SEPARATOR))));
                }

            }
            if (eHoldings.getHoldingsAccessInformation()==null) {
                accessInformation.setAuthenticationType(eResourceDocument.getOleAuthenticationType() != null ? eResourceDocument.getOleAuthenticationType().getOleAuthenticationTypeName() : "");
                accessInformation.setNumberOfSimultaneousUser(eResourceDocument.getNumOfSimultaneousUsers());
            }
            if (eHoldings.getHoldingsAccessInformation()!=null&&eHoldings.getHoldingsAccessInformation().getAuthenticationType()==null) {
                accessInformation.setAuthenticationType(eResourceDocument.getOleAuthenticationType() != null ? eResourceDocument.getOleAuthenticationType().getOleAuthenticationTypeName() : "");
            }
            if (eHoldings.getHoldingsAccessInformation()!=null&&eHoldings.getHoldingsAccessInformation().getNumberOfSimultaneousUser()==null) {
                accessInformation.setNumberOfSimultaneousUser(eResourceDocument.getNumOfSimultaneousUsers());
            }
            eHoldings.setHoldingsAccessInformation(accessInformation);
        }
    }

    public void getAccessLocationFromEInstance(OleHoldings eHoldings, WorkEInstanceOlemlForm olemlForm) {
        HoldingsAccessInformation accessInformation = eHoldings.getHoldingsAccessInformation();
        List<String> accessLocations = new ArrayList<>();
        if (accessInformation != null) {
            if (olemlForm.getExtendedEHoldingFields().getAccessLocation() != null && olemlForm.getExtendedEHoldingFields().getAccessLocation().size() > 0) {
                String accessId = "";
                for (String accessLoc : olemlForm.getExtendedEHoldingFields().getAccessLocation()) {
                    accessId += accessLoc;
                    accessId += OLEConstants.OLEEResourceRecord.ACCESS_LOCATION_SEPARATOR;
                }
                accessInformation.setAccessLocation(accessId.substring(0, (accessId.lastIndexOf(OLEConstants.OLEEResourceRecord.ACCESS_LOCATION_SEPARATOR))));
                eHoldings.setHoldingsAccessInformation(accessInformation);
            } else if (accessInformation.getAccessLocation() != null && !accessInformation.getAccessLocation().isEmpty()) {
                String[] accessId = accessInformation.getAccessLocation().split(OLEConstants.OLEEResourceRecord.ACCESS_LOCATION_SEPARATOR);
                for (String accessLoc : accessId) {
                    accessLocations.add(accessLoc);
                }
                olemlForm.getExtendedEHoldingFields().setAccessLocation(accessLocations);
            }
        }
    }

    public Map<String, List<String>> getSearchCriteriaMap(String searchBy, String searchCriteria, Map<String, List<String>> searchCriteriaMap) throws Exception {
        List<String> listOfBibs = new ArrayList<>();
        List<String> valueList = new ArrayList<>();
        org.kuali.ole.docstore.common.search.SearchParams search_Params = new org.kuali.ole.docstore.common.search.SearchParams();
        SearchResponse searchResponse = null;
        if (searchBy.equals(OLEConstants.OLEEResourceRecord.ERESOURCE_ISBN)) {
            if ("001".equals(OLEConstants.COMMON_IDENTIFIER_SEARCH)) {
                String code = "wbm-" + searchCriteria;
                search_Params.getSearchConditions().add(search_Params.buildSearchCondition("", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), "id", code), ""));
            } else {
                search_Params.getSearchConditions().add(search_Params.buildSearchCondition("", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), OLEConstants.COMMON_IDENTIFIER_SEARCH, searchCriteria), ""));
            }


            search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), "id"));


            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(search_Params);
            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                    String fieldName = searchResultField.getFieldName();
                    String fieldValue = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";

                    if (fieldName.equalsIgnoreCase("id") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("bibliographic")) {
                        listOfBibs.add(fieldValue);
                    }
                }
            }


            if (listOfBibs.size() > 0) {
                valueList.addAll(listOfBibs);
                searchCriteriaMap.put(searchBy, valueList);


            }
        } else if (searchBy.equals(OLEConstants.OLEEResourceRecord.ERESOURCE_OCLC)) {
            if ("001".equals(OLEConstants.OCLC_SEARCH)) {
                String code = "wbm-" + searchCriteria;
                search_Params.getSearchConditions().add(search_Params.buildSearchCondition("", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), "id", code), ""));
            } else {
                search_Params.getSearchConditions().add(search_Params.buildSearchCondition("", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), OLEConstants.OCLC_SEARCH, searchCriteria), ""));
            }
            search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), "id"));
            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(search_Params);
            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                    String fieldName = searchResultField.getFieldName();
                    String fieldValue = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";

                    if (fieldName.equalsIgnoreCase("id") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("bibliographic")) {
                        listOfBibs.add(fieldValue);
                    }
                }
            }
            if (listOfBibs.size() > 0) {
                valueList.addAll(listOfBibs);
                searchCriteriaMap.put(searchBy, valueList);
            }
        } else {
            valueList.add(searchCriteria);
            searchCriteriaMap.put(searchBy, valueList);
        }
        return searchCriteriaMap;
    }

    public void getInstanceIdFromERS(List<OLEEResourceInstance> oleERSInstanceList, OleHoldings eHoldings) {
        String instanceId = "";
        if (oleERSInstanceList.size() > 0) {
            for (OLEEResourceInstance oleERSInstance : oleERSInstanceList) {
                Map ersIdMap = new HashMap();
                ersIdMap.put(OLEConstants.OLEEResourceRecord.ERESOURCE_IDENTIFIER, oleERSInstance.getOleERSIdentifier());
                ersIdMap.put(OLEConstants.OLEEResourceRecord.ERESOURCE_INSTANCE_ID, oleERSInstance.getInstanceId());
                List<OLEEResourceInstance> eResourceInstance = (List<OLEEResourceInstance>) getBusinessObjectService().findMatching(OLEEResourceInstance.class, ersIdMap);
                if (eResourceInstance.size() > 0) {
                    for (OLEEResourceInstance eInstanceRec : eResourceInstance) {
                        instanceId += eInstanceRec.getInstanceId();
                        instanceId += ",";
                    }
                    //TODO: setRelatedInstanceIdentifier
//                    eHoldings.setRelatedInstanceIdentifier(instanceId.substring(0, (instanceId.lastIndexOf(","))));
                }
            }
        }
    }

    public void getPOIdFromERS(List<OLEEResourcePO> oleERSPOList, OleHoldings eHoldings) {
        String poId = "";
        if (oleERSPOList.size() > 0) {
            for (OLEEResourcePO oleERSPO : oleERSPOList) {
                Map ersIdMap = new HashMap();
                ersIdMap.put(OLEConstants.OLEEResourceRecord.ERESOURCE_IDENTIFIER, oleERSPO.getOleERSIdentifier());
                ersIdMap.put(OLEConstants.OLEEResourceRecord.ERESOURCE_PO_ITEM_ID, oleERSPO.getOlePOItemId());
                List<OLEEResourcePO> eResourcePO = (List<OLEEResourcePO>) getBusinessObjectService().findMatching(OLEEResourcePO.class, ersIdMap);
                if (eResourcePO.size() > 0) {
                    for (OLEEResourcePO ePORec : eResourcePO) {
                        poId += ePORec.getOlePOItemId();
                        poId += ",";
                    }
                    //TODO: set PurchaseOrder info
//                    eHoldings.setPurchaseOrderId(poId.substring(0, (poId.lastIndexOf(","))));
                }
            }
        }
    }

    public void getEResourcesLicenseFields(String eResourceId, WorkEInstanceOlemlForm eInstanceOlemlForm) {
        List<OLEEResourceLicense> oleERSLicenses = new ArrayList<OLEEResourceLicense>();
        Map ersIdMap = new HashMap();
        ersIdMap.put(OLEConstants.OLEEResourceRecord.ERESOURCE_IDENTIFIER, eResourceId);
        OLEEResourceRecordDocument eResourceDocument = getBusinessObjectService().findByPrimaryKey(OLEEResourceRecordDocument.class, ersIdMap);
        if (eResourceDocument != null) {
            oleERSLicenses = eResourceDocument.getOleERSLicenseRequests();
            if (oleERSLicenses.size() > 0) {
                for (OLEEResourceLicense oleeResourceLicense : oleERSLicenses) {
                    DocumentRouteHeaderValue documentRouteHeaderValue = oleeResourceLicense.getDocumentRouteHeaderValue();
                    if (documentRouteHeaderValue != null) {
                        String licenceTitle = documentRouteHeaderValue.getDocTitle();
                        if (licenceTitle != null && !licenceTitle.isEmpty()) {
                            licenceTitle = licenceTitle.substring(26);
                        }
                        oleeResourceLicense.setDocumentDescription(licenceTitle);
                    }
                }
                eInstanceOlemlForm.getExtendedEHoldingFields().setLicense(oleERSLicenses);
            }
        }
    }

    public OLEEResourceRecordDocument getNewOleERSDoc(OLEEResourceRecordDocument oleERSDoc) {
        if (oleERSDoc.getOleERSIdentifier() != null && !oleERSDoc.getOleERSIdentifier().isEmpty()) {
            Map<String, String> tempId = new HashMap<String, String>();
            tempId.put(OLEConstants.OLEEResourceRecord.ERESOURCE_IDENTIFIER, oleERSDoc.getOleERSIdentifier());
            OLEEResourceRecordDocument tempDocument = (OLEEResourceRecordDocument) getBusinessObjectService().findByPrimaryKey(OLEEResourceRecordDocument.class, tempId);
            if (tempDocument != null) {
                if (tempDocument.getOleMaterialTypes().size() > 0) {
                    getBusinessObjectService().delete(tempDocument.getOleMaterialTypes());
                }
                if (tempDocument.getOleFormatTypes().size() > 0) {
                    getBusinessObjectService().delete(tempDocument.getOleFormatTypes());
                }
                if (tempDocument.getOleContentTypes().size() > 0) {
                    getBusinessObjectService().delete(tempDocument.getOleContentTypes());
                }
                if (tempDocument.getSelectors().size() > 0) {
                    getBusinessObjectService().delete(tempDocument.getSelectors());
                }
                if (tempDocument.getRequestors().size() > 0) {
                    getBusinessObjectService().delete(tempDocument.getRequestors());
                }
                if (tempDocument.getReqSelComments().size() > 0) {
                    getBusinessObjectService().delete(tempDocument.getReqSelComments());
                }
                if (tempDocument.getOleEResourceVariantTitleList().size() > 0) {
                    getBusinessObjectService().delete(tempDocument.getOleEResourceVariantTitleList());
                }
                if (tempDocument.getEresNotes().size() > 0) {
                    getBusinessObjectService().delete(tempDocument.getEresNotes());
                }
                if (tempDocument.getOleERSLicenseRequests().size() > 0) {
                    getBusinessObjectService().delete(tempDocument.getOleERSLicenseRequests());
                }
                if (tempDocument.getOleERSEventLogs().size() > 0) {
                    getBusinessObjectService().delete(tempDocument.getOleERSEventLogs());
                }
                if (tempDocument.getOleERSPOItems().size() > 0) {
                    getBusinessObjectService().delete(tempDocument.getOleERSPOItems());
                }
                if (tempDocument.getOleERSInstances().size() > 0) {
                    getBusinessObjectService().delete(tempDocument.getOleERSInstances());
                }
                if (tempDocument.getOleERSInvoices().size() > 0) {
                    getBusinessObjectService().delete(tempDocument.getOleERSInvoices());
                }
                if (tempDocument.getAccountingLines().size() > 0) {
                    getBusinessObjectService().delete(tempDocument.getAccountingLines());
                }
            }
        }
        return oleERSDoc;
    }

   /* public List<WorkBibDocument> getWorkBibDocuments(List<String> instanceIdsList, String docType) {
        List<LinkedHashMap<String, String>> instanceIdMapList = new ArrayList<LinkedHashMap<String, String>>();
        for (String instanceId : instanceIdsList) {
            LinkedHashMap<String, String> instanceIdMap = new LinkedHashMap<String, String>();
            instanceIdMap.put(docType, instanceId);
            instanceIdMapList.add(instanceIdMap);
        }
        QueryService queryService = QueryServiceImpl.getInstance();
        List<WorkBibDocument> workBibDocuments = new ArrayList<WorkBibDocument>();
        try {
            workBibDocuments = queryService.getWorkBibRecords(instanceIdMapList);
        } catch (Exception ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
        return workBibDocuments;
    }*/

    public void getDefaultCovergeDate(OLEEResourceRecordDocument oleERSDoc) {
        String defaultCoverage = oleERSDoc.getDefaultCoverage();
        String startCov = "";
        String endCov = "";
        String dummyStartCov = "";
        String dummyEndCov = "";
        String separator = getParameter(OLEConstants.OLEEResourceRecord.COMMA_SEPARATOR);
        String dateSeparator = getParameter(OLEConstants.OLEEResourceRecord.COVERAGE_DATE_SEPARATOR);
        if (StringUtils.isNotEmpty(defaultCoverage)) {
            if (defaultCoverage.contains(separator)) {
                String[] defaultCoverageDates = defaultCoverage.split(dateSeparator);
                if (defaultCoverageDates[0].contains(separator)) {
                    String[] startCoverage = defaultCoverageDates[0].split(separator);
                    if (startCoverage.length > 0) {
                        if (startCoverage.length > 0 && !startCoverage[0].isEmpty()) {
                            startCov += OLEConstants.OLEEResourceRecord.SPACE + OLEConstants.OLEEResourceRecord.DEFAULT_DATE_VOL;
                            startCov += startCoverage[0];
                            startCov += separator;
                            dummyStartCov += OLEConstants.OLEEResourceRecord.DEFAULT_DATE_VOL + startCoverage[0] + separator;
                        } else {
                            dummyStartCov += separator;
                        }
                        if (startCoverage.length > 1 && !startCoverage[1].isEmpty()) {
                            startCov += OLEConstants.OLEEResourceRecord.SPACE + OLEConstants.OLEEResourceRecord.DEFAULT_DATE_ISSUE;
                            startCov += startCoverage[1];
                            startCov += separator;
                            dummyStartCov += OLEConstants.OLEEResourceRecord.DEFAULT_DATE_ISSUE + startCoverage[1] + separator;
                        } else {
                            dummyStartCov += separator;
                        }
                        if (startCoverage.length > 2 && !startCoverage[2].isEmpty()) {
                            startCov += OLEConstants.OLEEResourceRecord.SPACE + startCoverage[2] + OLEConstants.OLEEResourceRecord.SPACE;
                            startCov += separator;
                            dummyStartCov += startCoverage[2] + separator;
                        } else {
                            dummyStartCov += separator;
                        }
                        /*for (String covDate : startCoverage) {
                            if (covDate != null && !covDate.isEmpty()) {
                                startCov += covDate;

                            }
                        }*/
                        startCov = startCov.substring(0, startCov.lastIndexOf(separator));
                    } else {
                        startCov = "";
                    }
                }
                if (defaultCoverageDates[1].contains(separator)) {
                    String[] endCoverage = defaultCoverageDates[1].split(separator);
                    if (endCoverage.length > 0) {
                        if (endCoverage.length > 0 && !endCoverage[0].isEmpty()) {
                            endCov += OLEConstants.OLEEResourceRecord.SPACE + OLEConstants.OLEEResourceRecord.DEFAULT_DATE_VOL;
                            endCov += endCoverage[0];
                            endCov += separator;
                            dummyEndCov += OLEConstants.OLEEResourceRecord.DEFAULT_DATE_VOL + endCoverage[0] + separator;
                        } else {
                            dummyEndCov += separator;
                        }
                        if (endCoverage.length > 1 && !endCoverage[1].isEmpty()) {
                            endCov += OLEConstants.OLEEResourceRecord.SPACE + OLEConstants.OLEEResourceRecord.DEFAULT_DATE_ISSUE;
                            endCov += endCoverage[1];
                            endCov += separator;
                            dummyEndCov += OLEConstants.OLEEResourceRecord.DEFAULT_DATE_ISSUE + endCoverage[1] + separator;
                        } else {
                            dummyEndCov += separator;
                        }
                        if (endCoverage.length > 2 && !endCoverage[2].isEmpty()) {
                            endCov += OLEConstants.OLEEResourceRecord.SPACE + endCoverage[2];
                            endCov += separator;
                            dummyEndCov += endCoverage[2] + separator;
                        } else {
                            dummyEndCov += separator;
                        }
                        /*for (String endDate : endCoverage) {
                            if (endDate != null && !endDate.isEmpty()) {
                                endCov += endDate;
                                endCov += getParameter(OLEConstants.OLEEResourceRecord.DEFAULT_COVERAGE_SEPARATOR);
                            }
                        }*/
                        endCov = endCov.substring(0, endCov.lastIndexOf(separator));
                    } else {
                        endCov = "";
                    }
                }
            }
            if ((endCov != null && !endCov.isEmpty()) && (startCov != null && !startCov.isEmpty())) {
                oleERSDoc.setDefaultCoverageView(startCov + dateSeparator + endCov);
                oleERSDoc.setDummyDefaultCoverage(dummyStartCov + dateSeparator + dummyEndCov);
            } else if (startCov != null && !startCov.isEmpty()) {
                oleERSDoc.setDefaultCoverageView(startCov);
                oleERSDoc.setDummyDefaultCoverage(dummyStartCov);
            } else if (endCov != null && !endCov.isEmpty()) {
                oleERSDoc.setDefaultCoverageView(endCov);
                oleERSDoc.setDummyDefaultCoverage(dummyEndCov);
            } else {
                oleERSDoc.setDefaultCoverageView(null);
                oleERSDoc.setDummyDefaultCoverage(null);
            }
        }
    }

    public void getDefaultPerpetualAccessDate(OLEEResourceRecordDocument oleERSDoc) {
        String defaultPerpetualAcc = oleERSDoc.getDefaultPerpetualAccess();
        String startPerAcc = "";
        String endPerAcc = "";
        String dummyStartPerAcc = "";
        String dummyEndPerAcc = "";
        String separator = getParameter(OLEConstants.OLEEResourceRecord.COMMA_SEPARATOR);
        String dateSeparator = getParameter(OLEConstants.OLEEResourceRecord.PERPETUAL_ACCESS_DATE_SEPARATOR);
        if (StringUtils.isNotEmpty(defaultPerpetualAcc)) {
            if (defaultPerpetualAcc.contains(dateSeparator)) {
                String[] defaultPerAccDates = defaultPerpetualAcc.split(dateSeparator);
                if (defaultPerAccDates[0].contains(separator)) {
                    String[] startPerpetualAccess = defaultPerAccDates[0].split(separator);
                    if (startPerpetualAccess.length > 0) {
                        if (startPerpetualAccess.length > 0 && !startPerpetualAccess[0].isEmpty()) {
                            startPerAcc += OLEConstants.OLEEResourceRecord.SPACE + OLEConstants.OLEEResourceRecord.DEFAULT_DATE_VOL;
                            startPerAcc += startPerpetualAccess[0];
                            startPerAcc += separator;
                            dummyStartPerAcc += OLEConstants.OLEEResourceRecord.DEFAULT_DATE_VOL + startPerpetualAccess[0] + separator;
                        } else {
                            dummyStartPerAcc += separator;
                        }
                        if (startPerpetualAccess.length > 1 && !startPerpetualAccess[1].isEmpty()) {
                            startPerAcc += OLEConstants.OLEEResourceRecord.SPACE + OLEConstants.OLEEResourceRecord.DEFAULT_DATE_ISSUE;
                            startPerAcc += startPerpetualAccess[1];
                            startPerAcc += separator;
                            dummyStartPerAcc += OLEConstants.OLEEResourceRecord.DEFAULT_DATE_ISSUE + startPerpetualAccess[1] + separator;
                        } else {
                            dummyStartPerAcc += separator;
                        }
                        if (startPerpetualAccess.length > 2 && !startPerpetualAccess[2].isEmpty()) {
                            startPerAcc += OLEConstants.OLEEResourceRecord.SPACE + startPerpetualAccess[2] + OLEConstants.OLEEResourceRecord.SPACE;
                            startPerAcc += separator;
                            dummyStartPerAcc += startPerpetualAccess[2] + separator;
                        } else {
                            dummyStartPerAcc += separator;
                        }
                        /*for (String perAccDate : startPerpetualAccess) {
                            if (perAccDate != null && !perAccDate.isEmpty()) {
                                startPerAcc += perAccDate;
                                startPerAcc += getParameter(OLEConstants.OLEEResourceRecord.DEFAULT_PERPETUAL_ACCESS_SEPARATOR);
                            }
                        }*/
                        startPerAcc = startPerAcc.substring(0, startPerAcc.lastIndexOf(separator));
                    } else {
                        startPerAcc = "";
                    }
                }
                if (defaultPerAccDates[1].contains(separator)) {
                    String[] endPerpetualAcc = defaultPerAccDates[1].split(separator);
                    if (endPerpetualAcc.length > 0) {
                        if (endPerpetualAcc.length > 0 && !endPerpetualAcc[0].isEmpty()) {
                            endPerAcc += OLEConstants.OLEEResourceRecord.SPACE + OLEConstants.OLEEResourceRecord.DEFAULT_DATE_VOL;
                            endPerAcc += endPerpetualAcc[0];
                            endPerAcc += separator;
                            dummyEndPerAcc += OLEConstants.OLEEResourceRecord.DEFAULT_DATE_VOL + endPerpetualAcc[0] + separator;
                        } else {
                            dummyEndPerAcc += separator;
                        }
                        if (endPerpetualAcc.length > 1 && !endPerpetualAcc[1].isEmpty()) {
                            endPerAcc += OLEConstants.OLEEResourceRecord.SPACE + OLEConstants.OLEEResourceRecord.DEFAULT_DATE_ISSUE;
                            endPerAcc += endPerpetualAcc[1];
                            endPerAcc += separator;
                            dummyEndPerAcc += OLEConstants.OLEEResourceRecord.DEFAULT_DATE_ISSUE + endPerpetualAcc[1] + separator;
                        } else {
                            dummyEndPerAcc += separator;
                        }
                        if (endPerpetualAcc.length > 2 && !endPerpetualAcc[2].isEmpty()) {
                            endPerAcc += OLEConstants.OLEEResourceRecord.SPACE + endPerpetualAcc[2];
                            endPerAcc += separator;
                            dummyEndPerAcc += endPerpetualAcc[2] + separator;
                        } else {
                            dummyEndPerAcc += separator;
                        }
                        /*for (String perAccDate : endPerpetualAcc) {
                            if (perAccDate != null && !perAccDate.isEmpty()) {
                                endPerAcc += perAccDate;
                                endPerAcc += getParameter(OLEConstants.OLEEResourceRecord.DEFAULT_PERPETUAL_ACCESS_SEPARATOR);
                            }
                        }*/
                        endPerAcc = endPerAcc.substring(0, endPerAcc.lastIndexOf(separator));
                    } else {
                        endPerAcc = "";
                    }
                }
            }
            if ((endPerAcc != null && !endPerAcc.isEmpty()) && (startPerAcc != null && !startPerAcc.isEmpty())) {
                oleERSDoc.setDefaultPerpetualAccessView(startPerAcc + dateSeparator + endPerAcc);
                oleERSDoc.setDummyDefaultPerpetualAccess(dummyStartPerAcc + dateSeparator + dummyEndPerAcc);
            } else if (startPerAcc != null && !startPerAcc.isEmpty()) {
                oleERSDoc.setDefaultPerpetualAccessView(startPerAcc);
                oleERSDoc.setDummyDefaultPerpetualAccess(dummyStartPerAcc);
            } else if (endPerAcc != null && !endPerAcc.isEmpty()) {
                oleERSDoc.setDefaultPerpetualAccessView(endPerAcc);
                oleERSDoc.setDummyDefaultPerpetualAccess(dummyEndPerAcc);
            } else {
                oleERSDoc.setDefaultPerpetualAccessView(null);
                oleERSDoc.setDummyDefaultPerpetualAccess(null);
            }

        }
    }

    public OLEEResourceRecordDocument saveDefaultCoverageDate(OLEEResourceRecordDocument oleeResourceRecordDocument) {
        StringBuffer defaultCoverageDate = new StringBuffer();
        OLEEResourceInstance oleERSInstance = oleeResourceRecordDocument.getOleERSInstance();
        String separator = getParameter(OLEConstants.OLEEResourceRecord.COMMA_SEPARATOR);
        if (oleERSInstance != null && oleERSInstance.getCovStartDate() != null) {
            defaultCoverageDate.append(StringUtils.isNotEmpty(oleERSInstance.getCovStartVolume()) ? oleERSInstance.getCovStartVolume() + separator : separator);
            defaultCoverageDate.append(StringUtils.isNotEmpty(oleERSInstance.getCovStartIssue()) ? oleERSInstance.getCovStartIssue() + separator : separator);
            defaultCoverageDate.append(StringUtils.isNotEmpty(oleERSInstance.getCovStartDate()) ? oleERSInstance.getCovStartDate() : "");
            if (defaultCoverageDate.length() > 0) {
                defaultCoverageDate.append(getParameter(OLEConstants.OLEEResourceRecord.COVERAGE_DATE_SEPARATOR));
            }
            defaultCoverageDate.append(StringUtils.isNotEmpty(oleERSInstance.getCovEndVolume()) ? oleERSInstance.getCovEndVolume() + separator : separator);
            defaultCoverageDate.append(StringUtils.isNotEmpty(oleERSInstance.getCovEndIssue()) ? oleERSInstance.getCovEndIssue() + separator : separator);
            defaultCoverageDate.append(StringUtils.isNotEmpty(oleERSInstance.getCovEndDate()) ? oleERSInstance.getCovEndDate() : "");
            if (StringUtils.isNotEmpty(defaultCoverageDate)) {
                oleeResourceRecordDocument.setDefaultCoverage(defaultCoverageDate.toString());
            }
        }
        return oleeResourceRecordDocument;
    }

    public OLEEResourceRecordDocument saveDefaultPerpetualAccessDate(OLEEResourceRecordDocument oleeResourceRecordDocument) {
        StringBuffer defaultPerpetualDate = new StringBuffer();
        OLEEResourceInstance oleERSInstance = oleeResourceRecordDocument.getOleERSInstance();
        String separator = getParameter(OLEConstants.OLEEResourceRecord.COMMA_SEPARATOR);
        if (oleERSInstance != null && oleERSInstance.getPerpetualAccStartDate() != null) {
            defaultPerpetualDate.append(StringUtils.isNotEmpty(oleERSInstance.getPerpetualAccStartVolume()) ? oleERSInstance.getPerpetualAccStartVolume() + separator : separator);
            defaultPerpetualDate.append(StringUtils.isNotEmpty(oleERSInstance.getPerpetualAccStartIssue()) ? oleERSInstance.getPerpetualAccStartIssue() + separator : separator);
            defaultPerpetualDate.append(StringUtils.isNotEmpty(oleERSInstance.getPerpetualAccStartDate()) ? oleERSInstance.getPerpetualAccStartDate() : "");
            if (defaultPerpetualDate.length() > 0) {
                defaultPerpetualDate.append(getParameter(OLEConstants.OLEEResourceRecord.PERPETUAL_ACCESS_DATE_SEPARATOR));
            }
            defaultPerpetualDate.append(StringUtils.isNotEmpty(oleERSInstance.getPerpetualAccEndVolume()) ? oleERSInstance.getPerpetualAccEndVolume() + separator : separator);
            defaultPerpetualDate.append(StringUtils.isNotEmpty(oleERSInstance.getPerpetualAccEndIssue()) ? oleERSInstance.getPerpetualAccEndIssue() + separator : separator);
            defaultPerpetualDate.append(StringUtils.isNotEmpty(oleERSInstance.getPerpetualAccEndDate()) ? oleERSInstance.getPerpetualAccEndDate() : "");
            if (StringUtils.isNotEmpty(defaultPerpetualDate)) {
                oleeResourceRecordDocument.setDefaultPerpetualAccess(defaultPerpetualDate.toString());
            }
        }
        return oleeResourceRecordDocument;
    }

    public void getNewInstance(OLEEResourceRecordDocument oleERSDoc, String documentNumber) throws Exception {

        if (OleDocstoreResponse.getInstance().getEditorResponse() != null) {
            HashMap<String, OLEEditorResponse> oleEditorResponses = OleDocstoreResponse.getInstance().getEditorResponse();
            OLEEditorResponse oleEditorResponse = oleEditorResponses.get(documentNumber);
            String separator = getParameter(OLEConstants.OLEEResourceRecord.COMMA_SEPARATOR);
            String isbnAndissn = "";
            List<String> instanceId = new ArrayList<String>();
            List<OLEEResourceInstance> oleeResourceInstances = oleERSDoc.getOleERSInstances();
            if (oleeResourceInstances.size() == 0) {
                oleeResourceInstances = new ArrayList<OLEEResourceInstance>();
            }
            // List<OleCopy> copyList = new ArrayList<>();
            //getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(oleEditorResponse.getBib().getId());

            if (oleEditorResponse != null && StringUtils.isNotEmpty(oleEditorResponse.getLinkedInstanceId())) {
                instanceId.add(oleEditorResponse.getLinkedInstanceId());
            }
            Holdings holdings = null;
            if (oleEditorResponse != null && oleERSDoc.getSelectInstance() != null && (oleERSDoc.getSelectInstance().equals(OLEConstants.OLEEResourceRecord.LINK_EXIST_INSTANCE)) || oleERSDoc.getSelectInstance().equals(OLEConstants.OLEEResourceRecord.CREATE_NEW_INSTANCE)) {
                holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(oleEditorResponse.getLinkedInstanceId());

            }
            int index = -1;
            if (holdings != null && holdings.getId() != null) {
                HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
                OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
                if (holdings instanceof org.kuali.ole.docstore.common.document.EHoldings) {
                    if (oleEditorResponse != null && oleEditorResponse.getLinkedInstanceId().equalsIgnoreCase(holdings.getId())) {
                        OLEEResourceInstance oleeResourceInstance = new OLEEResourceInstance();
                        if (oleERSDoc.getOleERSInstances() != null && oleERSDoc.getOleERSInstances().size() > 0) {
                            for (OLEEResourceInstance eResourceInstance : oleeResourceInstances) {
                                if (eResourceInstance.getInstanceId().equals(oleEditorResponse.getLinkedInstanceId())) {
                                    index = oleeResourceInstances.indexOf(eResourceInstance);
                                    oleeResourceInstance = eResourceInstance;
                                }
                            }
                        }
                        oleeResourceInstance.setSubscriptionStatus(oleHoldings.getSubscriptionStatus());
                        oleeResourceInstance.setInstanceTitle(holdings.getBib().getTitle());
                        buildInstanceHolding(oleeResourceInstance, oleHoldings, oleERSDoc);
                        oleeResourceInstance.setInstancePublisher(oleHoldings.getPublisher());
                        if (oleHoldings.getPlatform() != null && StringUtils.isNotBlank(oleHoldings.getPlatform().getPlatformName())) {
                            oleeResourceInstance.setPlatformId(oleHoldings.getPlatform().getPlatformName());
                        }else {
                            oleeResourceInstance.setPlatformId(null);
                        }
                        // oleeResourceInstance.setPublicDisplayNote(workEInstanceDocument.getPublicDisplayNote());
                        StringBuffer urls = new StringBuffer();
                        for(Link link :oleHoldings.getLink()){
                            urls.append(link.getUrl());
                            urls.append(",");
                        }
                        if(urls.toString().contains(",")){
                            String url = urls.substring(0,urls.lastIndexOf(","));
                            oleeResourceInstance.setUrl(url);
                        }

                        SearchParams searchParams = new SearchParams();
                        searchParams.getSearchConditions().add(searchParams.buildSearchCondition(null, searchParams.buildSearchField(OLEConstants.BIB_DOC_TYPE, OLEConstants.BIB_SEARCH, holdings.getBib().getId()), null));
                        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(OLEConstants.BIB_DOC_TYPE, OLEConstants.OLEEResourceRecord.ERESOURCE_ISBN));
                        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(OLEConstants.BIB_DOC_TYPE, OLEConstants.OLEEResourceRecord.ERESOURCE_ISSN));
                        SearchResponse searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                        SearchResult searchResult;
                        if (searchResponse.getSearchResults().size() > 0) {
                            searchResult = searchResponse.getSearchResults().get(0);
                            searchResult.getSearchResultFields();
                            for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                                isbnAndissn += searchResultField.getFieldValue();
                                isbnAndissn += separator;
                            }
                        }
                        if (StringUtils.isNotEmpty(isbnAndissn)) {
                            isbnAndissn = isbnAndissn.substring(0, isbnAndissn.lastIndexOf(separator));
                        }
                        oleeResourceInstance.setOleERSIdentifier(oleERSDoc.getOleERSIdentifier());
                        oleeResourceInstance.setIsbn(isbnAndissn);
                        oleeResourceInstance.setStatus(oleHoldings.getAccessStatus());
                        oleeResourceInstance.setSubscriptionStatus(oleHoldings.getSubscriptionStatus());
                        oleeResourceInstance.setBibId(holdings.getBib().getId());
                        oleeResourceInstance.setInstanceId(holdings.getId());
                        oleeResourceInstance.setInstanceFlag("false");
                        if (index >= 0) {
                            oleeResourceInstances.add(index, oleeResourceInstance);
                        } else {
                            oleeResourceInstances.add(oleeResourceInstance);
                        }
                        updateEResInOleCopy(holdings, oleERSDoc);
                    }
                }
                if (holdings instanceof org.kuali.ole.docstore.common.document.PHoldings) {
                    if (oleEditorResponse != null && oleEditorResponse.getLinkedInstanceId().equalsIgnoreCase(holdings.getId())) {
                        OLEEResourceInstance oleeResourceInstance = new OLEEResourceInstance();
                        if (oleERSDoc.getOleERSInstances() != null && oleERSDoc.getOleERSInstances().size() > 0) {
                            for (OLEEResourceInstance eResourceInstance : oleeResourceInstances) {
                                if (eResourceInstance.getInstanceId().equals(oleEditorResponse.getLinkedInstanceId())) {
                                    index = oleeResourceInstances.indexOf(eResourceInstance);
                                    oleeResourceInstance = eResourceInstance;
                                }
                            }
                        }
                        oleeResourceInstance.setOleERSIdentifier(oleERSDoc.getOleERSIdentifier());
                        oleeResourceInstance.setSubscriptionStatus(oleHoldings.getSubscriptionStatus());
                        oleeResourceInstance.setInstanceTitle(holdings.getBib().getTitle());
                        oleeResourceInstance.setInstancePublisher(holdings.getBib().getPublisher());
                        SearchParams searchParams = new SearchParams();
                        searchParams.getSearchConditions().add(searchParams.buildSearchCondition(null, searchParams.buildSearchField(OLEConstants.BIB_DOC_TYPE, OLEConstants.BIB_SEARCH, holdings.getBib().getId()), null));
                        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(OLEConstants.BIB_DOC_TYPE, OLEConstants.OLEEResourceRecord.ERESOURCE_ISBN));
                        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(OLEConstants.BIB_DOC_TYPE, OLEConstants.OLEEResourceRecord.ERESOURCE_ISSN));
                        SearchResponse searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                        SearchResult searchResult;
                        if (searchResponse.getSearchResults().size() > 0) {
                            searchResult = searchResponse.getSearchResults().get(0);
                            searchResult.getSearchResultFields();
                            for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                                isbnAndissn += searchResultField.getFieldValue();
                                isbnAndissn += separator;
                            }
                        }
                        if (StringUtils.isNotEmpty(isbnAndissn)) {
                            isbnAndissn = isbnAndissn.substring(0, isbnAndissn.lastIndexOf(separator));
                        }
                        oleeResourceInstance.setIsbn(isbnAndissn);
                        oleeResourceInstance.setBibId(holdings.getBib().getId());
                        oleeResourceInstance.setInstanceId(holdings.getId());
                        oleeResourceInstance.setHoldingsId(oleHoldings.getHoldingsIdentifier());
                        oleeResourceInstance.setInstanceFlag("true");
                        if (index >= 0) {
                            oleeResourceInstances.add(index, oleeResourceInstance);
                        } else {
                            oleeResourceInstances.add(oleeResourceInstance);
                        }
                        updateEResInOleCopy(holdings, oleERSDoc);
                    }
                }
            }
            oleERSDoc.setOleERSInstances(oleeResourceInstances);
            OleDocstoreResponse.getInstance().setEditorResponse(null);
        }
    }

    public void getNewInstance(OLEEResourceRecordDocument oleERSDoc, String documentNumber,Holdings holdings) throws Exception {

        if (OleDocstoreResponse.getInstance().getEditorResponse() != null) {
            HashMap<String, OLEEditorResponse> oleEditorResponses = OleDocstoreResponse.getInstance().getEditorResponse();
            OLEEditorResponse oleEditorResponse = oleEditorResponses.get(documentNumber);
            String separator = getParameter(OLEConstants.OLEEResourceRecord.COMMA_SEPARATOR);
            String isbnAndissn = "";
            List<String> instanceId = new ArrayList<String>();
            List<OLEEResourceInstance> oleeResourceInstances = oleERSDoc.getOleERSInstances();
            if (oleeResourceInstances.size() == 0) {
                oleeResourceInstances = new ArrayList<OLEEResourceInstance>();
            }
            // List<OleCopy> copyList = new ArrayList<>();
            //getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(oleEditorResponse.getBib().getId());

            if (oleEditorResponse != null && StringUtils.isNotEmpty(oleEditorResponse.getLinkedInstanceId())) {
                instanceId.add(oleEditorResponse.getLinkedInstanceId());
            }
            //Holdings holdings = null;
/*            if (oleEditorResponse != null && oleERSDoc.getSelectInstance() != null && (oleERSDoc.getSelectInstance().equals(OLEConstants.OLEEResourceRecord.LINK_EXIST_INSTANCE)) || oleERSDoc.getSelectInstance().equals(OLEConstants.OLEEResourceRecord.CREATE_NEW_INSTANCE)) {
                holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(oleEditorResponse.getLinkedInstanceId());

            }*/
            int index = -1;
            if (holdings != null && holdings.getId() != null) {
                HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
                OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
                if (holdings instanceof org.kuali.ole.docstore.common.document.EHoldings) {
                    if (oleEditorResponse != null && oleEditorResponse.getLinkedInstanceId().equalsIgnoreCase(holdings.getId())) {
                        OLEEResourceInstance oleeResourceInstance = new OLEEResourceInstance();
                        if (oleERSDoc.getOleERSInstances() != null && oleERSDoc.getOleERSInstances().size() > 0) {
                            for (OLEEResourceInstance eResourceInstance : oleeResourceInstances) {
                                if (eResourceInstance.getInstanceId().equals(oleEditorResponse.getLinkedInstanceId())) {
                                    index = oleeResourceInstances.indexOf(eResourceInstance);
                                    oleeResourceInstance = eResourceInstance;
                                }
                            }
                        }
                        oleeResourceInstance.setSubscriptionStatus(oleHoldings.getSubscriptionStatus());
                        oleeResourceInstance.setInstanceTitle(holdings.getBib().getTitle());
                        buildInstanceHolding(oleeResourceInstance, oleHoldings, oleERSDoc);
                        oleeResourceInstance.setInstancePublisher(oleHoldings.getPublisher());
                        if (oleHoldings.getPlatform() != null && StringUtils.isNotBlank(oleHoldings.getPlatform().getPlatformName())) {
                            oleeResourceInstance.setPlatformId(oleHoldings.getPlatform().getPlatformName());
                        }else {
                            oleeResourceInstance.setPlatformId(null);
                        }
                        // oleeResourceInstance.setPublicDisplayNote(workEInstanceDocument.getPublicDisplayNote());
                        StringBuffer urls = new StringBuffer();
                        for(Link link :oleHoldings.getLink()){
                            urls.append(link.getUrl());
                            urls.append(",");
                        }
                        if(urls.toString().contains(",")){
                            String url = urls.substring(0,urls.lastIndexOf(","));
                            oleeResourceInstance.setUrl(url);
                        }

                        SearchParams searchParams = new SearchParams();
                        searchParams.getSearchConditions().add(searchParams.buildSearchCondition(null, searchParams.buildSearchField(OLEConstants.BIB_DOC_TYPE, OLEConstants.BIB_SEARCH, holdings.getBib().getId()), null));
                        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(OLEConstants.BIB_DOC_TYPE, OLEConstants.OLEEResourceRecord.ERESOURCE_ISBN));
                        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(OLEConstants.BIB_DOC_TYPE, OLEConstants.OLEEResourceRecord.ERESOURCE_ISSN));
                        SearchResponse searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                        SearchResult searchResult;
                        if (searchResponse.getSearchResults().size() > 0) {
                            searchResult = searchResponse.getSearchResults().get(0);
                            searchResult.getSearchResultFields();
                            for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                                isbnAndissn += searchResultField.getFieldValue();
                                isbnAndissn += separator;
                            }
                        }
                        if (StringUtils.isNotEmpty(isbnAndissn)) {
                            isbnAndissn = isbnAndissn.substring(0, isbnAndissn.lastIndexOf(separator));
                        }
                        oleeResourceInstance.setIsbn(isbnAndissn);
                        oleeResourceInstance.setStatus(oleHoldings.getAccessStatus());
                        oleeResourceInstance.setSubscriptionStatus(oleHoldings.getSubscriptionStatus());
                        oleeResourceInstance.setBibId(holdings.getBib().getId());
                        oleeResourceInstance.setInstanceId(holdings.getId());
                        oleeResourceInstance.setInstanceFlag("false");
                        oleeResourceInstance.setOleERSIdentifier(oleERSDoc.getOleERSIdentifier());
                        if (index >= 0) {
                            oleeResourceInstances.add(index, oleeResourceInstance);
                        } else {
                            oleeResourceInstances.add(oleeResourceInstance);
                        }
                        updateEResInOleCopy(holdings, oleERSDoc);
                    }
                }
                if (holdings instanceof org.kuali.ole.docstore.common.document.PHoldings) {
                    if (oleEditorResponse != null && oleEditorResponse.getLinkedInstanceId().equalsIgnoreCase(holdings.getId())) {
                        OLEEResourceInstance oleeResourceInstance = new OLEEResourceInstance();
                        if (oleERSDoc.getOleERSInstances() != null && oleERSDoc.getOleERSInstances().size() > 0) {
                            for (OLEEResourceInstance eResourceInstance : oleeResourceInstances) {
                                if (eResourceInstance.getInstanceId().equals(oleEditorResponse.getLinkedInstanceId())) {
                                    index = oleeResourceInstances.indexOf(eResourceInstance);
                                    oleeResourceInstance = eResourceInstance;
                                }
                            }
                        }
                        oleeResourceInstance.setOleERSIdentifier(oleERSDoc.getOleERSIdentifier());
                        oleeResourceInstance.setSubscriptionStatus(oleHoldings.getSubscriptionStatus());
                        oleeResourceInstance.setInstanceTitle(holdings.getBib().getTitle());
                        oleeResourceInstance.setInstancePublisher(holdings.getBib().getPublisher());
                        SearchParams searchParams = new SearchParams();
                        searchParams.getSearchConditions().add(searchParams.buildSearchCondition(null, searchParams.buildSearchField(OLEConstants.BIB_DOC_TYPE, OLEConstants.BIB_SEARCH, holdings.getBib().getId()), null));
                        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(OLEConstants.BIB_DOC_TYPE, OLEConstants.OLEEResourceRecord.ERESOURCE_ISBN));
                        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(OLEConstants.BIB_DOC_TYPE, OLEConstants.OLEEResourceRecord.ERESOURCE_ISSN));
                        SearchResponse searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                        SearchResult searchResult;
                        if (searchResponse.getSearchResults().size() > 0) {
                            searchResult = searchResponse.getSearchResults().get(0);
                            searchResult.getSearchResultFields();
                            for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                                isbnAndissn += searchResultField.getFieldValue();
                                isbnAndissn += separator;
                            }
                        }
                        if (StringUtils.isNotEmpty(isbnAndissn)) {
                            isbnAndissn = isbnAndissn.substring(0, isbnAndissn.lastIndexOf(separator));
                        }
                        oleeResourceInstance.setIsbn(isbnAndissn);
                        oleeResourceInstance.setBibId(holdings.getBib().getId());
                        oleeResourceInstance.setInstanceId(holdings.getId());
                        oleeResourceInstance.setHoldingsId(oleHoldings.getHoldingsIdentifier());
                        oleeResourceInstance.setInstanceFlag("true");
                        if (index >= 0) {
                            oleeResourceInstances.add(index, oleeResourceInstance);
                        } else {
                            oleeResourceInstances.add(oleeResourceInstance);
                        }
                        updateEResInOleCopy(holdings, oleERSDoc);
                    }
                }
            }
            oleERSDoc.setOleERSInstances(oleeResourceInstances);
            OleDocstoreResponse.getInstance().setEditorResponse(null);
        }
    }

    public void getPoForERS(OLEEResourceRecordDocument oleERSDoc) {
        try {
            Holdings holdings = null;
            Set<String> holdingsId = new TreeSet<>();
            List<OLEEResourcePO> oleeResourcePOs = new ArrayList<>();
            List<OLEEResourcePO> oleLinkedeResourcePOs = new ArrayList<>();
            List<OleCopy> copies =  null;
            if(oleERSDoc.getOleERSIdentifier() != null) {
                copies=  getCopies(oleERSDoc.getOleERSIdentifier(), OLEConstants.OLEEResourceRecord.ERESOURCE_IDENTIFIER,"E-Resource");
                for (OleCopy copy : copies) {
                    if (copy.getPoItemId() != null) {
                        getPOFromCopy(oleERSDoc.getTitle(), copy, oleeResourcePOs);
                    }
                }
            }
            for (OLELinkedEresource linkedEresource : oleERSDoc.getOleLinkedEresources()) {
                if (!"parent".equalsIgnoreCase(linkedEresource.getRelationShipType())) {
                    copies = getCopies(linkedEresource.getLinkedERSIdentifier(), OLEConstants.OLEEResourceRecord.ERESOURCE_IDENTIFIER,"E-Resource");
                    Map<String, String> oleResourceMap = new HashMap<String, String>();
                    oleResourceMap.put(OLEConstants.OLEEResourceRecord.ERESOURCE_IDENTIFIER, linkedEresource.getLinkedERSIdentifier());
                    List<OLEEResourceRecordDocument> oleeResourceRecordDocumentList = (List<OLEEResourceRecordDocument>) getBusinessObjectService().findMatching(OLEEResourceRecordDocument.class, oleResourceMap);
                    linkedEresource.setOleeResourceRecordDocument(oleeResourceRecordDocumentList.get(0));
                    for (OleCopy copy : copies) {
                        if (copy.getPoItemId() != null) {
                            getPOFromCopy(linkedEresource.getOleeResourceRecordDocument().getTitle(), copy, oleLinkedeResourcePOs);
                            oleeResourcePOs.addAll(oleLinkedeResourcePOs);
                        }
                    }
                }
            }
            for (OLEEResourceInstance oleeResourceInstance : oleERSDoc.getOleERSInstances()) {
                copies = getCopies(oleeResourceInstance.getInstanceId(), OLEConstants.INSTANCE_ID,"");
                for (OleCopy copy : copies) {
                    if (copy.getPoItemId() != null) {
                        if (holdingsId.add(oleeResourceInstance.getInstanceId())) {
                            holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(oleeResourceInstance.getInstanceId());
                        }
                        if (holdings != null) {
                            getPOFromCopy(holdings.getBib().getTitle(), copy, oleeResourcePOs);
                        }
                    }
                }
            }
            for (OLEEResourceInstance oleeResourceInstance : oleERSDoc.geteRSInstances()) {
                copies = getCopies(oleeResourceInstance.getInstanceId(), OLEConstants.INSTANCE_ID,"");
                for (OleCopy copy : copies) {
                    if (copy.getPoItemId() != null) {
                        if (holdingsId.add(oleeResourceInstance.getInstanceId())) {
                            holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(oleeResourceInstance.getInstanceId());
                        }
                        if (holdings != null) {
                            getPOFromCopy(holdings.getBib().getTitle(), copy, oleLinkedeResourcePOs);
                        }
                    }
                }
            }
            oleERSDoc.setOleERSPOItems(oleeResourcePOs);
            oleERSDoc.setLinkedERSPOItems(oleLinkedeResourcePOs);
            getPOAndInvoiceItemsWithoutDuplicate(oleERSDoc);
        } catch (Exception e) {
            LOG.error("Exception " + e);
        }
    }


    public void getInvoiceForERS(OLEEResourceRecordDocument oleERSDoc) {
        try {
            List<OleCopy> copies = null;
            List<OLEEResourceInvoices> oleeResourceInvoiceses = new ArrayList<>();

            if (oleERSDoc.getOleERSIdentifier() != null) {
                copies = getCopies(oleERSDoc.getOleERSIdentifier(), OLEConstants.OLEEResourceRecord.ERESOURCE_IDENTIFIER,"E-Resource");
                for (OleCopy copy : copies) {
                    if (copy.getPoItemId() != null) {
                        getInvoiceFromCopy(copy, oleeResourceInvoiceses);
                    }
                }
            }
            for (OLELinkedEresource linkedEresource : oleERSDoc.getOleLinkedEresources()) {
                if (!"parent".equalsIgnoreCase(linkedEresource.getRelationShipType())) {
                    copies = getCopies(linkedEresource.getLinkedERSIdentifier(), OLEConstants.OLEEResourceRecord.ERESOURCE_IDENTIFIER,"E-Resource");
                    Map<String, String> oleResourceMap = new HashMap<String, String>();
                    oleResourceMap.put(OLEConstants.OLEEResourceRecord.ERESOURCE_IDENTIFIER, linkedEresource.getLinkedERSIdentifier());
                    List<OLEEResourceRecordDocument> oleeResourceRecordDocumentList = (List<OLEEResourceRecordDocument>) getBusinessObjectService().findMatching(OLEEResourceRecordDocument.class, oleResourceMap);
                    linkedEresource.setOleeResourceRecordDocument(oleeResourceRecordDocumentList.get(0));
                    for (OleCopy copy : copies) {
                        if (copy.getPoItemId() != null) {
                            getInvoiceFromCopy(copy, oleeResourceInvoiceses);
                        }
                    }
                }
            }
            for (OLEEResourceInstance oleeResourceInstance : oleERSDoc.getOleERSInstances()) {
                copies = getCopies(oleeResourceInstance.getInstanceId(), OLEConstants.INSTANCE_ID,"");
                for (OleCopy copy : copies) {
                    if (copy.getPoItemId() != null) {
                        getInvoiceFromCopy(copy, oleeResourceInvoiceses);

                    }
                }
            }
            for (OLEEResourceInstance oleeResourceInstance : oleERSDoc.geteRSInstances()) {
                copies = getCopies(oleeResourceInstance.getInstanceId(), OLEConstants.INSTANCE_ID,"");
                for (OleCopy copy : copies) {
                    if (copy.getPoItemId() != null) {
                        getInvoiceFromCopy(copy, oleeResourceInvoiceses);

                    }
                }
            }
            oleERSDoc.setOleERSInvoices(oleeResourceInvoiceses);
            getPOAndInvoiceItemsWithoutDuplicate(oleERSDoc);
        } catch (Exception e) {
            LOG.error("Exception " + e);
        }
    }

    private void getInvoiceFromCopy(OleCopy copy, List<OLEEResourceInvoices> oleeResourceInvoiceses) {
        Map<String, String> criteriaInvIdMap = new HashMap<String, String>();
        criteriaInvIdMap.put(OLEConstants.OLEEResourceRecord.INV_PO_ITEM_ID, copy.getPoItemId().toString());
        List<OleInvoiceItem> oleInvoiceItems = (List<OleInvoiceItem>) getBusinessObjectService().findMatching(OleInvoiceItem.class, criteriaInvIdMap);
        if (oleInvoiceItems.size() > 0) {
            for (OleInvoiceItem oleInvoiceItem : oleInvoiceItems) {
                OLEEResourceInvoices oleEResInvoice = new OLEEResourceInvoices();
                oleEResInvoice.setInvoiceId(oleInvoiceItem.getItemIdentifier().toString());
                OleInvoiceDocument oleInvoiceDocument = (OleInvoiceDocument) oleInvoiceItem.getInvoiceDocument();
                if (oleInvoiceItem.getInvoiceDocument() != null) {
                    oleEResInvoice.setInvoiceNumber(oleInvoiceItem.getInvoiceDocument().getDocumentNumber());
                    oleEResInvoice.setInvoiceDate(oleInvoiceItem.getInvoiceDocument().getInvoiceDate());
                    oleEResInvoice.setVendorName(oleInvoiceItem.getInvoiceDocument().getVendorName());
                    if (SpringContext.getBean(DocumentHeaderService.class) != null) {
                        oleInvoiceDocument.setDocumentHeader(SpringContext.getBean(DocumentHeaderService.class).getDocumentHeaderById(oleInvoiceDocument.getDocumentNumber()));
                        oleEResInvoice.setInvoiceStatus(oleInvoiceDocument.getApplicationDocumentStatus());
                    }
                }
                oleEResInvoice.setInvoicedAmount(oleInvoiceItem.getExtendedPrice().toString());
                Map map = new HashMap();
                map.put(OLEConstants.OLEEResourceRecord.INV_PO_ITEM_ID, oleInvoiceItem.getItemIdentifier());
                OlePaymentRequestItem olePaymentRequestItem = getBusinessObjectService().findByPrimaryKey(OlePaymentRequestItem.class, map);
                if (olePaymentRequestItem != null) {
                    oleEResInvoice.setPaidDate(olePaymentRequestItem.getPaymentRequestDocument().getPaymentRequestPayDate());
                }
                //invoice note
                if (CollectionUtils.isNotEmpty(oleInvoiceItem.getNotes())) {
                    StringBuffer invoiceNotes = new StringBuffer();
                    for (OleInvoiceNote oleInvoiceNote : oleInvoiceItem.getNotes()) {
                        invoiceNotes.append(oleInvoiceNote.getNote());
                        invoiceNotes.append(OLEConstants.COMMA);
                        invoiceNotes.append(' ');
                    }
                    if (invoiceNotes.length() > 0) {
                        invoiceNotes.deleteCharAt(invoiceNotes.length() - 2);
                        oleEResInvoice.setInvoiceNote(invoiceNotes.toString());
                    }
                }
                oleEResInvoice.setVendorDetail(oleInvoiceDocument.getVendorDetail());

                //accounting lines
                if (oleInvoiceItem.getSourceAccountingLines() != null && oleInvoiceItem.getSourceAccountingLines().size() > 0) {
                    List<OLEEResourceInvoiceAccountingLine> oleeResourceInvoiceAccountingLineList = new ArrayList<>();
                    for (PurApAccountingLine accountingLine : oleInvoiceItem.getSourceAccountingLines()) {
                        OLEEResourceInvoiceAccountingLine oleeResourceInvoiceAccountingLine = new OLEEResourceInvoiceAccountingLine();
                        oleeResourceInvoiceAccountingLine.setChartOfAccountsCode(accountingLine.getChartOfAccountsCode());
                        oleeResourceInvoiceAccountingLine.setAccountNumber(accountingLine.getAccountNumber());
                        oleeResourceInvoiceAccountingLine.setSubAccountNumber(accountingLine.getSubAccountNumber());
                        oleeResourceInvoiceAccountingLine.setFinancialObjectCode(accountingLine.getFinancialObjectCode());
                        oleeResourceInvoiceAccountingLine.setFinancialSubObjectCode(accountingLine.getFinancialSubObjectCode());
                        oleeResourceInvoiceAccountingLine.setProjectCode(accountingLine.getProjectCode());
                        oleeResourceInvoiceAccountingLine.setOrganizationReferenceId(accountingLine.getOrganizationReferenceId());
                        oleeResourceInvoiceAccountingLine.setAmount(accountingLine.getAmount());
                        oleeResourceInvoiceAccountingLine.setAccountLinePercent(accountingLine.getAccountLinePercent());
                        oleeResourceInvoiceAccountingLineList.add(oleeResourceInvoiceAccountingLine);
                    }
                    oleEResInvoice.setAccountingLines(oleeResourceInvoiceAccountingLineList);
                }
                oleeResourceInvoiceses.add(oleEResInvoice);
            }
        }
    }

    public void getPOFromCopy(String title, OleCopy copy, List<OLEEResourcePO> oleeResourcePOs) {
        Map<String, String> criteriaPOIdMap = new HashMap<String, String>();
        criteriaPOIdMap.put(OLEConstants.OLEEResourceRecord.PO_ITEM_ID, copy.getPoItemId().toString());
        List<OlePurchaseOrderItem> olePurchaseOrderItems = (List<OlePurchaseOrderItem>) getBusinessObjectService().findMatching(OlePurchaseOrderItem.class, criteriaPOIdMap);
        if (olePurchaseOrderItems.size() > 0) {
            for (OlePurchaseOrderItem olePurchaseOrderItem : olePurchaseOrderItems) {
                if (olePurchaseOrderItem != null && olePurchaseOrderItem.isItemActiveIndicator()) {
                    Map map = new HashMap();
                    map.put(OLEConstants.DOC_NUM, olePurchaseOrderItem.getDocumentNumber());
                    OlePurchaseOrderDocument olePurchaseOrderDocument = getBusinessObjectService().findByPrimaryKey(OlePurchaseOrderDocument.class, map);
                    if (olePurchaseOrderDocument != null) {
                        DocumentHeader documentHeader = (FinancialSystemDocumentHeader) SpringContext.getBean(DocumentHeaderService.class).getDocumentHeaderById(olePurchaseOrderDocument.getDocumentNumber());
                        if (documentHeader != null) {
                            olePurchaseOrderDocument.setDocumentHeader(documentHeader);
                        }
                        if (olePurchaseOrderDocument.getApplicationDocumentStatus() != null && !olePurchaseOrderDocument.getApplicationDocumentStatus().equals("Retired Version")) {
                            OLEEResourcePO oleeResourcePO = new OLEEResourcePO();
                            oleeResourcePO.setTitle(title);
                            oleeResourcePO.setOlePOItemId(olePurchaseOrderDocument.getPurapDocumentIdentifier());
                            oleeResourcePO.setPoStatus(olePurchaseOrderDocument.getApplicationDocumentStatus());
                            if(StringUtils.isNotBlank(olePurchaseOrderItem.getPurposeId())) {
                                Map<String,String> purposeMap = new HashMap<>();
                                purposeMap.put(OLEConstants.OlePurchaseOrderPurpose.PURCHASE_ORDER_PURPOSE_ID, olePurchaseOrderItem.getPurposeId());
                                OlePurchaseOrderPurpose olePurchaseOrderPurpose = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePurchaseOrderPurpose.class, purposeMap);
                                if(olePurchaseOrderPurpose != null) {
                                    oleeResourcePO.setPurpose(olePurchaseOrderPurpose.getPurchaseOrderPurposeCode());
                                }
                            }
                            Integer poCreatedYear = olePurchaseOrderDocument.getPostingYear();
                            KualiDecimal currentFYCost = new KualiDecimal(0);
                            KualiDecimal previousYearFYCost = new KualiDecimal(0);
                            KualiDecimal previousTwoYearFYCost = new KualiDecimal(0);
                            Integer currentYear = getUniversityDateService().getCurrentFiscalYear();
                            if (currentYear.compareTo(poCreatedYear) == 0) {
                                currentFYCost = currentFYCost.add(getInvoicedAmount(String.valueOf(olePurchaseOrderDocument.getPurapDocumentIdentifier()).trim()));
                                oleeResourcePO.setPaidAmountCurrentFY(currentFYCost.toString());
                            } else if (currentYear.compareTo(poCreatedYear) == 1) {
                                previousYearFYCost = previousYearFYCost.add(getInvoicedAmountForPreviousFY(String.valueOf(olePurchaseOrderDocument.getPurapDocumentIdentifier()).trim()));
                                oleeResourcePO.setPaidAmountPreviousFY(previousYearFYCost.toString());
                            } else if (currentYear.compareTo(poCreatedYear) > 1) {
                                previousTwoYearFYCost = previousTwoYearFYCost.add(getInvoicedAmountForTwoPreviousFY(String.valueOf(olePurchaseOrderDocument.getPurapDocumentIdentifier()).trim()));
                                oleeResourcePO.setPaidAmountTwoYearsPreviousFY(previousTwoYearFYCost.toString());
                            }
                            oleeResourcePO.setVendorDetail(olePurchaseOrderDocument.getVendorDetail());
                            oleeResourcePOs.add(oleeResourcePO);
                        }
                    }
                }
            }
        }
    }

    public void getHoldingsField(OLEEResourceInstance oleeResourceInstance, OleHoldings oleHoldings) {
        Map<String,String> map=new HashMap();
        map.put("oleERSIdentifier", oleHoldings.getEResourceId());
        List<OLEEResourceRecordDocument> oleeResourceRecordDocuments = (List<OLEEResourceRecordDocument>) getBusinessObjectService().findMatching(OLEEResourceRecordDocument.class, map);
        OLEEResourceRecordDocument oleeResourceRecordDocument =null;
        if(oleeResourceRecordDocuments.size() > 0){
            oleeResourceRecordDocument = oleeResourceRecordDocuments.get(0);
        }
        buildInstanceHolding(oleeResourceInstance, oleHoldings, oleeResourceRecordDocuments.get(0));
    }

    private void buildInstanceHolding(OLEEResourceInstance oleeResourceInstance, OleHoldings oleHoldings, OLEEResourceRecordDocument oleeResourceRecordDocument) {
        String start="";
        String end="";
        String holdings = "";
        String startDate = "";
        String endDate = "";
        String space = OLEConstants.OLEEResourceRecord.SPACE;
        String separator = getParameter(OLEConstants.OLEEResourceRecord.COVERAGE_DATE_SEPARATOR);
        String commaSeparator = getParameter(OLEConstants.OLEEResourceRecord.COMMA_SEPARATOR) + space;
        if(oleeResourceRecordDocument != null) {
            String defaultCoverage = oleeResourceRecordDocument.getDefaultCoverage();
            if (defaultCoverage != null) {
                String[] defaultCovDates = defaultCoverage.split("-");
                String defCovStartDat = defaultCovDates.length > 0 ? defaultCovDates[0] : "";
                if (!defCovStartDat.isEmpty()) {
                    String[] covStartDate = defCovStartDat.split(",");
                    if (covStartDate.length > 2 && !covStartDate[2].isEmpty()) {
                        start = covStartDate[2];
                    }
                }
                String defCovEndDat = defaultCovDates.length > 1 ? defaultCovDates[1] : "";
                if (!defCovEndDat.isEmpty()) {
                    String[] covEndDate = defCovEndDat.split(",");
                    if (covEndDate.length > 2 && !covEndDate[2].isEmpty()) {
                        end = covEndDate[2];
                    }
                }
            }
        }
        if (oleHoldings.getExtentOfOwnership().size() > 0 && oleHoldings.getExtentOfOwnership().get(0).getCoverages() != null) {
            List<Coverage> coverageDates = oleHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage();
            if (coverageDates.size() > 0) {
                for (Coverage coverageDate : coverageDates) {
                    startDate = coverageDate.getCoverageStartDate();
                    endDate = coverageDate.getCoverageEndDate();
                    if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
                        holdings += startDate + space + separator + space + endDate;
                        holdings += commaSeparator;
                    } else if (startDate != null && !startDate.isEmpty()) {
                        holdings += startDate + space + separator + space + end;
                        holdings += commaSeparator;
                    } else if (endDate != null && !endDate.isEmpty()) {
                        holdings += start + space + separator + space + endDate;
                        holdings += commaSeparator;
                    }
                }
            }
        } else {
            if (start != null && !start.isEmpty() && end != null && !end.isEmpty()) {
                holdings += start + space + separator + space + end;
                holdings += commaSeparator;
            }
            else if(start != null && !start.isEmpty()){
                holdings += start + space + separator + space + end;
                holdings += commaSeparator;
            }
            else if(end != null && !end.isEmpty()){
                holdings += start + space + separator + space + end;
                holdings += commaSeparator;
            }
        }
        if (holdings != null && !holdings.isEmpty()) {
            holdings = holdings.substring(0, holdings.lastIndexOf(commaSeparator));
        }
        oleeResourceInstance.setInstanceHoldings(holdings);
    }

    @Override
    public void getDefaultCovDatesToPopup(OLEEResourceRecordDocument oleeResourceRecordDocument, String defaultCov) {
        String[] defaultCovDates = defaultCov.split("-");
        String defCovStartDat = defaultCovDates.length > 0 ? defaultCovDates[0] : "";
        if (oleeResourceRecordDocument.getOleERSInstance() != null) {
            if (!defCovStartDat.isEmpty()) {
                String[] covStartDate = defCovStartDat.split(",");
                if (covStartDate.length > 0 && !covStartDate[0].isEmpty()) {
                    oleeResourceRecordDocument.getOleERSInstance().setCovStartVolume(covStartDate[0].contains(OLEConstants.OLEEResourceRecord.DEFAULT_DATE_VOL) ? covStartDate[0].substring(7, covStartDate[0].length()) : "");
                }
                if (covStartDate.length > 1 && !covStartDate[1].isEmpty()) {
                    oleeResourceRecordDocument.getOleERSInstance().setCovStartIssue(covStartDate[1].contains(OLEConstants.OLEEResourceRecord.DEFAULT_DATE_ISSUE) ? covStartDate[1].substring(6, covStartDate[1].length()) : "");
                }
                if (covStartDate.length > 2 && !covStartDate[2].isEmpty()) {
                    oleeResourceRecordDocument.getOleERSInstance().setCovStartDate(covStartDate[2]);
                    oleeResourceRecordDocument.setCovStartDate(oleeResourceRecordDocument.getOleERSInstance().getCovStartDate());
                }
            }
            String defCovEndDat = defaultCovDates.length > 1 ? defaultCovDates[1] : "";
            if (!defCovEndDat.isEmpty()) {
                String[] covEndDate = defCovEndDat.split(",");
                if (covEndDate.length > 0 && !covEndDate[0].isEmpty()) {
                    oleeResourceRecordDocument.getOleERSInstance().setCovEndVolume(covEndDate[0].contains(OLEConstants.OLEEResourceRecord.DEFAULT_DATE_VOL) ? covEndDate[0].substring(7, covEndDate[0].length()) : "");
                }
                if (covEndDate.length > 1 && !covEndDate[1].isEmpty()) {
                    oleeResourceRecordDocument.getOleERSInstance().setCovEndIssue(covEndDate[1].contains(OLEConstants.OLEEResourceRecord.DEFAULT_DATE_ISSUE) ? covEndDate[1].substring(6, covEndDate[1].length()) : "");
                }
                if (covEndDate.length > 2 && !covEndDate[2].isEmpty()) {
                    oleeResourceRecordDocument.getOleERSInstance().setCovEndDate(covEndDate[2]);
                    oleeResourceRecordDocument.setCovEndDate(oleeResourceRecordDocument.getOleERSInstance().getCovEndDate());
                }
            }
        }
    }

    @Override
    public void getDefaultPerAccDatesToPopup(OLEEResourceRecordDocument oleeResourceRecordDocument, String defaultPerpetualAcc) {
        String[] defaultPerAccDates = defaultPerpetualAcc.split("-");
        String defPerAccStartDat = defaultPerAccDates.length > 0 ? defaultPerAccDates[0] : "";
        if (oleeResourceRecordDocument.getOleERSInstance() != null) {
            if (!defPerAccStartDat.isEmpty()) {
                String[] perAccStartDate = defPerAccStartDat.split(",");
                if (perAccStartDate.length > 0 && !perAccStartDate[0].isEmpty()) {
                    oleeResourceRecordDocument.getOleERSInstance().setPerpetualAccStartVolume(perAccStartDate[0].contains(OLEConstants.OLEEResourceRecord.DEFAULT_DATE_VOL) ? perAccStartDate[0].substring(7, perAccStartDate[0].length()) : "");
                }
                if (perAccStartDate.length > 1 && !perAccStartDate[1].isEmpty()) {
                    oleeResourceRecordDocument.getOleERSInstance().setPerpetualAccStartIssue(perAccStartDate[1].contains(OLEConstants.OLEEResourceRecord.DEFAULT_DATE_ISSUE) ? perAccStartDate[1].substring(6, perAccStartDate[1].length()) : "");
                }
                if (perAccStartDate.length > 2 && !perAccStartDate[2].isEmpty()) {
                    oleeResourceRecordDocument.getOleERSInstance().setPerpetualAccStartDate(perAccStartDate[2]);
                    oleeResourceRecordDocument.setPerAccStartDate(oleeResourceRecordDocument.getOleERSInstance().getPerpetualAccStartDate());
                }
            }
            String defPerAccEndDat = defaultPerAccDates.length > 1 ? defaultPerAccDates[1] : "";
            if (!defPerAccEndDat.isEmpty()) {
                String[] perAccEndDate = defPerAccEndDat.split(",");
                if (perAccEndDate.length > 0 && !perAccEndDate[0].isEmpty()) {
                    oleeResourceRecordDocument.getOleERSInstance().setPerpetualAccEndVolume(perAccEndDate[0].contains(OLEConstants.OLEEResourceRecord.DEFAULT_DATE_VOL) ? perAccEndDate[0].substring(7, perAccEndDate[0].length()) : "");
                }
                if (perAccEndDate.length > 1 && !perAccEndDate[1].isEmpty()) {
                    oleeResourceRecordDocument.getOleERSInstance().setPerpetualAccEndIssue(perAccEndDate[1].contains(OLEConstants.OLEEResourceRecord.DEFAULT_DATE_ISSUE) ? perAccEndDate[1].substring(6, perAccEndDate[1].length()) : "");
                }
                if (perAccEndDate.length > 2 && !perAccEndDate[2].isEmpty()) {
                    oleeResourceRecordDocument.getOleERSInstance().setPerpetualAccEndDate(perAccEndDate[2]);
                    oleeResourceRecordDocument.setPerAccEndDate(oleeResourceRecordDocument.getOleERSInstance().getPerpetualAccEndDate());
                }
            }
        }
    }

    public boolean validateEResourceDocument(OLEEResourceRecordDocument oleeResourceRecordDocument) {
        boolean flag = false;
        boolean isSelectorBlank = false;
        boolean isRequestorBlank = false;
        boolean isReqSelCommentBlank = false;
        boolean isVariantTitleBlank = false;
        if (oleeResourceRecordDocument.getOleMaterialTypes().size() == 0) {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(OLEConstants.OLEEResourceRecord.DOCUMENT_MATERIAL_TYPES, OLEConstants.OLEEResourceRecord.MATERIAL_TYPE_REQUIRED, new String[]{"Material Type"});
            flag = true;
        }
        if (oleeResourceRecordDocument.getOleFormatTypes().size() == 0) {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(OLEConstants.OLEEResourceRecord.DOCUMENT_FORMAT_TYPES, OLEConstants.OLEEResourceRecord.FORMAT_TYPE_REQUIRED, new String[]{"Format Type"});
            flag = true;
        }
        if (oleeResourceRecordDocument.getOleContentTypes().size() == 0) {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(OLEConstants.OLEEResourceRecord.DOCUMENT_CONTENT_TYPES, OLEConstants.OLEEResourceRecord.CONTENT_TYPE_REQUIRED, new String[]{"Content Type"});
            flag = true;
        }
        if (StringUtils.isNotBlank(oleeResourceRecordDocument.getVendorName())) {
            Map vendorMap = new HashMap();
            vendorMap.put(OLEConstants.VENDOR_NAME, oleeResourceRecordDocument.getVendorName());
            List<VendorDetail> vendorDetails = (List<VendorDetail>) getBusinessObjectService().findMatching(VendorDetail.class, vendorMap);
            if (vendorDetails != null && vendorDetails.size() > 0) {
                oleeResourceRecordDocument.setVendorId(vendorDetails.get(0).getVendorNumber());
                oleeResourceRecordDocument.setActiveVendor(vendorDetails.get(0).isActiveIndicator());
            } else {
                GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(OLEConstants.OLEEResourceRecord.OLEERMAINTAB_OVERVIEW, OLEConstants.OLEEResourceRecord.INVALID_VENDOR);
                flag = true;
            }
        }else {
            oleeResourceRecordDocument.setVendorId(null);
        }
        oleeResourceRecordDocument.setPublisherId(null);
        if (StringUtils.isNotBlank(oleeResourceRecordDocument.getPublisher())) {
            Map vendorMap = new HashMap();
            vendorMap.put(OLEConstants.VENDOR_NAME, oleeResourceRecordDocument.getPublisher());
            List<VendorDetail> vendorDetails = (List<VendorDetail>) getBusinessObjectService().findMatching(VendorDetail.class, vendorMap);
            if (vendorDetails != null && vendorDetails.size() > 0) {
                oleeResourceRecordDocument.setPublisherId(vendorDetails.get(0).getVendorNumber());
                oleeResourceRecordDocument.setActivePublisher(vendorDetails.get(0).isActiveIndicator());
            } else {
                GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(OLEConstants.OLEEResourceRecord.OLEERMAINTAB_OVERVIEW, OLEConstants.OLEEResourceRecord.INVALID_PUBLISHER);
                flag = true;
            }
        }
        if (oleeResourceRecordDocument.getRequestors().size() > 0) {
            for (OLEEResourceRequestor oleeResourceRequestor : oleeResourceRecordDocument.getRequestors()) {
                if (oleeResourceRequestor.getRequestorId() == null || oleeResourceRequestor.getRequestorId().equalsIgnoreCase("")) {
                    if (oleeResourceRecordDocument.getRequestors().size() != 1) {
                        isRequestorBlank = true;
                    }
                }
            }
            if (isRequestorBlank) {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLEEResourceRecord.REQUESTOR_SECTION_ID, OLEConstants.OLEEResourceRecord.SHOULD_NOT_BLANK, new String[]{"Requestor Type"});
                flag = true;
            }
        }
        if (oleeResourceRecordDocument.getSelectors().size() > 0) {
            for (OLEEResourceSelector oleeResourceSelector : oleeResourceRecordDocument.getSelectors()) {
                if (oleeResourceSelector.getSelectorId() == null || oleeResourceSelector.getSelectorId().equalsIgnoreCase("")) {
                    if (oleeResourceRecordDocument.getSelectors().size() != 1) {
                        isSelectorBlank = true;
                    }

                }
            }
            if (isSelectorBlank) {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLEEResourceRecord.SELECTOR_SECTION_ID, OLEConstants.OLEEResourceRecord.SHOULD_NOT_BLANK, new String[]{"Selector Type"});
                flag = true;
            }
        }
        if (oleeResourceRecordDocument.getReqSelComments().size() > 0) {
            for (OLEEResourceReqSelComments oleeResourceReqSelComments : oleeResourceRecordDocument.getReqSelComments()) {
                if (oleeResourceReqSelComments.getOleReqSelComments() == null || oleeResourceReqSelComments.getOleReqSelComments().equalsIgnoreCase("")) {
                    if (oleeResourceRecordDocument.getReqSelComments().size() != 1) {
                        isReqSelCommentBlank = true;
                    }
                }
            }
            if (isReqSelCommentBlank) {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLEEResourceRecord.REQUESTOR_SELECTOR_COMMENT_SECTION_ID, OLEConstants.OLEEResourceRecord.SHOULD_NOT_BLANK, new String[]{"RequestorSelectorComment"});
                flag = true;
            }
        }
        if (oleeResourceRecordDocument.getOleEResourceVariantTitleList().size() > 0) {
            for (OLEEResourceVariantTitle oleEResourceVariantTitle : oleeResourceRecordDocument.getOleEResourceVariantTitleList()) {
                if (oleEResourceVariantTitle.getOleVariantTitle() == null || oleEResourceVariantTitle.getOleVariantTitle().equalsIgnoreCase("")) {
                    if (oleeResourceRecordDocument.getOleEResourceVariantTitleList().size() != 1) {
                        isVariantTitleBlank = true;
                    }
                }
            }
            if (isVariantTitleBlank) {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLEEResourceRecord.VARIANT_TITLE_SECTION_ID, OLEConstants.OLEEResourceRecord.SHOULD_NOT_BLANK, new String[]{"Variant Title"});
                flag = true;
            }
        }
        if (oleeResourceRecordDocument.getAccountingLines().size() > 0) {
            BigDecimal totalPercentage = BigDecimal.ZERO;
            BigDecimal desiredPercent = new BigDecimal("100");
            for (OLEEResourceAccountingLine accountingLine : oleeResourceRecordDocument.getAccountingLines()) {
                if (validateAccountingLines(accountingLine, KRADConstants.GLOBAL_ERRORS)) {
                    flag = true;
                }
                totalPercentage = totalPercentage.add(accountingLine.getAccountLinePercent());
            }
            if (desiredPercent.compareTo(totalPercentage) != 0) {
                GlobalVariables.getMessageMap().putErrorForSectionId(KRADConstants.GLOBAL_ERRORS, OLEConstants.OLEEResourceRecord.ERROR_PERCENTAGE_LESS_THAN_HUNDRED);
                flag = true;
            }
        }

        return flag;
    }

    public void saveEResourceInstanceToDocstore(OLEEResourceRecordDocument oleeResourceRecordDocument) throws Exception {
        if (oleeResourceRecordDocument.getOleERSInstances() != null && oleeResourceRecordDocument.getOleERSInstances().size() != 0) {
            List<OLEEResourceInstance> oleeResourceInstanceList =oleeResourceRecordDocument.getOleERSInstances();
            List<OLEEResourceInstance> oleeResourceInstanceDletedList = new ArrayList<>();
            List<org.kuali.ole.docstore.common.document.HoldingsTree> holdingsTreeList = new ArrayList<>();
            for (OLEEResourceInstance oleeResourceInstance : oleeResourceInstanceList) {
                if (oleeResourceInstance != null) {
                    if (org.apache.commons.lang.StringUtils.isBlank(oleeResourceInstance.getSubscriptionStatus())
                            && org.apache.commons.lang.StringUtils.isNotBlank(oleeResourceRecordDocument.getSubscriptionStatus()) ) {
                        oleeResourceInstance.setSubscriptionStatus(oleeResourceRecordDocument.getSubscriptionStatus());
                        oleeResourceRecordDocument.getOleERSInstancesForSave().add(oleeResourceInstance);
                    }
                    boolean isUpdate = false;
                    String eHoldingsId = oleeResourceInstance.getInstanceId();
                    if (StringUtils.isNotEmpty(eHoldingsId)) {

                        Holdings holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(eHoldingsId);
                        if (StringUtils.isNotEmpty(holdings.getId())) {
                            OleHoldings oleHoldings = (OleHoldings) holdings.deserializeContent(holdings.getContent());

                            if(oleHoldings != null) {
                                if (StringUtils.isEmpty(oleHoldings.getEResourceId())) {
                                    oleHoldings.setEResourceId(oleeResourceRecordDocument.getOleERSIdentifier());
                                    isUpdate = true;
                                }
                                StatisticalSearchingCode statisticalSearchingCode = new StatisticalSearchingCode();
                                if (oleeResourceRecordDocument.getOleStatisticalCode() != null) {
                                    statisticalSearchingCode.setCodeValue(oleeResourceRecordDocument.getOleStatisticalCode().getStatisticalSearchingCode());
                                }
                                if (oleHoldings.getStatisticalSearchingCode() == null &&  statisticalSearchingCode.getCodeValue() != null) {
                                    oleHoldings.setStatisticalSearchingCode(statisticalSearchingCode);
                                    isUpdate = true;
                                }
                                if (oleHoldings.getHoldingsAccessInformation() == null ) {
                                    oleHoldings.getHoldingsAccessInformation().setNumberOfSimultaneousUser(oleeResourceRecordDocument.getNumOfSimultaneousUsers());
                                    oleHoldings.getHoldingsAccessInformation().setAccessLocation(oleeResourceRecordDocument.getAccessLocationId());
                                    oleHoldings.getHoldingsAccessInformation().setAuthenticationType(oleeResourceRecordDocument.getOleAuthenticationType().getOleAuthenticationTypeName());
                                    isUpdate = true;
                                }
                                buildInstanceHolding(oleeResourceInstance, oleHoldings, oleeResourceRecordDocument);
                                holdings.setContent(holdings.serializeContent(oleHoldings));
                                if(isUpdate){
                                    HoldingsTree holdingsTree = new HoldingsTree();
                                    holdings.setOperation(DocstoreDocument.OperationType.UPDATE);
                                    holdingsTree.setHoldings(holdings);
                                    holdingsTreeList.add(holdingsTree);
                                }
                            }
                        } else {
                            oleeResourceInstanceDletedList.add(oleeResourceInstance);
                        }
                        if (CollectionUtils.isNotEmpty(oleeResourceInstanceDletedList)) {
                            oleeResourceRecordDocument.getOleERSInstances().removeAll(oleeResourceInstanceDletedList);
                            oleeResourceRecordDocument.getOleERSInstancesForDelete().addAll(oleeResourceInstanceDletedList);
                        }
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(oleeResourceRecordDocument.getOleERSInstancesForSave())) {
                getBusinessObjectService().save(oleeResourceRecordDocument.getOleERSInstancesForSave());
            }


            if(holdingsTreeList.size()  > 0) {
                BibTrees bibTrees = new BibTrees();
                BibTree bibTree = new BibTree();
                bibTree.getHoldingsTrees().addAll(holdingsTreeList);
                bibTrees.getBibTrees().add(bibTree);
                getDocstoreClientLocator().getDocstoreClient().processBibTrees(bibTrees);
            }
        }
    }

    public boolean validateCoverageStartDates(OLEEResourceRecordDocument oleeResourceRecordDocument, OLEEResourceRecordForm oleERSForm) {
        boolean coverageStartFlag = true;
        OLEEResourceInstance oleeResourceInstance = oleeResourceRecordDocument.getOleERSInstance();
        oleERSForm.setDefaultCovStartDateErrorMessage(null);
        String coverageStartDate = "";
        try {
            if (oleeResourceInstance != null) {
                coverageStartDate = oleeResourceInstance.getCovStartDate();
                if (StringUtils.isNotEmpty(coverageStartDate)) {
                    if (coverageStartDate.matches(convertDateFormatToRegex(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE))) {
                        //String dateFormat = getDateFormat(coverageStartDate);
                        String dateFormat = coverageStartDate;
                        oleeResourceInstance.setCovStartDate(dateFormat);
                    } else if (coverageStartDate.matches(calendarYearAgo)) {
                        String[] coverageStartYear = coverageStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageStartYear.length > 0 && coverageStartYear[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            /*String previousYearOfCovDate = getYearFormat();
                            previousYearOfCovDate = getFirstDay(previousYearOfCovDate);*/
                            String previousYearOfCovDate = coverageStartDate;
                            oleeResourceInstance.setCovStartDate(previousYearOfCovDate);
                        } else {
                            coverageStartFlag = false;
                            oleERSForm.setCoverageFlag(true);
                            oleERSForm.setDefaultCovStartDateErrorMessage(OLEConstants.OLEEResourceRecord.COV_START_DATE_FORMAT_INV);
                        }
                    } else if (coverageStartDate.matches(calendarYearsAgo)) {
                        String[] coverageStartYears = coverageStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageStartYears.length > 0 && !coverageStartYears[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            /*String previousYearsOfCovDate = getYearsFormat(coverageStartYears);
                            previousYearsOfCovDate = getFirstDay(previousYearsOfCovDate);*/
                            String previousYearsOfCovDate = coverageStartDate;
                            oleeResourceInstance.setCovStartDate(previousYearsOfCovDate);
                        } else {
                            coverageStartFlag = false;
                            oleERSForm.setCoverageFlag(true);
                            oleERSForm.setDefaultCovStartDateErrorMessage(OLEConstants.OLEEResourceRecord.COV_START_DATE_FORMAT_INV);
                        }
                    } else if (coverageStartDate.matches(monthAgo)) {
                        String[] coverageStartMonth = coverageStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageStartMonth.length > 0 && coverageStartMonth[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousMonthOfCovDate = getMonthFormat();
                            String previousMonthOfCovDate = coverageStartDate;
                            oleeResourceInstance.setCovStartDate(previousMonthOfCovDate);
                        } else {
                            coverageStartFlag = false;
                            oleERSForm.setCoverageFlag(true);
                            oleERSForm.setDefaultCovStartDateErrorMessage(OLEConstants.OLEEResourceRecord.COV_START_DATE_FORMAT_INV);
                        }
                    } else if (coverageStartDate.matches(monthsAgo)) {
                        String[] coverageStartMonths = coverageStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageStartMonths.length > 0 && !coverageStartMonths[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousMonthsOfCovDate = getMonthsFormat(coverageStartMonths);
                            String previousMonthsOfCovDate = coverageStartDate;
                            oleeResourceInstance.setCovStartDate(previousMonthsOfCovDate);
                        } else {
                            coverageStartFlag = false;
                            oleERSForm.setCoverageFlag(true);
                            oleERSForm.setDefaultCovStartDateErrorMessage(OLEConstants.OLEEResourceRecord.COV_START_DATE_FORMAT_INV);
                        }
                    } else if (coverageStartDate.matches(weekAgo)) {
                        String[] coverageStartWeek = coverageStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageStartWeek.length > 0 && coverageStartWeek[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousWeekOfCovDate = getWeekFormat();
                            String previousWeekOfCovDate = coverageStartDate;
                            oleeResourceInstance.setCovStartDate(previousWeekOfCovDate);
                        } else {
                            coverageStartFlag = false;
                            oleERSForm.setCoverageFlag(true);
                            oleERSForm.setDefaultCovStartDateErrorMessage(OLEConstants.OLEEResourceRecord.COV_START_DATE_FORMAT_INV);
                        }
                    } else if (coverageStartDate.matches(weeksAgo)) {
                        String[] coverageStartWeeks = coverageStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageStartWeeks.length > 0 && !coverageStartWeeks[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousYearsOfCovDate = getWeeksFormat(coverageStartWeeks);
                            String previousYearsOfCovDate = coverageStartDate;
                            oleeResourceInstance.setCovStartDate(previousYearsOfCovDate);
                        } else {
                            coverageStartFlag = false;
                            oleERSForm.setCoverageFlag(true);
                            oleERSForm.setDefaultCovStartDateErrorMessage(OLEConstants.OLEEResourceRecord.COV_START_DATE_FORMAT_INV);
                        }
                    } else if (coverageStartDate.matches(dayAgo)) {
                        String[] coverageStartDay = coverageStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageStartDay.length > 0 && coverageStartDay[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousDayOfCovDate = getDayFormat();
                            String previousDayOfCovDate = coverageStartDate;
                            oleeResourceInstance.setCovStartDate(previousDayOfCovDate);
                        } else {
                            coverageStartFlag = false;
                            oleERSForm.setCoverageFlag(true);
                            oleERSForm.setDefaultCovStartDateErrorMessage(OLEConstants.OLEEResourceRecord.COV_START_DATE_FORMAT_INV);
                        }
                    } else if (coverageStartDate.matches(daysAgo)) {
                        String[] coverageStartDays = coverageStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageStartDays.length > 0 && !coverageStartDays[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousDaysOfCovDate = getDaysFormat(coverageStartDays);
                            String previousDaysOfCovDate = coverageStartDate;
                            oleeResourceInstance.setCovStartDate(previousDaysOfCovDate);
                        } else {
                            coverageStartFlag = false;
                            oleERSForm.setCoverageFlag(true);
                            oleERSForm.setDefaultCovStartDateErrorMessage(OLEConstants.OLEEResourceRecord.COV_START_DATE_FORMAT_INV);
                        }
                    } else {
                        coverageStartFlag = false;
                        oleERSForm.setCoverageFlag(true);
                        oleERSForm.setDefaultCovStartDateErrorMessage(OLEConstants.OLEEResourceRecord.COV_START_DATE_FORMAT_INV);
                    }
                }
            }
            oleeResourceRecordDocument.setCovStartDate(coverageStartDate);
        } catch (Exception ex) {
            LOG.error("Exception while validating the coverage start date format in EResource" + ex.getMessage());
            throw new RuntimeException();
        }
        return coverageStartFlag;
    }

    public boolean validateCoverageEndDates(OLEEResourceRecordDocument oleeResourceRecordDocument, OLEEResourceRecordForm oleERSForm) {
        boolean coverageEndFlag = true;
        OLEEResourceInstance oleeResourceInstance = oleeResourceRecordDocument.getOleERSInstance();
        oleERSForm.setDefaultCovEndDateErrorMessage(null);
        String coverageEndDate = "";
        try {
            if (oleeResourceInstance != null) {
                coverageEndDate = oleeResourceInstance.getCovEndDate();
                if (StringUtils.isNotEmpty(coverageEndDate)) {
                    if (coverageEndDate.matches(convertDateFormatToRegex(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE))) {
                        //String dateFormat = getDateFormat(coverageEndDate);
                        String dateFormat = coverageEndDate;
                        oleeResourceInstance.setCovEndDate(dateFormat);
                    } else if (coverageEndDate.matches(calendarYearAgo)) {
                        String[] coverageEndYear = coverageEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageEndYear.length > 0 && coverageEndYear[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            /*String previousYearOfCovDate = getYearFormat();
                            previousYearOfCovDate = getLastDay(previousYearOfCovDate);*/
                            String previousYearOfCovDate = coverageEndDate;
                            oleeResourceInstance.setCovEndDate(previousYearOfCovDate);
                        } else {
                            coverageEndFlag = false;
                            oleERSForm.setCoverageFlag(true);
                            oleERSForm.setDefaultCovEndDateErrorMessage(OLEConstants.OLEEResourceRecord.COV_END_DATE_FORMAT_INV);
                        }
                    } else if (coverageEndDate.matches(calendarYearsAgo)) {
                        String[] coverageEndYears = coverageEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageEndYears.length > 0 && !coverageEndYears[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            /*String previousYearsOfCovDate = getYearsFormat(coverageEndYears);
                            previousYearsOfCovDate = getLastDay(previousYearsOfCovDate);*/
                            String previousYearsOfCovDate = coverageEndDate;
                            oleeResourceInstance.setCovEndDate(previousYearsOfCovDate);
                        } else {
                            coverageEndFlag = false;
                            oleERSForm.setCoverageFlag(true);
                            oleERSForm.setDefaultCovEndDateErrorMessage(OLEConstants.OLEEResourceRecord.COV_END_DATE_FORMAT_INV);
                        }
                    } else if (coverageEndDate.matches(monthAgo)) {
                        String[] coverageEndMonth = coverageEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageEndMonth.length > 0 && coverageEndMonth[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousMonthOfCovDate = getMonthFormat();
                            String previousMonthOfCovDate = coverageEndDate;
                            oleeResourceInstance.setCovEndDate(previousMonthOfCovDate);
                        } else {
                            coverageEndFlag = false;
                            oleERSForm.setCoverageFlag(true);
                            oleERSForm.setDefaultCovEndDateErrorMessage(OLEConstants.OLEEResourceRecord.COV_END_DATE_FORMAT_INV);
                        }
                    } else if (coverageEndDate.matches(monthsAgo)) {
                        String[] coverageEndMonths = coverageEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageEndMonths.length > 0 && !coverageEndMonths[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousMonthsOfCovDate = getMonthsFormat(coverageEndMonths);
                            String previousMonthsOfCovDate = coverageEndDate;
                            oleeResourceInstance.setCovEndDate(previousMonthsOfCovDate);
                        } else {
                            coverageEndFlag = false;
                            oleERSForm.setCoverageFlag(true);
                            oleERSForm.setDefaultCovEndDateErrorMessage(OLEConstants.OLEEResourceRecord.COV_END_DATE_FORMAT_INV);
                        }
                    } else if (coverageEndDate.matches(weekAgo)) {
                        String[] coverageEndWeek = coverageEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageEndWeek.length > 0 && coverageEndWeek[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousWeekOfCovEndDate = getWeekFormat();
                            String previousWeekOfCovEndDate = coverageEndDate;
                            oleeResourceInstance.setCovEndDate(previousWeekOfCovEndDate);
                        } else {
                            coverageEndFlag = false;
                            oleERSForm.setCoverageFlag(true);
                            oleERSForm.setDefaultCovEndDateErrorMessage(OLEConstants.OLEEResourceRecord.COV_END_DATE_FORMAT_INV);
                        }
                    } else if (coverageEndDate.matches(weeksAgo)) {
                        String[] coverageEndWeeks = coverageEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageEndWeeks.length > 0 && !coverageEndWeeks[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousWeeksOfCovEndDate = getWeeksFormat(coverageEndWeeks);
                            String previousWeeksOfCovEndDate = coverageEndDate;
                            oleeResourceInstance.setCovEndDate(previousWeeksOfCovEndDate);
                        } else {
                            coverageEndFlag = false;
                            oleERSForm.setCoverageFlag(true);
                            oleERSForm.setDefaultCovEndDateErrorMessage(OLEConstants.OLEEResourceRecord.COV_END_DATE_FORMAT_INV);
                        }
                    } else if (coverageEndDate.matches(dayAgo)) {
                        String[] coverageEndDay = coverageEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageEndDay.length > 0 && coverageEndDay[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousDayOfCovDate = getDayFormat();
                            String previousDayOfCovDate = coverageEndDate;
                            oleeResourceInstance.setCovEndDate(previousDayOfCovDate);
                        } else {
                            coverageEndFlag = false;
                            oleERSForm.setCoverageFlag(true);
                            oleERSForm.setDefaultCovEndDateErrorMessage(OLEConstants.OLEEResourceRecord.COV_END_DATE_FORMAT_INV);
                        }
                    } else if (coverageEndDate.matches(daysAgo)) {
                        String[] coverageEndDays = coverageEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageEndDays.length > 0 && !coverageEndDays[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousDaysOfCovEndDate = getDaysFormat(coverageEndDays);
                            String previousDaysOfCovEndDate = coverageEndDate;
                            oleeResourceInstance.setCovEndDate(previousDaysOfCovEndDate);
                        } else {
                            coverageEndFlag = false;
                            oleERSForm.setCoverageFlag(true);
                            oleERSForm.setDefaultCovEndDateErrorMessage(OLEConstants.OLEEResourceRecord.COV_END_DATE_FORMAT_INV);
                        }
                    } else {
                        coverageEndFlag = false;
                        oleERSForm.setCoverageFlag(true);
                        oleERSForm.setDefaultCovEndDateErrorMessage(OLEConstants.OLEEResourceRecord.COV_END_DATE_FORMAT_INV);
                    }
                }
            }
            oleeResourceRecordDocument.setCovEndDate(coverageEndDate);
        } catch (Exception ex) {
            LOG.error("Exception while validating the coverage end date format in EResource" + ex.getMessage());
            throw new RuntimeException();
        }
        return coverageEndFlag;
    }

    public boolean validatePerpetualAccessStartDates(OLEEResourceRecordDocument oleeResourceRecordDocument, OLEEResourceRecordForm oleERSForm) {
        boolean perpetualAccessStartFlag = true;
        OLEEResourceInstance oleeResourceInstance = oleeResourceRecordDocument.getOleERSInstance();
        oleERSForm.setDefaultPerAccStartDateErrorMessage(null);
        String perpetualAccessStartDate = "";
        try {
            if (oleeResourceInstance != null) {
                perpetualAccessStartDate = oleeResourceInstance.getPerpetualAccStartDate();
                if (StringUtils.isNotEmpty(perpetualAccessStartDate)) {
                    if (perpetualAccessStartDate.matches(convertDateFormatToRegex(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE))) {
                        //String dateFormat = getDateFormat(perpetualAccessStartDate);
                        String dateFormat = perpetualAccessStartDate;
                        oleeResourceInstance.setPerpetualAccStartDate(dateFormat);
                    } else if (perpetualAccessStartDate.matches(calendarYearAgo)) {
                        String[] perpetualAccessStartYear = perpetualAccessStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessStartYear.length > 0 && perpetualAccessStartYear[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            /*String previousYearOfPerpetualAccDate = getYearFormat();
                            previousYearOfPerpetualAccDate = getFirstDay(previousYearOfPerpetualAccDate);*/
                            String previousYearOfPerpetualAccDate = perpetualAccessStartDate;
                            oleeResourceInstance.setPerpetualAccStartDate(previousYearOfPerpetualAccDate);
                        } else {
                            perpetualAccessStartFlag = false;
                            oleERSForm.setPerpetualAccessFlag(true);
                            oleERSForm.setDefaultPerAccStartDateErrorMessage(OLEConstants.OLEEResourceRecord.PER_ACC_START_DATE_FORMAT_INV);
                        }
                    } else if (perpetualAccessStartDate.matches(calendarYearsAgo)) {
                        String[] perpetualAccessStartYears = perpetualAccessStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessStartYears.length > 0 && !perpetualAccessStartYears[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            /*String previousYearsOfPerpetualAccDate = getYearsFormat(perpetualAccessStartYears);
                            previousYearsOfPerpetualAccDate = getFirstDay(previousYearsOfPerpetualAccDate);*/
                            String previousYearsOfPerpetualAccDate = perpetualAccessStartDate;
                            oleeResourceInstance.setPerpetualAccStartDate(previousYearsOfPerpetualAccDate);
                        } else {
                            perpetualAccessStartFlag = false;
                            oleERSForm.setPerpetualAccessFlag(true);
                            oleERSForm.setDefaultPerAccStartDateErrorMessage(OLEConstants.OLEEResourceRecord.PER_ACC_START_DATE_FORMAT_INV);
                        }
                    } else if (perpetualAccessStartDate.matches(monthAgo)) {
                        String[] perpetualAccessStartMonth = perpetualAccessStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessStartMonth.length > 0 && perpetualAccessStartMonth[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousMonthOfPerpetualAccDate = getMonthFormat();
                            String previousMonthOfPerpetualAccDate = perpetualAccessStartDate;
                            oleeResourceInstance.setPerpetualAccStartDate(previousMonthOfPerpetualAccDate);
                        } else {
                            perpetualAccessStartFlag = false;
                            oleERSForm.setPerpetualAccessFlag(true);
                            oleERSForm.setDefaultPerAccStartDateErrorMessage(OLEConstants.OLEEResourceRecord.PER_ACC_START_DATE_FORMAT_INV);
                        }
                    } else if (perpetualAccessStartDate.matches(monthsAgo)) {
                        String[] perpetualAccessStartMonths = perpetualAccessStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessStartMonths.length > 0 && !perpetualAccessStartMonths[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousMonthsOfPerpetualAccDate = getMonthsFormat(perpetualAccessStartMonths);
                            String previousMonthsOfPerpetualAccDate = perpetualAccessStartDate;
                            oleeResourceInstance.setPerpetualAccStartDate(previousMonthsOfPerpetualAccDate);
                        } else {
                            perpetualAccessStartFlag = false;
                            oleERSForm.setPerpetualAccessFlag(true);
                            oleERSForm.setDefaultPerAccStartDateErrorMessage(OLEConstants.OLEEResourceRecord.PER_ACC_START_DATE_FORMAT_INV);
                        }
                    } else if (perpetualAccessStartDate.matches(weekAgo)) {
                        String[] perpetualAccessStartWeek = perpetualAccessStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessStartWeek.length > 0 && perpetualAccessStartWeek[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousWeekOfCovEndDate = getWeekFormat();
                            String previousWeekOfCovEndDate = perpetualAccessStartDate;
                            oleeResourceInstance.setPerpetualAccStartDate(previousWeekOfCovEndDate);
                        } else {
                            perpetualAccessStartFlag = false;
                            oleERSForm.setPerpetualAccessFlag(true);
                            oleERSForm.setDefaultPerAccStartDateErrorMessage(OLEConstants.OLEEResourceRecord.PER_ACC_START_DATE_FORMAT_INV);
                        }
                    } else if (perpetualAccessStartDate.matches(weeksAgo)) {
                        String[] perpetualAccessStartWeeks = perpetualAccessStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessStartWeeks.length > 0 && !perpetualAccessStartWeeks[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousWeeksOfCovEndDate = getWeeksFormat(perpetualAccessStartWeeks);
                            String previousWeeksOfCovEndDate = perpetualAccessStartDate;
                            oleeResourceInstance.setPerpetualAccStartDate(previousWeeksOfCovEndDate);
                        } else {
                            perpetualAccessStartFlag = false;
                            oleERSForm.setPerpetualAccessFlag(true);
                            oleERSForm.setDefaultPerAccStartDateErrorMessage(OLEConstants.OLEEResourceRecord.PER_ACC_START_DATE_FORMAT_INV);
                        }
                    } else if (perpetualAccessStartDate.matches(dayAgo)) {
                        String[] perpetualAccessStartDay = perpetualAccessStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessStartDay.length > 0 && perpetualAccessStartDay[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousDayOfPerpetualAccDate = getDayFormat();
                            String previousDayOfPerpetualAccDate = perpetualAccessStartDate;
                            oleeResourceInstance.setPerpetualAccStartDate(previousDayOfPerpetualAccDate);
                        } else {
                            perpetualAccessStartFlag = false;
                            oleERSForm.setPerpetualAccessFlag(true);
                            oleERSForm.setDefaultPerAccStartDateErrorMessage(OLEConstants.OLEEResourceRecord.PER_ACC_START_DATE_FORMAT_INV);
                        }
                    } else if (perpetualAccessStartDate.matches(daysAgo)) {
                        String[] perpetualAccessStartDays = perpetualAccessStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessStartDays.length > 0 && !perpetualAccessStartDays[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousDaysOfPerpetualAccDate = getDaysFormat(perpetualAccessStartDays);
                            String previousDaysOfPerpetualAccDate = perpetualAccessStartDate;
                            oleeResourceInstance.setPerpetualAccStartDate(previousDaysOfPerpetualAccDate);
                        } else {
                            perpetualAccessStartFlag = false;
                            oleERSForm.setPerpetualAccessFlag(true);
                            oleERSForm.setDefaultPerAccStartDateErrorMessage(OLEConstants.OLEEResourceRecord.PER_ACC_START_DATE_FORMAT_INV);
                        }
                    } else {
                        perpetualAccessStartFlag = false;
                        oleERSForm.setDefaultPerAccStartDateErrorMessage(OLEConstants.OLEEResourceRecord.PER_ACC_START_DATE_FORMAT_INV);
                    }
                }
            }
            oleeResourceRecordDocument.setPerAccStartDate(perpetualAccessStartDate);
        } catch (Exception ex) {
            LOG.error("Exception while validating the perpetual access start date format in EResource" + ex.getMessage());
            throw new RuntimeException();
        }
        return perpetualAccessStartFlag;
    }

    public boolean validatePerpetualAccessEndDates(OLEEResourceRecordDocument oleeResourceRecordDocument, OLEEResourceRecordForm oleERSForm) {
        boolean perpetualAccessEndFlag = true;
        OLEEResourceInstance oleeResourceInstance = oleeResourceRecordDocument.getOleERSInstance();
        oleERSForm.setDefaultPerAccEndDateErrorMessage(null);
        String perpetualAccessEndDate = "";
        try {
            if (oleeResourceInstance != null) {
                perpetualAccessEndDate = oleeResourceInstance.getPerpetualAccEndDate();
                if (StringUtils.isNotEmpty(perpetualAccessEndDate)) {
                    if (perpetualAccessEndDate.matches(convertDateFormatToRegex(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE))) {
                        //String dateFormat = getDateFormat(perpetualAccessEndDate);
                        String dateFormat = perpetualAccessEndDate;
                        oleeResourceInstance.setPerpetualAccEndDate(dateFormat);
                    } else if (perpetualAccessEndDate.matches(calendarYearAgo)) {
                        String[] perpetualAccessEndYear = perpetualAccessEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessEndYear.length > 0 && perpetualAccessEndYear[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                           /* String previousYearOfPerpetualAccDate = getYearFormat();
                            previousYearOfPerpetualAccDate = getLastDay(previousYearOfPerpetualAccDate);*/
                            String previousYearOfPerpetualAccDate = perpetualAccessEndDate;
                            oleeResourceInstance.setPerpetualAccEndDate(previousYearOfPerpetualAccDate);
                        } else {
                            perpetualAccessEndFlag = false;
                            oleERSForm.setPerpetualAccessFlag(true);
                            oleERSForm.setDefaultPerAccEndDateErrorMessage(OLEConstants.OLEEResourceRecord.PER_ACC_END_DATE_FORMAT_INV);
                        }
                    } else if (perpetualAccessEndDate.matches(calendarYearsAgo)) {
                        String[] perpetualAccessEndYears = perpetualAccessEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessEndYears.length > 0 && !perpetualAccessEndYears[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            /*String previousYearsOfPerpetualAccDate = getYearsFormat(perpetualAccessEndYears);
                            previousYearsOfPerpetualAccDate = getLastDay(previousYearsOfPerpetualAccDate);*/
                            String previousYearsOfPerpetualAccDate = perpetualAccessEndDate;
                            oleeResourceInstance.setPerpetualAccEndDate(previousYearsOfPerpetualAccDate);
                        } else {
                            perpetualAccessEndFlag = false;
                            oleERSForm.setPerpetualAccessFlag(true);
                            oleERSForm.setDefaultPerAccEndDateErrorMessage(OLEConstants.OLEEResourceRecord.PER_ACC_END_DATE_FORMAT_INV);
                        }
                    } else if (perpetualAccessEndDate.matches(monthAgo)) {
                        String[] perpetualAccessEndMonth = perpetualAccessEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessEndMonth.length > 0 && perpetualAccessEndMonth[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousMonthOfPerpetualAccDate = getMonthFormat();
                            String previousMonthOfPerpetualAccDate = perpetualAccessEndDate;
                            oleeResourceInstance.setPerpetualAccEndDate(previousMonthOfPerpetualAccDate);
                        } else {
                            perpetualAccessEndFlag = false;
                            oleERSForm.setPerpetualAccessFlag(true);
                            oleERSForm.setDefaultPerAccEndDateErrorMessage(OLEConstants.OLEEResourceRecord.PER_ACC_END_DATE_FORMAT_INV);
                        }
                    } else if (perpetualAccessEndDate.matches(monthsAgo)) {
                        String[] perpetualAccessEndMonths = perpetualAccessEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessEndMonths.length > 0 && !perpetualAccessEndMonths[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousMonthsOfPerpetualAccDate = getMonthsFormat(perpetualAccessEndMonths);
                            String previousMonthsOfPerpetualAccDate = perpetualAccessEndDate;
                            oleeResourceInstance.setPerpetualAccEndDate(previousMonthsOfPerpetualAccDate);
                        } else {
                            perpetualAccessEndFlag = false;
                            oleERSForm.setPerpetualAccessFlag(true);
                            oleERSForm.setDefaultPerAccEndDateErrorMessage(OLEConstants.OLEEResourceRecord.PER_ACC_END_DATE_FORMAT_INV);
                        }
                    } else if (perpetualAccessEndDate.matches(weekAgo)) {
                        String[] perpetualAccessEndWeek = perpetualAccessEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessEndWeek.length > 0 && !perpetualAccessEndWeek[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousWeekOfPerpetualAccDate = getWeekFormat();
                            String previousWeekOfPerpetualAccDate = perpetualAccessEndDate;
                            oleeResourceInstance.setPerpetualAccEndDate(previousWeekOfPerpetualAccDate);
                        } else {
                            perpetualAccessEndFlag = false;
                            oleERSForm.setPerpetualAccessFlag(true);
                            oleERSForm.setDefaultPerAccEndDateErrorMessage(OLEConstants.OLEEResourceRecord.PER_ACC_END_DATE_FORMAT_INV);
                        }
                    } else if (perpetualAccessEndDate.matches(weeksAgo)) {
                        String[] perpetualAccessEndWeeks = perpetualAccessEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessEndWeeks.length > 0 && !perpetualAccessEndWeeks[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousWeeksOfPerpetualAccDate = getWeeksFormat(perpetualAccessEndWeeks);
                            String previousWeeksOfPerpetualAccDate = perpetualAccessEndDate;
                            oleeResourceInstance.setPerpetualAccEndDate(previousWeeksOfPerpetualAccDate);
                        } else {
                            perpetualAccessEndFlag = false;
                            oleERSForm.setPerpetualAccessFlag(true);
                            oleERSForm.setDefaultPerAccEndDateErrorMessage(OLEConstants.OLEEResourceRecord.PER_ACC_END_DATE_FORMAT_INV);
                        }
                    } else if (perpetualAccessEndDate.matches(dayAgo)) {
                        String[] perpetualAccessEndDay = perpetualAccessEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessEndDay.length > 0 && perpetualAccessEndDay[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousDayOfPerpetualAccDate = getDayFormat();
                            String previousDayOfPerpetualAccDate = perpetualAccessEndDate;
                            oleeResourceInstance.setPerpetualAccEndDate(previousDayOfPerpetualAccDate);
                        } else {
                            perpetualAccessEndFlag = false;
                            oleERSForm.setPerpetualAccessFlag(true);
                            oleERSForm.setDefaultPerAccEndDateErrorMessage(OLEConstants.OLEEResourceRecord.PER_ACC_END_DATE_FORMAT_INV);
                        }
                    } else if (perpetualAccessEndDate.matches(daysAgo)) {
                        String[] perpetualAccessEndDays = perpetualAccessEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessEndDays.length > 0 && !perpetualAccessEndDays[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousDaysOfPerpetualAccDate = getDaysFormat(perpetualAccessEndDays);
                            String previousDaysOfPerpetualAccDate = perpetualAccessEndDate;
                            oleeResourceInstance.setPerpetualAccEndDate(previousDaysOfPerpetualAccDate);
                        } else {
                            perpetualAccessEndFlag = false;
                            oleERSForm.setPerpetualAccessFlag(true);
                            oleERSForm.setDefaultPerAccEndDateErrorMessage(OLEConstants.OLEEResourceRecord.PER_ACC_END_DATE_FORMAT_INV);
                        }
                    } else {
                        perpetualAccessEndFlag = false;
                        oleERSForm.setDefaultPerAccEndDateErrorMessage(OLEConstants.OLEEResourceRecord.PER_ACC_END_DATE_FORMAT_INV);
                    }
                }
            }
            oleeResourceRecordDocument.setPerAccEndDate(perpetualAccessEndDate);
        } catch (Exception ex) {
            LOG.error("Exception while validating the perpetual access end date format in EResource" + ex.getMessage());
            throw new RuntimeException();
        }
        return perpetualAccessEndFlag;
    }

    public boolean validateDates(OleHoldings eHoldings) {
        boolean dateFlag = true;
        dateFlag &= validateCoverageStartDateForEHolding(eHoldings);
        dateFlag &= validateCoverageEndDateForEHolding(eHoldings);
        dateFlag &= validatePerpetualAccStartDateForEHolding(eHoldings);
        dateFlag &= validatePerpetualAccEndDateForEHolding(eHoldings);
        return dateFlag;
    }

    public boolean validateCoverageStartDateForEHolding(OleHoldings eHoldings) {
        boolean covStartDateFlag = true;
        List<Coverage> coverageList = new ArrayList<>();
        if (eHoldings.getExtentOfOwnership().size() > 0 && eHoldings.getExtentOfOwnership().get(0).getCoverages() != null
                && eHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage().size() > 0) {
            coverageList = eHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage();
            for (Coverage coverage : coverageList) {
                if(StringUtils.isNotEmpty(coverage.getCoverageStartDateString())) {
                    coverage.setCoverageStartDate(coverage.getCoverageStartDateString());
                } else if(StringUtils.isNotEmpty(coverage.getCoverageStartDateFormat())) {
                    coverage.setCoverageStartDate(coverage.getCoverageStartDateFormat());
                }
                covStartDateFlag &= validateCoverageStartDates(coverage);
            }
            if (!covStartDateFlag) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OleHoldings.ERROR_MSG_COV_START_DATE);
                return covStartDateFlag;
            }
        }
        return covStartDateFlag;
    }

    public boolean validateCoverageEndDateForEHolding(OleHoldings eHoldings) {
        boolean covEndDateFlag = true;
        List<Coverage> coverageList = new ArrayList<>();
        if (eHoldings.getExtentOfOwnership().size() > 0 && eHoldings.getExtentOfOwnership().get(0).getCoverages() != null
                && eHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage().size() > 0) {
            coverageList = eHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage();
            for (Coverage coverage : coverageList) {
                if(StringUtils.isNotEmpty(coverage.getCoverageEndDateString())) {
                    coverage.setCoverageEndDate(coverage.getCoverageEndDateString());
                } else if(StringUtils.isNotEmpty(coverage.getCoverageEndDateFormat())) {
                    coverage.setCoverageEndDate(coverage.getCoverageEndDateFormat());
                }
                covEndDateFlag &= validateCoverageEndDates(coverage);
            }
            if (!covEndDateFlag) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OleHoldings.ERROR_MSG_COV_END_DATE);
                return covEndDateFlag;
            }
        }
        return covEndDateFlag;
    }

    public boolean validatePerpetualAccStartDateForEHolding(OleHoldings eHoldings) {
        boolean perpetualAccStartDateFlag = true;
        List<PerpetualAccess> perpetualAccessList = new ArrayList<>();
        if (eHoldings.getExtentOfOwnership().size() > 0 && eHoldings.getExtentOfOwnership().get(0).getPerpetualAccesses() != null
                && eHoldings.getExtentOfOwnership().get(0).getPerpetualAccesses().getPerpetualAccess().size() > 0) {
            perpetualAccessList = eHoldings.getExtentOfOwnership().get(0).getPerpetualAccesses().getPerpetualAccess();
            for (PerpetualAccess perpetualAccess : perpetualAccessList) {
                if(StringUtils.isNotEmpty(perpetualAccess.getPerpetualAccessStartDateFormat())) {
                    perpetualAccess.setPerpetualAccessStartDate(perpetualAccess.getPerpetualAccessStartDateFormat());
                } else if(StringUtils.isNotEmpty(perpetualAccess.getPerpetualAccessStartDateString())) {
                    perpetualAccess.setPerpetualAccessStartDate(perpetualAccess.getPerpetualAccessStartDateString());
                }
                perpetualAccStartDateFlag &= validatePerpetualAccessStartDates(perpetualAccess);
            }
            if (!perpetualAccStartDateFlag) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OleHoldings.ERROR_MSG_PER_ACC_START_DATE);
                return perpetualAccStartDateFlag;
            }
        }
        return perpetualAccStartDateFlag;
    }

    public boolean validatePerpetualAccEndDateForEHolding(OleHoldings eHoldings) {
        boolean perpetualAccEndDateFlag = true;
        List<PerpetualAccess> perpetualAccessList = new ArrayList<>();
        if (eHoldings.getExtentOfOwnership().size() > 0 && eHoldings.getExtentOfOwnership().get(0).getPerpetualAccesses() != null
                && eHoldings.getExtentOfOwnership().get(0).getPerpetualAccesses().getPerpetualAccess().size() > 0) {
            perpetualAccessList = eHoldings.getExtentOfOwnership().get(0).getPerpetualAccesses().getPerpetualAccess();
            for (PerpetualAccess perpetualAccess : perpetualAccessList) {
                if(StringUtils.isNotEmpty(perpetualAccess.getPerpetualAccessEndDateString())) {
                    perpetualAccess.setPerpetualAccessEndDate(perpetualAccess.getPerpetualAccessEndDateString());
                } else if(StringUtils.isNotEmpty(perpetualAccess.getPerpetualAccessEndDateFormat())) {
                    perpetualAccess.setPerpetualAccessEndDate(perpetualAccess.getPerpetualAccessEndDateFormat());
                }
                perpetualAccEndDateFlag &= validatePerpetualAccessEndDates(perpetualAccess);
            }
            if (!perpetualAccEndDateFlag) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OleHoldings.ERROR_MSG_PER_ACC_END_DATE);
                return perpetualAccEndDateFlag;
            }
        }
        return perpetualAccEndDateFlag;
    }

    private boolean validateCoverageStartDates(Coverage coverage) {
        boolean coverageStartFlag = true;
        String coverageStartDate = "";
        try {
            if (coverage != null) {
                coverageStartDate = coverage.getCoverageStartDate();
                if (StringUtils.isNotEmpty(coverageStartDate)) {
                    if (coverageStartDate.matches(convertDateFormatToRegex(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE))) {
                        //String dateFormat = getDateFormat(coverageStartDate);
                        String dateFormat = coverageStartDate;
                        coverage.setCoverageStartDate(dateFormat);
                    } else if (coverageStartDate.matches(calendarYearAgo)) {
                        String[] coverageStartYear = coverageStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageStartYear.length > 0 && coverageStartYear[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            /*String previousYearOfCovDate = getYearFormat();
                            previousYearOfCovDate = getFirstDay(previousYearOfCovDate);*/
                            String previousYearOfCovDate = coverageStartDate;
                            coverage.setCoverageStartDate(previousYearOfCovDate);
                        } else {
                            coverage.setCoverageStartDate(coverageStartDate);
                            coverageStartFlag = false;
                        }
                    } else if (coverageStartDate.matches(calendarYearsAgo)) {
                        String[] coverageStartYears = coverageStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageStartYears.length > 0 && !coverageStartYears[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            /*String previousYearsOfCovDate = getYearsFormat(coverageStartYears);
                            previousYearsOfCovDate = getFirstDay(previousYearsOfCovDate);*/
                            String previousYearsOfCovDate = coverageStartDate;
                            coverage.setCoverageStartDate(previousYearsOfCovDate);
                        } else {
                            coverageStartFlag = false;
                        }
                    } else if (coverageStartDate.matches(monthAgo)) {
                        String[] coverageStartMonth = coverageStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageStartMonth.length > 0 && coverageStartMonth[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousMonthOfCovDate = getMonthFormat();
                            String previousMonthOfCovDate = coverageStartDate;
                            coverage.setCoverageStartDate(previousMonthOfCovDate);
                        } else {
                            coverageStartFlag = false;
                        }
                    } else if (coverageStartDate.matches(monthsAgo)) {
                        String[] coverageStartMonths = coverageStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageStartMonths.length > 0 && !coverageStartMonths[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousMonthsOfCovDate = getMonthsFormat(coverageStartMonths);
                            String previousMonthsOfCovDate = coverageStartDate;
                            coverage.setCoverageStartDate(previousMonthsOfCovDate);
                        } else {
                            coverageStartFlag = false;
                        }
                    } else if (coverageStartDate.matches(weekAgo)) {
                        String[] coverageStartWeek = coverageStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageStartWeek.length > 0 && coverageStartWeek[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousWeekOfCovDate = getWeekFormat();
                            String previousWeekOfCovDate = coverageStartDate;
                            coverage.setCoverageStartDate(previousWeekOfCovDate);
                        } else {
                            coverageStartFlag = false;
                        }
                    } else if (coverageStartDate.matches(weeksAgo)) {
                        String[] coverageStartWeeks = coverageStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageStartWeeks.length > 0 && !coverageStartWeeks[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousYearsOfCovDate = getWeeksFormat(coverageStartWeeks);
                            String previousYearsOfCovDate = coverageStartDate;
                            coverage.setCoverageStartDate(previousYearsOfCovDate);
                        } else {
                            coverageStartFlag = false;
                        }
                    } else if (coverageStartDate.matches(dayAgo)) {
                        String[] coverageStartDay = coverageStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageStartDay.length > 0 && coverageStartDay[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousDayOfCovDate = getDayFormat();
                            String previousDayOfCovDate = coverageStartDate;
                            coverage.setCoverageStartDate(previousDayOfCovDate);
                        } else {
                            coverageStartFlag = false;
                        }
                    } else if (coverageStartDate.matches(daysAgo)) {
                        String[] coverageStartDays = coverageStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageStartDays.length > 0 && !coverageStartDays[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousDaysOfCovDate = getDaysFormat(coverageStartDays);
                            String previousDaysOfCovDate = coverageStartDate;
                            coverage.setCoverageStartDate(previousDaysOfCovDate);
                        } else {
                            coverageStartFlag = false;
                        }
                    } else {
                        coverage.setCoverageStartDate(coverageStartDate);
                        coverageStartFlag = false;
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception while validating the coverage start date format in EHoldings" + ex.getMessage());
            throw new RuntimeException();
        }
        return coverageStartFlag;
    }

    private boolean validateCoverageEndDates(Coverage coverage) {
        boolean coverageEndFlag = true;
        String coverageEndDate = "";
        try {
            if (coverage != null) {
                coverageEndDate = coverage.getCoverageEndDate();
                if (StringUtils.isNotEmpty(coverageEndDate)) {
                    if (coverageEndDate.matches(convertDateFormatToRegex(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE))) {
                        //String dateFormat = getDateFormat(coverageEndDate);
                        String dateFormat = coverageEndDate;
                        coverage.setCoverageEndDate(dateFormat);
                    } else if (coverageEndDate.matches(calendarYearAgo)) {
                        String[] coverageEndYear = coverageEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageEndYear.length > 0 && coverageEndYear[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            /*String previousYearOfCovDate = getYearFormat();
                            previousYearOfCovDate = getLastDay(previousYearOfCovDate);*/
                            String previousYearOfCovDate = coverageEndDate;
                            coverage.setCoverageEndDate(previousYearOfCovDate);
                        } else {
                            coverageEndFlag = false;
                        }
                    } else if (coverageEndDate.matches(calendarYearsAgo)) {
                        String[] coverageEndYears = coverageEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageEndYears.length > 0 && !coverageEndYears[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            /*String previousYearsOfCovDate = getYearsFormat(coverageEndYears);
                            previousYearsOfCovDate = getLastDay(previousYearsOfCovDate);*/
                            String previousYearsOfCovDate = coverageEndDate;
                            coverage.setCoverageEndDate(previousYearsOfCovDate);
                        } else {
                            coverageEndFlag = false;
                        }
                    } else if (coverageEndDate.matches(monthAgo)) {
                        String[] coverageEndMonth = coverageEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageEndMonth.length > 0 && coverageEndMonth[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousMonthOfCovDate = getMonthFormat();
                            String previousMonthOfCovDate = coverageEndDate;
                            coverage.setCoverageEndDate(previousMonthOfCovDate);
                        } else {
                            coverageEndFlag = false;
                        }
                    } else if (coverageEndDate.matches(monthsAgo)) {
                        String[] coverageEndMonths = coverageEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageEndMonths.length > 0 && !coverageEndMonths[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousMonthsOfCovDate = getMonthsFormat(coverageEndMonths);
                            String previousMonthsOfCovDate = coverageEndDate;
                            coverage.setCoverageEndDate(previousMonthsOfCovDate);
                        } else {
                            coverageEndFlag = false;
                        }
                    } else if (coverageEndDate.matches(weekAgo)) {
                        String[] coverageEndWeek = coverageEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageEndWeek.length > 0 && coverageEndWeek[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousWeekOfCovEndDate = getWeekFormat();
                            String previousWeekOfCovEndDate = coverageEndDate;
                            coverage.setCoverageEndDate(previousWeekOfCovEndDate);
                        } else {
                            coverageEndFlag = false;
                        }
                    } else if (coverageEndDate.matches(weeksAgo)) {
                        String[] coverageEndWeeks = coverageEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageEndWeeks.length > 0 && !coverageEndWeeks[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousWeeksOfCovEndDate = getWeeksFormat(coverageEndWeeks);
                            String previousWeeksOfCovEndDate = coverageEndDate;
                            coverage.setCoverageEndDate(previousWeeksOfCovEndDate);
                        } else {
                            coverageEndFlag = false;
                        }
                    } else if (coverageEndDate.matches(dayAgo)) {
                        String[] coverageEndDay = coverageEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageEndDay.length > 0 && coverageEndDay[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousDayOfCovDate = getDayFormat();
                            String previousDayOfCovDate = coverageEndDate;
                            coverage.setCoverageEndDate(previousDayOfCovDate);
                        } else {
                            coverageEndFlag = false;
                        }
                    } else if (coverageEndDate.matches(daysAgo)) {
                        String[] coverageEndDays = coverageEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (coverageEndDays.length > 0 && !coverageEndDays[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousDaysOfCovEndDate = getDaysFormat(coverageEndDays);
                            String previousDaysOfCovEndDate = coverageEndDate;
                            coverage.setCoverageEndDate(previousDaysOfCovEndDate);
                        } else {
                            coverageEndFlag = false;
                        }
                    } else {
                        coverageEndFlag = false;
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception while validating the coverage end date format in EHoldings " + ex.getMessage());
            throw new RuntimeException();
        }
        return coverageEndFlag;
    }

    private boolean validatePerpetualAccessStartDates(PerpetualAccess perpetualAccess) {
        boolean perpetualAccessStartFlag = true;
        String perpetualAccessStartDate = "";
        try {
            if (perpetualAccess != null) {
                perpetualAccessStartDate = perpetualAccess.getPerpetualAccessStartDate();
                if (StringUtils.isNotEmpty(perpetualAccessStartDate)) {
                    if (perpetualAccessStartDate.matches(convertDateFormatToRegex(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE))) {
                        //String dateFormat = getDateFormat(perpetualAccessStartDate);
                        String dateFormat = perpetualAccessStartDate;
                        perpetualAccess.setPerpetualAccessStartDate(dateFormat);
                    } else if (perpetualAccessStartDate.matches(calendarYearAgo)) {
                        String[] perpetualAccessStartYear = perpetualAccessStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessStartYear.length > 0 && perpetualAccessStartYear[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            /*String previousYearOfPerpetualAccDate = getYearFormat();
                            previousYearOfPerpetualAccDate = getFirstDay(previousYearOfPerpetualAccDate);*/
                            String previousYearOfPerpetualAccDate = perpetualAccessStartDate;
                            perpetualAccess.setPerpetualAccessStartDate(previousYearOfPerpetualAccDate);
                        } else {
                            perpetualAccessStartFlag = false;
                        }
                    } else if (perpetualAccessStartDate.matches(calendarYearsAgo)) {
                        String[] perpetualAccessStartYears = perpetualAccessStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessStartYears.length > 0 && !perpetualAccessStartYears[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            /*String previousYearsOfPerpetualAccDate = getYearsFormat(perpetualAccessStartYears);
                            previousYearsOfPerpetualAccDate = getFirstDay(previousYearsOfPerpetualAccDate);*/
                            String previousYearsOfPerpetualAccDate = perpetualAccessStartDate;
                            perpetualAccess.setPerpetualAccessStartDate(previousYearsOfPerpetualAccDate);
                        } else {
                            perpetualAccessStartFlag = false;
                        }
                    } else if (perpetualAccessStartDate.matches(monthAgo)) {
                        String[] perpetualAccessStartMonth = perpetualAccessStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessStartMonth.length > 0 && perpetualAccessStartMonth[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousMonthOfPerpetualAccDate = getMonthFormat();
                            String previousMonthOfPerpetualAccDate = perpetualAccessStartDate;
                            perpetualAccess.setPerpetualAccessStartDate(previousMonthOfPerpetualAccDate);
                        } else {
                            perpetualAccessStartFlag = false;
                        }
                    } else if (perpetualAccessStartDate.matches(monthsAgo)) {
                        String[] perpetualAccessStartMonths = perpetualAccessStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessStartMonths.length > 0 && !perpetualAccessStartMonths[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousMonthsOfPerpetualAccDate = getMonthsFormat(perpetualAccessStartMonths);
                            String previousMonthsOfPerpetualAccDate = perpetualAccessStartDate;
                            perpetualAccess.setPerpetualAccessStartDate(previousMonthsOfPerpetualAccDate);
                        } else {
                            perpetualAccessStartFlag = false;
                        }
                    } else if (perpetualAccessStartDate.matches(weekAgo)) {
                        String[] perpetualAccessStartWeek = perpetualAccessStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessStartWeek.length > 0 && perpetualAccessStartWeek[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousWeekOfCovEndDate = getWeekFormat();
                            String previousWeekOfCovEndDate = perpetualAccessStartDate;
                            perpetualAccess.setPerpetualAccessStartDate(previousWeekOfCovEndDate);
                        } else {
                            perpetualAccessStartFlag = false;
                        }
                    } else if (perpetualAccessStartDate.matches(weeksAgo)) {
                        String[] perpetualAccessStartWeeks = perpetualAccessStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessStartWeeks.length > 0 && !perpetualAccessStartWeeks[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousWeeksOfCovEndDate = getWeeksFormat(perpetualAccessStartWeeks);
                            String previousWeeksOfCovEndDate = perpetualAccessStartDate;
                            perpetualAccess.setPerpetualAccessStartDate(previousWeeksOfCovEndDate);
                        } else {
                            perpetualAccessStartFlag = false;
                        }
                    } else if (perpetualAccessStartDate.matches(dayAgo)) {
                        String[] perpetualAccessStartDay = perpetualAccessStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessStartDay.length > 0 && perpetualAccessStartDay[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousDayOfPerpetualAccDate = getDayFormat();
                            String previousDayOfPerpetualAccDate = perpetualAccessStartDate;
                            perpetualAccess.setPerpetualAccessStartDate(previousDayOfPerpetualAccDate);
                        } else {
                            perpetualAccessStartFlag = false;
                        }
                    } else if (perpetualAccessStartDate.matches(daysAgo)) {
                        String[] perpetualAccessStartDays = perpetualAccessStartDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessStartDays.length > 0 && !perpetualAccessStartDays[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousDaysOfPerpetualAccDate = getDaysFormat(perpetualAccessStartDays);
                            String previousDaysOfPerpetualAccDate = perpetualAccessStartDate;
                            perpetualAccess.setPerpetualAccessStartDate(previousDaysOfPerpetualAccDate);
                        } else {
                            perpetualAccessStartFlag = false;
                        }
                    } else {
                        perpetualAccessStartFlag = false;
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception while validating the Perpetual access start date format in EHoldings " + ex.getMessage());
            throw new RuntimeException();
        }
        return perpetualAccessStartFlag;
    }

    private boolean validatePerpetualAccessEndDates(PerpetualAccess perpetualAccess) {
        boolean perpetualAccessEndFlag = true;
        String perpetualAccessEndDate = "";
        try {
            if (perpetualAccess != null) {
                perpetualAccessEndDate = perpetualAccess.getPerpetualAccessEndDate();
                if (StringUtils.isNotEmpty(perpetualAccessEndDate)) {
                    if (perpetualAccessEndDate.matches(convertDateFormatToRegex(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE))) {
                        //String dateFormat = getDateFormat(perpetualAccessEndDate);
                        String dateFormat = perpetualAccessEndDate;
                        perpetualAccess.setPerpetualAccessEndDate(dateFormat);
                    } else if (perpetualAccessEndDate.matches(calendarYearAgo)) {
                        String[] perpetualAccessEndYear = perpetualAccessEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessEndYear.length > 0 && perpetualAccessEndYear[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            /*String previousYearOfPerpetualAccDate = getYearFormat();
                            previousYearOfPerpetualAccDate = getLastDay(previousYearOfPerpetualAccDate);*/
                            String previousYearOfPerpetualAccDate = perpetualAccessEndDate;
                            perpetualAccess.setPerpetualAccessEndDate(previousYearOfPerpetualAccDate);
                        } else {
                            perpetualAccessEndFlag = false;
                        }
                    } else if (perpetualAccessEndDate.matches(calendarYearsAgo)) {
                        String[] perpetualAccessEndYears = perpetualAccessEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessEndYears.length > 0 && !perpetualAccessEndYears[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            /*String previousYearsOfPerpetualAccDate = getYearsFormat(perpetualAccessEndYears);
                            previousYearsOfPerpetualAccDate = getLastDay(previousYearsOfPerpetualAccDate);*/
                            String previousYearsOfPerpetualAccDate = perpetualAccessEndDate;
                            perpetualAccess.setPerpetualAccessEndDate(previousYearsOfPerpetualAccDate);
                        } else {
                            perpetualAccessEndFlag = false;
                        }
                    } else if (perpetualAccessEndDate.matches(monthAgo)) {
                        String[] perpetualAccessEndMonth = perpetualAccessEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessEndMonth.length > 0 && perpetualAccessEndMonth[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousMonthOfPerpetualAccDate = getMonthFormat();
                            String previousMonthOfPerpetualAccDate = perpetualAccessEndDate;
                            perpetualAccess.setPerpetualAccessEndDate(previousMonthOfPerpetualAccDate);
                        } else {
                            perpetualAccessEndFlag = false;
                        }
                    } else if (perpetualAccessEndDate.matches(monthsAgo)) {
                        String[] perpetualAccessEndMonths = perpetualAccessEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessEndMonths.length > 0 && !perpetualAccessEndMonths[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousMonthsOfPerpetualAccDate = getMonthsFormat(perpetualAccessEndMonths);
                            String previousMonthsOfPerpetualAccDate = perpetualAccessEndDate;
                            perpetualAccess.setPerpetualAccessEndDate(previousMonthsOfPerpetualAccDate);
                        } else {
                            perpetualAccessEndFlag = false;
                        }
                    } else if (perpetualAccessEndDate.matches(weekAgo)) {
                        String[] perpetualAccessEndWeek = perpetualAccessEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessEndWeek.length > 0 && !perpetualAccessEndWeek[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousWeekOfPerpetualAccDate = getWeekFormat();
                            String previousWeekOfPerpetualAccDate = perpetualAccessEndDate;
                            perpetualAccess.setPerpetualAccessEndDate(previousWeekOfPerpetualAccDate);
                        } else {
                            perpetualAccessEndFlag = false;
                        }
                    } else if (perpetualAccessEndDate.matches(weeksAgo)) {
                        String[] perpetualAccessEndWeeks = perpetualAccessEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessEndWeeks.length > 0 && !perpetualAccessEndWeeks[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousWeeksOfPerpetualAccDate = getWeeksFormat(perpetualAccessEndWeeks);
                            String previousWeeksOfPerpetualAccDate = perpetualAccessEndDate;
                            perpetualAccess.setPerpetualAccessEndDate(previousWeeksOfPerpetualAccDate);
                        } else {
                            perpetualAccessEndFlag = false;
                        }
                    } else if (perpetualAccessEndDate.matches(dayAgo)) {
                        String[] perpetualAccessEndDay = perpetualAccessEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessEndDay.length > 0 && perpetualAccessEndDay[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousDayOfPerpetualAccDate = getDayFormat();
                            String previousDayOfPerpetualAccDate = perpetualAccessEndDate;
                            perpetualAccess.setPerpetualAccessEndDate(previousDayOfPerpetualAccDate);
                        } else {
                            perpetualAccessEndFlag = false;
                        }
                    } else if (perpetualAccessEndDate.matches(daysAgo)) {
                        String[] perpetualAccessEndDays = perpetualAccessEndDate.split(OLEConstants.OLEEResourceRecord.SPACE);
                        if (perpetualAccessEndDays.length > 0 && !perpetualAccessEndDays[0].equals(OLEConstants.OLEEResourceRecord.ONE)) {
                            //String previousDaysOfPerpetualAccDate = getDaysFormat(perpetualAccessEndDays);
                            String previousDaysOfPerpetualAccDate = perpetualAccessEndDate;
                            perpetualAccess.setPerpetualAccessEndDate(previousDaysOfPerpetualAccDate);
                        } else {
                            perpetualAccessEndFlag = false;
                        }
                    } else {
                        perpetualAccessEndFlag = false;
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception while validating the Perpetual access end date format in EHoldings " + ex.getMessage());
            throw new RuntimeException();
        }
        return perpetualAccessEndFlag;
    }

    private String getDateFormat(String perpetualAccessEndDate) {
        Date date = new Date(perpetualAccessEndDate);
        String dateFormat = new SimpleDateFormat(OLEConstants.OLEEResourceRecord.DATE_FORMAT).format(date);
        return dateFormat;
    }

    private String getYearFormat() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date previousYear = calendar.getTime();
        String year = new SimpleDateFormat(OLEConstants.OLEEResourceRecord.DATE_FORMAT).format(previousYear);
        return year;
    }

    private String getYearsFormat(String[] years) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -(Integer.parseInt(years[0])));
        Date previousYears = calendar.getTime();
        String numberOfYears = new SimpleDateFormat(OLEConstants.OLEEResourceRecord.DATE_FORMAT).format(previousYears);
        return numberOfYears;
    }

    private String getMonthFormat() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date previousMonth = calendar.getTime();
        String month = new SimpleDateFormat(OLEConstants.OLEEResourceRecord.DATE_FORMAT).format(previousMonth);
        return month;
    }

    private String getMonthsFormat(String[] months) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -(Integer.parseInt(months[0])));
        Date previousMonths = calendar.getTime();
        String numberOfMonths = new SimpleDateFormat(OLEConstants.OLEEResourceRecord.DATE_FORMAT).format(previousMonths);
        return numberOfMonths;
    }

    private String getWeekFormat() {
        Calendar calendar = Calendar.getInstance();
        int days = 7;
        calendar.add(Calendar.DATE, -(days));
        Date previousWeek = calendar.getTime();
        String week = new SimpleDateFormat(OLEConstants.OLEEResourceRecord.DATE_FORMAT).format(previousWeek);
        return week;
    }

    private String getWeeksFormat(String[] weeks) {
        Calendar calendar = Calendar.getInstance();
        int days = Integer.parseInt(weeks[0]) * 7;
        calendar.add(Calendar.DATE, -(days));
        Date previousWeeks = calendar.getTime();
        String numberOfWeeks = new SimpleDateFormat(OLEConstants.OLEEResourceRecord.DATE_FORMAT).format(previousWeeks);
        return numberOfWeeks;
    }

    private String getDayFormat() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date previousDay = calendar.getTime();
        String day = new SimpleDateFormat(OLEConstants.OLEEResourceRecord.DATE_FORMAT).format(previousDay);
        return day;
    }

    private String getDaysFormat(String[] days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -(Integer.parseInt(days[0])));
        Date previousDays = calendar.getTime();
        String numberOfDays = new SimpleDateFormat(OLEConstants.OLEEResourceRecord.DATE_FORMAT).format(previousDays);
        return numberOfDays;
    }

    private String getFirstDay(String firstDay) {
        String[] date = firstDay.split(OLEConstants.SLASH);
        String yearAlone = "";
        if (date.length > 1) {
            yearAlone = date[2];
        }
        yearAlone = firstDayOfYear + yearAlone;
        return yearAlone;
    }

    private String getLastDay(String lastDay) {
        String[] date = lastDay.split(OLEConstants.SLASH);
        String yearAlone = "";
        if (date.length > 1) {
            yearAlone = date[2];
        }
        yearAlone = lastDayOfYear + yearAlone;
        return yearAlone;
    }

    private void getPOAndInvoiceItemsWithoutDuplicate(OLEEResourceRecordDocument oleERSDoc) {
        List<OLEEResourcePO> oleeResourcePOItems = oleERSDoc.getOleERSPOItems();
        List<OLEEResourceInvoices> oleERSInvoices = oleERSDoc.getOleERSInvoices();
        Map avoidingDuplicateMap = new HashMap<>();
        for (OLEEResourcePO oleeResourcePO : oleeResourcePOItems) {
            avoidingDuplicateMap.put(oleeResourcePO.getOlePOItemId(),oleeResourcePO);
        }
        oleERSDoc.getOleERSPOItems().clear();
        oleERSDoc.getOleERSPOItems().addAll((Collection<? extends OLEEResourcePO>) avoidingDuplicateMap.values());
        oleERSDoc.getLinkedERSPOItems().clear();
        oleERSDoc.getLinkedERSPOItems().addAll((Collection<? extends OLEEResourcePO>) avoidingDuplicateMap.values());
        avoidingDuplicateMap.clear();
        for (OLEEResourceInvoices oleeResourceInvoice : oleERSInvoices) {
            avoidingDuplicateMap.put(oleeResourceInvoice.getInvoiceId(),oleeResourceInvoice);
        }
        oleERSDoc.getOleERSInvoices().clear();
        oleERSDoc.getOleERSInvoices().addAll((Collection<? extends OLEEResourceInvoices>) avoidingDuplicateMap.values());
    }

    public void getAcquisitionInfoFromPOAndInvoice(String holdingsId,WorkEInstanceOlemlForm workEInstanceOlemlForm) {
        if (!DocumentUniqueIDPrefix.hasPrefix(holdingsId)) {
            holdingsId = DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML, holdingsId);
        }
        Map map = new HashMap();
        map.put(OLEConstants.INSTANCE_ID, holdingsId);
        List<OleCopy> oleCopyList = (List) getBusinessObjectService().findMatching(OleCopy.class, map);
        StringBuffer linkedPos = new StringBuffer();
        StringBuffer vendor = new StringBuffer();
        StringBuffer orderType = new StringBuffer();
        StringBuffer orderFormat = new StringBuffer();
        StringBuffer fundCode = new StringBuffer();
        KualiDecimal currentFYCost=new KualiDecimal(0);
        for (OleCopy oleCopy : oleCopyList) {
            if (oleCopy.getPoItemId() != null) {
                map.clear();
                map.put(OLEConstants.OLEEResourceRecord.PO_ITEM_ID, oleCopy.getPoItemId().toString());
                OlePurchaseOrderItem olePurchaseOrderItem = getBusinessObjectService().findByPrimaryKey(OlePurchaseOrderItem.class, map);
                if (olePurchaseOrderItem != null) {
                    // vendor, current FY cost & order type
                    map.clear();
                    map.put(OLEConstants.DOC_NUM, olePurchaseOrderItem.getDocumentNumber());
                    OlePurchaseOrderDocument olePurchaseOrderDocument = getBusinessObjectService().findByPrimaryKey(OlePurchaseOrderDocument.class, map);
                    if (olePurchaseOrderDocument != null ){
                        // po
                        linkedPos.append(olePurchaseOrderDocument.getPurapDocumentIdentifier());
                        linkedPos.append(OLEConstants.COMMA);
                        linkedPos.append(' ');
                        Integer poId = olePurchaseOrderDocument.getPurapDocumentIdentifier();
                        vendor.append(olePurchaseOrderDocument.getVendorName());
                        vendor.append(OLEConstants.COMMA);
                        vendor.append(' ');

                        map.clear();
                        map.put(OLEConstants.PURCHASE_ORDER_TYPE_ID, olePurchaseOrderDocument.getPurchaseOrderTypeId());
                        Collection<PurchaseOrderType> purchaseOrderTypeDocumentList = getBusinessObjectService().findMatching(PurchaseOrderType.class, map);
                        if (purchaseOrderTypeDocumentList != null && purchaseOrderTypeDocumentList.size() > 0) {
                            PurchaseOrderType purchaseOrderTypeDoc = purchaseOrderTypeDocumentList.iterator().next();
                            orderType.append(purchaseOrderTypeDoc.getPurchaseOrderType());
                            orderType.append(OLEConstants.SEMI_COLON);
                            orderType.append(' ');
                        }
                    }
                    // payment status & Fund code
                    map.clear();
                    map.put(OLEConstants.OLEEResourceRecord.INV_PO_ITEM_ID, olePurchaseOrderItem.getItemIdentifier());
                    List<OleInvoiceItem> oleInvoiceItems = (List<OleInvoiceItem>) getBusinessObjectService().findMatching(OleInvoiceItem.class, map);
                    if (oleInvoiceItems != null && oleInvoiceItems.size() > 0) {
                        for (OleInvoiceItem oleInvoiceItem : oleInvoiceItems) {
                            map.put(OLEConstants.OLEEResourceRecord.INV_PO_ITEM_ID, oleInvoiceItem.getItemIdentifier());
                            OlePaymentRequestItem olePaymentRequestItem = getBusinessObjectService().findByPrimaryKey(OlePaymentRequestItem.class, map);
                            if (olePaymentRequestItem != null) {
                                workEInstanceOlemlForm.getExtendedEHoldingFields().setPaymentStatus(OLEConstants.PAID);
                                break;
                            }
                        }
                        // fund code
                        Set<String> oleFundCodeList = new HashSet<>();
                        for (OleInvoiceItem oleInvoiceItem : oleInvoiceItems) {
                            List<PurApAccountingLine> purApAccountingLines = oleInvoiceItem.getSourceAccountingLines();
                            List<String> oleFundCodes = new ArrayList<>();
                            if (purApAccountingLines != null && purApAccountingLines.size() > 0) {
                                oleFundCodes = getFundCodeList(purApAccountingLines);
                                oleFundCodeList.addAll(oleFundCodes);
                            }
                        }
                        if(CollectionUtils.isNotEmpty(oleFundCodeList)){
                            for(String fundCodeId : oleFundCodeList){
                                OleFundCode oleFundCode = new OleFundCode();
                                Map fundCodeMap = new HashMap();
                                fundCodeMap.put("fundCodeId",fundCodeId);
                                oleFundCode = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OleFundCode.class,fundCodeMap);
                                if (oleFundCode != null){
                                    fundCode.append(oleFundCode.getFundCode());
                                    fundCode.append(OLEConstants.COMMA);
                                    fundCode.append(' ');
                                }
                            }
                        }
                        if (fundCode.length() > 0) {
                            fundCode.deleteCharAt(fundCode.length() - 2);
                            workEInstanceOlemlForm.getExtendedEHoldingFields().setFundCode(fundCode.toString());
                        }
                    }
                    // order format
                    if (olePurchaseOrderItem.getFormatTypeId() != null) {
                        map.clear();
                        map.put(OLEConstants.FORMAT_TYPE_ID, olePurchaseOrderItem.getFormatTypeId());
                        OleFormatType oleFormatType = getBusinessObjectService().findByPrimaryKey(OleFormatType.class, map);
                        if (oleFormatType != null) {
                            orderFormat.append(oleFormatType.getFormatTypeName());
                            orderFormat.append(OLEConstants.COMMA);
                            orderFormat.append(' ');
                        }
                    }
                }
            }
        }
        if (linkedPos.length() > 0) {
            if (vendor.length() > 0) {
                String[] holdingVendors = vendor.toString().split(",");
                Set set = new HashSet();
                for (String holdingVendor : holdingVendors) {
                    set.add(holdingVendor.trim());
                }
                holdingVendors =  (String[]) set.toArray(new String[0]);
                StringBuffer vendors = new StringBuffer();
                for(String eholdingVendor : holdingVendors) {
                    vendors.append(eholdingVendor);
                }
                workEInstanceOlemlForm.getExtendedEHoldingFields().setVendorName(vendors.toString());
            }
            if (orderType.length() > 0) {
                String[] holdingOrderTypes = orderType.toString().split(":");
                Set set = new HashSet();
                for (String holdingOrderType : holdingOrderTypes) {
                    set.add(holdingOrderType.trim());
                }
                holdingOrderTypes = (String[]) set.toArray(new String[0]);
                StringBuffer orderTypes = new StringBuffer();
                for(String eholdingOrderType : holdingOrderTypes) {
                    orderTypes.append(eholdingOrderType);
                }
                workEInstanceOlemlForm.getExtendedEHoldingFields().setOrderType(orderTypes.toString());
            }
            if (orderFormat.length() > 0) {
                String[] orderFormats = orderFormat.toString().split(",");
                Set set = new HashSet();
                for (String orderFormatObject : orderFormats) {
                    set.add(orderFormatObject.trim());
                }
                orderFormats =  (String[]) set.toArray(new String[0]);
                StringBuffer eholdingOrderFormats = new StringBuffer();
                for(String eholdingOrderFormat : orderFormats) {
                    eholdingOrderFormats.append(eholdingOrderFormat);
                }
               workEInstanceOlemlForm.getExtendedEHoldingFields().setOrderFormat(eholdingOrderFormats.toString());
            }
            String pos[] = linkedPos.toString().split(",");
            Set set = new HashSet();
            for (String po : pos) {
                set.add(po.trim());
            }
            pos = (String[]) set.toArray(new String[0]);
            StringBuffer poLink = new StringBuffer();
            StringBuffer poNos = new StringBuffer();
            for (String po : pos) {
                String link = null;
                if (StringUtils.isNotBlank(po)) {
                    Map poMap = new HashMap();
                    poMap.put(org.kuali.ole.sys.OLEConstants.PUR_DOC_IDENTIFIER, po.trim());
                    List<OlePurchaseOrderDocument> olePurchaseOrderDocumentList = (List) getBusinessObjectService().findMatching(OlePurchaseOrderDocument.class, poMap);
                    if (olePurchaseOrderDocumentList != null && olePurchaseOrderDocumentList.size() > 0) {
                        for (OlePurchaseOrderDocument olePurchaseOrderDocument : olePurchaseOrderDocumentList) {
                            boolean validPO = olePurchaseOrderDocumentList != null ? olePurchaseOrderDocument.getPurchaseOrderCurrentIndicatorForSearching() : false;
                            if (validPO) {
                                link = ConfigContext.getCurrentContextConfig().getProperty("kew.url") + org.kuali.ole.sys.OLEConstants.PO_LINE_ITEM_URL + olePurchaseOrderDocument.getDocumentNumber();
                                poLink.append("<a href=" + link + " target='_blank'>" + po.trim() + "</a>, ");
                                poNos.append(po.trim());
                                poNos.append(",");
                            }
                        }
                    }
                    currentFYCost = currentFYCost.add(getInvoicedAmount(po.trim()));
                }

            }
            if (poLink.length() > 0) {
                poLink.deleteCharAt(poLink.length() - 2);
                workEInstanceOlemlForm.getExtendedEHoldingFields().setPurchaseOrderId(poLink.toString());
                workEInstanceOlemlForm.getExtendedEHoldingFields().setPurchaseOrderNo(poNos.toString());
            }
            workEInstanceOlemlForm.getExtendedEHoldingFields().setCurrentFYCost(currentFYCost.toString());
            if (org.apache.commons.lang.StringUtils.isBlank(workEInstanceOlemlForm.getExtendedEHoldingFields().getPaymentStatus())) {
                workEInstanceOlemlForm.getExtendedEHoldingFields().setPaymentStatus(OLEConstants.NOT_PAID);
            }
        }
    }

    public KualiDecimal getInvoicedAmount(String poId) {
        BigDecimal itemUnitPrice = BigDecimal.ZERO;
        Integer currentYear = getUniversityDateService().getCurrentFiscalYear();
        Map preqMap = new HashMap();
        preqMap.put(OLEConstants.PUR_ORDER_IDENTIFIER,poId);
        preqMap.put(OLEConstants.POSTING_YEAR,currentYear);
        List<OlePaymentRequestDocument> olePaymentRequestDocumentList = (List) getBusinessObjectService().findMatching(OlePaymentRequestDocument.class, preqMap);
        if(olePaymentRequestDocumentList.size() > 0) {
            for(OlePaymentRequestDocument olePaymentRequestDocument : olePaymentRequestDocumentList) {
                preqMap.clear();
                preqMap.put(OLEConstants.PURAP_DOC_IDENTIFIER,olePaymentRequestDocument.getPurapDocumentIdentifier());
                preqMap.put(OLEConstants.ITEM_TYPE_CODE,OLEConstants.ITEM_TYP_CD_VALUE);
                List<OlePaymentRequestItem> olePaymentRequestItemList = (List) getBusinessObjectService().findMatching(OlePaymentRequestItem.class,preqMap);
                if(olePaymentRequestItemList.size() > 0) {
                    for(OlePaymentRequestItem olePaymentRequestItem : olePaymentRequestItemList) {
                        itemUnitPrice = itemUnitPrice.add(olePaymentRequestItem.getItemUnitPrice());
                    }
                }
            }
        }
        preqMap.clear();
        preqMap.put(OLEConstants.PUR_ORDER_IDENTIFIER,poId);
        preqMap.put(OLEConstants.POSTING_YEAR,currentYear);
        List<OleVendorCreditMemoDocument> vendorCreditMemoDocumentList = (List) getBusinessObjectService().findMatching(OleVendorCreditMemoDocument.class,preqMap);
        if(vendorCreditMemoDocumentList.size() > 0) {
            for(OleVendorCreditMemoDocument vendorCreditMemoDocument : vendorCreditMemoDocumentList) {
                preqMap.clear();
                preqMap.put(OLEConstants.PURAP_DOC_IDENTIFIER,vendorCreditMemoDocument.getPurapDocumentIdentifier());
                preqMap.put(OLEConstants.ITEM_TYPE_CODE,OLEConstants.ITEM_TYP_CD_VALUE);
                List<OleCreditMemoItem> creditMemoItemList = (List) getBusinessObjectService().findMatching(OleCreditMemoItem.class,preqMap);
                if(creditMemoItemList.size() > 0) {
                    for(OleCreditMemoItem creditMemoItem : creditMemoItemList) {
                        itemUnitPrice = itemUnitPrice.subtract(creditMemoItem.getItemUnitPrice());
                    }
                }

            }
        }
        return new KualiDecimal(itemUnitPrice);
    }

    public KualiDecimal getInvoicedAmountForPreviousFY(String poId) {
        BigDecimal itemUnitPrice = BigDecimal.ZERO;
        Integer previousYear = new Integer(SpringContext.getBean(UniversityDateService.class).getCurrentFiscalYear().intValue() - 1);
        Map preqMap = new HashMap();
        preqMap.put(OLEConstants.PUR_ORDER_IDENTIFIER,poId);
        preqMap.put(OLEConstants.POSTING_YEAR,previousYear);
        List<OlePaymentRequestDocument> olePaymentRequestDocumentList = (List) getBusinessObjectService().findMatching(OlePaymentRequestDocument.class, preqMap);
        if(olePaymentRequestDocumentList.size() > 0) {
            for(OlePaymentRequestDocument olePaymentRequestDocument : olePaymentRequestDocumentList) {
                preqMap.clear();
                preqMap.put(OLEConstants.PURAP_DOC_IDENTIFIER,olePaymentRequestDocument.getPurapDocumentIdentifier());
                preqMap.put(OLEConstants.ITEM_TYPE_CODE,OLEConstants.ITEM_TYP_CD_VALUE);
                List<OlePaymentRequestItem> olePaymentRequestItemList = (List) getBusinessObjectService().findMatching(OlePaymentRequestItem.class,preqMap);
                if(olePaymentRequestItemList.size() > 0) {
                    for(OlePaymentRequestItem olePaymentRequestItem : olePaymentRequestItemList) {
                        itemUnitPrice = itemUnitPrice.add(olePaymentRequestItem.getItemUnitPrice());
                    }
                }
            }
        }
        preqMap.clear();
        preqMap.put(OLEConstants.PUR_ORDER_IDENTIFIER,poId);
        preqMap.put(OLEConstants.POSTING_YEAR,previousYear);
        List<OleVendorCreditMemoDocument> vendorCreditMemoDocumentList = (List) getBusinessObjectService().findMatching(OleVendorCreditMemoDocument.class,preqMap);
        if(vendorCreditMemoDocumentList.size() > 0) {
            for(OleVendorCreditMemoDocument vendorCreditMemoDocument : vendorCreditMemoDocumentList) {
                preqMap.clear();
                preqMap.put(OLEConstants.PURAP_DOC_IDENTIFIER,vendorCreditMemoDocument.getPurapDocumentIdentifier());
                preqMap.put(OLEConstants.ITEM_TYPE_CODE,OLEConstants.ITEM_TYP_CD_VALUE);
                List<OleCreditMemoItem> creditMemoItemList = (List) getBusinessObjectService().findMatching(OleCreditMemoItem.class,preqMap);
                if(creditMemoItemList.size() > 0) {
                    for(OleCreditMemoItem creditMemoItem : creditMemoItemList) {
                        itemUnitPrice = itemUnitPrice.subtract(creditMemoItem.getItemUnitPrice());
                    }
                }

            }
        }
        return new KualiDecimal(itemUnitPrice);
    }

    public KualiDecimal getInvoicedAmountForTwoPreviousFY(String poId) {
        BigDecimal itemUnitPrice = BigDecimal.ZERO;
        Integer previousYear = new Integer(SpringContext.getBean(UniversityDateService.class).getCurrentFiscalYear().intValue() - 2);
        Map preqMap = new HashMap();
        preqMap.put(OLEConstants.PUR_ORDER_IDENTIFIER,poId);
        preqMap.put(OLEConstants.POSTING_YEAR,previousYear);
        List<OlePaymentRequestDocument> olePaymentRequestDocumentList = (List) getBusinessObjectService().findMatching(OlePaymentRequestDocument.class, preqMap);
        if(olePaymentRequestDocumentList.size() > 0) {
            for(OlePaymentRequestDocument olePaymentRequestDocument : olePaymentRequestDocumentList) {
                preqMap.clear();
                preqMap.put(OLEConstants.PURAP_DOC_IDENTIFIER,olePaymentRequestDocument.getPurapDocumentIdentifier());
                preqMap.put(OLEConstants.ITEM_TYPE_CODE,OLEConstants.ITEM_TYP_CD_VALUE);
                List<OlePaymentRequestItem> olePaymentRequestItemList = (List) getBusinessObjectService().findMatching(OlePaymentRequestItem.class,preqMap);
                if(olePaymentRequestItemList.size() > 0) {
                    for(OlePaymentRequestItem olePaymentRequestItem : olePaymentRequestItemList) {
                        itemUnitPrice = itemUnitPrice.add(olePaymentRequestItem.getItemUnitPrice());
                    }
                }
            }
        }
        preqMap.clear();
        preqMap.put(OLEConstants.PUR_ORDER_IDENTIFIER,poId);
        preqMap.put(OLEConstants.POSTING_YEAR,previousYear);
        List<OleVendorCreditMemoDocument> vendorCreditMemoDocumentList = (List) getBusinessObjectService().findMatching(OleVendorCreditMemoDocument.class,preqMap);
        if(vendorCreditMemoDocumentList.size() > 0) {
            for(OleVendorCreditMemoDocument vendorCreditMemoDocument : vendorCreditMemoDocumentList) {
                preqMap.clear();
                preqMap.put(OLEConstants.PURAP_DOC_IDENTIFIER,vendorCreditMemoDocument.getPurapDocumentIdentifier());
                preqMap.put(OLEConstants.ITEM_TYPE_CODE,OLEConstants.ITEM_TYP_CD_VALUE);
                List<OleCreditMemoItem> creditMemoItemList = (List) getBusinessObjectService().findMatching(OleCreditMemoItem.class,preqMap);
                if(creditMemoItemList.size() > 0) {
                    for(OleCreditMemoItem creditMemoItem : creditMemoItemList) {
                        itemUnitPrice = itemUnitPrice.subtract(creditMemoItem.getItemUnitPrice());
                    }
                }

            }
        }
        return new KualiDecimal(itemUnitPrice);
    }

    private List<String> getFundCodeList(List<PurApAccountingLine> purApAccountingLines) {
        int totalInvoiceAccountLine = purApAccountingLines.size();
        List<List<String>> oleFundCodeIdList = new ArrayList<>();
        List<String> fundCodes = new ArrayList<>();
        for(PurApAccountingLine purApAccountingLine : purApAccountingLines){
            List<OleFundCodeAccountingLine> oleFundCodeAccountingLines = new ArrayList<>();
            OLESelectDaoOjb oleSelectDaoOjb = (OLESelectDaoOjb) SpringContext.getBean("oleSelectDaoOjb");
            if(oleSelectDaoOjb != null){
                oleFundCodeAccountingLines = oleSelectDaoOjb.getFundCodeList(purApAccountingLine);
            }
            if(CollectionUtils.isNotEmpty(oleFundCodeAccountingLines)){
                List<String> fundCodeIdList = new ArrayList<>();
                for (Iterator<OleFundCodeAccountingLine> iterator = oleFundCodeAccountingLines.iterator(); iterator.hasNext(); ) {
                    OleFundCodeAccountingLine oleFundCodeAccountingLine = iterator.next();
                    fundCodeIdList.add(oleFundCodeAccountingLine.getFundCodeId());
                }
                if(CollectionUtils.isNotEmpty(fundCodeIdList)){
                    oleFundCodeIdList.add(fundCodeIdList);
                }
            }
        }
        if(CollectionUtils.isNotEmpty(oleFundCodeIdList) && oleFundCodeIdList.size() == totalInvoiceAccountLine){
            List<String> oleFundCodeList = oleFundCodeIdList.get(0);
            for(int index = 1; index < oleFundCodeIdList.size();index++){
                List<String> fundCodeList = oleFundCodeIdList.get(index);
                oleFundCodeList.retainAll(fundCodeList);
            }
            fundCodes.addAll(oleFundCodeList);
        }
        return fundCodes;
    }

    private void updateEResInOleCopy(Holdings holdings, OLEEResourceRecordDocument oleERSDoc) {
        Map<String, String> criteriaMap = new HashMap<>();
        criteriaMap.put(OLEConstants.INSTANCE_ID, holdings.getId());
        List<OleCopy> copies = (List<OleCopy>) getBusinessObjectService().findMatching(OleCopy.class,
                criteriaMap);
        if (copies.size() > 0) {
            oleERSDoc.getCopyList().addAll(copies);
        } else {
            List<OleCopy> newCopies = new ArrayList<OleCopy>();
            OleCopy oleCopy = new OleCopy();
            oleCopy.setBibId(holdings.getBib().getId());
            oleCopy.setOleERSIdentifier(oleERSDoc.getOleERSIdentifier() != null ? oleERSDoc.getOleERSIdentifier() : "");
            oleCopy.setInstanceId(holdings.getId());
            newCopies.add(oleCopy);
            oleERSDoc.getCopyList().addAll(newCopies);
        }
    }

    @Override
    public void removeDuplicateEresDocumentsFromList(List<OLEEResourceRecordDocument> eresourceDocumentList) {
        Map eresourceMap = new HashMap();
        List eResourceList = new ArrayList();
        for (OLEEResourceRecordDocument oleEResourceRecordDocument : eresourceDocumentList) {
            eresourceMap.put(oleEResourceRecordDocument.getDocumentNumber(), oleEResourceRecordDocument);
        }
        eResourceList.addAll((Set) eresourceMap.keySet());
        eresourceDocumentList.clear();
        for (int eResourceCount = 0; eResourceCount < eResourceList.size(); eResourceCount++) {
            eresourceDocumentList.add((OLEEResourceRecordDocument) eresourceMap.get(eResourceList.get(eResourceCount)));
        }
    }

    @Override
    public List<OLEEResourceRecordDocument> filterEResRecBystatusDate(Date beginDate, Date endDate,List<OLEEResourceRecordDocument> eresourceList) {
        List<OLEEResourceRecordDocument> eresourceDocumentList = new ArrayList<OLEEResourceRecordDocument>();
        try {
            String begin = null;
            if (beginDate != null) {
                begin = dateFormat.format(beginDate);
            }
            String end = null;
            if (endDate != null) {
                end = dateFormat.format(endDate);
            }
            boolean isValid = false;
            eresourceDocumentList.clear();
            for (OLEEResourceRecordDocument oleEResourceRecordDocumentList : eresourceList) {
                String status = oleEResourceRecordDocumentList.getStatusDate();
                Date statusDate = simpleDateFormat.parse(status);
                OleLicenseRequestServiceImpl oleLicenseRequestService = GlobalResourceLoader.getService(OLEConstants.OleLicenseRequest.LICENSE_REQUEST_SERVICE);
                isValid = oleLicenseRequestService.validateDate(statusDate, begin, end);
                if (isValid) {
                    eresourceDocumentList.add(oleEResourceRecordDocumentList);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception while calling the licenseRequest service" + e.getMessage());
            throw new RuntimeException(e);
        }
        return eresourceDocumentList;
    }

    @Override
    public List<RequisitionItem> generateItemList(OLEEResourceOrderRecord oleEResourceOrderRecord, OleRequisitionDocument requisitionDocument) throws Exception {
        List<RequisitionItem> items = new ArrayList<RequisitionItem>();
        int itemLineNumber = 1;
        items.add(createRequisitionItem(oleEResourceOrderRecord, itemLineNumber, requisitionDocument));
        return items;
    }

    @Override
    public List<RequisitionItem> generateMultipleItemsForOneRequisition(List<OLEEResourceOrderRecord> oleEResourceOrderRecordList, OleRequisitionDocument requisitionDocument) throws Exception {
        List<RequisitionItem> items = new ArrayList<RequisitionItem>();
        int itemLineNumber = 0;
        for (OLEEResourceOrderRecord oleEResourceOrderRecord : oleEResourceOrderRecordList) {
            itemLineNumber++;
            items.add(createRequisitionItem(oleEResourceOrderRecord, itemLineNumber, requisitionDocument));
        }
        return items;
    }

    private RequisitionItem createRequisitionItem(OLEEResourceOrderRecord oleEResourceOrderRecord, int itemLineNumber, OleRequisitionDocument requisitionDocument) throws Exception {
        OleRequisitionItem item = new OleRequisitionItem();
        item.setOleEResourceOrderRecord(oleEResourceOrderRecord);
        item.setItemLineNumber(itemLineNumber);
        item.setItemUnitOfMeasureCode(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.UOM));
        item.setItemQuantity(new KualiDecimal(oleEResourceOrderRecord.getOleEResourceTxnRecord().getQuantity()));
        item.setItemNoOfParts(new KualiInteger(oleEResourceOrderRecord.getOleEResourceTxnRecord().getItemNoOfParts()));
        item.setItemUnitPrice(new BigDecimal(oleEResourceOrderRecord.getOleEResourceTxnRecord().getListPrice()));
        item.setItemTypeCode(oleEResourceOrderRecord.getOleEResourceTxnRecord().getItemType());
        item.setItemListPrice(new KualiDecimal(oleEResourceOrderRecord.getOleEResourceTxnRecord().getListPrice()));
        item.setItemLocation(oleEResourceOrderRecord.getOleEResourceTxnRecord().getDefaultLocation());
        if (ObjectUtils.isNotNull(oleEResourceOrderRecord.getOleBibRecord()) && ObjectUtils.isNotNull(oleEResourceOrderRecord.getOleBibRecord().getBibUUID())) {
            item.setItemTitleId(oleEResourceOrderRecord.getOleBibRecord().getBibUUID());
        } else if (StringUtils.isNotBlank(oleEResourceOrderRecord.getOleERSIdentifier())) {
            item.setOleERSIdentifier(oleEResourceOrderRecord.getOleERSIdentifier());
        }
        if (oleEResourceOrderRecord.getBibTree()!=null){
            item.setBibTree(oleEResourceOrderRecord.getBibTree());
        }
        item.setPurposeId(oleEResourceOrderRecord.getOleEResourceTxnRecord().getPurposeId());
        item.setLinkToOrderOption(oleEResourceOrderRecord.getLinkToOrderOption());
        org.kuali.ole.module.purap.businessobject.ItemType itemType = getBusinessObjectService().findBySinglePrimaryKey(org.kuali.ole.module.purap.businessobject.ItemType.class, "ITEM");
        item.setItemType(itemType);

        List<PurApAccountingLine> sourceAccountingLines = new ArrayList<>();
        if (oleEResourceOrderRecord.getOleEResourceTxnRecord().getCretePOAccountingLines() != null) {
            for (OLECretePOAccountingLine cretePOAccountingLine : oleEResourceOrderRecord.getOleEResourceTxnRecord().getCretePOAccountingLines()) {
                RequisitionAccount requisitionAccount = new RequisitionAccount();
                requisitionAccount.setChartOfAccountsCode(cretePOAccountingLine.getChartOfAccountsCode());
                requisitionAccount.setAccountNumber(cretePOAccountingLine.getAccountNumber());
                if (StringUtils.isNotBlank(cretePOAccountingLine.getSubAccountNumber())){
                    requisitionAccount.setSubAccountNumber(cretePOAccountingLine.getSubAccountNumber());
                }
                requisitionAccount.setFinancialObjectCode(cretePOAccountingLine.getFinancialObjectCode());
                if (StringUtils.isNotBlank(cretePOAccountingLine.getFinancialSubObjectCode())){
                    requisitionAccount.setFinancialSubObjectCode(cretePOAccountingLine.getFinancialSubObjectCode());
                }
                if (StringUtils.isNotBlank(cretePOAccountingLine.getProjectCode())){
                    requisitionAccount.setProjectCode(cretePOAccountingLine.getProjectCode());
                }
                if (StringUtils.isNotBlank(cretePOAccountingLine.getOrganizationReferenceId())){
                    requisitionAccount.setOrganizationReferenceId(cretePOAccountingLine.getOrganizationReferenceId());
                }
                requisitionAccount.setDebitCreditCode(OLEConstants.GL_DEBIT_CODE);
                if (StringUtils.isNotBlank(oleEResourceOrderRecord.getOleEResourceTxnRecord().getListPrice())) {
                    requisitionAccount.setAmount(new KualiDecimal(oleEResourceOrderRecord.getOleEResourceTxnRecord().getListPrice()));
                }
                requisitionAccount.setAccountLinePercent(cretePOAccountingLine.getAccountLinePercent());
                sourceAccountingLines.add(requisitionAccount);
            }
        }
        item.setSourceAccountingLines(sourceAccountingLines);

        if (oleEResourceOrderRecord.getOleBibRecord() != null) {
            setItemDescription(oleEResourceOrderRecord, item);
        } else if (StringUtils.isNotBlank(oleEResourceOrderRecord.getOleERSIdentifier())) {
            OLEEResourceRecordDocument oleeResourceRecordDocument = getBusinessObjectService().findBySinglePrimaryKey(OLEEResourceRecordDocument.class, oleEResourceOrderRecord.getOleERSIdentifier());
            if (oleeResourceRecordDocument != null) {
                item.setItemDescription(oleeResourceRecordDocument.getTitle());
            }
        }
        setForeignCurrencyDetails(item,requisitionDocument);
        return item;
    }

    private void setForeignCurrencyDetails(OleRequisitionItem item,OleRequisitionDocument requisitionDocument){
        boolean currencyTypeIndicator = true;
        if (requisitionDocument.getVendorDetail().getCurrencyType()!=null){
            if(requisitionDocument.getVendorDetail().getCurrencyType().getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)){
                currencyTypeIndicator=true;
            }
            else{
                currencyTypeIndicator=false;
            }
        }
        if(!currencyTypeIndicator){
            item.setItemForeignListPrice(item.getItemListPrice());
            item.setItemForeignDiscountType(item.getItemDiscountType());
            item.setItemForeignDiscount(item.getItemDiscount());
            item.setItemListPrice(new KualiDecimal(0.00));
            getOlePurapService().calculateForeignCurrency(item);
            Long currencyTypeId = requisitionDocument.getVendorDetail().getCurrencyType().getCurrencyTypeId();
            Map documentNumberMap = new HashMap();
            documentNumberMap.put(OleSelectConstant.CURRENCY_TYPE_ID, currencyTypeId);
            List<OleExchangeRate> exchangeRateList = (List) getBusinessObjectService().findMatchingOrderBy(OleExchangeRate.class, documentNumberMap, OleSelectConstant.EXCHANGE_RATE_DATE, false);
            Iterator iterator = exchangeRateList.iterator();
            if (iterator.hasNext()) {
                OleExchangeRate tempOleExchangeRate = (OleExchangeRate) iterator.next();
                item.setItemExchangeRate(tempOleExchangeRate.getExchangeRate());
            }
            if (item.getItemExchangeRate() != null && item.getItemForeignUnitCost() != null) {
                item.setItemUnitCostUSD(new KualiDecimal(item.getItemForeignUnitCost().bigDecimalValue().divide(item.getItemExchangeRate(), 4, RoundingMode.HALF_UP)));
                item.setItemUnitPrice(item.getItemUnitCostUSD().bigDecimalValue());
                item.setItemListPrice(item.getItemUnitCostUSD());
            }
        }
    }

    @Override
    public List<OLEEResourceOrderRecord> fetchOleOrderRecordList(List<OLECreatePO> createPOs, String linkToOrderOption, String location) throws Exception {
        List<OLEEResourceOrderRecord> oleEResourceOrderRecordList = new ArrayList<>();
        for (OLECreatePO createPO : createPOs) {
            OLEEResourceOrderRecord oleEResourceOrderRecord = new OLEEResourceOrderRecord();
            oleEResourceOrderRecord.addMessageToMap(OLEConstants.IS_VALID_RECORD, true);
            oleEResourceOrderRecord.addMessageToMap(OLEConstants.IS_APO_RULE, true);
            oleEResourceOrderRecord.setLinkToOrderOption(linkToOrderOption);

            if (createPO.getBibId() != null) {
                BibId bibTree = new BibId();
                bibTree.setId(createPO.getBibId());
                HoldingsId holdingsId = new HoldingsId();
                holdingsId.setId(createPO.getInstanceId());
                bibTree.getHoldingsIds().add(holdingsId);
                oleEResourceOrderRecord.setBibTree(bibTree);
                oleEResourceOrderRecord.setBibId(bibTree.getId());

                OleBibRecord oleBibRecord = new OleBibRecord();
                oleBibRecord.setBibUUID(createPO.getBibId());
                oleBibRecord.setBib(getDocstoreClientLocator().getDocstoreClient().retrieveBib(createPO.getBibId()));
                oleEResourceOrderRecord.setOleBibRecord(oleBibRecord);
            }else if (StringUtils.isNotBlank(createPO.getOleERSIdentifier())){
                oleEResourceOrderRecord.setOleERSIdentifier(createPO.getOleERSIdentifier());
            }

            OLEEResourceTxnRecord oleEResourceTxnRecord = new OLEEResourceTxnRecord();
            oleEResourceTxnRecord.setCostSource(getParameter(OLEConstants.OLEEResourceRecord.COST_SOURCE,OLEConstants.ERESOURCE_CMPNT));
            oleEResourceTxnRecord.setMethodOfPOTransmission(getParameter(OLEConstants.OLEEResourceRecord.METHOD_OF_PO_TRANSMISSION, OLEConstants.ERESOURCE_CMPNT));
            oleEResourceTxnRecord.setDefaultLocation(location);
            oleEResourceTxnRecord.setVendorNumber(createPO.getVendorId());
            oleEResourceTxnRecord.setDeliveryBuildingRoomNumber(getParameter(OLEConstants.OLEEResourceRecord.BUILDING_ROOM_NUMBER, OLEConstants.ERESOURCE_CMPNT));
            oleEResourceTxnRecord.setDeliveryCampusCode(getParameter(OLEConstants.OLEEResourceRecord.DELIVERY_CAMPUS_CODE, OLEConstants.ERESOURCE_CMPNT));
            oleEResourceTxnRecord.setBuildingCode(getParameter(OLEConstants.OLEEResourceRecord.BUILDING_CODE, OLEConstants.ERESOURCE_CMPNT));
            if (StringUtils.isNotBlank(createPO.getPrice())){
                oleEResourceTxnRecord.setListPrice(createPO.getPrice());
            }else {
                oleEResourceTxnRecord.setListPrice("0");
            }
            oleEResourceTxnRecord.setChartCode(getParameter(OLEConstants.OLEEResourceRecord.CHART_CODE, OLEConstants.ERESOURCE_CMPNT));
            oleEResourceTxnRecord.setOrgCode(getParameter(OLEConstants.OLEEResourceRecord.ORG_CODE, OLEConstants.ERESOURCE_CMPNT));
            oleEResourceTxnRecord.setFundingSource(getParameter(OLEConstants.OLEEResourceRecord.FUNDING_SOURCE, OLEConstants.ERESOURCE_CMPNT));
            oleEResourceTxnRecord.setCretePOAccountingLines(createPO.getAccountingLines());
            oleEResourceTxnRecord.setItemType(OLEConstants.ITEM_TYP);
            oleEResourceTxnRecord.setRequisitionSource(OleSelectConstant.REQUISITON_SRC_TYPE_AUTOINGEST);
            if (StringUtils.isNotBlank(createPO.getInstanceFlag()) && createPO.getInstanceFlag().equalsIgnoreCase("true")) {
                oleEResourceTxnRecord.setQuantity("1");
                oleEResourceTxnRecord.setItemNoOfParts("1");
            }else {
                oleEResourceTxnRecord.setQuantity(getParameter(OLEConstants.OLEEResourceRecord.QUANTITY, OLEConstants.ERESOURCE_CMPNT));
                oleEResourceTxnRecord.setItemNoOfParts(getParameter(OLEConstants.OLEEResourceRecord.NO_OF_PARTS, OLEConstants.ERESOURCE_CMPNT));
            }
            oleEResourceTxnRecord.setOrderType(createPO.getOrderTypeId());
            oleEResourceTxnRecord.setPurposeId(createPO.getPurposeId());
            oleEResourceOrderRecord.setOleEResourceTxnRecord(oleEResourceTxnRecord);
            oleEResourceOrderRecordList.add(oleEResourceOrderRecord);
        }
        return oleEResourceOrderRecordList;
    }

    /*public void setUserForOrderRecords(List<OleOrderRecord> oleOrderRecords) {
        String user = null;
        if (oleOrderRecords.size() > 0) {
            user = GlobalVariables.getUserSession().getPrincipalName();
            if (user == null) {
                user = getConfigurationService().getPropertyValueAsString(
                        OleSelectNotificationConstant.ACCOUNT_DOCUMENT_INTIATOR);
            }
            GlobalVariables.setUserSession(new UserSession(user));
        }
    }*/

    @Override
    public String validateAccountngLinesVendorAndPrice(OLECreatePO createPO) {
        String validationMsg = new String();
        BigDecimal totalPercentage = BigDecimal.ZERO;
        BigDecimal desiredPercent = new BigDecimal("100");
        if (createPO.getAccountingLines()!=null && createPO.getAccountingLines().size()>0){
            for (OLECretePOAccountingLine cretePOAccountingLine : createPO.getAccountingLines()){
                validationMsg = validateAccountingLines(validationMsg, cretePOAccountingLine);
                totalPercentage = totalPercentage.add(cretePOAccountingLine.getAccountLinePercent());
            }
            if (desiredPercent.compareTo(totalPercentage) != 0) {
                validationMsg = validationMsg.concat("The total percent of the accounts must equal 100%");
            }
        }else {
            validationMsg = validationMsg.concat("Atleast one accounting line should be there");
        }
        if (StringUtils.isBlank(createPO.getVendorId())) {
            if (validationMsg.length() > 1) {
                validationMsg = validationMsg.concat(", ");
            }
            validationMsg = validationMsg.concat("Vendor is required");
        }
        if (StringUtils.isNotBlank(createPO.getPrice())) {
            try {
                new BigDecimal(createPO.getPrice());
            } catch (Exception e) {
                if (validationMsg.length() > 1) {
                    validationMsg = validationMsg.concat(" and ");
                }
                validationMsg = validationMsg.concat("Invalid Estimated Price");
            }

        }
        if (validationMsg.length() > 1) {
            validationMsg = validationMsg.concat(" for '" + createPO.getTitle() + "'");
            validationMsg = validationMsg.concat(OLEConstants.BREAK);
        }
        return validationMsg;
    }

    @Override
    public String getParameter(String parameterName, String componentName) {
        LOG.debug("Inside getParameter()");
        String parameter = "";
        try {
            Map<String, String> criteriaMap = new HashMap<String, String>();
            criteriaMap.put(OLEConstants.NAMESPACE_CODE, OLEConstants.SELECT_NMSPC);
            criteriaMap.put(OLEConstants.COMPONENT_CODE, componentName);
            criteriaMap.put(OLEConstants.NAME_SELECTOR, parameterName);
            List<ParameterBo> parametersList = (List<ParameterBo>) getBusinessObjectService().findMatching(ParameterBo.class, criteriaMap);
            for (ParameterBo parameterBo : parametersList) {
                parameter = parameterBo.getValue();
            }
        } catch (Exception e) {
            LOG.error("Exception while getting parameter value", e);
        }
        LOG.debug("End of getParameter()");
        return parameter;
    }

    @Override
    public void getBannerMessage(OLEEResourceRecordDocument oleEResourceRecordDocument) {
        Set<String> platformIds = new HashSet<>();
        String bannerMessage = new String();
        for (OLEEResourceInstance eResourceInstance : oleEResourceRecordDocument.getOleERSInstances()) {
            if (StringUtils.isNotBlank(eResourceInstance.getPlatformId())) {
                platformIds.add(eResourceInstance.getPlatformId());
            }
        }
        if (platformIds.size() > 0) {
                Map platformMap = new HashMap();
            platformMap.put(OLEConstants.OLE_PLATFORM_ID, platformIds);
            List<OLEPlatformRecordDocument> olePlatformRecordDocumentList = (List<OLEPlatformRecordDocument>) getBusinessObjectService().findMatching(OLEPlatformRecordDocument.class, platformMap);
            if (CollectionUtils.isNotEmpty(olePlatformRecordDocumentList)) {
                for (OLEPlatformRecordDocument olePlatformRecordDocument : olePlatformRecordDocumentList) {
                if (olePlatformRecordDocument != null) {
                    List<OLEPlatformEventLog> olePlatformEventLogList = olePlatformRecordDocument.getEventLogs();
                    for (OLEPlatformEventLog olePlatformEventLog : olePlatformEventLogList) {
                            if (StringUtils.isNotBlank(olePlatformEventLog.getLogTypeId()) && olePlatformEventLog.getLogTypeId().equals("2")) {
                                if (StringUtils.isNotBlank(olePlatformEventLog.getEventStatus()) && !olePlatformEventLog.getEventStatus().equals("Resolved")) {
                                    bannerMessage = bannerMessage.concat("NOTICE: " + olePlatformRecordDocument.getName() + " is experiencing a problem " + makeUrlClickable(olePlatformRecordDocument.getDocumentNumber()) + " for more details.");
                                    bannerMessage = bannerMessage.concat(OLEConstants.BREAK);
                                break;
                            }
                        }
                    }
                }
            }
        }
        }
        oleEResourceRecordDocument.setBannerMessage(bannerMessage);
    }

    @Override
    public List<OLECreatePO> getInstances(OLEEResourceRecordDocument oleeResourceRecordDocument, String purposeId){
        List<OLEEResourceInstance> oleERSInstances = oleeResourceRecordDocument.getOleERSInstances();
        List<OLECreatePO> instancePOs = new ArrayList<>();
        for (OLEEResourceInstance oleeResourceInstance : oleERSInstances) {
            boolean poExists = false;
            boolean saveChanges = false;
            Map<String, String> criteriaMap = new HashMap<String, String>();
            criteriaMap.put(OLEConstants.INSTANCE_ID, oleeResourceInstance.getInstanceId());
            List<OleCopy> copies = (List<OleCopy>) getBusinessObjectService().findMatching(OleCopy.class, criteriaMap);
            for (OleCopy copy : copies) {
                if (copy.getPoItemId() != null) {
                    Map<String, String> criteriaPOIdMap = new HashMap<String, String>();
                    criteriaPOIdMap.put(OLEConstants.OLEEResourceRecord.PO_ITEM_ID, copy.getPoItemId().toString());
                    List<OlePurchaseOrderItem> olePurchaseOrderItems = (List<OlePurchaseOrderItem>) getBusinessObjectService().findMatching(OlePurchaseOrderItem.class, criteriaPOIdMap);
                    if (olePurchaseOrderItems.size() > 0) {
                        for (OlePurchaseOrderItem olePurchaseOrderItem : olePurchaseOrderItems) {
                            Map map = new HashMap();
                            map.put(OLEConstants.DOC_NUM, olePurchaseOrderItem.getDocumentNumber());
                            OlePurchaseOrderDocument olePurchaseOrderDocument = getBusinessObjectService().findByPrimaryKey(OlePurchaseOrderDocument.class, map);
                            if (olePurchaseOrderDocument != null) {
                                poExists = true;
                                OLECreatePO instancePO = new OLECreatePO();
                                instancePO.setPoId(olePurchaseOrderDocument.getPurapDocumentIdentifier().toString());
                                instancePO.setInstanceFlag(oleeResourceInstance.getInstanceFlag());
                                instancePO.setInstanceId(oleeResourceInstance.getInstanceId());
                                instancePO.setBibId(oleeResourceInstance.getBibId());
                                instancePO.setTitle(oleeResourceInstance.getInstanceTitle());
                                instancePO.setGokbId("0002");
                                instancePO.setIsbnNIssn(oleeResourceRecordDocument.getIsbn());
                                instancePO.setVendorId(olePurchaseOrderDocument.getVendorNumber());
                                instancePO.setOrderTypeId(olePurchaseOrderDocument.getPurchaseOrderTypeId().toString());
                                instancePO.setPrice(olePurchaseOrderItem.getItemListPrice().toString());
                                instancePO.setPurposeId(olePurchaseOrderItem.getPurposeId());
                                List<OLECretePOAccountingLine> accountingLines = new ArrayList<>();
                                if (olePurchaseOrderItem.getSourceAccountingLines() != null && olePurchaseOrderItem.getSourceAccountingLines().size() > 0) {
                                    for (PurApAccountingLine purApAccountingLine : olePurchaseOrderItem.getSourceAccountingLines()){
                                        OLECretePOAccountingLine oleCretePOAccountingLine = new OLECretePOAccountingLine();
                                        oleCretePOAccountingLine.setChartOfAccountsCode(purApAccountingLine.getChartOfAccountsCode());
                                        oleCretePOAccountingLine.setAccountNumber(purApAccountingLine.getAccountNumber());
                                        oleCretePOAccountingLine.setSubAccountNumber(purApAccountingLine.getSubAccountNumber());
                                        oleCretePOAccountingLine.setFinancialObjectCode(purApAccountingLine.getFinancialObjectCode());
                                        oleCretePOAccountingLine.setFinancialSubObjectCode(purApAccountingLine.getFinancialSubObjectCode());
                                        oleCretePOAccountingLine.setProjectCode(purApAccountingLine.getProjectCode());
                                        oleCretePOAccountingLine.setOrganizationReferenceId(purApAccountingLine.getOrganizationReferenceId());
                                        oleCretePOAccountingLine.setAccountLinePercent(purApAccountingLine.getAccountLinePercent());
                                        accountingLines.add(oleCretePOAccountingLine);
                                    }
                                    instancePO.setAccountingLines(accountingLines);
                                }
                                instancePOs.add(instancePO);
                            }
                        }
                    }
                }
            }
            Map createPoMap = new HashMap();
            createPoMap.put(OLEConstants.OLEEResourceRecord.ERESOURCE_IDENTIFIER, oleeResourceRecordDocument.getOleERSIdentifier());
            List<OLECreatePO> oleCreatePOs = (List<OLECreatePO>) getBusinessObjectService().findMatching(OLECreatePO.class,createPoMap);
            if (oleCreatePOs!=null && oleCreatePOs.size()>0){
                for (OLECreatePO oleCreatePO : oleCreatePOs){
                    if (StringUtils.isNotBlank(oleCreatePO.getInstanceId())){
                        saveChanges =true;
                        OLECreatePO instancePO = new OLECreatePO();
                        instancePO.setSelectFlag(true);
                        instancePO.setOleERSIdentifier(oleeResourceRecordDocument.getOleERSIdentifier());
                        instancePO.setInstanceFlag(oleCreatePO.getInstanceFlag());
                        instancePO.setInstanceId(oleCreatePO.getInstanceId());
                        instancePO.setBibId(oleeResourceInstance.getBibId());
                        instancePO.setTitle(oleeResourceInstance.getInstanceTitle());
                        instancePO.setGokbId("0002");
                        instancePO.setIsbnNIssn(oleeResourceRecordDocument.getIsbn());
                        instancePO.setVendorId(oleCreatePO.getVendorId());
                        instancePO.setOrderTypeId(oleCreatePO.getOrderTypeId().toString());
                        instancePO.setPrice(oleCreatePO.getPrice());
                        instancePO.setPurposeId(oleCreatePO.getPurposeId());
                        instancePO.setAccountingLines(oleCreatePO.getAccountingLines());
                        instancePOs.add(instancePO);
                    }
                }
            }
            if (!poExists && !saveChanges) {
                OLECreatePO instancePO = new OLECreatePO();
                instancePO.setOleERSIdentifier(oleeResourceRecordDocument.getOleERSIdentifier());
                instancePO.setInstanceFlag(oleeResourceInstance.getInstanceFlag());
                instancePO.setInstanceId(oleeResourceInstance.getInstanceId());
                instancePO.setBibId(oleeResourceInstance.getBibId());
                instancePO.setTitle(oleeResourceInstance.getInstanceTitle());
                instancePO.setGokbId("0002");
                instancePO.setAccountingLines(getOLECretePOAccountingLines(oleeResourceRecordDocument.getAccountingLines()));
                instancePO.setIsbnNIssn(oleeResourceRecordDocument.getIsbn());
                instancePO.setVendorId(oleeResourceRecordDocument.getVendorId());
                instancePO.setOrderTypeId(oleeResourceRecordDocument.getOrderTypeId().toString());
                instancePO.setPrice(oleeResourceRecordDocument.getEstimatedPrice());
                instancePO.setPurposeId(purposeId);
                instancePOs.add(instancePO);
            }
        }
        List<OLECreatePO> instancePOsWithoutDuplicate = removeDuplicateForPOInstanceAndEResource(instancePOs);
        return instancePOsWithoutDuplicate;
    }


    private List<OLECreatePO> removeDuplicateForPOInstanceAndEResource(List<OLECreatePO> objectList) {
        Map avoidingDuplicateMap = new HashMap<>();
        for (OLECreatePO oleCreatePO : objectList) {
            avoidingDuplicateMap.put(oleCreatePO.getPoId(),oleCreatePO);
        }
        objectList.clear();
        objectList.addAll((Collection<? extends OLECreatePO>) avoidingDuplicateMap.values());
        return objectList;
    }

    @Override
    public List<OLECreatePO> getEresources(OLEEResourceRecordDocument oleeResourceRecordDocument, String purposeId) {
        List<OLECreatePO> eResources = new ArrayList<>();
        boolean poExists = false;
        boolean saveChanges = false;
        Map<String, String> criteriaMap = new HashMap<String, String>();
        criteriaMap.put(OLEConstants.OLEEResourceRecord.ERESOURCE_IDENTIFIER, oleeResourceRecordDocument.getOleERSIdentifier());
        criteriaMap.put(OLEConstants.LOC, "E-Resource");
        List<OleCopy> copies = (List<OleCopy>) getBusinessObjectService().findMatching(OleCopy.class, criteriaMap);
        for (OleCopy copy : copies) {
            if (copy.getPoItemId() != null) {
                Map<String, String> criteriaPOIdMap = new HashMap<String, String>();
                criteriaPOIdMap.put(OLEConstants.OLEEResourceRecord.PO_ITEM_ID, copy.getPoItemId().toString());
                List<OlePurchaseOrderItem> olePurchaseOrderItems = (List<OlePurchaseOrderItem>) getBusinessObjectService().findMatching(OlePurchaseOrderItem.class, criteriaPOIdMap);
                if (olePurchaseOrderItems.size() > 0) {
                    for (OlePurchaseOrderItem olePurchaseOrderItem : olePurchaseOrderItems) {
                        Map map = new HashMap();
                        map.put(OLEConstants.DOC_NUM, olePurchaseOrderItem.getDocumentNumber());
                        OlePurchaseOrderDocument olePurchaseOrderDocument = getBusinessObjectService().findByPrimaryKey(OlePurchaseOrderDocument.class, map);
                        if (olePurchaseOrderDocument != null) {
                            poExists = true;
                            OLECreatePO eResourcePO = new OLECreatePO();
                            eResourcePO.setPoId(olePurchaseOrderDocument.getPurapDocumentIdentifier().toString());
                            eResourcePO.setTitle(oleeResourceRecordDocument.getTitle());
                            eResourcePO.setGokbId("0002");
                            eResourcePO.setOleERSIdentifier(oleeResourceRecordDocument.getOleERSIdentifier());
                            eResourcePO.setIsbnNIssn(oleeResourceRecordDocument.getIsbn());
                            eResourcePO.setVendorId(olePurchaseOrderDocument.getVendorNumber());
                            eResourcePO.setOrderTypeId(olePurchaseOrderDocument.getPurchaseOrderTypeId().toString());
                            eResourcePO.setPrice(olePurchaseOrderItem.getItemListPrice().toString());
                            eResourcePO.setPurposeId(olePurchaseOrderItem.getPurposeId());
                            List<OLECretePOAccountingLine> accountingLines = new ArrayList<>();
                            if (olePurchaseOrderItem.getSourceAccountingLines() != null && olePurchaseOrderItem.getSourceAccountingLines().size() > 0) {
                                for (PurApAccountingLine purApAccountingLine : olePurchaseOrderItem.getSourceAccountingLines()){
                                    OLECretePOAccountingLine oleCretePOAccountingLine = new OLECretePOAccountingLine();
                                    oleCretePOAccountingLine.setChartOfAccountsCode(purApAccountingLine.getChartOfAccountsCode());
                                    oleCretePOAccountingLine.setAccountNumber(purApAccountingLine.getAccountNumber());
                                    oleCretePOAccountingLine.setSubAccountNumber(purApAccountingLine.getSubAccountNumber());
                                    oleCretePOAccountingLine.setFinancialObjectCode(purApAccountingLine.getFinancialObjectCode());
                                    oleCretePOAccountingLine.setFinancialSubObjectCode(purApAccountingLine.getFinancialSubObjectCode());
                                    oleCretePOAccountingLine.setProjectCode(purApAccountingLine.getProjectCode());
                                    oleCretePOAccountingLine.setOrganizationReferenceId(purApAccountingLine.getOrganizationReferenceId());
                                    oleCretePOAccountingLine.setAccountLinePercent(purApAccountingLine.getAccountLinePercent());
                                    accountingLines.add(oleCretePOAccountingLine);
                                }
                                eResourcePO.setAccountingLines(accountingLines);
                            }
                            eResources.add(eResourcePO);
                        }
                    }
                }
            }
        }
        Map createPoMap = new HashMap();
        createPoMap.put(OLEConstants.OLEEResourceRecord.ERESOURCE_IDENTIFIER, oleeResourceRecordDocument.getOleERSIdentifier());
        List<OLECreatePO> oleCreatePOs = (List<OLECreatePO>) getBusinessObjectService().findMatching(OLECreatePO.class,createPoMap);
        if (oleCreatePOs!=null && oleCreatePOs.size()>0){
            for (OLECreatePO oleCreatePO : oleCreatePOs){
                if (StringUtils.isBlank(oleCreatePO.getInstanceId())){
                    saveChanges = true;
                    OLECreatePO eResourcePO = new OLECreatePO();
                    eResourcePO.setSelectFlag(true);
                    eResourcePO.setTitle(oleeResourceRecordDocument.getTitle());
                    eResourcePO.setGokbId(oleeResourceRecordDocument.getGokbId());
                    eResourcePO.setIsbnNIssn(oleeResourceRecordDocument.getIsbn());
                    eResourcePO.setPrice(oleCreatePO.getPrice());
                    eResourcePO.setVendorId(oleCreatePO.getVendorId());
                    eResourcePO.setOrderTypeId(oleCreatePO.getOrderTypeId().toString());
                    eResourcePO.setOleERSIdentifier(oleeResourceRecordDocument.getOleERSIdentifier());
                    eResourcePO.setPurposeId(oleCreatePO.getPurposeId());
                    eResourcePO.setAccountingLines(oleCreatePO.getAccountingLines());
                    eResources.add(eResourcePO);
                }
            }
        }
        if (!poExists && !saveChanges) {
            OLECreatePO eResourcePO = new OLECreatePO();
            eResourcePO.setTitle(oleeResourceRecordDocument.getTitle());
            eResourcePO.setGokbId(oleeResourceRecordDocument.getGokbId());
            eResourcePO.setAccountingLines(getOLECretePOAccountingLines(oleeResourceRecordDocument.getAccountingLines()));
            eResourcePO.setIsbnNIssn(oleeResourceRecordDocument.getIsbn());
            eResourcePO.setPrice(oleeResourceRecordDocument.getEstimatedPrice());
            eResourcePO.setVendorId(oleeResourceRecordDocument.getVendorId());
            eResourcePO.setOrderTypeId(oleeResourceRecordDocument.getOrderTypeId().toString());
            eResourcePO.setOleERSIdentifier(oleeResourceRecordDocument.getOleERSIdentifier());
            eResourcePO.setPurposeId(purposeId);
            eResources.add(eResourcePO);
        }
        List<OLECreatePO> eResourcePOsWithoutDuplicate =removeDuplicateForPOInstanceAndEResource(eResources);
        return eResourcePOsWithoutDuplicate;
    }

    @Override
    public String validateAccountingLines(String errorMessage, OLECretePOAccountingLine accountingLine) {
        //chart code validation
        Chart chart = getBusinessObjectService().findBySinglePrimaryKey(Chart.class, accountingLine.getChartOfAccountsCode());
        if (chart == null) {
            if (errorMessage.length() > 1) {
                errorMessage = errorMessage.concat(", ");
            }
            errorMessage = errorMessage.concat("Invalid Chart Code");
        }
        //Account no validation
        Map accNoMap = new HashMap();
        accNoMap.put(OLEConstants.ACCOUNT_NUMBER, accountingLine.getAccountNumber());
        Account account = getBusinessObjectService().findByPrimaryKey(Account.class, accNoMap);
        if (account == null) {
            if (errorMessage.length() > 1) {
                errorMessage = errorMessage.concat(", ");
            }
            errorMessage = errorMessage.concat("Invalid Account Number");
        } else {
            accNoMap = new HashMap();
            accNoMap.put(OLEConstants.ACCOUNT_NUMBER, accountingLine.getAccountNumber());
            accNoMap.put(OLEConstants.OLEBatchProcess.CHART_OF_ACCOUNTS_CODE, accountingLine.getChartOfAccountsCode());
            List<Account> accountList = (List<Account>) getBusinessObjectService().findMatching(Account.class, accNoMap);
            if (accountList.size() == 0) {
                if (errorMessage.length() > 1) {
                    errorMessage = errorMessage.concat(", ");
                }
                errorMessage = errorMessage.concat("The combination of Chart Code and account number is not valid");
            }
        }
        //Object Code validation
        UniversityDateService universityDateService = SpringContext.getBean(UniversityDateService.class);
        Integer fiscalYear = universityDateService.getCurrentUniversityDate().getUniversityFiscalYear();
        Map objectCodeMap = new HashMap();
        objectCodeMap.put(OLEConstants.OLEBatchProcess.OBJECT_CODE, accountingLine.getFinancialObjectCode());
        objectCodeMap.put(org.kuali.ole.sys.OLEConstants.FISCAL_YEAR, fiscalYear);
        List<ObjectCode> objectCodeList = (List<ObjectCode>) getBusinessObjectService().findMatching(ObjectCode.class, objectCodeMap);
        if (objectCodeList.size() == 0) {
            if (errorMessage.length() > 1) {
                errorMessage = errorMessage.concat(", ");
            }
            errorMessage = errorMessage.concat("Invalid Object Code");
        }
        //Sub-Account no validation
        String subAccNo = accountingLine.getSubAccountNumber();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(subAccNo)) {
            Map subAccNoMap = new HashMap();
            subAccNoMap.put(OLEConstants.SUB_ACCOUNT_NUMBER, subAccNo);
            SubAccount subAccount = getBusinessObjectService().findByPrimaryKey(SubAccount.class, subAccNoMap);
            if (subAccount == null) {
                if (errorMessage.length() > 1) {
                    errorMessage = errorMessage.concat(", ");
                }
                errorMessage = errorMessage.concat("Invalid Sub Account Number");
            } else {
                subAccNoMap = new HashMap();
                subAccNoMap.put(OLEConstants.SUB_ACCOUNT_NUMBER, subAccNo);
                subAccNoMap.put(OLEConstants.ACCOUNT_NUMBER, accountingLine.getAccountNumber());
                subAccNoMap.put(OLEConstants.OLEBatchProcess.CHART_OF_ACCOUNTS_CODE, accountingLine.getChartOfAccountsCode());
                List<SubAccount> subAccountList = (List<SubAccount>) getBusinessObjectService().findMatching(SubAccount.class, subAccNoMap);
                if (subAccountList.size() == 0) {
                    if (errorMessage.length() > 1) {
                        errorMessage = errorMessage.concat(", ");
                    }
                    errorMessage = errorMessage.concat("The combination of Chart Code, Account number and Sub-Account number is not valid");
                }
            }
        }
        //Sub Object Code validation
        String subObjectCode = accountingLine.getFinancialSubObjectCode();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(subObjectCode)) {
            Map subObjectCodeMap = new HashMap();
            subObjectCodeMap.put(OLEConstants.FINANCIAL_SUB_OBJECT_CODE, subObjectCode);
            SubObjectCode subObject = getBusinessObjectService().findByPrimaryKey(SubObjectCode.class, subObjectCodeMap);
            if (subObject == null) {
                if (errorMessage.length() > 1) {
                    errorMessage = errorMessage.concat(", ");
                }
                errorMessage = errorMessage.concat("Invalid Sub Object Code");
            } else {
                subObjectCodeMap = new HashMap();
                subObjectCodeMap.put(OLEConstants.FINANCIAL_SUB_OBJECT_CODE, subObjectCode);
                subObjectCodeMap.put(OLEConstants.ACCOUNT_NUMBER, accountingLine.getAccountNumber());
                subObjectCodeMap.put(OLEConstants.OLEBatchProcess.CHART_OF_ACCOUNTS_CODE, accountingLine.getChartOfAccountsCode());
                subObjectCodeMap.put(OLEConstants.OLEBatchProcess.OBJECT_CODE, accountingLine.getFinancialObjectCode());
                subObjectCodeMap.put(org.kuali.ole.sys.OLEConstants.FISCAL_YEAR, fiscalYear);
                List<SubObjectCode> subObjectCodeList = (List<SubObjectCode>) getBusinessObjectService().findMatching(SubObjectCode.class, subObjectCodeMap);
                if (subObjectCodeList.size() == 0) {
                    if (errorMessage.length() > 1) {
                        errorMessage = errorMessage.concat(", ");
                    }
                    errorMessage = errorMessage.concat("The combination of Chart Code, Account number, Object Code & Sub-Object Code is not valid");
                }
            }
        }
        //Project Code validation
        String projectCode = accountingLine.getProjectCode();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(projectCode)) {
            Map projectCodeMap = new HashMap();
            projectCodeMap.put(OLEConstants.CODE, projectCode);
            List<ProjectCode> projectCodeList = (List<ProjectCode>) getBusinessObjectService().findMatching(ProjectCode.class, projectCodeMap);
            if (projectCodeList.size() == 0) {
                if (errorMessage.length() > 1) {
                    errorMessage = errorMessage.concat(", ");
                }
                errorMessage = errorMessage.concat("Invalid Project Code");
            }
        }
        return errorMessage;
    }

    @Override
    public boolean validateAccountingLines(OLEEResourceAccountingLine accountingLine, String sectionId) {
        boolean flag = false;
        //chart code validation
        Chart chart = getBusinessObjectService().findBySinglePrimaryKey(Chart.class, accountingLine.getChartOfAccountsCode());
        if (chart == null) {
            GlobalVariables.getMessageMap().putErrorForSectionId(sectionId, OLEConstants.OLEEResourceRecord.ERROR_INVALID_CHART_CODE);
            flag = true;
        }
        //Account no validation
        Map accNoMap = new HashMap();
        accNoMap.put(OLEConstants.ACCOUNT_NUMBER, accountingLine.getAccountNumber());
        Account account = getBusinessObjectService().findByPrimaryKey(Account.class, accNoMap);
        if (account == null) {
            GlobalVariables.getMessageMap().putErrorForSectionId(sectionId, OLEConstants.OLEEResourceRecord.ERROR_INVALID_ACCOUNT_NUM);
            flag = true;
        } else {
            accNoMap = new HashMap();
            accNoMap.put(OLEConstants.ACCOUNT_NUMBER, accountingLine.getAccountNumber());
            accNoMap.put(OLEConstants.OLEBatchProcess.CHART_OF_ACCOUNTS_CODE, accountingLine.getChartOfAccountsCode());
            List<Account> accountList = (List<Account>) getBusinessObjectService().findMatching(Account.class, accNoMap);
            if (accountList.size() == 0) {
                GlobalVariables.getMessageMap().putErrorForSectionId(sectionId, OLEConstants.OLEEResourceRecord.ERROR_INVALID_COMBINATION_ACCOUNT_NUM);
                flag = true;
            }
        }
        //Object Code validation
        UniversityDateService universityDateService = SpringContext.getBean(UniversityDateService.class);
        Integer fiscalYear = universityDateService.getCurrentUniversityDate().getUniversityFiscalYear();
        Map objectCodeMap = new HashMap();
        objectCodeMap.put(OLEConstants.OLEBatchProcess.OBJECT_CODE, accountingLine.getFinancialObjectCode());
        objectCodeMap.put(org.kuali.ole.sys.OLEConstants.FISCAL_YEAR, fiscalYear);
        List<ObjectCode> objectCodeList = (List<ObjectCode>) getBusinessObjectService().findMatching(ObjectCode.class, objectCodeMap);
        if (objectCodeList.size() == 0) {
            GlobalVariables.getMessageMap().putErrorForSectionId(sectionId, OLEConstants.OLEEResourceRecord.ERROR_INVALID_OBJECT_CODE);
            flag = true;
        }
        //Sub-Account no validation
        String subAccNo = accountingLine.getSubAccountNumber();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(subAccNo)) {
            Map subAccNoMap = new HashMap();
            subAccNoMap.put(OLEConstants.SUB_ACCOUNT_NUMBER, subAccNo);
            SubAccount subAccount = getBusinessObjectService().findByPrimaryKey(SubAccount.class, subAccNoMap);
            if (subAccount == null) {
                GlobalVariables.getMessageMap().putErrorForSectionId(sectionId, OLEConstants.OLEEResourceRecord.ERROR_INVALID_SUB_ACCOUNT_NUM);
                flag = true;
            } else {
                subAccNoMap = new HashMap();
                subAccNoMap.put(OLEConstants.SUB_ACCOUNT_NUMBER, subAccNo);
                subAccNoMap.put(OLEConstants.ACCOUNT_NUMBER, accountingLine.getAccountNumber());
                subAccNoMap.put(OLEConstants.OLEBatchProcess.CHART_OF_ACCOUNTS_CODE, accountingLine.getChartOfAccountsCode());
                List<SubAccount> subAccountList = (List<SubAccount>) getBusinessObjectService().findMatching(SubAccount.class, subAccNoMap);
                if (subAccountList.size() == 0) {
                    GlobalVariables.getMessageMap().putErrorForSectionId(sectionId, OLEConstants.OLEEResourceRecord.ERROR_INVALID_COMBINATION_SUB_ACCOUNT_NUM);
                    flag = true;
                }
            }
        }
        //Sub Object Code validation
        String subObjectCode = accountingLine.getFinancialSubObjectCode();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(subObjectCode)) {
            Map subObjectCodeMap = new HashMap();
            subObjectCodeMap.put(OLEConstants.FINANCIAL_SUB_OBJECT_CODE, subObjectCode);
            SubObjectCode subObject = getBusinessObjectService().findByPrimaryKey(SubObjectCode.class, subObjectCodeMap);
            if (subObject == null) {
                GlobalVariables.getMessageMap().putErrorForSectionId(sectionId, OLEConstants.OLEEResourceRecord.ERROR_INVALID_SUB_OBJECT_CODE);
                flag = true;
            } else {
                subObjectCodeMap = new HashMap();
                subObjectCodeMap.put(OLEConstants.FINANCIAL_SUB_OBJECT_CODE, subObjectCode);
                subObjectCodeMap.put(OLEConstants.ACCOUNT_NUMBER, accountingLine.getAccountNumber());
                subObjectCodeMap.put(OLEConstants.OLEBatchProcess.CHART_OF_ACCOUNTS_CODE, accountingLine.getChartOfAccountsCode());
                subObjectCodeMap.put(OLEConstants.OLEBatchProcess.OBJECT_CODE, accountingLine.getFinancialObjectCode());
                subObjectCodeMap.put(org.kuali.ole.sys.OLEConstants.FISCAL_YEAR, fiscalYear);
                List<SubObjectCode> subObjectCodeList = (List<SubObjectCode>) getBusinessObjectService().findMatching(SubObjectCode.class, subObjectCodeMap);
                if (subObjectCodeList.size() == 0) {
                    GlobalVariables.getMessageMap().putErrorForSectionId(sectionId, OLEConstants.OLEEResourceRecord.ERROR_INVALID_COMBINATION_SUB_OBJECT_CODE);
                    flag = true;
                }
            }
        }
        //Project Code validation
        String projectCode = accountingLine.getProjectCode();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(projectCode)) {
            Map projectCodeMap = new HashMap();
            projectCodeMap.put(OLEConstants.CODE, projectCode);
            List<ProjectCode> projectCodeList = (List<ProjectCode>) getBusinessObjectService().findMatching(ProjectCode.class, projectCodeMap);
            if (projectCodeList.size() == 0) {
                GlobalVariables.getMessageMap().putErrorForSectionId(sectionId, OLEConstants.OLEEResourceRecord.ERROR_INVALID_PROJECT_CODE);
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public RequisitionDocument setDocumentValues(OleRequisitionDocument requisitionDocument, OLEEResourceOrderRecord oleEResourceOrderRecord) throws Exception {
        // ******************Document Overview Section******************
        requisitionDocument.setStatusCode(PurapConstants.RequisitionStatuses.APPDOC_IN_PROCESS);
        /**
         * Commented vendorPoNumber based on JIRA-2842
         */
        //requisitionDocument.setVendorPoNumber(oleOrderRecord.getOleEResourceTxnRecord().getVendorNumber());
        requisitionDocument.setVendorPoNumber(oleEResourceOrderRecord.getOleEResourceTxnRecord().getVendorItemIdentifier());
        // ******************Financial Document Detail Section******************
        // ******************Requisition Detail Section******************
        requisitionDocument.setChartOfAccountsCode(oleEResourceOrderRecord.getOleEResourceTxnRecord().getChartCode());
        requisitionDocument.setOrganizationCode(oleEResourceOrderRecord.getOleEResourceTxnRecord().getOrgCode());
        requisitionDocument.setDocumentFundingSourceCode(oleEResourceOrderRecord.getOleEResourceTxnRecord().getFundingSource());
        requisitionDocument.setUseTaxIndicator(true);//oleOrderRecord.getOleEResourceTxnRecord().getUseTaxIndicator()
        // ******************Delivery Section******************
        setDeliveryDetails(requisitionDocument, oleEResourceOrderRecord);
        requisitionDocument.setDeliveryCampusCode(oleEResourceOrderRecord.getOleEResourceTxnRecord().getDeliveryCampusCode());
        // ******************Vendor Section******************
        setVendorDetails(requisitionDocument, oleEResourceOrderRecord);
        // ******************Items Section******************
        // ******************Capital Assets Section******************
        // ******************Payment INfo Section******************
        // ******************Additional Institutional Info Section******************
        requisitionDocument.getDocumentHeader().setDocumentDescription("Document Description");
        requisitionDocument.setPurchaseOrderTransmissionMethodCode(getOleReqPOCreateDocumentService().getTransmissionMethodCode(oleEResourceOrderRecord.getOleEResourceTxnRecord().getMethodOfPOTransmission()));//FAX
        requisitionDocument.setPurchaseOrderCostSourceCode(oleEResourceOrderRecord.getOleEResourceTxnRecord().getCostSource());//CON
        requisitionDocument.setRequestorPersonName(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.REQUESTOR_PERSON_NAME));
        requisitionDocument.setRequestorPersonPhoneNumber(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.REQUESTOR_PERSON_PHONE_NUMBER));
        requisitionDocument.setRequestorPersonEmailAddress(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.REQUESTOR_PERSON_EMAIL_ADDRESS));
        requisitionDocument.setOrganizationAutomaticPurchaseOrderLimit(new KualiDecimal(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.VENDOR_CONTRACT_DEFAULT_APO_LIMIT)));
        requisitionDocument.setPurchaseOrderAutomaticIndicator(Boolean.parseBoolean(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.PURCHASE_ORDER_AUTOMATIC_INDICATIOR)));
        requisitionDocument.setReceivingDocumentRequiredIndicator(oleEResourceOrderRecord.getOleEResourceTxnRecord().isReceivingRequired());
        requisitionDocument.setPaymentRequestPositiveApprovalIndicator(oleEResourceOrderRecord.getOleEResourceTxnRecord().isPayReqPositiveApprovalReq());
        requisitionDocument.setRequisitionSourceCode(oleEResourceOrderRecord.getOleEResourceTxnRecord().getRequisitionSource());
        return requisitionDocument;
    }

    @Override
    public void setItemDescription(OLEEResourceOrderRecord oleEResourceOrderRecord, OleRequisitionItem item) throws Exception {
        String title = oleEResourceOrderRecord.getOleBibRecord().getBib().getTitle() != null ? oleEResourceOrderRecord.getOleBibRecord().getBib().getTitle()+ "," : "";
        String author = oleEResourceOrderRecord.getOleBibRecord().getBib().getAuthor() != null ? oleEResourceOrderRecord.getOleBibRecord().getBib().getAuthor()+ "," : "";
        String publisher = oleEResourceOrderRecord.getOleBibRecord().getBib().getPublisher() != null ? oleEResourceOrderRecord.getOleBibRecord().getBib().getPublisher()+ "," : "";
        String isbn = oleEResourceOrderRecord.getOleBibRecord().getBib().getIsbn() != null ? oleEResourceOrderRecord.getOleBibRecord().getBib().getIsbn() + ",": "";
        String description = title + author
                + publisher + isbn;
        item.setItemDescription(description.substring(0, (description.lastIndexOf(","))));
        item.setItemTitle(oleEResourceOrderRecord.getOleBibRecord().getBib().getTitle());
        item.setItemAuthor(oleEResourceOrderRecord.getOleBibRecord().getBib().getAuthor());
        item.setBibUUID(oleEResourceOrderRecord.getOleBibRecord().getBibUUID());

    }

    private void setDeliveryDetails(OleRequisitionDocument requisitionDocument, OLEEResourceOrderRecord oleEResourceOrderRecord) {
        if (LOG.isDebugEnabled())
            LOG.debug("bibInfoBean.getDeliveryBuildingCode----------->" + oleEResourceOrderRecord.getOleEResourceTxnRecord().getBuildingCode());

        if (oleEResourceOrderRecord.getOleEResourceTxnRecord().getDeliveryCampusCode() != null && oleEResourceOrderRecord.getOleEResourceTxnRecord().getBuildingCode() != null && oleEResourceOrderRecord.getOleEResourceTxnRecord().getDeliveryBuildingRoomNumber() != null) {
            Map<String,String> deliveryMap = new HashMap<>();
            deliveryMap.put(OLEConstants.OLEBatchProcess.BUILDING_CODE, oleEResourceOrderRecord.getOleEResourceTxnRecord().getBuildingCode());
            deliveryMap.put(OLEConstants.OLEBatchProcess.CAMPUS_CODE, oleEResourceOrderRecord.getOleEResourceTxnRecord().getDeliveryCampusCode());
            deliveryMap.put(OLEConstants.BUILDING_ROOM_NUMBER, oleEResourceOrderRecord.getOleEResourceTxnRecord().getDeliveryBuildingRoomNumber());
            Room room = getBusinessObjectService().findByPrimaryKey(Room.class, deliveryMap);
            Building building = getVendorService().getBuildingDetails(oleEResourceOrderRecord.getOleEResourceTxnRecord().getDeliveryCampusCode(), oleEResourceOrderRecord.getOleEResourceTxnRecord().getBuildingCode());
            if (building != null && room != null) {
                requisitionDocument.setDeliveryBuildingCode(building.getBuildingCode());
                requisitionDocument.setDeliveryCampusCode(building.getCampusCode());
                requisitionDocument.setDeliveryBuildingLine1Address(building.getBuildingStreetAddress());
                requisitionDocument.setDeliveryBuildingName(building.getBuildingName());
                requisitionDocument.setDeliveryCityName(building.getBuildingAddressCityName());
                requisitionDocument.setDeliveryStateCode(building.getBuildingAddressStateCode());
                requisitionDocument.setDeliveryPostalCode(building.getBuildingAddressZipCode());
                requisitionDocument.setDeliveryCountryCode(building.getBuildingAddressCountryCode());
                requisitionDocument.setDeliveryBuildingRoomNumber(room.getBuildingRoomNumber());
                requisitionDocument.setDeliveryToName(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.DELIVERY_TO_NAME));

                requisitionDocument.setBillingCountryCode(building.getBuildingCode());
                requisitionDocument.setBillingLine1Address(building.getBuildingStreetAddress());
                requisitionDocument.setBillingName(building.getBuildingName());
                requisitionDocument.setBillingCityName(building.getBuildingAddressCityName());
                requisitionDocument.setBillingStateCode(building.getBuildingAddressStateCode());
                requisitionDocument.setBillingPostalCode(building.getBuildingAddressZipCode());
                requisitionDocument.setBillingCountryCode(building.getBuildingAddressCountryCode());
                requisitionDocument.setBillingPhoneNumber(getOlePurapService().getParameter(org.kuali.ole.sys.OLEConstants.BILL_PHN_NBR));

            }
        }
    }

    private void setVendorDetails(OleRequisitionDocument requisitionDocument, OLEEResourceOrderRecord oleEResourceOrderRecord) {
        if (oleEResourceOrderRecord.getOleEResourceTxnRecord().getVendorNumber() != null) {
            VendorDetail vendorDetail = getVendorService().getVendorDetail(oleEResourceOrderRecord.getOleEResourceTxnRecord().getVendorNumber());

            requisitionDocument.setVendorCustomerNumber(oleEResourceOrderRecord.getOleEResourceTxnRecord().getVendorInfoCustomer());
            requisitionDocument.setVendorNumber(oleEResourceOrderRecord.getOleEResourceTxnRecord().getVendorNumber());
            requisitionDocument.setVendorNumber(vendorDetail.getVendorNumber());
            requisitionDocument.setVendorName(vendorDetail.getVendorName());
            requisitionDocument.setVendorHeaderGeneratedIdentifier(vendorDetail.getVendorHeaderGeneratedIdentifier());
            requisitionDocument.setVendorDetailAssignedIdentifier(vendorDetail.getVendorDetailAssignedIdentifier());
            requisitionDocument.setVendorDetail(vendorDetail);
            String deliveryCampus = oleEResourceOrderRecord.getOleEResourceTxnRecord().getDeliveryCampusCode();
            Integer headerId = null;
            Integer detailId = null;
            int dashInd = vendorDetail.getVendorNumber().indexOf('-');
            // make sure there's at least one char before and after '-'
            if (dashInd > 0 && dashInd < vendorDetail.getVendorNumber().length() - 1) {
                headerId = new Integer(vendorDetail.getVendorNumber().substring(0, dashInd));
                detailId = new Integer(vendorDetail.getVendorNumber().substring(dashInd + 1));
            }
            VendorAddress vendorAddress = getVendorService().getVendorDefaultAddress(headerId, detailId, VendorConstants.AddressTypes.PURCHASE_ORDER, deliveryCampus);
            getOleReqPOCreateDocumentService().setVendorAddress(vendorAddress, requisitionDocument);

            List<VendorContract> vendorContracts = vendorDetail.getVendorContracts();
            for (Iterator<VendorContract> vendorContract = vendorContracts.iterator(); vendorContract.hasNext(); ) {
                requisitionDocument.setVendorContractGeneratedIdentifier((vendorContract.next()).getVendorContractGeneratedIdentifier());
            }
        }

    }

    private List<OLECretePOAccountingLine> getOLECretePOAccountingLines(List<OLEEResourceAccountingLine> oleeResourceAccountingLines){
        List<OLECretePOAccountingLine> accountingLines = new ArrayList<>();
        for (OLEEResourceAccountingLine accountingLine : oleeResourceAccountingLines){
            OLECretePOAccountingLine oleCretePOAccountingLine = new OLECretePOAccountingLine();
            oleCretePOAccountingLine.setChartOfAccountsCode(accountingLine.getChartOfAccountsCode());
            oleCretePOAccountingLine.setAccountNumber(accountingLine.getAccountNumber());
            oleCretePOAccountingLine.setSubAccountNumber(accountingLine.getSubAccountNumber());
            oleCretePOAccountingLine.setFinancialObjectCode(accountingLine.getFinancialObjectCode());
            oleCretePOAccountingLine.setFinancialSubObjectCode(accountingLine.getFinancialSubObjectCode());
            oleCretePOAccountingLine.setProjectCode(accountingLine.getProjectCode());
            oleCretePOAccountingLine.setOrganizationReferenceId(accountingLine.getOrganizationReferenceId());
            oleCretePOAccountingLine.setAccountLinePercent(accountingLine.getAccountLinePercent());
            accountingLines.add(oleCretePOAccountingLine);
        }
        return accountingLines;
    }

    public String makeUrlClickable(String docId){
        String url = ConfigContext.getCurrentContextConfig().getProperty("ole.platform.url") + PLATFORM_URL_START + docId + PLATFORM_URL_END;
        return HREF_START + url + HREF_END;
    }

    private void setDefaultValuesToVendor(VendorDetail vendorDetail) {
        List<VendorAddress> vendorAddresses = vendorDetail.getVendorAddresses();
        VendorHeader vendorHeader = vendorDetail.getVendorHeader();
        Map vendorTypeMap = new HashMap();
        vendorTypeMap.put("vendorTypeDescription", getParameter(OLEConstants.VENDOR_TYPE));
        List<VendorType> vendorTypes = (List<VendorType>) getBusinessObjectService().findMatching(VendorType.class, vendorTypeMap);
        if (vendorTypes != null && vendorTypes.size() > 0) {
            VendorType vendorType = vendorTypes.get(0);
            vendorHeader.setVendorTypeCode(vendorType.getVendorTypeCode());
        } else {
            vendorHeader.setVendorTypeCode("PO");
        }

        vendorHeader.setVendorForeignIndicator(false);

        Map ownershipTypeMap = new HashMap();
        ownershipTypeMap.put("vendorOwnershipDescription", getParameter(OLEConstants.OWNERSHIP_TYPE));
        List<OwnershipType> ownershipTypes = (List<OwnershipType>) getBusinessObjectService().findMatching(OwnershipType.class, ownershipTypeMap);
        if (ownershipTypes != null && ownershipTypes.size() > 0) {
            OwnershipType ownershipType = ownershipTypes.get(0);
            vendorHeader.setVendorOwnershipCode(ownershipType.getVendorOwnershipCode());
        } else {
            vendorHeader.setVendorOwnershipCode("CP");
        }


        Map currencyMap = new HashMap();
        currencyMap.put("currencyType", getParameter(OLEConstants.CURRENCY_TYPE));
        List<OleCurrencyType> oleCurrencyTypes = (List<OleCurrencyType>) getBusinessObjectService().findMatching(OleCurrencyType.class, currencyMap);
        if (oleCurrencyTypes != null && oleCurrencyTypes.size() > 0) {
            OleCurrencyType oleCurrencyType = oleCurrencyTypes.get(0);
            vendorDetail.setCurrencyTypeId(oleCurrencyType.getCurrencyTypeId());
        } else {
            vendorDetail.setCurrencyTypeId(new Long(1));
        }

        Map paymentMap = new HashMap();
        paymentMap.put("paymentMethod", getParameter(OLEConstants.PAYMENT_METHOD));
        List<OlePaymentMethod> olePaymentMethods = (List<OlePaymentMethod>) getBusinessObjectService().findMatching(OlePaymentMethod.class, paymentMap);
        if (olePaymentMethods != null && olePaymentMethods.size() > 0) {
            OlePaymentMethod olePaymentMethod = olePaymentMethods.get(0);
            vendorDetail.setPaymentMethodId(olePaymentMethod.getPaymentMethodId());
        } else {
            vendorDetail.setPaymentMethodId(1);
        }

        VendorAddress vendorAddress = new VendorAddress();

        Map addressMap = new HashMap();
        addressMap.put("vendorAddressTypeDescription", getParameter(OLEConstants.ADDRESS_TYPE));
        List<AddressType> addressTypes = (List<AddressType>) getBusinessObjectService().findMatching(AddressType.class, addressMap);
        if (addressTypes != null && addressTypes.size() > 0) {
            AddressType addressType = addressTypes.get(0);
            vendorAddress.setVendorAddressTypeCode(addressType.getVendorAddressTypeCode());
        } else {
            vendorAddress.setVendorAddressTypeCode("PO");
        }
        vendorAddress.setVendorLine1Address(getParameter(OLEConstants.ADDRESS));
        vendorAddress.setVendorStateCode(getParameter(OLEConstants.STATE));
        vendorAddress.setVendorCityName(getParameter(OLEConstants.CITY));
        vendorAddress.setVendorZipCode(getParameter(OLEConstants.POSTAL_CODE));
        vendorAddress.setVendorCountryCode(getParameter(OLEConstants.COUNTRY));
        vendorAddress.setActive(true);
        vendorAddress.setVendorDefaultAddressIndicator(true);
        vendorAddresses.add(vendorAddress);
    }

    public void createVendor(String organizationName, Integer gokbOrganizationId, String variantName) {
        try {
            FinancialSystemMaintenanceDocument financialSystemMaintenanceDocument = (FinancialSystemMaintenanceDocument) KRADServiceLocatorWeb.getDocumentService().getNewDocument(org.kuali.ole.sys.OLEConstants.Vendor.DOCUMENT_TYPE);
            VendorDetail vendorDetail = (VendorDetail) financialSystemMaintenanceDocument.getNewMaintainableObject().getDataObject();
            if (StringUtils.isNotBlank(organizationName)) {
                vendorDetail.setVendorName(organizationName);
            } else {
                vendorDetail.setVendorName(gokbOrganizationId.toString());
            }
            if (vendorDetail.getVendorName().length() < 40) {
                financialSystemMaintenanceDocument.getDocumentHeader().setDocumentDescription(vendorDetail.getVendorName());
            } else {
                String documentDescription = vendorDetail.getVendorName().substring(0, 39);
                financialSystemMaintenanceDocument.getDocumentHeader().setDocumentDescription(documentDescription);
            }
            vendorDetail.setNonBillable(true);
            vendorDetail.setGokbId(gokbOrganizationId);
            if (org.apache.commons.lang.StringUtils.isNotBlank(variantName)) {
                VendorAlias vendorAlias = new VendorAlias();
                vendorAlias.setVendorAliasName(variantName);
                vendorAlias.setGokbVendorAliasInd(true);
                vendorAlias.setVendorAliasType(new AliasType());
                vendorAlias.setActive(true);
                vendorDetail.getVendorAliases().add(vendorAlias);
            }
            vendorDetail.setActiveIndicator(true);

            setDefaultValuesToVendor(vendorDetail);

            VendorEventLog vendorEventLog = new VendorEventLog();
            vendorEventLog.setLogTypeId("3");
            vendorEventLog.setDate(new Timestamp(System.currentTimeMillis()));
            vendorEventLog.setUserId(GlobalVariables.getUserSession().getPrincipalId());
            vendorEventLog.setNote("Vendor is created through Gokb import");
            vendorDetail.getEventLogs().add(vendorEventLog);
            KRADServiceLocatorWeb.getDocumentService().routeDocument(financialSystemMaintenanceDocument, null, null);

        } catch (Exception e) {
            LOG.error("Exception while routing vendor document through E-Resource" + e);
        }
    }

    public void updateVendor(VendorDetail vendorDetail, String organizationName) {
        if (org.apache.commons.lang.StringUtils.isNotBlank(organizationName)) {
            vendorDetail.setVendorName(organizationName);
        }
        vendorDetail.setGokbLastUpdated(new Timestamp(System.currentTimeMillis()));
        VendorEventLog vendorEventLog = new VendorEventLog();
        vendorEventLog.setLogTypeId("3");
        vendorEventLog.setDate(new Timestamp(System.currentTimeMillis()));
        vendorEventLog.setUserId(GlobalVariables.getUserSession().getPrincipalId());
        vendorEventLog.setNote("Vendor is Updated through Gokb import");
        vendorDetail.getEventLogs().add(vendorEventLog);
        getBusinessObjectService().save(vendorDetail);
    }

    private void setPlatformProviderDetails(Integer platformProviderId, OLEPlatformRecordDocument olePlatformRecordDocument) {
        Map vendorMap = new HashMap();
        vendorMap.put(OLEConstants.GOKB_ID, platformProviderId);
        List<VendorDetail> vendorDetailList = (List<VendorDetail>) getBusinessObjectService().findMatching(VendorDetail.class, vendorMap);
        if (vendorDetailList != null && vendorDetailList.size() > 0) {
            olePlatformRecordDocument.setVendorHeaderGeneratedIdentifier(vendorDetailList.get(0).getVendorHeaderGeneratedIdentifier());
            olePlatformRecordDocument.setVendorDetailAssignedIdentifier(vendorDetailList.get(0).getVendorDetailAssignedIdentifier());
        }
    }

    private String getPlatformStatusId(String platformStatus) {
        Map statusMap = new HashMap();
        statusMap.put(OLEConstants.PLATFORM_STATUS_NAME, platformStatus);
        List<OLEPlatformStatus> platformStatusList = (List<OLEPlatformStatus>) getBusinessObjectService().findMatching(OLEPlatformStatus.class, statusMap);
        if (platformStatusList != null && platformStatusList.size() > 0) {
            return platformStatusList.get(0).getPlatformStatusId();
        }
        return null;
    }

    public OLEPlatformRecordDocument createPlatform(String platformName, Integer gokbPlatformId, String softwarePlatform, String platformStatus, Integer platformProviderId) {
        try {
            OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) KRADServiceLocatorWeb.getDocumentService().getNewDocument(OLEConstants.OLE_PLATFORM_DOC);
            if (org.apache.commons.lang.StringUtils.isNotBlank(platformName)) {
                olePlatformRecordDocument.setName(platformName);
            } else {
                olePlatformRecordDocument.setName(gokbPlatformId.toString());
            }
            if (olePlatformRecordDocument.getName().length() < 40) {
                olePlatformRecordDocument.getDocumentHeader().setDocumentDescription(olePlatformRecordDocument.getName());
            } else {
                String documentDescription = olePlatformRecordDocument.getName().substring(0, 39);
                olePlatformRecordDocument.getDocumentHeader().setDocumentDescription(documentDescription);
            }
            olePlatformRecordDocument.setGokbId(gokbPlatformId);
            if (StringUtils.isNotBlank(softwarePlatform)) {
                olePlatformRecordDocument.setSoftware(softwarePlatform);
            }
            if (StringUtils.isNotBlank(platformStatus)) {
                String platformStatusId = getPlatformStatusId(platformStatus);
                if (platformStatusId != null) {
                    olePlatformRecordDocument.setStatusId(platformStatusId);
                } else {
                    olePlatformRecordDocument.setStatusId("1");
                }
            } else {
                olePlatformRecordDocument.setStatusId("1");
            }
            if (platformProviderId != null) {
                setPlatformProviderDetails(platformProviderId, olePlatformRecordDocument);
            }
            OLEPlatformEventLog platformEventLog = new OLEPlatformEventLog();
            platformEventLog.setSaveFlag(true);
            platformEventLog.setLogTypeId("3");
            platformEventLog.setEventReportedDate(new java.sql.Date(System.currentTimeMillis()));
            platformEventLog.setEventUserId(GlobalVariables.getUserSession().getPrincipalId());
            platformEventLog.setEventNote("Platform Record is created through Gokb import");
            olePlatformRecordDocument.getEventLogs().add(platformEventLog);
            olePlatformRecordDocument = (OLEPlatformRecordDocument) KRADServiceLocatorWeb.getDocumentService().saveDocument(olePlatformRecordDocument);
            return olePlatformRecordDocument;
        } catch (Exception e) {
            LOG.error("Exception while saving Platform Record through E-Resource" + e);
        }
        return null;
    }

    public void updatePlatform(OLEPlatformRecordDocument olePlatformRecordDocument, String platformName, String platformStatus, String softwarePlatform, Integer platformProviderId) {
        if (StringUtils.isNotBlank(platformName)) {
            olePlatformRecordDocument.setName(platformName);
        }
        if (StringUtils.isNotBlank(platformStatus)) {
            String platformStatusId = getPlatformStatusId(platformStatus);
            if (platformStatusId != null) {
                olePlatformRecordDocument.setStatusId(platformStatusId);
            }
        }
        if (StringUtils.isNotBlank(softwarePlatform)) {
            olePlatformRecordDocument.setSoftware(softwarePlatform);
        }
        if (platformProviderId != null) {
            setPlatformProviderDetails(platformProviderId, olePlatformRecordDocument);
        }
        OLEPlatformEventLog platformEventLog = new OLEPlatformEventLog();
        platformEventLog.setSaveFlag(true);
        platformEventLog.setLogTypeId("3");
        platformEventLog.setEventReportedDate(new java.sql.Date(System.currentTimeMillis()));
        platformEventLog.setEventUserId(GlobalVariables.getUserSession().getPrincipalId());
        platformEventLog.setEventNote("Platform Record is Updated through Gokb import");
        olePlatformRecordDocument.getEventLogs().add(platformEventLog);
        KRADServiceLocatorWeb.getDocumentService().updateDocument(olePlatformRecordDocument);
        //TODO searchable
    }

    public void updatePublisher(List<OLEGOKbTIPP> oleGoKbTIPPList, OLEEResourceRecordDocument oleeResourceRecordDocument) {
        if (oleGoKbTIPPList != null && oleGoKbTIPPList.size() > 0) {
            OLEGOKbTIPP oleGoKbTIPP = oleGoKbTIPPList.get(0);
            if (oleGoKbTIPP != null && oleGoKbTIPP.getPublisherId() != null) {
                updatePublisherDetails(oleeResourceRecordDocument, oleGoKbTIPP.getPublisherId());
            }
        }
    }

    public void updatePublisherDetails(OLEEResourceRecordDocument oleeResourceRecordDocument, Integer publisherId) {
        OleGokbOrganization oleGokbOrganization = getBusinessObjectService().findBySinglePrimaryKey(OleGokbOrganization.class, publisherId);
        if (oleGokbOrganization != null) {
            Map vendorMap = new HashMap();
            vendorMap.put(OLEConstants.GOKB_ID, oleGokbOrganization.getGokbOrganizationId());
            List<VendorDetail> vendorDetails = (List<VendorDetail>) getBusinessObjectService().findMatching(VendorDetail.class, vendorMap);
            if (vendorDetails != null && vendorDetails.size() > 0) {
                VendorDetail vendorDetail = vendorDetails.get(0);
                oleeResourceRecordDocument.setPublisherId(vendorDetail.getVendorNumber());
                oleeResourceRecordDocument.setActivePublisher(vendorDetail.isActiveIndicator());
                KRADServiceLocatorWeb.getDocumentService().updateDocument(oleeResourceRecordDocument);
            }
            updateEResVendorAssociation(oleeResourceRecordDocument);
        }
    }

    public void updatePlatformProvider(OLEEResourceRecordDocument oleeResourceRecordDocument) {
        Set<String> platform = new HashSet<>();
        StringBuffer platformProvider = new StringBuffer();
        for (OLEEResourceInstance oleEResourceInstance : oleeResourceRecordDocument.getOleERSInstances()){
            if (StringUtils.isNotBlank(oleEResourceInstance.getPlatformId())){
                OLEPlatformRecordDocument olePlatformRecordDocument = getBusinessObjectService().findBySinglePrimaryKey(OLEPlatformRecordDocument.class,oleEResourceInstance.getPlatformId());
                if (olePlatformRecordDocument!=null && !platform.contains(olePlatformRecordDocument.getName())){
                    platform.add(olePlatformRecordDocument.getName());
                    platformProvider.append(olePlatformRecordDocument.getName());
                    platformProvider.append(OLEConstants.COMMA);
                }
            }
        }
        if (platformProvider.length()>0){
            oleeResourceRecordDocument.setPlatformProvider(platformProvider.substring(0, platformProvider.length() - 1));
        }
    }

    public void updateEResVendorAssociation(OLEEResourceRecordDocument oleEResourceRecordDocument){
        if (oleEResourceRecordDocument != null) {
            Map roleMap = new HashMap();
            roleMap.put("associatedEntityId", oleEResourceRecordDocument.getOleERSIdentifier());
            roleMap.put("vendorRoleId", "2");
            List<OLEVendorAssociation> vendorAssociations = (List<OLEVendorAssociation>) getBusinessObjectService().findMatching(OLEVendorAssociation.class, roleMap);
            if (!(vendorAssociations != null && vendorAssociations.size() > 0)) {
                if (oleEResourceRecordDocument.getOleERSIdentifier() != null && StringUtils.isNotBlank(oleEResourceRecordDocument.getPublisherId())) {
                    String[] publisherDetails = oleEResourceRecordDocument.getPublisherId().split("-");
                    OLEVendorAssociation oleVendorAssociation = new OLEVendorAssociation();
                    oleVendorAssociation.setVendorHeaderGeneratedIdentifier(publisherDetails.length > 0 ? Integer.parseInt(publisherDetails[0]) : 0);
                    oleVendorAssociation.setVendorDetailAssignedIdentifier(publisherDetails.length > 1 ? Integer.parseInt(publisherDetails[1]) : 0);
                    oleVendorAssociation.setVendorRoleId("2");
                    oleVendorAssociation.setAssociatedEntityId(oleEResourceRecordDocument.getOleERSIdentifier());
                    getBusinessObjectService().save(oleVendorAssociation);
                }
            }
        }
    }

    public void storeEventAttachments(MultipartFile attachmentFile) throws IOException {
        String location = "";
        location = getKualiConfigurationService().getPropertyValueAsString(
                KRADConstants.ATTACHMENTS_PENDING_DIRECTORY_KEY) + OLEConstants.OleLicenseRequest.ERESOURCE_EVENT_TMP_LOCATION;
        LOG.info("Event Attachment LOG :" + location);
        File dirLocation = new File(location);
        if (!dirLocation.exists()) {
            boolean success = dirLocation.mkdirs();
            if (!success) {
                LOG.error("Could not generate directory for File at: " + dirLocation.getAbsolutePath());
            }
        }
        location = location + File.separator + attachmentFile.getOriginalFilename();
        InputStream fileContents = attachmentFile.getInputStream();
        File fileOut = new File(location);
        FileOutputStream streamOut = null;
        BufferedOutputStream bufferedStreamOut = null;
        try {
            streamOut = new FileOutputStream(fileOut);
            bufferedStreamOut = new BufferedOutputStream(streamOut);
            int c;
            while ((c = fileContents.read()) != -1) {
                bufferedStreamOut.write(c);
            }
        } finally {
            bufferedStreamOut.close();
            streamOut.close();
        }
    }

    public void processEventAttachments(List<OLEEResourceEventLog> oleEResourceEventLogs) {
        LOG.debug("Inside processEventAttachments method");
        List<OLEEResourceEventLog> eventAttachments = new ArrayList<OLEEResourceEventLog>();
        if (oleEResourceEventLogs.size() > 0) {
            try {
                String location = "";
                location = getKualiConfigurationService().getPropertyValueAsString(
                        KRADConstants.ATTACHMENTS_PENDING_DIRECTORY_KEY) + OLEConstants.OleLicenseRequest.ERESOURCE_EVENT_TMP_LOCATION;
                LOG.info("Event Attachment LOG :" + location);
                for (OLEEResourceEventLog oleEResourceEventLog : oleEResourceEventLogs) {
                    if (StringUtils.isNotBlank(oleEResourceEventLog.getAttachmentFileName1())) {
                        File file = new File(location + File.separator + oleEResourceEventLog.getAttachmentFileName1());
                        oleEResourceEventLog.setAttachmentContent1(FileUtils.readFileToByteArray(file));
                    }
                    if (StringUtils.isNotBlank(oleEResourceEventLog.getAttachmentFileName2())) {
                        File file = new File(location + File.separator + oleEResourceEventLog.getAttachmentFileName2());
                        oleEResourceEventLog.setAttachmentContent2(FileUtils.readFileToByteArray(file));
                    }
                }
                for (OLEEResourceEventLog oleEResourceEventLog : oleEResourceEventLogs) {
                    if (StringUtils.isNotBlank(oleEResourceEventLog.getAttachmentFileName1())) {
                        File file = new File(location + File.separator + oleEResourceEventLog.getAttachmentFileName1());
                        file.delete();
                    }
                    if (StringUtils.isNotBlank(oleEResourceEventLog.getAttachmentFileName2())) {
                        File file = new File(location + File.separator + oleEResourceEventLog.getAttachmentFileName2());
                        file.delete();
                    }
                }
            } catch (Exception e) {
                LOG.error("Exception while saving attachment" + e);
            }

        }
    }

    public boolean addAttachmentFile(OLEEResourceEventLog oleEResourceEventLog, String sectionId) {
        boolean validFile = true;
        MultipartFile attachmentFile1 = oleEResourceEventLog.getAttachmentFile1();
        if (attachmentFile1 != null && !org.apache.commons.lang.StringUtils.isBlank(attachmentFile1.getOriginalFilename())) {
            if (attachmentFile1.getSize() == 0) {
                GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(sectionId,
                        RiceKeyConstants.ERROR_UPLOADFILE_EMPTY, attachmentFile1.getOriginalFilename());
                validFile = false;
            } else {
                try {
                    oleEResourceEventLog.setAttachmentFileName1(attachmentFile1.getOriginalFilename());
                    oleEResourceEventLog.setAttachmentMimeType1(attachmentFile1.getContentType());
                    storeEventAttachments(attachmentFile1);
                } catch (Exception e) {
                    LOG.error("Exception while storing the Attachment Document" + e);
                }
            }
        }
        MultipartFile attachmentFile2 = oleEResourceEventLog.getAttachmentFile2();
        if (attachmentFile2 != null && !org.apache.commons.lang.StringUtils.isBlank(attachmentFile2.getOriginalFilename())) {
            if (attachmentFile2.getSize() == 0) {
                GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(sectionId,
                        RiceKeyConstants.ERROR_UPLOADFILE_EMPTY, attachmentFile2.getOriginalFilename());
                validFile = false;
            } else {
                try {
                    oleEResourceEventLog.setAttachmentFileName2(attachmentFile2.getOriginalFilename());
                    oleEResourceEventLog.setAttachmentMimeType2(attachmentFile2.getContentType());
                    storeEventAttachments(attachmentFile2);
                } catch (Exception e) {
                    LOG.error("Exception while storing the Attachment Document" + e);
                }
            }
        }
        return validFile;
    }

    public void downloadAttachment(HttpServletResponse response, String eventLogId, String fileName, byte[] attachmentContent, String attachmentMimeType) throws Exception {
        File file;
        String location = "";
        location = getKualiConfigurationService().getPropertyValueAsString(
                KRADConstants.ATTACHMENTS_PENDING_DIRECTORY_KEY) + OLEConstants.OleLicenseRequest.ERESOURCE_EVENT_TMP_LOCATION;
        if (eventLogId == null) {
            file = new File(getKualiConfigurationService().getPropertyValueAsString(
                    KRADConstants.ATTACHMENTS_PENDING_DIRECTORY_KEY) + OLEConstants.OleLicenseRequest.ERESOURCE_EVENT_TMP_LOCATION +
                    File.separator + fileName);
            LOG.info("Uploaded file location : " + file.getAbsolutePath());
        } else {
            file = new File(location + File.separator + fileName);
            FileUtils.writeByteArrayToFile(file, attachmentContent);
        }
        response.setContentType(attachmentMimeType);
        response.setContentLength((int) file.length());
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + fileName + "\"");
        InputStream fis = new BufferedInputStream(new FileInputStream(file));
        FileCopyUtils.copy(fis, response.getOutputStream());
    }

    private String convertDateFormatToRegex(String format){
        format = format.replace("\\", "\\\\").replace(".", "\\.").replace("-", "\\-").replace("+", "\\+").replace("(",
                "\\(").replace(")", "\\)").replace("[", "\\[").replace("]", "\\]").replace("|", "\\|").replace("yyyy",
                "((1|2)[0-9]{3})").replace("yy", "([0-9]{2})").replaceAll("M{4,}",
                "([@]+)") //"(January|February|March|April|May|June|July|August|September|October|November|December)")
                .replace("MMM", "([@]{3})") //"(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)")
                .replace("MM", "(0[1-9]|1[012])").replace("M", "(0?[1-9]|1[012])").replace("dd",
                        "(0[1-9]|[12][0-9]|3[01])").replace("d", "(0?[1-9]|[12][0-9]|3[01])").replace("hh",
                        "(1[0-2]|0[1-9])").replace("h", "(1[0-2]|0?[1-9])").replace("HH", "(2[0-3]|1[0-9]|0[0-9])")
                .replace("H", "(2[0-3]|1[0-9]|0?[0-9])").replace("kk", "(2[0-4]|1[0-9]|0[1-9])").replace("k",
                        "(2[0-4]|1[0-9]|0?[1-9])").replace("KK", "(1[01]|0[0-9])").replace("K", "(1[01]|0?[0-9])")
                .replace("mm", "([0-5][0-9])").replace("m", "([1-5][0-9]|0?[0-9])").replace("ss", "([0-5][0-9])")
                .replace("s", "([1-5][0-9]|0?[0-9])").replace("SSS", "([0-9][0-9][0-9])").replace("SS",
                        "([0-9][0-9][0-9]?)").replace("S", "([0-9][0-9]?[0-9]?)").replaceAll("E{4,}",
                        "([@]+)")//"(Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday)")
                .replaceAll("E{1,3}", "([@]{3})")//"(Mon|Tue|Wed|Thu|Fri|Sat|Sun)")
                .replace("DDD", "(3[0-6][0-5]|[1-2][0-9][0-9]|0[0-9][1-9])").replace("DD",
                        "(3[0-6][0-5]|[1-2][0-9][0-9]|0?[0-9][1-9])").replace("D",
                        "(3[0-6][0-5]|[1-2][0-9][0-9]|0?[0-9]?[1-9])").replace("F", "([1-5])").replace("ww",
                        "(5[0-3]|[1-4][0-9]|0[1-9])").replace("w", "(5[0-3]|[1-4][0-9]|[1-9])").replace("W", "([1-5])")
                .replaceAll("z{4,}", "([@]+)").replaceAll("z{1,3}", "([@]{1,4})").replaceAll("a{1,}", "([aApP][mM])")
                .replaceAll("G{1,}", "([aA][dD]|[bB][cC])").replace(" ", "\\s").replace("@", "a-zA-Z");

        return format;

    }
    public OLEEResourceRecordDocument populateInstanceAndEInstance(OLEEResourceRecordDocument oleeResourceRecordDocument){
        if (oleeResourceRecordDocument.getOleERSIdentifier() != null) {
            Map ids = new HashMap();
            ids.put("oleERSIdentifier", oleeResourceRecordDocument.getOleERSIdentifier());
            List<OLEEResourceInstance> oleeResourceInstances = (List<OLEEResourceInstance>) getBusinessObjectService().findMatching(OLEEResourceInstance.class, ids);
            oleeResourceInstances = populateUrlForHoldings(oleeResourceInstances);
            oleeResourceRecordDocument.geteRSInstances().clear();
            oleeResourceRecordDocument.geteRSInstances().addAll(oleeResourceInstances);
            oleeResourceRecordDocument.setOleERSInstances(oleeResourceInstances);
            ids = new HashMap();
            List<OLEEResourceInstance> linkedResourceInstances = new ArrayList<>();
            for (OLELinkedEresource linkedEresource : oleeResourceRecordDocument.getOleLinkedEresources()) {
                if (linkedEresource.getRelationShipType().equalsIgnoreCase("child")) {
                    ids.put("oleERSIdentifier", linkedEresource.getLinkedERSIdentifier());
                    linkedResourceInstances.addAll(getBusinessObjectService().findMatching(OLEEResourceInstance.class, ids));
                    for (OLEEResourceLicense oleeResourceLicense : linkedEresource.getOleeResourceRecordDocument().getOleERSLicenseRequests()) {
                        oleeResourceRecordDocument.getOleERSLicenseRequests().add(oleeResourceLicense);
                    }
                }
            }
            oleeResourceRecordDocument.geteRSInstances().addAll(linkedResourceInstances);
        }
        return oleeResourceRecordDocument;
    }

    public List<OLEEResourceInstance> populateUrlForHoldings(List<OLEEResourceInstance> oleEResourceInstances) {
        Set<String> holdingIds = new HashSet<>();
        for(OLEEResourceInstance oleeResourceInstance : oleEResourceInstances) {
            holdingIds.add(DocumentUniqueIDPrefix.getDocumentId(oleeResourceInstance.getInstanceId()));
        }
        if(CollectionUtils.isNotEmpty(holdingIds)) {
            Map linkMap = new HashMap<>();
            linkMap.put("holdingsId",holdingIds);
            List<HoldingsUriRecord> holdingsUriRecordList = (List<HoldingsUriRecord>) getBusinessObjectService().findMatching(HoldingsUriRecord.class, linkMap);
            if(CollectionUtils.isNotEmpty(holdingsUriRecordList)) {
                for(OLEEResourceInstance oleeResourceInstance : oleEResourceInstances) {
                    String url = "";
                    List<String> urlList = new ArrayList<>();
                    for(HoldingsUriRecord holdingsUriRecord : holdingsUriRecordList) {
                        if(StringUtils.isNotBlank(holdingsUriRecord.getHoldingsId()) && holdingsUriRecord.getHoldingsId().equals(DocumentUniqueIDPrefix.getDocumentId(oleeResourceInstance.getInstanceId()))) {
                            if(StringUtils.isNotBlank(holdingsUriRecord.getUri())) {
                                urlList.add(holdingsUriRecord.getUri());
                            }
                        }
                    }
                    if(CollectionUtils.isNotEmpty(urlList)) {
                        for(int i=0 ; i<urlList.size() ; i++) {
                            if(i == 0) {
                                url = urlList.get(i);
                            } else {
                                url = url + "," + urlList.get(i);
                            }
                        }
                    }
                    if(StringUtils.isNotBlank(url)) {
                        oleeResourceInstance.setUrl(url);
                    }
                }
            }
        }
        return oleEResourceInstances;
    }

    public UniversityDateService getUniversityDateService() {
        if(universityDateService == null) {
            universityDateService = SpringContext.getBean(UniversityDateService.class);
        }
        return universityDateService;
    }


    private List<OleCopy> getCopies(String id,String key, String location) {
        List<OleCopy> copies = null;
        if (StringUtils.isNotBlank(id)) {
            Map<String, String> criteriaMap = new HashMap<>();
            criteriaMap.put(key, id);
            if(StringUtils.isNotBlank(location)){
                criteriaMap.put(OLEConstants.LOC, location);
            }
            copies = (List<OleCopy>) getBusinessObjectService().findMatching(OleCopy.class,
                    criteriaMap);
        }
        return copies;
    }

}
