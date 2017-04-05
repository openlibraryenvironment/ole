package org.kuali.ole.common;

import org.kuali.ole.common.enums.DocFormat;
import org.kuali.ole.common.enums.DocType;
import org.kuali.ole.model.jpa.*;
import org.kuali.ole.repo.*;
import org.kuali.ole.util.HelperUtil;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: chandrasekharag
 * Date: 4/3/14
 * Time: 6:17 PM
 * To change this template use File | Setti    public static Map<String, String> FIELDS_TO_TAGS_2_INCLUDE_MAP = new HashMap<String, String>();    public static Map<String, String> FIELDS_TO_TAGS_2_EXCLUDE_MAP = new HashMap<String, String>();
 * ngs | File Templates.
 */
public class DocumentSearchConfig extends HelperUtil{

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
        List<DocTypeConfig> docTypeConfigList = getRepository(DocTypeConfigRepository.class).findAll();

        for (DocTypeConfig docTypeConfig : docTypeConfigList) {
            Map typeMap = new HashMap();
            typeMap.put("docTypeId", docTypeConfig.getId());
            List<DocFormatConfig> docFormatConfigList = getRepository(DocFormatConfigRepository.class).findAll();
            for (DocFormatConfig docFormatConfig : docFormatConfigList) {
                Map formatMap = new HashMap();
                formatMap.put("docTypeId", docTypeConfig.getId());
                formatMap.put("docFormatId", docFormatConfig.getId());
                List<DocFieldConfig> docFieldConfigList = getRepository(DocFieldConfigRepository.class).findAll();
                if (docFieldConfigList.size() > 0) {
                    DocFormatConfig docFormat = docFieldConfigList.get(0).getDocFormatConfig();
                    DocTypeConfig docType = docFieldConfigList.get(0).getDocTypeConfig();
                    for (DocFieldConfig docFieldConfig : docFieldConfigList) {
                        if (docFieldConfig.getDocFormatConfig() != null)
                            continue;
                        docFieldConfig.setDocFormatConfig(docFormat);
                        docFieldConfig.setDocTypeConfig(docType);
                    }
                }
                docFormatConfig.setDocFieldConfigs(docFieldConfigList);
            }
            docTypeConfig.setDocFormatConfigs(docFormatConfigList);
        }

        documentSearchConfig.setDocTypeConfigs(docTypeConfigList);

        List<SearchResultPage> searchResultPageList = getRepository(SearchResultPageRepository.class).findAll();
        List<SearchFacetPage> searchFacetPageList = getRepository(SearchFacetPageRepository.class).findAll();
        List<Integer> pageSizeList = new ArrayList<>();
        for (SearchResultPage searchResultPage : searchResultPageList) {
            Integer pageSize = searchResultPage.getPageSize();
            pageSizeList.add(pageSize);
        }
        Collections.sort(pageSizeList);
        documentSearchConfig.setPageSizes(pageSizeList);
        if (searchFacetPageList.size() > 0) {
            documentSearchConfig.setFacetPageSizeLong(searchFacetPageList.get(0).getLongSize());
            documentSearchConfig.setFacetPageSizeShort(searchFacetPageList.get(0).getShortSize());
        }
        buildIncludeAndExcludeMapping();

        return documentSearchConfig;
    }

    public static void buildIncludeAndExcludeMapping() {
        for (DocTypeConfig docTypeConfig : documentSearchConfig.getDocTypeConfigs()) {
            for (DocFormatConfig docFormatConfig : docTypeConfig.getDocFormatConfigs()) {
                if (DocType.BIB.isEqualTo(docTypeConfig.getName()) && DocFormat.MARC.isEqualTo(docFormatConfig.getName())) {
                    for (DocFieldConfig docFieldConfig : docFormatConfig.getDocFieldConfigs()) {
                        FIELDS_TO_TAGS_2_INCLUDE_MAP.put(docFieldConfig.getName(), docFieldConfig.getIncludePath());
                        FIELDS_TO_TAGS_2_EXCLUDE_MAP.put(docFieldConfig.getName(), docFieldConfig.getExcludePath());
                    }
                }
            }
        }
    }

    public static void buildIncludeAndExcludeMapping(List<String> fieldsNames) {
        for (DocTypeConfig docTypeConfig : documentSearchConfig.getDocTypeConfigs()) {
            for (DocFormatConfig docFormatConfig : docTypeConfig.getDocFormatConfigs()) {
                if (DocType.BIB.isEqualTo(docTypeConfig.getName()) && DocFormat.MARC.isEqualTo(docFormatConfig.getName())) {
                    for (DocFieldConfig docFieldConfig : docFormatConfig.getDocFieldConfigs()) {
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
