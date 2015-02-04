package org.kuali.ole.docstore.engine.service.index.solr;

import org.apache.solr.common.SolrInputDocument;
import org.kuali.ole.docstore.common.document.License;
import org.kuali.ole.docstore.common.document.LicenseAttachment;
import org.kuali.ole.docstore.common.document.Licenses;
import org.kuali.ole.docstore.indexer.solr.DocumentLocalId;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 2/25/14
 * Time: 6:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class LicenseAttachmentIndexer extends LicenseOnixplIndexer {
    private static LicenseAttachmentIndexer licenseAttachmentIndexer;

    public static LicenseOnixplIndexer getInstance() {
        if(licenseAttachmentIndexer == null) {
            licenseAttachmentIndexer = new LicenseAttachmentIndexer();
        }
        return licenseAttachmentIndexer;
    }

    protected void buildSolrInputDocument(Object object, List<SolrInputDocument> solrInputDocuments) {
        LicenseAttachment licenseAttachment = (LicenseAttachment) object;
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        solrInputDocument.addField("dateEntered", new Date());
        solrInputDocument.addField("createdBy", licenseAttachment.getCreatedBy());
        solrInputDocument.addField(DOC_TYPE, DocType.LICENSE.getDescription());
        solrInputDocument.addField(DOC_FORMAT, licenseAttachment.getFormat());
        solrInputDocument.setField(DOC_CATEGORY, DocCategory.WORK.getCode());
        solrInputDocument.setField("id", licenseAttachment.getId());
        solrInputDocument.setField("uniqueId", licenseAttachment.getId());
        solrInputDocument.setField("LocalId_search", DocumentLocalId.getDocumentId(licenseAttachment.getId()));
        solrInputDocument.setField("LocalId_display", DocumentLocalId.getDocumentIdDisplay(licenseAttachment.getId()));
        solrInputDocuments.add(solrInputDocument);

    }

    protected void updateRecordInSolr(Object object, List<SolrInputDocument> solrInputDocuments) {
        LicenseAttachment licenseAttachment = (LicenseAttachment) object;
        this.buildSolrInputDocument(object, solrInputDocuments);
        solrInputDocuments.get(0).addField("updatedBy", licenseAttachment.getUpdatedBy());
        solrInputDocuments.get(0).addField("dateUpdated", new Date());

    }

}
