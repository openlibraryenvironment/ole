package org.kuali.ole.docstore.document.rdbms;

import org.kuali.ole.docstore.document.DocumentManager;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates the suitable implementation of DocumentManager for the given document category, type and format.
 *
 * @author tirumalesh.b
 * @version %I%, %G%
 *          Date: 28/8/12 Time: 12:52 PM
 */
public class RdbmsDocumentManagerFactory {
    private static RdbmsDocumentManagerFactory documentManagerFactory = new RdbmsDocumentManagerFactory();
    private Map<String, DocumentManager> documentManagerMap = new HashMap<String, DocumentManager>();

    public static RdbmsDocumentManagerFactory getInstance() {
        return documentManagerFactory;
    }

    private RdbmsDocumentManagerFactory() {
        initDocumentManagerMap();
    }

    private void initDocumentManagerMap() {
        String key = DocCategory.WORK.getCode() + DocType.BIB.getCode() + DocFormat.MARC.getCode();
        documentManagerMap.put(key, RdbmsWorkBibMarcDocumentManager.getInstance());

        key = DocCategory.WORK.getCode() + DocType.BIB.getCode() + DocFormat.DUBLIN_CORE.getCode();
        documentManagerMap.put(key, RdbmsWorkBibDocumentManager.getInstance());

        key = DocCategory.WORK.getCode() + DocType.BIB.getCode() + DocFormat.DUBLIN_UNQUALIFIED.getCode();
        documentManagerMap.put(key, RdbmsWorkBibDocumentManager.getInstance());

        key = DocCategory.WORK.getCode() + DocType.INSTANCE.getCode() + DocFormat.OLEML.getCode();
        documentManagerMap.put(key, RdbmsWorkInstanceDocumentManager.getInstance());

        key = DocCategory.WORK.getCode() + DocType.ITEM.getCode() + DocFormat.OLEML.getCode();
        documentManagerMap.put(key, RdbmsWorkItemDocumentManager.getInstance());

        key = DocCategory.WORK.getCode() + DocType.HOLDINGS.getCode() + DocFormat.OLEML.getCode();
        documentManagerMap.put(key, RdbmsWorkHoldingsDocumentManager.getInstance());

        key = DocCategory.WORK.getCode() + DocType.EINSTANCE.getCode() + DocFormat.OLEML.getCode();
        documentManagerMap.put(key, RdbmsWorkEInstanceDocumentManager.getInstance());

        key = DocCategory.WORK.getCode() + DocType.EHOLDINGS.getCode() + DocFormat.OLEML.getCode();
        documentManagerMap.put(key, RdbmsWorkEHoldingsDocumentManager.getInstance());

    }

    public DocumentManager getDocumentManager(String docCategory, String docType, String docFormat) {
        return documentManagerMap.get(docCategory + docType + docFormat);
    }

    public DocumentManager getDocumentManager(RequestDocument requestDocument) {
        return documentManagerMap
                .get(requestDocument.getCategory() + requestDocument.getType() + requestDocument.getFormat());
    }
}
