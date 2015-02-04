package org.kuali.ole.docstore.engine.factory;

import org.kuali.ole.docstore.engine.service.storage.rdbms.*;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/17/13
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentManagerFactory {
    private static DocumentManagerFactory documentManagerFactory = new DocumentManagerFactory();
    private Map<String, DocumentManager> documentManagerMap = new HashMap<String, DocumentManager>();

    public static DocumentManagerFactory getInstance() {
        return documentManagerFactory;
    }

    private DocumentManagerFactory() {
        initDocumentManagerMap();
    }

    private void initDocumentManagerMap() {
        String key = DocCategory.WORK.getCode() + DocType.BIB.getCode() + DocFormat.MARC.getCode();
        documentManagerMap.put(key, RdbmsBibMarcDocumentManager.getInstance());

        key = DocCategory.WORK.getCode() + DocType.BIB.getCode() + DocFormat.DUBLIN_CORE.getCode();
        documentManagerMap.put(key, RdbmsBibDocumentManager.getInstance());

        key = DocCategory.WORK.getCode() + DocType.BIB.getCode() + DocFormat.DUBLIN_UNQUALIFIED.getCode();
        documentManagerMap.put(key, RdbmsBibDocumentManager.getInstance());

//        key = DocCategory.WORK.getCode() + DocType.INSTANCE.getCode() + DocFormat.OLEML.getCode();
//        documentManagerMap.put(key, RdbmsWorkInstanceDocumentManager.getInstance());
//
        key = DocCategory.WORK.getCode() + DocType.ITEM.getCode() + DocFormat.OLEML.getCode();
        documentManagerMap.put(key, RdbmsItemDocumentManager.getInstance());
//
        key = DocCategory.WORK.getCode() + DocType.HOLDINGS.getCode() + DocFormat.OLEML.getCode();
        documentManagerMap.put(key, RdbmsHoldingsDocumentManager.getInstance());
//
        key = DocCategory.WORK.getCode() + DocType.LICENSE.getCode() + DocFormat.ONIXPL.getCode();
        documentManagerMap.put(key, RdbmsLicenseOnixplDocumentManager.getInstance());
        key = DocCategory.WORK.getCode() + DocType.LICENSE.getCode() + DocFormat.PDF.getCode();
        documentManagerMap.put(key, RdbmsLicenseAttachmentDocumentManager.getInstance());
        key = DocCategory.WORK.getCode() + DocType.LICENSE.getCode() + DocFormat.XSLT.getCode();
        documentManagerMap.put(key, RdbmsLicenseAttachmentDocumentManager.getInstance());
        key = DocCategory.WORK.getCode() + DocType.LICENSE.getCode() + DocFormat.DOC.getCode();
        documentManagerMap.put(key, RdbmsLicenseAttachmentDocumentManager.getInstance());

//        key = DocCategory.WORK.getCode() + DocType.EINSTANCE.getCode() + DocFormat.OLEML.getCode();
//        documentManagerMap.put(key, RdbmsWorkEInstanceDocumentManager.getInstance());
//
//        key = DocCategory.WORK.getCode() + DocType.EHOLDINGS.getCode() + DocFormat.OLEML.getCode();
//        documentManagerMap.put(key, RdbmsWorkEHoldingsDocumentManager.getInstance());

    }

    public DocumentManager getDocumentManager(String docCategory, String docType, String docFormat) {
        return documentManagerMap.get(docCategory + docType + docFormat);
    }

}
