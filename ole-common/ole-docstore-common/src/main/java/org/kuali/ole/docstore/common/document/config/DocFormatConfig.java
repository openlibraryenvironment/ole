package org.kuali.ole.docstore.common.document.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chandrasekharag
 * Date: 4/3/14
 * Time: 6:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocFormatConfig extends ConfigDocument {

    private Integer docTypeId;
    private DocTypeConfig docType;

      private List<DocFieldConfig> docFieldConfigList = new ArrayList<>();

    public DocTypeConfig getDocType() {
        return docType;
    }

    public void setDocType(DocTypeConfig docType) {
        this.docType = docType;
    }

    public Integer getDocTypeId() {
        return docTypeId;
    }

    public void setDocTypeId(Integer docTypeId) {
        this.docTypeId = docTypeId;
    }

    public List<DocFieldConfig> getDocFieldConfigList() {
        return docFieldConfigList;
    }

    public void setDocFieldConfigList(List<DocFieldConfig> docFieldConfigList) {
        this.docFieldConfigList = docFieldConfigList;
    }
}
