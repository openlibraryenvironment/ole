package org.kuali.ole.docstore.indexer.solr;

import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: mjagan
 * Date: 7/2/13
 * Time: 4:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentIndexerManagerFactory {

    private static DocumentIndexerManagerFactory documentIndexerManagerFactory = new DocumentIndexerManagerFactory();
    private Map<String, IndexerService> indexerManagerMap = new HashMap<String, IndexerService>();

    public static DocumentIndexerManagerFactory getInstance() {
        return documentIndexerManagerFactory;
    }

    private DocumentIndexerManagerFactory() {
        initDocumentManagerMap();
    }

    private void initDocumentManagerMap() {
        String key = DocCategory.WORK.getCode() + DocType.BIB.getDescription() + DocFormat.MARC.getCode();
        indexerManagerMap.put(key, WorkBibMarcDocumentIndexer.getInstance());

        key = DocCategory.WORK.getCode() + DocType.BIB.getDescription() + DocFormat.DUBLIN_CORE.getCode();
        indexerManagerMap.put(key, WorkBibDocumentIndexer.getInstance());

        key = DocCategory.WORK.getCode() + DocType.BIB.getDescription() + DocFormat.DUBLIN_UNQUALIFIED.getCode();
        indexerManagerMap.put(key, WorkBibDocumentIndexer.getInstance());

        key = DocCategory.WORK.getCode() + DocType.INSTANCE.getCode() + DocFormat.OLEML.getCode();
        indexerManagerMap.put(key, WorkInstanceDocumentIndexer.getInstance());

        key = DocCategory.WORK.getCode() + DocType.HOLDINGS.getCode() + DocFormat.OLEML.getCode();
        indexerManagerMap.put(key, WorkHoldingsDocumentIndexer.getInstance());

        key = DocCategory.WORK.getCode() + DocType.ITEM.getCode() + DocFormat.OLEML.getCode();
        indexerManagerMap.put(key, WorkItemDocumentIndexer.getInstance());

        key = DocCategory.WORK.getCode() + DocType.SOURCEHOLDINGS.getCode() + DocFormat.OLEML.getCode();
        indexerManagerMap.put(key, WorkInstanceDocumentIndexer.getInstance());

        key = DocCategory.WORK.getCode() + DocType.LICENSE.getCode() + DocFormat.ONIXPL.getCode();
        indexerManagerMap.put(key, WorkLicenseDocumentIndexer.getInstance());

        key = DocCategory.WORK.getCode() + DocType.LICENSE.getCode() + DocFormat.PDF.getCode();
        indexerManagerMap.put(key, WorkLicenseDocumentIndexer.getInstance());

        key = DocCategory.WORK.getCode() + DocType.LICENSE.getCode() + DocFormat.DOC.getCode();
        indexerManagerMap.put(key, WorkLicenseDocumentIndexer.getInstance());

        key = DocCategory.WORK.getCode() + DocType.LICENSE.getCode() + DocFormat.XSLT.getCode();
        indexerManagerMap.put(key, WorkLicenseDocumentIndexer.getInstance());

        key = DocCategory.WORK.getCode() + DocType.EINSTANCE.getCode() + DocFormat.OLEML.getCode();
        indexerManagerMap.put(key, WorkEInstanceDocumentIndexer.getInstance());

        key = DocCategory.WORK.getCode() + DocType.EHOLDINGS.getCode() + DocFormat.OLEML.getCode();
        indexerManagerMap.put(key, WorkEInstanceDocumentIndexer.getInstance());

    }

    public IndexerService getDocumentIndexManager(String docCategory, String docType, String docFormat) {
        return indexerManagerMap.get(docCategory + docType + docFormat);
    }


}
