package org.kuali.ole.describe.controller;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.form.DocumentConfigForm;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.config.*;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: chandrasekharag
 * Date: 4/3/14
 * Time: 6:40 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/documentconfigcontroller")
public class DocumentConfigController extends UifControllerBase {
    private static final Logger LOG = LoggerFactory
            .getLogger(DocumentConfigController.class);

    private DocstoreClientLocator docstoreClientLocator = null ;
    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return  SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    private BusinessObjectService businessObjectService;

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }
    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        return new DocumentConfigForm();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {

        DocumentConfigForm documentConfigForm = (DocumentConfigForm) form;
        if (documentConfigForm.getDocTypeConfigList().size() == 0) {
            List<DocTypeConfig> docTypeConfigList = (List<DocTypeConfig>) getBusinessObjectService().findAll(DocTypeConfig.class);
            documentConfigForm.setDocTypeConfigList(docTypeConfigList);
        }

        if (documentConfigForm.getDocFormatConfigList().size() == 0) {
            //  List<DocFormatConfig> docFormatConfigList = (List<DocFormatConfig>) getBusinessObjectService().findAll(DocFormatConfig.class);
            // documentConfigForm.setDocFormatConfigList(docFormatConfigList);
        }

        if (documentConfigForm.getDocFieldConfigList().size() == 0) {

            // List<DocFieldConfig> docFieldConfigList = (List<DocFieldConfig>) getBusinessObjectService().findAll(DocFieldConfig.class);
            //  documentConfigForm.setDocFieldConfigList(docFieldConfigList);

        }

        if (documentConfigForm.getSearchResultPageList().size() == 0) {
            List<SearchResultPage> searchResultPageList = (List<SearchResultPage>) getBusinessObjectService().findAll(SearchResultPage.class);

            documentConfigForm.setSearchResultPageList(searchResultPageList);

        }

        if (documentConfigForm.getSearchFacetPage() == null) {
            List<SearchFacetPage> searchFacetPageList = (List<SearchFacetPage>) getBusinessObjectService().findAll(SearchFacetPage.class);
            if (searchFacetPageList.size() > 0) {
                documentConfigForm.setSearchFacetPage(searchFacetPageList.get(0));
            }
        }
        return super.navigate(documentConfigForm, result, request, response);
    }


    @Override
    @RequestMapping(params = "methodToCall=refresh")
    public ModelAndView refresh(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {

        DocumentConfigForm documentConfigForm = (DocumentConfigForm) form;
        String docType = documentConfigForm.getDocFormatDocType();
        HashMap hashMap = new HashMap();
        hashMap.put("name", docType);
        DocTypeConfig docTypeConfig = (DocTypeConfig) getBusinessObjectService().findByPrimaryKey(DocTypeConfig.class, hashMap);
        if (docTypeConfig != null) {
            Integer docTypeId = docTypeConfig.getId();
            HashMap hashMapDocFormat = new HashMap();
            hashMapDocFormat.put("docTypeId", docTypeId);
            List<DocFormatConfig> docFormatConfigList = (List<DocFormatConfig>) getBusinessObjectService().findMatching(DocFormatConfig.class, hashMapDocFormat);
            documentConfigForm.setDocFormatConfigList(docFormatConfigList);
        }
        return super.navigate(documentConfigForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=saveDocType")
    public ModelAndView saveDocType(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {

        DocumentConfigForm documentConfigForm = (DocumentConfigForm) form;
        List<DocTypeConfig> docTypeConfigList = documentConfigForm.getDocTypeConfigList();
        List<Integer> insertList = new ArrayList<>();
        List<Integer> deleteList = new ArrayList<>();
        List<DocTypeConfig> docTypeConfigListDB = (List<DocTypeConfig>) getBusinessObjectService().findAll(DocTypeConfig.class);
        for (DocTypeConfig docTypeConfig : docTypeConfigList) {
            Integer docTypeId = docTypeConfig.getId(); //1234
            insertList.add(docTypeId);
        }
        for (DocTypeConfig docTypeConfig1 : docTypeConfigListDB) {
            Integer id = docTypeConfig1.getId();//123456  56
            deleteList.add(id);
        }
        for (int i = 0; i < deleteList.size(); i++) {
            if (!insertList.contains(deleteList.get(i))) {
                Map map = new HashMap();
                Map mapFormat = new HashMap();
                map.put("id", deleteList.get(i));
                mapFormat.put("docTypeId", deleteList.get(i));
                getBusinessObjectService().deleteMatching(DocFieldConfig.class, mapFormat);
                getBusinessObjectService().deleteMatching(DocFormatConfig.class, mapFormat);
                getBusinessObjectService().deleteMatching(DocTypeConfig.class, map);
            }
        }
        getBusinessObjectService().save(docTypeConfigList);
        reload(documentConfigForm, result, request, response);
        GlobalVariables.getMessageMap().putInfoForSectionId("DocTypeConfig", OLEConstants.DOC_TYPES_SAVED);
       // GlobalVariables.getMessageMap().putErrorForSectionId("DocumentConfigMessageSection", OLEConstants.DOC_TYPES_SAVED);
        return super.navigate(documentConfigForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=saveDocFormat")
    public ModelAndView saveDocFormat(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {

        DocumentConfigForm documentConfigForm = (DocumentConfigForm) form;
        List<DocFormatConfig> docFormatConfigList = documentConfigForm.getDocFormatConfigList();
        List<Integer> insertList = new ArrayList<>();
        List<Integer> deleteList = new ArrayList<>();
        HashMap hashMap = new HashMap();
        String dataFormatDocType = documentConfigForm.getDocFormatDocType();
        if (!"".equals(dataFormatDocType)) {
            hashMap.put("name", dataFormatDocType);
            DocTypeConfig docTypeConfig = (DocTypeConfig) getBusinessObjectService().findByPrimaryKey(DocTypeConfig.class, hashMap);
            Integer docTypeId= docTypeConfig.getId();
            for(int i=0;i<docFormatConfigList.size();i++){
                docFormatConfigList.get(i).setDocTypeId(docTypeId);
            }
            Map map1 = new HashMap();
            map1.put("docTypeId", docTypeConfig.getId());

            List<DocFormatConfig> docFormatConfigListDB = (List<DocFormatConfig>) getBusinessObjectService().findMatching(DocFormatConfig.class, map1);
            for (DocFormatConfig docFormatConfig : docFormatConfigList) {
                Integer formatDocTypeId = docFormatConfig.getId(); //1234
                insertList.add(formatDocTypeId);
            }
            for (DocFormatConfig docFormatConfig : docFormatConfigListDB) {
                Integer id = docFormatConfig.getId();//123456  56
                deleteList.add(id);
            }
            for (int i = 0; i < deleteList.size(); i++) {
                if (!insertList.contains(deleteList.get(i))) {
                    Map mapDocField = new HashMap();
                    Map mapFormat = new HashMap();
                    mapDocField.put("docFormatId", deleteList.get(i));
                    // mapFormat.put("docTypeId", deleteList.get(i));
                    mapFormat.put("id", deleteList.get(i));
                    mapDocField.put("docTypeId", docTypeConfig.getId());
                    getBusinessObjectService().deleteMatching(DocFieldConfig.class, mapDocField);
                    getBusinessObjectService().deleteMatching(DocFormatConfig.class, mapFormat);
                }
            }
            getBusinessObjectService().save(docFormatConfigList);
            GlobalVariables.getMessageMap().putInfo("DocFormatConfig", OLEConstants.DOC_FORMAT_SAVED);
            //GlobalVariables.getMessageMap().putErrorForSectionId("DocumentConfigMessageSection", OLEConstants.DOC_FORMAT_SAVED);
            reload(documentConfigForm, result, request, response);
        }
        return super.navigate(documentConfigForm, result, request, response);
    }


    @RequestMapping(params = "methodToCall=saveDocField")
    public ModelAndView saveDocField(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {

        DocumentConfigForm documentConfigForm = (DocumentConfigForm) form;
        List<DocFieldConfig> docFieldConfigListDB = new ArrayList<>();
        List<DocFieldConfig> docFieldConfigList = documentConfigForm.getDocFieldConfigList();
        List<Integer> insertList = new ArrayList<>();
        List<Integer> deleteList = new ArrayList<>();
        String dataFormatDocType = documentConfigForm.getDocFieldDocType();
        String docFormat = documentConfigForm.getDocFormat();
        if (!"".equals(dataFormatDocType) && !"".equals(docFormat)) {
            Map docTypeMap = new HashMap();
            docTypeMap.put("name", dataFormatDocType);
            List<DocTypeConfig> docTypeConfigList = (List<DocTypeConfig>) getBusinessObjectService().findMatching(DocTypeConfig.class, docTypeMap);
            if (docTypeConfigList.size() > 0) {
                int docTypeId = docTypeConfigList.get(0).getId();
                Map docFormatMap = new HashMap();
                docFormatMap.put("name", docFormat);
                docFormatMap.put("docTypeId", docTypeId);
                List<DocFormatConfig> docFormatConfigList = (List<DocFormatConfig>) getBusinessObjectService().findMatching(DocFormatConfig.class, docFormatMap);
                if (docFormatConfigList.size() > 0) {
                    int docFormatId = docFormatConfigList.get(0).getId();
                    Map docFieldMap = new HashMap();
                    docFieldMap.put("docTypeId", docTypeId);
                    docFieldMap.put("docFormatId", docFormatId);

                    for (int i = 0; i < docFieldConfigList.size(); i++) {
                        docFieldConfigList.get(i).setDocTypeId(docTypeId);
                        docFieldConfigList.get(i).setDocFormatId(docFormatId);
                    }

                    docFieldConfigListDB = (List<DocFieldConfig>) getBusinessObjectService().findMatching(DocFieldConfig.class, docFieldMap);

                    for (DocFieldConfig docFieldConfig : docFieldConfigList) {
                        insertList.add(docFieldConfig.getId());
                    }
                    for (DocFieldConfig docFieldConfig : docFieldConfigListDB) {
                        Integer id = docFieldConfig.getId();
                        deleteList.add(id);
                    }
                    for (int i = 0; i < deleteList.size(); i++) {
                        if (!insertList.contains(deleteList.get(i))) {
                            Map mapDocField = new HashMap();
                            mapDocField.put("id", deleteList.get(i));
                            getBusinessObjectService().deleteMatching(DocFieldConfig.class, mapDocField);
                        }
                    }
                }
            }
        }
        getBusinessObjectService().save(docFieldConfigList);
        reload(documentConfigForm, result, request, response);
        GlobalVariables.getMessageMap().putInfo("DocField-searchParams-Section", OLEConstants.DOC_FIELDS_SAVED);
       // GlobalVariables.getMessageMap().putErrorForSectionId("DocumentConfigMessageSection", OLEConstants.DOC_FIELDS_SAVED);
        return super.navigate(documentConfigForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=refreshforDocField")
    public ModelAndView refreshforDocField(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) {

        DocumentConfigForm documentConfigForm = (DocumentConfigForm) form;
        Map docTypeMap = new HashMap();
        docTypeMap.put("name", documentConfigForm.getDocFieldDocType());
        List<DocTypeConfig> docTypeConfigList = (List<DocTypeConfig>) getBusinessObjectService().findMatching(DocTypeConfig.class, docTypeMap);
        if (docTypeConfigList.size() > 0) {
            int docTypeId = docTypeConfigList.get(0).getId();
            String docFormat = documentConfigForm.getDocFormat();
            Map docFormatMap = new HashMap();
            docFormatMap.put("name", docFormat);
            docFormatMap.put("docTypeId",docTypeId);
            List<DocFormatConfig> docFormatConfigList = (List<DocFormatConfig>) getBusinessObjectService().findMatching(DocFormatConfig.class, docFormatMap);
            if (docFormatConfigList.size() > 0) {
                int docFormatId = docFormatConfigList.get(0).getId();
                Map docFieldMap = new HashMap();
                docFieldMap.put("docTypeId",docTypeId);
                docFieldMap.put("docFormatId", docFormatId);
                List<DocFieldConfig> docFieldConfigList = (List<DocFieldConfig>) getBusinessObjectService().findMatching(DocFieldConfig.class, docFieldMap);
                if (docFieldConfigList.size() > 0) {
                    documentConfigForm.setDocFieldConfigList(docFieldConfigList);
                }
            }else{
                documentConfigForm.setDocFieldConfigList(null);
            }
        }
        return super.navigate(documentConfigForm, result, request, response);
    }


    @RequestMapping(params = "methodToCall=refreshforDocFormat")
    public ModelAndView refreshforDocFormat(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) {

        DocumentConfigForm documentConfigForm = (DocumentConfigForm) form;
        documentConfigForm.setDocFormatConfigList(null);
        documentConfigForm.setDocFieldConfigList(null);
        return super.navigate(documentConfigForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=saveSearchResultPage")
    public ModelAndView saveSearchResultPage(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                             HttpServletRequest request, HttpServletResponse response) {

        DocumentConfigForm documentConfigForm = (DocumentConfigForm) form;
        List<SearchResultPage> searchResultPageListDB = new ArrayList<>();
        List<SearchResultPage> searchResultPageList = documentConfigForm.getSearchResultPageList();
        List<Integer> insertList = new ArrayList<>();
        List<Integer> deleteList = new ArrayList<>();
        List<Integer> duplicateList = new ArrayList<>();
        List<SearchResultPage> searchResultPages =new ArrayList<>();
        searchResultPageListDB = (List<SearchResultPage>) getBusinessObjectService().findAll(SearchResultPage.class);
        for (SearchResultPage searchResultPage : searchResultPageList) {
            Integer docTypeId = searchResultPage.getId(); //1234
            duplicateList.add(searchResultPage.getSize());
            insertList.add(docTypeId);
        }
        for (SearchResultPage searchResultPage : searchResultPageListDB) {
            Integer id = searchResultPage.getId();//123456  56
            deleteList.add(id);
        }
        Set<Integer> duplicateSize = findDuplicates(duplicateList);
        if (duplicateSize.size() == 0) {
            for (int i = 0; i < deleteList.size(); i++) {
                if (insertList.contains(deleteList.get(i))) {
                    Map mapDocField = new HashMap();
                    mapDocField.put("id", deleteList.get(i));
                    getBusinessObjectService().deleteMatching(SearchResultPage.class, mapDocField);
                }
            }
            for(SearchResultPage searchResultPage:searchResultPageList){
                if(searchResultPage.getSize()!=null){
                searchResultPages.add((searchResultPage));
                }
            }
            getBusinessObjectService().save(searchResultPages);
            documentConfigForm.setSearchResultPageList(searchResultPages);
            reload(documentConfigForm, result, request, response);
            GlobalVariables.getMessageMap().putInfo("OLESearchResultPageConfigSearch-search-parentBean", OLEConstants.DOC_SEARCH_RESULT_SAVED);
        } else {
            GlobalVariables.getMessageMap().putInfo("OLESearchResultPageConfigSearch-search-parentBean", OLEConstants.DOC_SEARCH_RESULT_DUPLICATE);
        }
        // GlobalVariables.getMessageMap().putErrorForSectionId("DocumentConfigMessageSection", OLEConstants.DOC_SEARCH_RESULT_SAVED);
        return super.navigate(documentConfigForm, result, request, response);
    }
    public Set<Integer> findDuplicates(List<Integer> duplicateList) {

        final Set<Integer> duplicateSet = new HashSet<Integer>();
        final Set<Integer> setIdList = new HashSet<Integer>();

        for (Integer duplicateInt : duplicateList) {
            if (!setIdList.add(duplicateInt) && duplicateInt!=null) {
                duplicateSet.add(duplicateInt);
            }
        }
        return duplicateSet;
    }


    @RequestMapping(params = "methodToCall=saveFacetPage")
    public ModelAndView saveFacetPage(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {

        DocumentConfigForm documentConfigForm = (DocumentConfigForm) form;
        SearchFacetPage searchFacetPage = documentConfigForm.getSearchFacetPage();
        getBusinessObjectService().save(searchFacetPage);
        reload(documentConfigForm, result, request, response);
        GlobalVariables.getMessageMap().putInfo("OLEFacetPageConfigSearch-search-parentBean", OLEConstants.DOC_FACET_PAGE_SAVED);
       // GlobalVariables.getMessageMap().putErrorForSectionId("DocumentConfigMessageSection", OLEConstants.DOC_FACET_PAGE_SAVED);
        return super.navigate(documentConfigForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=reload")
    public ModelAndView reload(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {

        DocumentSearchConfig.reloadDocumentConfig();

        try {
            getDocstoreClientLocator().getDocstoreClient().reloadConfiguration();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return super.navigate(form, result, request, response);
    }

    @RequestMapping(params = "methodToCall=resetDocTypes")
    public ModelAndView resetDocTypes(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        DocumentConfigForm documentConfigForm = (DocumentConfigForm) form;
            List<DocTypeConfig> docTypeConfigList = (List<DocTypeConfig>) getBusinessObjectService().findAll(DocTypeConfig.class);
            documentConfigForm.setDocTypeConfigList(docTypeConfigList);
        return super.navigate(form, result, request, response);
    }


    @RequestMapping(params = "methodToCall=resetDocFormats")
    public ModelAndView resetDocFormats(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {
        DocumentConfigForm documentConfigForm = (DocumentConfigForm) form;
        Map docTypeMap = new HashMap();
        docTypeMap.put("name",documentConfigForm.getDocFormatDocType());
        List<DocTypeConfig> docTypeConfigList = (List<DocTypeConfig>) getBusinessObjectService().findMatching(DocTypeConfig.class,docTypeMap);
        Integer docTypeId= docTypeConfigList.get(0).getId();
        HashMap hashMapDocFormat = new HashMap();
        hashMapDocFormat.put("docTypeId", docTypeId);
        List<DocFormatConfig> docFormatConfigList = (List<DocFormatConfig>) getBusinessObjectService().findMatching(DocFormatConfig.class, hashMapDocFormat);
        documentConfigForm.setDocFormatConfigList(docFormatConfigList);
        return super.navigate(form, result, request, response);
    }


    @RequestMapping(params = "methodToCall=resetDocFields")
    public ModelAndView resetDocFields(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) {
        DocumentConfigForm documentConfigForm = (DocumentConfigForm) form;
        Map docTypeMap = new HashMap();
        docTypeMap.put("name",documentConfigForm.getDocFieldDocType());
        List<DocTypeConfig> docTypeConfigList = (List<DocTypeConfig>) getBusinessObjectService().findMatching(DocTypeConfig.class,docTypeMap);
        Integer docTypeId= docTypeConfigList.get(0).getId();
        String docFormat= documentConfigForm.getDocFormat();
        HashMap hashMapDocFormat = new HashMap();
        hashMapDocFormat.put("docTypeId", docTypeId);
        hashMapDocFormat.put("name",docFormat);
        List<DocFormatConfig> docFormatConfigList = (List<DocFormatConfig>) getBusinessObjectService().findMatching(DocFormatConfig.class, hashMapDocFormat);
        if (docFormatConfigList.size() > 0) {
            int docFormatId = docFormatConfigList.get(0).getId();
            Map docFieldMap = new HashMap();
            docFieldMap.put("docTypeId",docTypeId);
            docFieldMap.put("docFormatId", docFormatId);
            List<DocFieldConfig> docFieldConfigList = (List<DocFieldConfig>) getBusinessObjectService().findMatching(DocFieldConfig.class, docFieldMap);
            if (docFieldConfigList.size() > 0) {
                documentConfigForm.setDocFieldConfigList(docFieldConfigList);
            } else {
                documentConfigForm.setDocFieldConfigList(null);
            }
        }else{
            documentConfigForm.setDocFieldConfigList(null);
        }
        documentConfigForm.setDocFormatConfigList(docFormatConfigList);
        return super.navigate(form, result, request, response);
    }

    @RequestMapping(params = "methodToCall=resetSearchResultPage")
    public ModelAndView resetSearchResultPage(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) {
        DocumentConfigForm documentConfigForm = (DocumentConfigForm) form;
        List<SearchResultPage> searchResultPageList = (List<SearchResultPage>) getBusinessObjectService().findAll(SearchResultPage.class);
        documentConfigForm.setSearchResultPageList(searchResultPageList);
        return super.navigate(form, result, request, response);
    }


    @RequestMapping(params = "methodToCall=resetFacetPage")
    public ModelAndView resetFacetPage(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {
        DocumentConfigForm documentConfigForm = (DocumentConfigForm) form;
        List<SearchFacetPage> searchFacetPageList = (List<SearchFacetPage>) getBusinessObjectService().findAll(SearchFacetPage.class);
        if (searchFacetPageList.size() > 0) {
            documentConfigForm.setSearchFacetPage(searchFacetPageList.get(0));
        }
        return super.navigate(form, result, request, response);
    }

}
