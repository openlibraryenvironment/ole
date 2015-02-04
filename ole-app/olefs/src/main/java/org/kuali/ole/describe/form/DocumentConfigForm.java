package org.kuali.ole.describe.form;

import org.kuali.ole.docstore.common.document.config.*;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chandrasekharag
 * Date: 4/3/14
 * Time: 6:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentConfigForm extends UifFormBase {
    public void setDocTypeConfigList(List<DocTypeConfig> docTypeConfigList) {
        this.docTypeConfigList = docTypeConfigList;
    }

    private List<DocTypeConfig> docTypeConfigList=new ArrayList<DocTypeConfig>();
    private List<DocFormatConfig> docFormatConfigList =new ArrayList<>();
    private List<DocFieldConfig> docFieldConfigList=new ArrayList<>();
    private List<SearchResultPage> searchResultPageList=new ArrayList<>();

    private SearchFacetPage searchFacetPage;
    private String editable;
    private String docFormatDocType;
    private String docFormat;
    private String docFieldDocType;


    public String getDocFormat() {
        return docFormat;
    }

    public void setDocFormat(String docFormat) {
        this.docFormat = docFormat;
    }

    public void setSearchFacetPage(SearchFacetPage searchFacetPage) {
        this.searchFacetPage = searchFacetPage;
    }

    public void setSearchResultPageList(List<SearchResultPage> searchResultPageList) {
        this.searchResultPageList = searchResultPageList;
    }

    public String getDocFormatDocType() {
        return docFormatDocType;
    }

    public void setDocFormatDocType(String docFormatDocType) {
        this.docFormatDocType = docFormatDocType;
    }

    public String getDocFieldDocType() {
        return docFieldDocType;
    }

    public void setDocFieldDocType(String docFieldDocType) {
        this.docFieldDocType = docFieldDocType;
    }

    public String getEditable() {
        return editable;
    }

    public void setDocFormatConfigList(List<DocFormatConfig> docFormatConfigList) {
        this.docFormatConfigList = docFormatConfigList;
    }

    public void setDocFieldConfigList(List<DocFieldConfig> docFieldConfigList) {
        this.docFieldConfigList = docFieldConfigList;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public List<DocTypeConfig> getDocTypeConfigList() {
        if (docTypeConfigList == null) {
            DocTypeConfig docTypeConfig =new DocTypeConfig();
            docTypeConfigList = new ArrayList<DocTypeConfig>();
            docTypeConfigList.add(docTypeConfig);
        }
        return docTypeConfigList;
    }



    public List<DocFormatConfig> getDocFormatConfigList() {
        if (docFormatConfigList == null) {
            docFormatConfigList = new ArrayList<>();
        }
        return docFormatConfigList;
    }


    public List<DocFieldConfig> getDocFieldConfigList() {
        if (docFieldConfigList == null) {
            docFieldConfigList = new ArrayList<>();
        }
        return docFieldConfigList;
    }


    public List<SearchResultPage> getSearchResultPageList() {
        if (searchResultPageList == null) {
            searchResultPageList = new ArrayList<>();
        }
        return searchResultPageList;
    }


    public SearchFacetPage getSearchFacetPage() {
       /* if (searchFacetPage == null) {
            searchFacetPage = new SearchFacetPage();
        }*/
        return searchFacetPage;
    }


}
