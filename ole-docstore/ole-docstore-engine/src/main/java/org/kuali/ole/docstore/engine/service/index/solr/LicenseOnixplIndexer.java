package org.kuali.ole.docstore.engine.service.index.solr;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.solr.common.SolrInputDocument;
import org.kuali.ole.docstore.common.document.License;
import org.kuali.ole.docstore.common.document.Licenses;
import org.kuali.ole.docstore.common.document.content.license.onixpl.ONIXPublicationsLicenseMessage;
import org.kuali.ole.docstore.common.document.content.license.onixpl.jaxb.LicenseOnixplRecordConverter;
import org.kuali.ole.docstore.indexer.solr.DocumentLocalId;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentCategory;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentConfig;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentFormat;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentType;
import org.kuali.ole.docstore.model.xmlpojo.metadata.DocumentMetaData;
import org.kuali.ole.docstore.model.xmlpojo.metadata.Field;
import org.kuali.ole.docstore.utility.XMLUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 2/25/14
 * Time: 6:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class LicenseOnixplIndexer extends DocstoreSolrIndexService {

    private static DocumentMetaData onixPlMetaData = new DocumentMetaData();
    StringBuffer allText = new StringBuffer();
    XMLUtility xmlUtility = new XMLUtility();
    private static LicenseOnixplIndexer licenseOnixplIndexer;

    public static LicenseOnixplIndexer getInstance() {
        if(licenseOnixplIndexer == null) {
            licenseOnixplIndexer = new LicenseOnixplIndexer();
        }
        return licenseOnixplIndexer;
    }
    static {

        List<DocumentCategory> docCategories = DocumentConfig.getInstance().getDocumentCategories();

        for (DocumentCategory cat : docCategories) {

            for (DocumentType type : cat.getDocumentTypes()) {

                for (DocumentFormat format : type.getDocumentFormats()) {

                    if (DocCategory.WORK.isEqualTo(cat.getId()) && DocType.LICENSE.isEqualTo(type.getId())
                            && DocFormat.ONIXPL.isEqualTo(format.getId())) {

                        for (org.kuali.ole.docstore.model.xmlpojo.config.Field field : format.getFields()) {

                            Field onixPlField = new Field();
                            onixPlField.setName(field.getId());
                            if (field.getMapping().getInclude() != null)
                                onixPlField.set("xpath", field.getMapping().getInclude());
                            onixPlMetaData.getFields().add(onixPlField);

                        }

                    }

                }

            }

        }

    }


    protected void buildSolrInputDocument(Object object, List<SolrInputDocument> solrInputDocuments) {
        License license = (License) object;
        LicenseOnixplRecordConverter licenseOnixplRecordConverter = new LicenseOnixplRecordConverter();

        String content = license.getContent();
        ONIXPublicationsLicenseMessage onixPublicationsLicenseMessage = licenseOnixplRecordConverter.unmarshal(content);

        SolrInputDocument solrDoc = new SolrInputDocument();
        solrDoc.addField(DOC_TYPE, DocType.LICENSE.getDescription());
        solrDoc.addField(DOC_FORMAT, DocFormat.ONIXPL.getCode());
        solrDoc.setField(DOC_CATEGORY, DocCategory.WORK.getCode());
        JXPathContext message = JXPathContext.newContext(onixPublicationsLicenseMessage);
        for (Field field : onixPlMetaData.getFields()) {
            String xp = field.get("xpath");
            if (xp != null && xp.trim().length() != 0) {
                Iterator values = message.iterate(xp);
                while (values.hasNext()) {
                    Object value = values.next();
                    solrDoc.addField(field.getName(), value);
                }
            }
        }
        //TODO:set additional attributes

        if (content != null) {
            allText.append(xmlUtility.getAllContentText(content));
        }
        solrDoc.addField(ALL_TEXT, allText.toString());
        solrDoc.addField("dateEntered", new Date());
        solrDoc.addField("createdBy", license.getCreatedBy());
        solrDoc.setField("id", license.getId());
        solrDoc.setField("uniqueId", license.getId());
        solrDoc.setField("LocalId_search", DocumentLocalId.getDocumentId(license.getId()));
        solrDoc.setField("LocalId_display", DocumentLocalId.getDocumentIdDisplay(license.getId()));


        solrInputDocuments.add(solrDoc);

    }


    public void createTrees(Object object) {
        List<SolrInputDocument> solrInputDocuments = new ArrayList<>();
        Licenses licenses = (Licenses) object;
        for(License license : licenses.getLicenses()) {
            buildSolrInputDocument(license, solrInputDocuments);
        }

        indexSolrDocuments(solrInputDocuments, true);
    }


    protected void updateRecordInSolr(Object object, List<SolrInputDocument> solrInputDocuments) {
        License license = (License) object;
        this.buildSolrInputDocument(object, solrInputDocuments);
        solrInputDocuments.get(0).addField("updatedBy", license.getUpdatedBy());
        solrInputDocuments.get(0).addField("dateUpdated", new Date());

    }

}
