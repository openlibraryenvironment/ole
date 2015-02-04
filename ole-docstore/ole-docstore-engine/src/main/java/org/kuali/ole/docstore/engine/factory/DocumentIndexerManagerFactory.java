package org.kuali.ole.docstore.engine.factory;


import org.kuali.ole.docstore.engine.service.index.solr.*;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/17/13
 * Time: 4:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentIndexerManagerFactory {
    private static DocumentIndexerManagerFactory documentIndexerManagerFactory = new DocumentIndexerManagerFactory();
    private Map<String, DocumentIndexer> indexerManagerMap = new HashMap<String, DocumentIndexer>();

    public static DocumentIndexerManagerFactory getInstance() {
        return documentIndexerManagerFactory;
    }

    private DocumentIndexerManagerFactory() {
        initDocumentManagerMap();
    }

    private void initDocumentManagerMap() {
        String key = DocCategory.WORK.getCode() + DocType.BIB.getDescription() + DocFormat.MARC.getCode();
        indexerManagerMap.put(key, BibMarcIndexer.getInstance());

        key = DocCategory.WORK.getCode() + DocType.BIB.getDescription() + DocFormat.DUBLIN_CORE.getCode();
        indexerManagerMap.put(key, BibDCIndexer.getInstance());

        key = DocCategory.WORK.getCode() + DocType.BIB.getDescription() + DocFormat.DUBLIN_UNQUALIFIED.getCode();
        indexerManagerMap.put(key, BibDcUnqualifiedIndexer.getInstance());

        key = DocCategory.WORK.getCode() + DocType.HOLDINGS.getCode() + DocFormat.OLEML.getCode();
        indexerManagerMap.put(key, HoldingsOlemlIndexer.getInstance());

        key = DocCategory.WORK.getCode() + DocType.EHOLDINGS.getCode() + DocFormat.OLEML.getCode();
        indexerManagerMap.put(key, HoldingsOlemlIndexer.getInstance());

        key = DocCategory.WORK.getCode() + DocType.ITEM.getCode() + DocFormat.OLEML.getCode();
        indexerManagerMap.put(key, ItemOlemlIndexer.getInstance());

        key = DocCategory.WORK.getCode() + DocType.LICENSE.getCode() + DocFormat.ONIXPL.getCode();
        indexerManagerMap.put(key, LicenseOnixplIndexer.getInstance());
        key = DocCategory.WORK.getCode() + DocType.LICENSE.getCode() + DocFormat.PDF.getCode();
        indexerManagerMap.put(key, LicenseAttachmentIndexer.getInstance());
        key = DocCategory.WORK.getCode() + DocType.LICENSE.getCode() + DocFormat.DOC.getCode();
        indexerManagerMap.put(key, LicenseAttachmentIndexer.getInstance());
        key = DocCategory.WORK.getCode() + DocType.LICENSE.getCode() + DocFormat.XSLT.getCode();
        indexerManagerMap.put(key, LicenseAttachmentIndexer.getInstance());

    }

    public DocumentIndexer getDocumentIndexManager(String docCategory, String docType, String docFormat) {
        return indexerManagerMap.get(docCategory + docType + docFormat);
    }

}
