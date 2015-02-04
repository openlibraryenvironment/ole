/*
 * Copyright 2011 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl2.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.docstore.discovery.solr.work.bib.dublin.unqualified;

import org.apache.solr.common.SolrInputDocument;
import org.kuali.ole.docstore.common.document.content.instance.Instance;
import org.kuali.ole.docstore.common.document.content.instance.InstanceCollection;
import org.kuali.ole.docstore.discovery.solr.work.bib.DocBuilder;
import org.kuali.ole.docstore.discovery.solr.work.instance.oleml.WorkInstanceOlemlDocBuilder;
import org.kuali.ole.docstore.discovery.util.Languages;
import org.kuali.ole.docstore.indexer.solr.DocumentLocalId;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentCategory;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentConfig;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentFormat;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.metadata.DocumentMetaData;
import org.kuali.ole.docstore.model.xmlpojo.metadata.Field;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.unqualified.OaiDcDoc;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.unqualified.Record;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.unqualified.Tag;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.unqualified.WorkBibDublinUnQualifiedRecord;
import org.kuali.ole.docstore.model.xstream.work.bib.dublin.unqualified.WorkBibDublinUnQualifiedRecordProcessor;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to Build WorkBibDublinUnQualifiedRecords or documents.
 *
 * @author Rajesh Chowdary K
 */
public class WorkBibDublinUnQualifiedDocBuilder extends DocBuilder implements WorkBibDublinUnqualifiedFields {

    private static DocumentMetaData dublinUnqMetaData = new DocumentMetaData();
    private static final String INSTANCE_IDENTIFIER = "instanceIdentifier";

    static {

        List<DocumentCategory> docCategories = DocumentConfig.getInstance().getDocumentCategories();

        for (DocumentCategory cat : docCategories) {

            for (DocumentType type : cat.getDocumentTypes()) {

                for (DocumentFormat format : type.getDocumentFormats()) {

                    if (DocCategory.WORK.isEqualTo(cat.getId()) && DocType.BIB.isEqualTo(type.getId())
                            && DocFormat.DUBLIN_UNQUALIFIED.isEqualTo(format.getId())) {

                        for (org.kuali.ole.docstore.model.xmlpojo.config.Field field : format.getFields()) {

                            Field dublinField = new Field();
                            dublinField.setName(field.getId());

                            if (field.getMapping().getInclude() != null)
                                dublinField.set("tag", field.getMapping().getInclude());

                            dublinUnqMetaData.getFields().add(dublinField);

                        }

                    }

                }

            }

        }

        // dublinUnqMetaData = DocumentsMetaData.getInstance().getDocumentMetaData(DocCategory.WORK.getCode(), DocType.BIB.getCode(),
        // DocFormat.DUBLIN_UNQUALIFIED.getCode());
    }

    /**
     * Method to build SolrInputDocument for a given WorkBibDublinRecord.
     *
     * @param doc
     * @return
     */
    public SolrInputDocument buildSolrInputDocument(OaiDcDoc doc) {
        SolrInputDocument solrDoc = new SolrInputDocument();
        solrDoc.addField(DOC_TYPE, DocType.BIB.getDescription());
        solrDoc.addField(DOC_FORMAT, DocFormat.DUBLIN_UNQUALIFIED.getCode());

        for (Field field : dublinUnqMetaData.getFields()) {

            if (field.getName().equals(PUBLICATIONDATE_FACET)) {
                addFieldToSolrDoc(field.getName(), buildPublicationDateFacetValues(buildFieldValue(field.getName(), field.get("tag"), doc)), solrDoc);
            } else {
                addFieldToSolrDoc(field.getName(), buildFieldValue(field.getName(), field.get("tag"), doc), solrDoc);
            }

        }

        addFieldToSolrDoc(ALL_TEXT, getAllText(doc), solrDoc);
        return solrDoc;
    }

    private void addFieldToSolrDoc(String fieldName, Object value, SolrInputDocument solrDoc) {
//        if (value instanceof List) {
//            for (Object obj : (List<Object>) value) {
//                solrDoc.addField(fieldName, obj);
//            }
//        } else {
//            solrDoc.addField(fieldName, value);
//        }
        if (value instanceof List) {
            if (fieldName.toLowerCase().endsWith("_sort")) // Sort fields only the first value to be inserted.
            {
                solrDoc.addField(fieldName, ((List) value).get(0));
            } else if (fieldName.endsWith("_facet")) {
                solrDoc.addField(fieldName, getSortString((List) value));
            } else {
                for (Object obj : (List<Object>) value)
                // All non Sort and Multi Valued Fields
                {
                    solrDoc.addField(fieldName, obj);
                }
            }
        } else {
            if (fieldName.endsWith("_sort")) {
                if (value != null) {
                    solrDoc.addField(fieldName, value.toString());
                }
            } else if (fieldName.endsWith("_facet")) {
                if (value != null) {
                    solrDoc.addField(fieldName, getSortString(value.toString()));
                }
            } else {
                solrDoc.addField(fieldName, value);
            }
        }
    }

    /**
     * Method to build Field Value.
     *
     * @param name    - Name of the Field defined in {@link WorkBibDublinUnQualifiedDocBuilder}
     * @param record
     * @param tagName
     * @return
     */
    private List<String> buildFieldValue(String name, String tagName, OaiDcDoc record) {
//        String tagName = null;
        List<String> fieldValues = new ArrayList<String>();

//        for (Field field : dublinUnqMetaData.getFields())
//            if (field.getName().endsWith(name)) {
//                tagName = field.get("tag");
//                break;
//            }

        for (Tag tag : record.get(tagName))
            if (name.startsWith("PublicationDate")) {
                String pubDate = extractPublicationDateWithRegex(tag.getValue());
                fieldValues.add(pubDate);
            } else if (tagName.equals(OaiDcDoc.LANGUAGE)) {
                String lang = Languages.getInstance(Languages.ISO_639_3).getLanguageDescription(tag.getValue());
                fieldValues.add(lang == null ? "Undefined" : lang);
            } else if (name.startsWith("ISBN")) {
                if (tag.getValue().toUpperCase().contains("(ISBN)"))
                    fieldValues.add(tag.getValue().toUpperCase().replace("(ISBN)", ""));
            } else if (name.startsWith("ISSN")) {
                if (tag.getValue().toUpperCase().contains("(ISSN)"))
                    fieldValues.add(tag.getValue().toUpperCase().replace("(ISSN)", ""));
            } else {
                fieldValues.add(tag.getValue());
            }

        if (fieldValues.size() > 0) {
            return fieldValues;
        } else {
            return null;
        }
    }

    /**
     * Method to give all_text field to a given WorkBibDublinRecord.
     *
     * @param record
     * @return
     */
    public List getAllText(OaiDcDoc record) {
        List allTxt = new ArrayList();
        for (Tag tag : record.getAllTags()) {
            allTxt.add(tag.getValue());
        }
        return allTxt;
    }

    /**
     * Method to build SolrInputDocuments for a given WorkBibDublinRecord.
     *
     * @param
     * @return
     */
    public List<SolrInputDocument> buildSolrInputDocuments(WorkBibDublinUnQualifiedRecord unqRecord) {
        List<SolrInputDocument> solrDocs = new ArrayList<SolrInputDocument>();
        if (unqRecord.getListRecords() != null) {
            for (Record rec : unqRecord.getListRecords().getRecords()) {
                if (rec.getMetadata() != null) {
                    for (OaiDcDoc doc : rec.getMetadata().getOaiDcDocs()) {
                        solrDocs.add(buildSolrInputDocument(doc));
                    }
                }
            }
        }
        return solrDocs;

    }

    public void buildSolrInputDocument(RequestDocument requestDocument, List<SolrInputDocument> solrInputDocuments) throws IOException, SAXException,
            XPathExpressionException, ParserConfigurationException {
        WorkBibDublinUnQualifiedRecordProcessor workBibDublinUnQualifiedRecordProcessor = new WorkBibDublinUnQualifiedRecordProcessor();
        WorkBibDublinUnQualifiedRecord bibDublinUnQualifiedRecord = workBibDublinUnQualifiedRecordProcessor.fromXML(requestDocument.getContent()
                .getContent());
        List<Record> records = bibDublinUnQualifiedRecord.getListRecords().getRecords();
        for (Record record : records) {
            List<OaiDcDoc> oaiDcDocs = record.getMetadata().getOaiDcDocs();
            for (OaiDcDoc oaiDcDoc : oaiDcDocs) {
                SolrInputDocument solrInputDocument = buildSolrInputDocument(oaiDcDoc);
                solrInputDocument.setField(ID, requestDocument.getUuid());
                solrInputDocument.addField(LOCALID_SEARCH, DocumentLocalId.getDocumentId(requestDocument.getUuid()));
                solrInputDocument.addField(LOCALID_DISPLAY, DocumentLocalId.getDocumentIdDisplay(requestDocument.getUuid()));
                solrInputDocument.addField(UNIQUE_ID, requestDocument.getUuid());
                solrInputDocument.setField(DOC_CATEGORY, DocCategory.WORK.getCode());
                solrInputDocuments.add(solrInputDocument);
                for (RequestDocument linkRequestDocument : requestDocument.getLinkedRequestDocuments()) {
                    String id = linkRequestDocument.getUuid();
                    if (id == null) {
                        id = getIndexerService(requestDocument).buildUuid();
                        linkRequestDocument.setUuid(id);
                        //id = linkRequestDocument.getId();
                    }
                    solrInputDocument.addField(INSTANCE_IDENTIFIER, id);
                    if (linkRequestDocument.getContent() != null
                            && linkRequestDocument.getContent().getContentObject() != null) {
                        InstanceCollection instanceCollection = (InstanceCollection) linkRequestDocument.getContent()
                                .getContentObject();
                        List<Instance> oleInstanceList = instanceCollection.getInstance();
                        for (Instance oleInstance : oleInstanceList) {
                            // To make sure that bib Id is added for instance but not duplicated.
                            if (!oleInstance.getResourceIdentifier().contains(requestDocument.getUuid())) {
                                // TODO: This logic should be also in docstore side - before creating instance node - so that instance xml has the resourceIdentifier field.
                                oleInstance.getResourceIdentifier().add(requestDocument.getUuid());
                            }
                            oleInstance.setInstanceIdentifier(id);
                        }
                    }
                    new WorkInstanceOlemlDocBuilder().buildSolrInputDocuments(linkRequestDocument, solrInputDocuments);
                }

            }
        }

    }
}
