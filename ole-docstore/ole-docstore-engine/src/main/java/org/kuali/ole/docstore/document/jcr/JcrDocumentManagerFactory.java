package org.kuali.ole.docstore.document.jcr;

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
public class JcrDocumentManagerFactory {
    private static JcrDocumentManagerFactory jcrDocumentManagerFactory = new JcrDocumentManagerFactory();
    private Map<String, DocumentManager> documentManagerMap = new HashMap<String, DocumentManager>();

    public static JcrDocumentManagerFactory getInstance() {
        return jcrDocumentManagerFactory;
    }

    private JcrDocumentManagerFactory() {
        initDocumentManagerMap();
    }

    private void initDocumentManagerMap() {
        String key = DocCategory.WORK.getCode() + DocType.BIB.getDescription() + DocFormat.MARC.getCode();
        documentManagerMap.put(key, JcrWorkBibMarcDocumentManager.getInstance());

        key = DocCategory.WORK.getCode() + DocType.BIB.getDescription() + DocFormat.DUBLIN_CORE.getCode();
        documentManagerMap.put(key, JcrWorkBibDocumentManager.getInstance());

        key = DocCategory.WORK.getCode() + DocType.BIB.getDescription() + DocFormat.DUBLIN_UNQUALIFIED.getCode();
        documentManagerMap.put(key, JcrWorkBibDocumentManager.getInstance());

        key = DocCategory.WORK.getCode() + DocType.INSTANCE.getCode() + DocFormat.OLEML.getCode();
        documentManagerMap.put(key, JcrWorkInstanceDocumentManager.getInstance());

        key = DocCategory.WORK.getCode() + DocType.HOLDINGS.getCode() + DocFormat.OLEML.getCode();
        documentManagerMap.put(key, JcrJcrWorkHoldingsDocumentManager.getInstance());

        key = DocCategory.WORK.getCode() + DocType.ITEM.getCode() + DocFormat.OLEML.getCode();
        documentManagerMap.put(key, JcrWorkItemDocumentManager.getInstance());

        key = DocCategory.WORK.getCode() + DocType.SOURCEHOLDINGS.getCode() + DocFormat.OLEML.getCode();
        documentManagerMap.put(key, JcrWorkInstanceDocumentManager.getInstance());

        key = DocCategory.WORK.getCode() + DocType.LICENSE.getCode() + DocFormat.ONIXPL.getCode();
        documentManagerMap.put(key, JcrWorkLicenseDocumentManager.getInstance());

        key = DocCategory.WORK.getCode() + DocType.LICENSE.getCode() + DocFormat.PDF.getCode();
        documentManagerMap.put(key, JcrWorkLicenseDocumentManager.getInstance());

        key = DocCategory.WORK.getCode() + DocType.LICENSE.getCode() + DocFormat.DOC.getCode();
        documentManagerMap.put(key, JcrWorkLicenseDocumentManager.getInstance());

        key = DocCategory.WORK.getCode() + DocType.LICENSE.getCode() + DocFormat.XSLT.getCode();
        documentManagerMap.put(key, JcrWorkLicenseDocumentManager.getInstance());

    }

    public DocumentManager getDocumentManager(String docCategory, String docType, String docFormat) {
        return documentManagerMap.get(docCategory + docType + docFormat);
    }

    public DocumentManager getDocumentManager(RequestDocument requestDocument) {
        return documentManagerMap
                .get(requestDocument.getCategory() + requestDocument.getType() + requestDocument.getFormat());
    }
}
