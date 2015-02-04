package org.kuali.ole.docstore.common.document.config;

import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: chandrasekharag
 * Date: 4/3/14
 * Time: 6:17 PM
 * To change this template use File | Setti    public static Map<String, String> FIELDS_TO_TAGS_2_INCLUDE_MAP = new HashMap<String, String>();    public static Map<String, String> FIELDS_TO_TAGS_2_EXCLUDE_MAP = new HashMap<String, String>();
 * ngs | File Templates.
 */
public class DocumentSearchConfig {

    private List<DocTypeConfig> docTypeConfigs = new ArrayList<>();   // bib ,holdings ….
    private static List<Integer> pageSizes = new ArrayList<>();
    private int facetPageSizeShort = 0;
    private int facetPageSizeLong = 0;
    private static DocumentSearchConfig documentSearchConfig;
    public static Map<String, String> FIELDS_TO_TAGS_2_INCLUDE_MAP = new HashMap<String, String>();
    public static Map<String, String> FIELDS_TO_TAGS_2_INCLUDE_MAP_SELECTED = new HashMap<String, String>();
    public static Map<String, String> FIELDS_TO_TAGS_2_EXCLUDE_MAP = new HashMap<String, String>();
    public static Map<String, String> FIELDS_TO_TAGS_2_EXCLUDE_MAP_SELECTED = new HashMap<String, String>();

    private DocumentSearchConfig() {


    }

    public static List<Integer> getPageSizes() {
        return pageSizes;
    }

    public void setPageSizes(List<Integer> pageSizes) {
        DocumentSearchConfig.pageSizes = pageSizes;
    }

    public static DocumentSearchConfig getDocumentSearchConfig() {
        if (documentSearchConfig == null) {
            documentSearchConfig = new DocumentSearchConfig();
            reloadDocumentConfig();
        }

        return documentSearchConfig;
    }

    public static DocumentSearchConfig reloadDocumentConfig() {
        List<DocTypeConfig> docTypeConfigList = (List<DocTypeConfig>) KRADServiceLocator.getBusinessObjectService().findAll(DocTypeConfig.class);

        for (DocTypeConfig docTypeConfig : docTypeConfigList) {
            Map typeMap = new HashMap();
            typeMap.put("docTypeId", docTypeConfig.getId());
            List<DocFormatConfig> docFormatConfigList = (List<DocFormatConfig>) KRADServiceLocator.getBusinessObjectService().findMatching(DocFormatConfig.class, typeMap);
            for (DocFormatConfig docFormatConfig : docFormatConfigList) {
                Map formatMap = new HashMap();
                formatMap.put("docTypeId", docTypeConfig.getId());
                formatMap.put("docFormatId", docFormatConfig.getId());
                List<DocFieldConfig> docFieldConfigList = (List<DocFieldConfig>) KRADServiceLocator.getBusinessObjectService().findMatching(DocFieldConfig.class, formatMap);
                if (docFieldConfigList.size() > 0) {
                    DocFormatConfig docFormat = docFieldConfigList.get(0).getDocFormat();
                    DocTypeConfig docType = docFieldConfigList.get(0).getDocType();
                    for (DocFieldConfig docFieldConfig : docFieldConfigList) {
                        if (docFieldConfig.getDocFormat() != null)
                            continue;
                        docFieldConfig.setDocFormat(docFormat);
                        docFieldConfig.setDocType(docType);
                    }
                }
                docFormatConfig.setDocFieldConfigList(docFieldConfigList);
            }
            docTypeConfig.setDocFormatConfigList(docFormatConfigList);
        }

        documentSearchConfig.setDocTypeConfigs(docTypeConfigList);

        List<SearchResultPage> searchResultPageList = (List<SearchResultPage>) KRADServiceLocator.getBusinessObjectService().findAll(SearchResultPage.class);
        List<SearchFacetPage> searchFacetPageList = (List<SearchFacetPage>) KRADServiceLocator.getBusinessObjectService().findAll(SearchFacetPage.class);
        List<Integer> pageSizeList = new ArrayList<>();
        for (SearchResultPage searchResultPage : searchResultPageList) {
            Integer pageSize = searchResultPage.getSize();
            pageSizeList.add(pageSize);
        }
        Collections.sort(pageSizeList);
        documentSearchConfig.setPageSizes(pageSizeList);
        if (searchFacetPageList.size() > 0) {
            documentSearchConfig.setFacetPageSizeLong(searchFacetPageList.get(0).getLongSize());
            documentSearchConfig.setFacetPageSizeShort(searchFacetPageList.get(0).getShotSize());
        }
        buildIncludeAndExcludeMapping();

        return documentSearchConfig;
    }

    public static void buildIncludeAndExcludeMapping() {
        for (DocTypeConfig docTypeConfig : documentSearchConfig.getDocTypeConfigs()) {
            for (DocFormatConfig docFormatConfig : docTypeConfig.getDocFormatConfigList()) {
                if (DocType.BIB.isEqualTo(docTypeConfig.getName()) && DocFormat.MARC.isEqualTo(docFormatConfig.getName())) {
                    for (DocFieldConfig docFieldConfig : docFormatConfig.getDocFieldConfigList()) {
                        FIELDS_TO_TAGS_2_INCLUDE_MAP.put(docFieldConfig.getName(), docFieldConfig.getIncludePath());
                        FIELDS_TO_TAGS_2_EXCLUDE_MAP.put(docFieldConfig.getName(), docFieldConfig.getExcludePath());
                    }
                }
            }
        }
    }

    public static void buildIncludeAndExcludeMapping(List<String> fieldsNames) {
        for (DocTypeConfig docTypeConfig : documentSearchConfig.getDocTypeConfigs()) {
            for (DocFormatConfig docFormatConfig : docTypeConfig.getDocFormatConfigList()) {
                if (DocType.BIB.isEqualTo(docTypeConfig.getName()) && DocFormat.MARC.isEqualTo(docFormatConfig.getName())) {
                    for (DocFieldConfig docFieldConfig : docFormatConfig.getDocFieldConfigList()) {
                        if(fieldsNames.contains(docFieldConfig.getName())) {
                            String includePath = docFieldConfig.getIncludePath();
                            String excludePath = docFieldConfig.getExcludePath();
                            if(includePath == null) {
                                includePath = "";
                            }
                            if (excludePath == null) {
                                excludePath = "";
                            }
                            FIELDS_TO_TAGS_2_INCLUDE_MAP_SELECTED.put(docFieldConfig.getName(), includePath);
                            FIELDS_TO_TAGS_2_EXCLUDE_MAP_SELECTED.put(docFieldConfig.getName(), excludePath);
                        }
                    }
                }
            }
        }
    }

    public List<DocTypeConfig> getDocTypeConfigs() {
        return docTypeConfigs;
    }

    public void setDocTypeConfigs(List<DocTypeConfig> docTypeConfigs) {
        this.docTypeConfigs = docTypeConfigs;
    }

    public int getFacetPageSizeShort() {
        return facetPageSizeShort;
    }

    public void setFacetPageSizeShort(int facetPageSizeShort) {
        this.facetPageSizeShort = facetPageSizeShort;
    }

    public int getFacetPageSizeLong() {
        return facetPageSizeLong;
    }

    public void setFacetPageSizeLong(int facetPageSizeLong) {
        this.facetPageSizeLong = facetPageSizeLong;
    }
}
