package org.kuali.ole.docstore.common.document.config;

/**
 * Created with IntelliJ IDEA.
 * User: chandrasekharag
 * Date: 4/3/14
 * Time: 6:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocFieldConfig extends ConfigDocument {

    private String includePath;
    private String excludePath;
    private boolean searchable;
    private boolean displayable;
    private boolean globallyEditable;
    private boolean exportable;
    private boolean facet;
    private Integer docTypeId;
    private Integer docFormatId;
    private DocTypeConfig docType;
    private DocFormatConfig docFormat;

    public DocTypeConfig getDocType() {
        return docType;
    }

    public void setDocType(DocTypeConfig docType) {
        this.docType = docType;
    }

    public DocFormatConfig getDocFormat() {
        return docFormat;
    }

    public void setDocFormat(DocFormatConfig docFormat) {
        this.docFormat = docFormat;
    }

    public Integer getDocTypeId() {
        return docTypeId;
    }

    public void setDocTypeId(Integer docTypeId) {
        this.docTypeId = docTypeId;
    }

    public Integer getDocFormatId() {
        return docFormatId;
    }

    public void setDocFormatId(Integer docFormatId) {
        this.docFormatId = docFormatId;
    }

    public String getIncludePath() {
        return includePath;
    }

    public void setIncludePath(String includePath) {
        this.includePath = includePath;
    }

    public String getExcludePath() {
        return excludePath;
    }

    public void setExcludePath(String excludePath) {
        this.excludePath = excludePath;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

    public boolean isDisplayable() {
        return displayable;
    }

    public void setDisplayable(boolean displayable) {
        this.displayable = displayable;
    }

    public boolean isGloballyEditable() {
        return globallyEditable;
    }

    public void setGloballyEditable(boolean globallyEditable) {
        this.globallyEditable = globallyEditable;
    }

    public boolean isExportable() {
        return exportable;
    }

    public void setExportable(boolean exportable) {
        this.exportable = exportable;
    }

    public boolean isFacet() {
        return facet;
    }

    public void setFacet(boolean facet) {
        this.facet = facet;
    }
}
