package org.kuali.ole.describe.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.converter.MarcXMLConverter;
import org.kuali.ole.describe.bo.BibDocumentSearchResult;
import org.kuali.ole.describe.bo.ExternalDataSourceConfig;
import org.kuali.ole.describe.bo.ImportBibSearch;
import org.kuali.ole.describe.bo.ImportBibUserPreferences;
import org.kuali.ole.describe.form.ImportBibForm;
import org.kuali.ole.describe.service.ImportBibService;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.BibMarc;
import org.kuali.ole.docstore.common.document.BibMarcMapper;
import org.kuali.ole.docstore.common.document.BibTree;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.ControlField;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.search.SearchCondition;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.model.xstream.work.bib.marc.MRKToMARCXMLConverter;
import org.kuali.ole.externalds.DataSourceConfig;
import org.kuali.ole.externalds.ExternalDataSource;
import org.kuali.ole.externalds.ExternalDataSourceFactory;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: pj7789
 * Date: 26/11/12
 * Time: 11:46 AM
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping(value = "/importBibController")
public class ImportBibController
        extends UifControllerBase {

    private static final Logger LOG = Logger.getLogger(ImportBibController.class);

    BibMarcRecordProcessor workBibMarcRecordProcessor = new BibMarcRecordProcessor();
    BibMarcMapper bibMarcMapper = BibMarcMapper.getInstance();

    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */


    /**
     * This method creates a instance of ListBoxForm
     *
     * @param request
     * @return ListBoxForm
     */
    @Override
    protected ImportBibForm createInitialForm(HttpServletRequest request) {
        return new ImportBibForm();
    }

    private boolean canPerformImportBibLocal(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.CAT_NAMESPACE, OLEConstants.INGEST_BIB_LOCAL);
    }

    private boolean canPerformImportBibExternal(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.CAT_NAMESPACE, OLEConstants.INGEST_BIB_EXTERNAL);
    }

    /**
     * This method takes the initial request when click on DublinEditor Screen.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        ImportBibForm importBibForm = (ImportBibForm) form;
        boolean hasPermission = canPerformImportBibLocal(GlobalVariables.getUserSession().getPrincipalId()) && canPerformImportBibExternal(GlobalVariables.getUserSession().getPrincipalId());
        if (!hasPermission) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_INFO, OLEConstants.ERROR_AUTHORIZATION);
            return getUIFModelAndView(importBibForm);
        }
        request.getSession().removeAttribute("bibUuidsList");
        return super.start(importBibForm, result, request, response);
    }

    /**
     * Used for Test-case
     *
     * @param result
     * @param request
     * @param response
     * @param importBibForm
     * @return ModelAndView
     */
    protected ModelAndView callSuper(BindingResult result, HttpServletRequest request, HttpServletResponse response,
                                     ImportBibForm importBibForm) {
        return super.navigate(importBibForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=overLay")
    public ModelAndView overLay(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {
        String uuid = request.getParameter("docId");
        ImportBibForm importBibForm = (ImportBibForm) form;
        boolean hasPermission = canPerformImportBibLocal(GlobalVariables.getUserSession().getPrincipalId()) && canPerformImportBibExternal(GlobalVariables.getUserSession().getPrincipalId());
        if (!hasPermission) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_INFO, OLEConstants.ERROR_AUTHORIZATION);
            return getUIFModelAndView(importBibForm);
        }
        importBibForm.setUuid(uuid);
        Bib bib = getDocstoreClientLocator().getDocstoreClient().retrieveBib(uuid);

        if (bib.getContent() != null && bib.getContent().length() > 0) {
            BibMarcRecords marcRecords = workBibMarcRecordProcessor.fromXML(bib.getContent());
            if (marcRecords != null && marcRecords.getRecords() != null && marcRecords.getRecords().size() > 0) {
                importBibForm.setExistingBibMarcRecord(marcRecords.getRecords().get(0));
            }
        }
        return getUIFModelAndView(importBibForm, "SearchBibViewPage");
    }

    @RequestMapping(params = "methodToCall=load")
    public ModelAndView load(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.info("Inside Search Method");
        ImportBibForm importBibForm = (ImportBibForm) form;
        MultipartFile multipartFile = importBibForm.getImportBibSearch().getLocationFile();
        importBibForm.getImportBibSearch().setReturnCheck(false);
        importBibForm.getMarcDocRecMap().clear();
        importBibForm.getBibMarcRecordList().clear();
        importBibForm.setMessage("");
        String marcXmlContent = getMarcXml(multipartFile, importBibForm);
        request.getSession().setAttribute("showDetailForLocalSearchRecord", marcXmlContent);
        if (marcXmlContent != null && marcXmlContent.length() > 0) {
            //convert xml to pojo
            BibMarcRecords bibMarcRecords = workBibMarcRecordProcessor.fromXML(marcXmlContent);
            List<BibMarcRecord> workBibMarcRecordList = new ArrayList<BibMarcRecord>();
            List<BibMarcRecord> workBibMarcRecords = new ArrayList<BibMarcRecord>();
            int uniCodeCount = 0;
            int marcCount = 0;
            if (bibMarcRecords != null) {
                for (BibMarcRecord workBibMarcRecord : bibMarcRecords.getRecords()) {
                    String leader = workBibMarcRecord.getLeader();
                    char unicode = leader.charAt(9);
                    if (unicode == 'a') {
                        workBibMarcRecordList.add(workBibMarcRecord);
                        uniCodeCount = uniCodeCount + 1;
                    } else {
                        workBibMarcRecords.add(workBibMarcRecord);
                        marcCount = marcCount + 1;
                    }
                }
                importBibForm.setBibMarcRecordList(workBibMarcRecordList);
            }
            LOG.info("No of Unicode records: " + uniCodeCount);
            LOG.info("No of Marc8 records: " + marcCount);
            importBibForm.getImportBibSearch().setSelectedFileName(multipartFile.getOriginalFilename());
            importBibForm.getImportBibSearch().setRecordsInFile(bibMarcRecords.getRecords().size());
            bibMarcRecords.setRecords(workBibMarcRecordList);
            importBibForm.getImportBibSearch().setRecordsInUnicode(workBibMarcRecordList.size());
            importBibForm.getImportBibSearch().setRecordsInNonUnicode(workBibMarcRecords.size());
            // convert pojo to workBibDoc
            List<Bib> workBibDocumentList = getBibDocumens(bibMarcRecords);
            List<BibDocumentSearchResult> importBibResultsList = mapMarcFieldValues(workBibDocumentList);
            // Sorting the import bib result list and mapping it to corresponding marc record
            SortedMap<BibDocumentSearchResult, BibMarcRecord> marcDocRecMapSorted = new TreeMap<BibDocumentSearchResult, BibMarcRecord>(new ImportBibService());
            if (!workBibMarcRecordList.isEmpty()) {
                for (int i = 0; i < importBibResultsList.size(); i++) {
                    marcDocRecMapSorted.put(importBibResultsList.get(i), importBibForm.getBibMarcRecordList().get(i));
                }
                importBibForm.setMarcDocRecMap(marcDocRecMapSorted);
            }
            List<BibDocumentSearchResult> searchList = new ArrayList<BibDocumentSearchResult>();
            searchList.addAll(importBibForm.getMarcDocRecMap().keySet());
            // adding the sorted key values to search results list to display in seacrch table
            importBibForm.getImportBibSearch().setLocalBibDocumentSearchResults(searchList);
            if (marcCount > 0 && uniCodeCount > 0) {
                GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "load.import.bib.unicode");
            } else if (marcCount > 0 && uniCodeCount == 0) {
                GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "load.import.bib.unicode");
            } else {
                importBibForm.setMessage("");
            }
        }
        return getUIFModelAndView(importBibForm);
    }

    @RequestMapping(params = "methodToCall=search")
    public ModelAndView search(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.info("Inside Search Method");
        ImportBibForm importBibForm = (ImportBibForm) form;
        SearchParams searchParams = importBibForm.getSearchParams();
        if (searchParams.getSearchConditions().get(0).getSearchField().getFieldValue() == "" || searchParams.getSearchConditions().get(0).getSearchField().getFieldValue() == null) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DESCRIBE_ENTER_SEARCH_TEXT);
            importBibForm.setBibMarcRecordList(null);
            importBibForm.setBibUuidsList(null);
            importBibForm.setImportBibSearch(null);
            searchParams.getSearchResultFields().clear();
            return getUIFModelAndView(importBibForm);
        }
        String source = importBibForm.getImportBibSearch().getSource();
        List<BibDocumentSearchResult> searchList = new ArrayList<BibDocumentSearchResult>();
        importBibForm.getImportBibSearch().setExternalBibDocumentSearchResults(searchList);
        if (source != null && source.length() > 0) {
            List<String> results = null;
            DataSourceConfig dataSourceConfig = new DataSourceConfig();
            BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
            Map parentCriteria = new HashMap();
            parentCriteria.put("id", source);
            ExternalDataSourceConfig externalDataSourceConfig = new ExternalDataSourceConfig();
            externalDataSourceConfig = businessObjectService.findByPrimaryKey(ExternalDataSourceConfig.class, parentCriteria);
            dataSourceConfig.setDomainName(externalDataSourceConfig.getDomainName());
            dataSourceConfig.setPortNum(externalDataSourceConfig.getPortNum());
            dataSourceConfig.setDatabaseName(externalDataSourceConfig.getDatabaseName());
            dataSourceConfig.setLoginId(externalDataSourceConfig.getLoginId());
            dataSourceConfig.setPassword(externalDataSourceConfig.getPassword());
            dataSourceConfig.setAuthKey(externalDataSourceConfig.getAuthKey());
            ExternalDataSource externalDataSource = ExternalDataSourceFactory.getInstance().getExternalDataSource(dataSourceConfig);
            // NOTE: Uncomment this line to enable Searching external Z39.50 data sources.
            /*try {
                results = externalDataSource.searchForBibs(searchParams, dataSourceConfig);
            } catch (Exception e){
                LOG.error("Error occurred while fetching the records :"+e);
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.EXTERNAL_DATA_IMPORT_ERROR);
                return getUIFModelAndView(importBibForm);
            }*/

            request.getSession().setAttribute("showDetailForExternalSearchRecord", results);
            BibMarcRecords marcRecords = prepareWorkBibMarcRecords(results);
            if (marcRecords != null && CollectionUtils.isNotEmpty(marcRecords.getRecords())) {
                importBibForm.setBibMarcRecordList(marcRecords.getRecords());
                List<Bib> workBibDocumentList = getBibDocumens(marcRecords);
                List<BibDocumentSearchResult> importBibResultsList = mapMarcFieldValues(workBibDocumentList);
                SortedMap<BibDocumentSearchResult, BibMarcRecord> marcDocRecMapSorted = new TreeMap<BibDocumentSearchResult, BibMarcRecord>(new ImportBibService());
                for (int i = 0; i < importBibResultsList.size(); i++) {
                    marcDocRecMapSorted.put(importBibResultsList.get(i), importBibForm.getBibMarcRecordList().get(i));
                }

                importBibForm.setMarcDocRecMap(marcDocRecMapSorted);
                searchList.addAll(importBibForm.getMarcDocRecMap().keySet());
                // adding the sorted key values to search results list to display in seacrch table
                importBibForm.getImportBibSearch().setExternalBibDocumentSearchResults(searchList);
                importBibForm.getImportBibSearch().setReturnCheck(true);
            } else {
                GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_ERRORS, OLEConstants.NO_RECORD_FOUND);
            }

        } else {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.SOURCE_NOT_SELECTED);
            importBibForm.setImportBibSearch(null);
            return getUIFModelAndView(importBibForm);
        }

        return getUIFModelAndView(importBibForm);
    }

    public BibMarcRecords prepareWorkBibMarcRecords(List<String> results) {
        MRKToMARCXMLConverter mrkToMARCXMLConverter = new MRKToMARCXMLConverter();
        StringBuffer sb = new StringBuffer(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><collection xmlns=\"http://www.loc.gov/MARC21/slim\">");
        for (String marcString : results) {
            String marcXML = mrkToMARCXMLConverter.convert(marcString);
            sb.append(marcXML.substring(marcXML.indexOf("<record>"), marcXML.indexOf("</collection>")));
        }
        sb.append("</collection>");
        String marcXmlContent = sb.toString();
        BibMarcRecords marcRecords = workBibMarcRecordProcessor.fromXML(marcXmlContent);
        return marcRecords;
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=clearSearch")
    public ModelAndView clearSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        LOG.info("Inside clearSearch Method");
        ImportBibForm importBibForm = (ImportBibForm) form;
        importBibForm.getImportBibSearch().setReturnCheck(true);
        importBibForm.getImportBibSearch().setExternalBibDocumentSearchResults(null);
        importBibForm.getImportBibSearch().setSource("");
        List<SearchCondition> searchConditions = new ArrayList<SearchCondition>();
        searchConditions.add(new SearchCondition());
        searchConditions.add(new SearchCondition());
        importBibForm.getSearchParams().getSearchConditions().addAll(searchConditions);
        return getUIFModelAndView(importBibForm);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=clearFile")
    public ModelAndView clearFile(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                  HttpServletRequest request, HttpServletResponse response) {
        LOG.info("Inside clearSearch Method");

        ImportBibForm importBibForm = (ImportBibForm) form;
        importBibForm.getImportBibSearch().setReturnCheck(false);
        importBibForm.getImportBibSearch().setLocalBibDocumentSearchResults(null);
        importBibForm.getImportBibSearch().setSelectedFileName("");
        importBibForm.getImportBibSearch().setRecordsInFile(0);
        importBibForm.getImportBibSearch().setRecordsImported(0);
        importBibForm.setMessage("");
        return getUIFModelAndView(importBibForm);
    }

    @RequestMapping(params = "methodToCall=actionLink")
    public ModelAndView actionLink(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
        ImportBibForm importBibForm = (ImportBibForm) form;
        int index = Integer.parseInt(form.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        List<BibMarcRecord> marcRecList = importBibForm.getBibMarcRecordList();
        BibMarcRecord marcRec = marcRecList.get(index);
        LOG.info("Marc Rec is..." + marcRec);
        return getUIFModelAndView(importBibForm);
    }

    @RequestMapping(params = "methodToCall=localNext")
    public ModelAndView next(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {
        ImportBibForm importBibForm = (ImportBibForm) form;
        if (importBibForm != null && importBibForm.getImportBibSearch() != null
                && importBibForm.getImportBibSearch().getLocalBibDocumentSearchResults() != null) {
            List<BibDocumentSearchResult> bibDocumentSearchResultList = importBibForm.getImportBibSearch()
                    .getLocalBibDocumentSearchResults();
            boolean isSelected = true;
            int i = 0;
            for (BibDocumentSearchResult workBibDocumentResult : bibDocumentSearchResultList) {
                i++;
                if (workBibDocumentResult.isSelectedMarc() && isSelected) {
                    isSelected = false;
                    importBibForm.getImportBibSearch().setSelectedMarc(
                            importBibForm.getImportBibSearch().getLocalBibDocumentSearchResults().get(i - 1));
                    importBibForm.setNewBibMarcRecord(importBibForm.getMarcDocRecMap().get(workBibDocumentResult));
                    importBibForm.getImportBibSearch().setSelectedRecordIndex(i - 1);
                }
            }
            if (isSelected) {
                GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "select.import.bib.record");
                return getUIFModelAndView(importBibForm, "SearchBibViewPage");
            }
        }
        importBibForm.setMessage(null);
        return getUIFModelAndView(importBibForm, "UserPreferenceViewPage");
    }

    @RequestMapping(params = "methodToCall=externalNext")
    public ModelAndView externalNext(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        ImportBibForm importBibForm = (ImportBibForm) form;
        if (importBibForm != null && importBibForm.getImportBibSearch() != null
                && importBibForm.getImportBibSearch().getExternalBibDocumentSearchResults() != null) {
            List<BibDocumentSearchResult> bibDocumentSearchResultList = importBibForm.getImportBibSearch()
                    .getExternalBibDocumentSearchResults();
            boolean isSelected = true;
            int i = 0;
            for (BibDocumentSearchResult workBibDocumentResult : bibDocumentSearchResultList) {
                i++;
                if (workBibDocumentResult.isSelectedExternalMarc() && isSelected) {
                    isSelected = false;
                    importBibForm.getImportBibSearch().setSelectedMarc(
                            importBibForm.getImportBibSearch().getExternalBibDocumentSearchResults().get(i - 1));
                    importBibForm.setNewBibMarcRecord(importBibForm.getMarcDocRecMap().get(workBibDocumentResult));
                    importBibForm.getImportBibSearch().setSelectedRecordIndex(i - 1);
                }
            }
            if (isSelected) {
                GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "select.import.bib.record");
                return getUIFModelAndView(importBibForm, "SearchBibViewPage");
            }
        }
        importBibForm.setMessage(null);
        return getUIFModelAndView(importBibForm, "UserPreferenceViewPage");
    }

    @RequestMapping(params = "methodToCall=loadUserPref")
    public ModelAndView loadUserPref(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {
        ImportBibForm importBibForm = (ImportBibForm) form;
        ImportBibUserPreferences importBibUserPreferences = importBibForm.getImportBibUserPreferences();
        if (importBibUserPreferences.getPrefId1().length() > 0) {
            importBibUserPreferences.setPrefId(Integer.valueOf(importBibUserPreferences.getPrefId1()));
            if (importBibUserPreferences.getPrefId() != null && importBibUserPreferences.getPrefId() > 0) {
                BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
                Map parentCriteria = new HashMap();
                parentCriteria.put("prefId", importBibUserPreferences.getPrefId());
                importBibUserPreferences = businessObjectService
                        .findByPrimaryKey(ImportBibUserPreferences.class, parentCriteria);
                importBibForm.setImportBibUserPreferences(importBibUserPreferences);
                parentCriteria.put("prefId", 1);
                ImportBibUserPreferences importBibAdminPreferences = businessObjectService
                        .findByPrimaryKey(ImportBibUserPreferences.class, parentCriteria);
                importBibForm.getImportBibUserPreferences()
                        .setAdminProtectedTags(importBibAdminPreferences.getProtectedTags());
                importBibForm.getImportBibUserPreferences().setAdminRemovalTags(importBibAdminPreferences.getRemovalTags());
            }
            importBibUserPreferences.setPrefId1(String.valueOf(importBibUserPreferences.getPrefId()));
        }

        if (importBibForm.getExistingBibMarcRecord() != null) {
            //converting marc record to WorkBibDocument
            BibMarcRecords marcRecords = new BibMarcRecords();
            List<BibMarcRecord> workBibMarcRecordList = new ArrayList<BibMarcRecord>();
            workBibMarcRecordList.add(importBibForm.getExistingBibMarcRecord());
            marcRecords.setRecords(workBibMarcRecordList);
            List<Bib> workBibDocumentList = getBibDocumens(marcRecords);
            //setting uuid (control field 001) to WorkBibMarc Document
            for (ControlField cf : importBibForm.getExistingBibMarcRecord().getControlFields()) {
                if (cf.getTag().equalsIgnoreCase("001")) {
                    workBibDocumentList.get(0).setId(cf.getValue());
                    break;
                }
            }
            // getting workbibdocument with Instance data.
            List<String> uuids = new ArrayList<>();
            for (Bib bib : workBibDocumentList) {
                uuids.add(bib.getId());
            }
            List<Bib> workBibDocuments = getDocstoreClientLocator().getDocstoreClient().retrieveBibs(uuids);
            List<BibDocumentSearchResult> importBibResultsList = mapMarcFieldValues(workBibDocuments);
            // setting workbibDocument to OverLayMarcRecord
            if (importBibForm.getImportBibConfirmReplace() != null && importBibResultsList != null
                    && importBibResultsList.size() > 0) {
                BibDocumentSearchResult bibDocumentSearchResult = importBibResultsList.get(0);
                BibTree bibTree = getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(bibDocumentSearchResult.getId());
                bibDocumentSearchResult.setHoldingsTree(bibTree.getHoldingsTrees().get(0));
                importBibForm.getImportBibConfirmReplace().setOverLayMarcRecord(bibDocumentSearchResult);
            }
        }
        if (importBibForm.getExistingBibMarcRecord() != null) {
            importBibUserPreferences.setImportType("overLay");
        } else if (importBibUserPreferences != null && "overLay".equalsIgnoreCase(importBibUserPreferences.getImportType())) {
            importBibUserPreferences.setImportType("newImport");
        }

        return getUIFModelAndView(importBibForm, "UserPreferenceViewPage");
    }


    @RequestMapping(params = "methodToCall=userPrefNext")
    public ModelAndView userPrefNext(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {
        ImportBibForm importBibForm = (ImportBibForm) form;
        ImportBibUserPreferences importBibUserPreferences = importBibForm.getImportBibUserPreferences();
        ImportBibService importBibService = new ImportBibService();
        if (importBibService.callNumValidation(importBibUserPreferences)) {
            GlobalVariables.getMessageMap().putError("documentForm.selectedHolding.callNumber.number", "error.import.bib.callnumber");
            return getUIFModelAndView(importBibForm, "UserPreferenceViewPage");
        }
        if (importBibService.proNRemTagValidation(importBibUserPreferences)) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, "invalid.import.bib.datafields");
            return getUIFModelAndView(importBibForm, "UserPreferenceViewPage");
        }
        importBibForm.setMessage(null);
        importBibService.applyUserPref(importBibForm);
        request.getSession().setAttribute("importBibForm", importBibForm);
        request.getSession().setAttribute("marcDocRecMap", importBibForm.getMarcDocRecMap());
        request.getSession().setAttribute("bibMarcRecordList", importBibForm.getBibMarcRecordList());
        request.getSession().setAttribute("bibUuidsList", importBibForm.getBibUuidsList());
        request.getSession().setAttribute("importBibSearch", importBibForm.getImportBibSearch());
        BibTree bibTree = importBibService.createBibTree(importBibForm.getNewBibMarcRecord(), importBibForm.getNewInstanceCollection(), importBibUserPreferences.getImportStatus());
        if (StringUtils.isNotEmpty(importBibForm.getUuid())) {
            bibTree.getBib().setId(importBibForm.getUuid());
        }
        request.getSession().setAttribute("bibTree", bibTree);
        if (!importBibUserPreferences.getImportType().equalsIgnoreCase("newImport")) {
            return getUIFModelAndView(importBibForm, "ConfirmReplace");
        }
        try {
            String url = ConfigContext.getCurrentContextConfig().getProperty("ole.fs.url.base");
            url = url + "/portal.do?channelTitle=Import Bib&channelUrl=" + url +
                    "/ole-kr-krad/editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType" +
                    "=bibliographic&docFormat=marc&loadFromSession=true&editable=true";
            return performRedirect(importBibForm, url);
        } catch (Exception e) {
            LOG.error("Exception while forwarding to editor." + e);
        }
        return getUIFModelAndView(importBibForm, "ConfirmImport");
    }

    @RequestMapping(params = "methodToCall=close")
    public ModelAndView close(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {

        String url = ConfigContext.getCurrentContextConfig().getProperty("ole.fs.url.base");
        return performRedirect(form, url);
    }


    @RequestMapping(params = "methodToCall=loadImportBibSearch")
    public ModelAndView loadImportBibSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        LOG.info("Inside confirmReplaceNext Method");
        ImportBibForm importBibForm = (ImportBibForm) form;
        if (importBibForm != null && importBibForm.getImportBibSearch() != null) {
            if (importBibForm.getMarcDocRecMap() != null && importBibForm.getImportBibSearch() != null &&
                    importBibForm.getImportBibSearch().getSelectedMarc() != null) {
                importBibForm.getMarcDocRecMap().remove(importBibForm.getImportBibSearch().getSelectedMarc());
                List<BibDocumentSearchResult> localRecList = new ArrayList<BibDocumentSearchResult>();
                List<BibMarcRecord> marcRecList = new ArrayList<BibMarcRecord>();
                for (Map.Entry<BibDocumentSearchResult, BibMarcRecord> record : importBibForm.getMarcDocRecMap().entrySet()) {
                    localRecList.add(record.getKey());
                    marcRecList.add(record.getValue());
                }
                importBibForm.getImportBibSearch().setLocalBibDocumentSearchResults(localRecList);
                importBibForm.setBibMarcRecordList(marcRecList);
            }
            importBibForm.getMarcDocRecMap().remove(importBibForm.getImportBibSearch().getSelectedMarc());
            importBibForm.getImportBibSearch()
                    .setRecordsImported(Math.max(0, importBibForm.getImportBibSearch().getRecordsImported() + 1));
            importBibForm.getImportBibSearch()
                    .setRecordsInFile(Math.max(0, importBibForm.getImportBibSearch().getRecordsInFile() - 1));
        }
        return getUIFModelAndView(importBibForm, "SearchBibViewPage");
    }

    @RequestMapping(params = "methodToCall=viewRecordNext")
    public ModelAndView viewRecordNext(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response)
            throws IOException, SolrServerException {
        LOG.info("Inside Search Method");
        ImportBibForm importBibForm = (ImportBibForm) form;
        BibTree bibTree = null;
        if (request != null && request.getSession() != null) {
            bibTree = (BibTree) request.getSession().getAttribute("responseBibTree");
            if (bibTree == null) {
                bibTree = (BibTree) request.getSession().getAttribute("bibTree");
            }
            if (request.getSession().getAttribute("importBibForm") != null) {
                ImportBibForm importBibForminSession = (ImportBibForm) request.getSession().getAttribute("importBibForm");
                if (importBibForminSession != null) {
                    importBibForm.setImportBibSearch(importBibForminSession.getImportBibSearch());
                    importBibForm.setBibMarcRecordList(importBibForminSession.getBibMarcRecordList());
                    importBibForm.setBibUuidsList(importBibForminSession.getBibUuidsList());
                    importBibForm.setMarcDocRecMap(importBibForminSession.getMarcDocRecMap());
                    request.getSession().removeAttribute("importBibForm");
                }
            }
            if (importBibForm.getMarcDocRecMap().isEmpty()) {
                importBibForm.setMarcDocRecMap((SortedMap<BibDocumentSearchResult, BibMarcRecord>) request.getSession().getAttribute("marcDocRecMap"));
                request.getSession().removeAttribute("marcDocRecMap");
            }
            if (importBibForm.getImportBibSearch().getLocalBibDocumentSearchResults() == null || importBibForm.getImportBibSearch().getLocalBibDocumentSearchResults().isEmpty()) {
                importBibForm.setImportBibSearch((ImportBibSearch) request.getSession().getAttribute("importBibSearch"));
                request.getSession().removeAttribute("importBibSearch");
            }
            if (importBibForm.getBibUuidsList().isEmpty() && request.getSession().getAttribute("bibUuidsList") != null) {
                importBibForm.setBibUuidsList((List<BibDocumentSearchResult>) request.getSession().getAttribute("bibUuidsList"));
                // request.getSession().removeAttribute("bibUuidsList");
            }
            if (importBibForm.getBibMarcRecordList() == null || importBibForm.getBibMarcRecordList().isEmpty()) {
                importBibForm.setBibMarcRecordList((List<BibMarcRecord>) request.getSession().getAttribute("bibMarcRecordList"));
                request.getSession().removeAttribute("bibMarcRecordList");
            }


            request.getSession().removeAttribute("bibTree");
            request.getSession().removeAttribute("responseBibTree");
        }

        List<Bib> bibDocumentList = new ArrayList<Bib>();

        if (bibTree != null) {

            Bib bib = bibTree.getBib();
            bib.deserializeContent(bib);
            bibDocumentList.add(bib);
            importBibForm.setUuid(bib.getId());
        }

        List<BibDocumentSearchResult> uuidList = importBibForm.getBibUuidsList();
        BibDocumentSearchResult bibDocumentSearchResult = new BibDocumentSearchResult();

        for (Bib workBibDocument : bibDocumentList) {
            bibDocumentSearchResult = new BibDocumentSearchResult();
            bibDocumentSearchResult.setId(workBibDocument.getId());
            bibDocumentSearchResult.setUuid(workBibDocument.getId());
            if (workBibDocument.getTitle() != null) {
                bibDocumentSearchResult.setDisplayField(workBibDocument.getTitle());
                bibDocumentSearchResult.setUuid(workBibDocument.getId());
            }
            uuidList.add(bibDocumentSearchResult);
        }
        importBibForm.setBibUuidsList(uuidList);
        return getUIFModelAndView(importBibForm, "ConfirmImport");
    }


    @RequestMapping(params = "methodToCall=confirmReplaceNext")
    public ModelAndView confirmReplaceNext(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        LOG.info("Inside loadImportBibSearch Method");
        ImportBibForm importBibForm = (ImportBibForm) form;

        try {
            String url = ConfigContext.getCurrentContextConfig().getProperty("ole.fs.url.base");
            url = url + "/portal.do?channelTitle=Import Bib&channelUrl=" + url
                    + "/ole-kr-krad/editorcontroller?viewId=EditorView&methodToCall=load&docCategory=work&docType=bibliographic&docFormat=marc&loadFromSession=true&editable=true&docId="
                    + importBibForm.getUuid();
            return performRedirect(importBibForm, url);
        } catch (Exception e) {
            LOG.error("Exception while forwarding to editor." + e);
        }
        return getUIFModelAndView(importBibForm, "ConfirmImport");
    }

    private String getMarcXml(MultipartFile file, UifFormBase form) throws IOException {
        String marcXMLContent = null;
        String modifiedXMLContent = null;
        MultipartFile multipartFile = file;
        String locationFileName = multipartFile.getOriginalFilename();
        if (locationFileName.toLowerCase().endsWith(".mrc")) {
            String fileContent = new String(multipartFile.getBytes());
            try {
                MarcXMLConverter marcXMLConverter = new MarcXMLConverter();
                marcXMLContent = marcXMLConverter.convert(fileContent);
                modifiedXMLContent = marcXMLContent.replace(
                        "collection xmlns=\"http://www.loc.gov/MARC21/slim\" xmlns=\"http://www.loc.gov/MARC21/slim",
                        "collection xmlns=\"http://www.loc.gov/MARC21/slim");
            } catch (Exception e) {
                LOG.error("Exception :", e);
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, "error.encoding.message");
            }
        } else {
            GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "select.import.bib.file");
        }
        return modifiedXMLContent;
    }

    private List<BibDocumentSearchResult> mapMarcFieldValues(List<Bib> bibList) {
        List<BibDocumentSearchResult> importBibResultsList = new ArrayList<BibDocumentSearchResult>();
        for (Bib bibDoc : bibList) {
            BibDocumentSearchResult workBibDocument = new BibDocumentSearchResult();
            workBibDocument.setId(bibDoc.getId());
            workBibDocument.setAuthor(bibDoc.getAuthor());
            workBibDocument.setTitle(bibDoc.getTitle());
            workBibDocument.setPublicationDate(bibDoc.getPublicationDate());
            workBibDocument.setEdition(bibDoc.getEdition());
            importBibResultsList.add(workBibDocument);
        }
        return importBibResultsList;
    }

    @RequestMapping(params = "methodToCall=showDetailForLocalSearchRecord")
    public ModelAndView showDetailForLocalSearchRecord(@ModelAttribute("KualiForm") UifFormBase form,
                                                       BindingResult result, HttpServletRequest request,
                                                       HttpServletResponse response) throws Exception {
        MRKToMARCXMLConverter mrkToMARCXMLConverter = new MRKToMARCXMLConverter();
        String bibId = request.getParameter("bibId");
        LOG.info("showDetailForLocalSearchRecord bibId " + bibId);
        ImportBibForm importBibForm = (ImportBibForm) form;
        String marcXmlContent = (String) request.getSession().getAttribute("showDetailForLocalSearchRecord");
        SortedMap<BibDocumentSearchResult, BibMarcRecord> marcDocRecMapSorted = sort(marcXmlContent);
        List<BibDocumentSearchResult> searchList = new ArrayList<BibDocumentSearchResult>();
        searchList.addAll(marcDocRecMapSorted.keySet());
        if (marcXmlContent != null && marcXmlContent.length() > 0) {
            BibMarcRecord workBibMarcRecord = marcDocRecMapSorted.get(searchList.get(Integer.parseInt(bibId)));
            List<BibMarcRecord> workBibMarcRecordList = new ArrayList<BibMarcRecord>();
            workBibMarcRecordList.add(workBibMarcRecord);
            BibMarcRecords marcRecordsNew = new BibMarcRecords();
            marcRecordsNew.setRecords(workBibMarcRecordList);
            String detailXML = workBibMarcRecordProcessor.toXml(marcRecordsNew);
            LOG.info("detailXML " + detailXML);
            String marc = mrkToMARCXMLConverter.ConvertMarcXMLToMRK(detailXML);
            importBibForm.setMessage("<pre>" + marc + "</pre>");
            LOG.info("marc " + marc);
        }
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=showDetailForExternalSearchRecord")
    public ModelAndView showDetailForExternalSearchRecord(@ModelAttribute("KualiForm") UifFormBase form,
                                                          BindingResult result, HttpServletRequest request,
                                                          HttpServletResponse response) {
        String bibId = request.getParameter("bibId");
        ImportBibForm importBibForm = (ImportBibForm) form;
        List<String> results = (List<String>) request.getSession().getAttribute("showDetailForExternalSearchRecord");
        String resultvalue = results.get(Integer.parseInt(bibId));
        importBibForm.setMessage("<pre>" + resultvalue + "</pre>");
        return getUIFModelAndView(form);
    }

    public SortedMap<BibDocumentSearchResult, BibMarcRecord> sort(String marcXmlContent) {
        if (marcXmlContent != null && marcXmlContent.length() > 0) {
            //convert xml to pojo
            BibMarcRecords marcRecords = workBibMarcRecordProcessor.fromXML(marcXmlContent);
            List<BibMarcRecord> workBibMarcRecordList = marcRecords.getRecords();
            // convert pojo to workBibDoc
            List<Bib> workBibDocumentList = getBibDocumens(marcRecords);
            List<BibDocumentSearchResult> importBibResultsList = mapMarcFieldValues(workBibDocumentList);
            // Sorting the import bib result list and mapping it to corresponding marc record
            SortedMap<BibDocumentSearchResult, BibMarcRecord> marcDocRecMapSorted
                    = new TreeMap<BibDocumentSearchResult, BibMarcRecord>(new ImportBibService());
            for (int i = 0; i < importBibResultsList.size(); i++) {
                marcDocRecMapSorted.put(importBibResultsList.get(i), workBibMarcRecordList.get(i));
            }
            return marcDocRecMapSorted;
        } else {
            SortedMap<BibDocumentSearchResult, BibMarcRecord> marcDocRecMapSortedZero
                    = new TreeMap<BibDocumentSearchResult, BibMarcRecord>(new ImportBibService());
            return marcDocRecMapSortedZero;
        }
    }

    private List<Bib> getBibDocumens(BibMarcRecords bibMarcRecords) {
        List<Bib> workBibDocumentList = new ArrayList<>();
        for (BibMarcRecord bibMarcRecord : bibMarcRecords.getRecords()) {
            Bib bibMarc = new BibMarc();
            bibMarcMapper.extractFields(bibMarcRecord, bibMarc);
            workBibDocumentList.add(bibMarc);
        }
        return workBibDocumentList;
    }

}
