package org.kuali.ole.docstore.common.document.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chandrasekharag
 * Date: 4/3/14
 * Time: 6:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocTypeConfig extends ConfigDocument{

    private List<DocFormatConfig> docFormatConfigList = new ArrayList<>();

    public List<DocFormatConfig> getDocFormatConfigList() {
        return docFormatConfigList;
    }

    public void setDocFormatConfigList(List<DocFormatConfig> docFormatConfigList) {
        this.docFormatConfigList = docFormatConfigList;
    }
}
