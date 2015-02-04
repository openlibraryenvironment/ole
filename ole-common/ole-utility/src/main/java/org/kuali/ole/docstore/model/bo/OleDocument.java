package org.kuali.ole.docstore.model.bo;

import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.enums.DocCategory;

/**
 * Base class for logical representation of any document used in OLE.
 * (These documents come in different formats and are stored in DocStore module and indexed in Discovery module.)
 * User: tirumalesh.b
 * Date: 1/2/12 Time: 2:57 PM
 */
public class OleDocument {
    protected String id;
    protected DocCategory docCategory;
    protected DocType docType;

    public OleDocument() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DocCategory getDocCategory() {
        return docCategory;
    }

    public void setDocCategory(DocCategory docCategory) {
        this.docCategory = docCategory;
    }

    public DocType getDocType() {
        return docType;
    }

    public void setDocType(DocType docType) {
        this.docType = docType;
    }
}
