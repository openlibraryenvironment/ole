package org.kuali.ole.dsng.indexer;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.common.document.config.DocumentSearchConfig;
import org.kuali.ole.docstore.common.document.content.bib.marc.*;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.indexer.solr.DocumentLocalId;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.utility.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by SheikS on 11/26/2015.
 */
public class BibIndexer extends OleDsNgIndexer {

    private static final Logger LOG = LoggerFactory.getLogger(BibIndexer.class);

    private BibMarcRecordProcessor bibMarcRecordProcessor;
    private DocumentSearchConfig documentSearchConfig;

    @Override
    public void indexDocument(Object object) {
        List<SolrInputDocument> solrInputDocuments = buildSolrInputDocument(object);
        commitDocumentToSolr(solrInputDocuments);
    }

    @Override
    public List<SolrInputDocument> buildSolrInputDocument(Object object) {
        List<SolrInputDocument> solrInputDocuments = new ArrayList<SolrInputDocument>();
        BibRecord bibRecord = (BibRecord) object;
        BibMarcRecords bibMarcRecords = getBibMarcRecordProcessor().fromXML(bibRecord.getContent());
        SolrInputDocument solrInputDocument = buildSolrInputDocumentWithBibMarcRecord(bibMarcRecords.getRecords().get(0));

        setCommonFields(bibRecord, solrInputDocument);

        solrInputDocuments.add(solrInputDocument);
        return solrInputDocuments;
    }

    public SolrInputDocument buildSolrInputDocumentWithBibMarcRecord(BibMarcRecord record) {
        SolrInputDocument solrInputDocument = new SolrInputDocument();

        solrInputDocument.addField(LEADER, record.getLeader());

        // Title Field Calculations.
        List<ControlField> controlFieldList = record.getControlFields();

        for (ControlField cf : controlFieldList) {
            solrInputDocument.addField("controlfield_" + cf.getTag(), cf.getValue());
        }

        solrInputDocument.addField(DOC_TYPE, DocType.BIB.getDescription());
        solrInputDocument.addField(DOC_FORMAT, DocFormat.MARC.getDescription());
        return solrInputDocument;
    }

    protected void setCommonFields(BibRecord bibRecord, SolrInputDocument solrInputDocument) {
        String bibIdWithPrefix = DocumentUniqueIDPrefix.getPrefixedId(bibRecord.getUniqueIdPrefix(), String.valueOf(bibRecord.getBibId()));
        solrInputDocument.setField(ID, bibIdWithPrefix);
        solrInputDocument.addField(LOCALID_SEARCH, DocumentLocalId.getDocumentId(bibRecord.getBibId()));
        solrInputDocument.addField(LOCALID_DISPLAY, DocumentLocalId.getDocumentIdDisplay(bibRecord.getBibId()));
        solrInputDocument.addField(UNIQUE_ID, bibIdWithPrefix);
        solrInputDocument.setField(DOC_CATEGORY, DocCategory.WORK.getCode());
        solrInputDocument.setField(BIB_ID, bibIdWithPrefix);

        solrInputDocument.setField(STATUS_SEARCH, bibRecord.getStatus());
        solrInputDocument.setField(STATUS_DISPLAY, bibRecord.getStatus());

        if (null != bibRecord.getStatusUpdatedDate()) {
            solrInputDocument.setField(STATUS_UPDATED_ON, getDate(bibRecord.getStatusUpdatedDate().toString()));
        }

        solrInputDocument.addField(STAFF_ONLY_FLAG, bibRecord.getStaffOnlyFlag());

        String createdBy = bibRecord.getCreatedBy();
        solrInputDocument.setField(CREATED_BY, createdBy);
        solrInputDocument.setField(UPDATED_BY, createdBy);

        Date date = new Date();
        Date createdDate = null;

        if (null != bibRecord.getDateCreated()) {
            createdDate = getDate(bibRecord.getDateCreated().toString());
            solrInputDocument.setField(DATE_ENTERED, createdDate);
        } else {
            solrInputDocument.setField(DATE_ENTERED, date);
        }

        if (null != bibRecord.getStatusUpdatedDate()) {
            solrInputDocument.setField(DATE_UPDATED, getDate(bibRecord.getStatusUpdatedDate().toString()));
        } else {
            if (null != bibRecord.getDateCreated()) {
                // Updated date will have created date value when bib is not updated after it is created.
                solrInputDocument.setField(DATE_UPDATED, createdDate);
            } else {
                solrInputDocument.setField(DATE_UPDATED, date);
            }
        }
    }


    private Date getDate(String dateStr) {
        DateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
        try {
            if (StringUtils.isNotEmpty(dateStr)) {
                return format.parse(dateStr);
            } else {
                return new Date();
            }

        } catch (ParseException e) {
            LOG.info("Exception : " + dateStr + " for format:: " + Constants.DATE_FORMAT, e);
            return new Date();
        }
    }

    public BibMarcRecordProcessor getBibMarcRecordProcessor() {
        if(null == bibMarcRecordProcessor) {
            bibMarcRecordProcessor = new BibMarcRecordProcessor();
        }
        return bibMarcRecordProcessor;
    }

    public void setBibMarcRecordProcessor(BibMarcRecordProcessor bibMarcRecordProcessor) {
        this.bibMarcRecordProcessor = bibMarcRecordProcessor;
    }

    public DocumentSearchConfig getDocumentSearchConfig() {
        if(null == documentSearchConfig) {
            documentSearchConfig = DocumentSearchConfig.getDocumentSearchConfig();
        }
        return documentSearchConfig;
    }

    public void setDocumentSearchConfig(DocumentSearchConfig documentSearchConfig) {
        this.documentSearchConfig = documentSearchConfig;
    }
}
