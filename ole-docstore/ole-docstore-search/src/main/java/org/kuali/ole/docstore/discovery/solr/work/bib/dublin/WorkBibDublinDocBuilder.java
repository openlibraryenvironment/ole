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
package org.kuali.ole.docstore.discovery.solr.work.bib.dublin;

import org.apache.solr.common.SolrInputDocument;
import org.kuali.ole.docstore.discovery.solr.work.bib.DocBuilder;
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
import org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.DCValue;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.WorkBibDublinRecord;
import org.kuali.ole.docstore.model.xstream.work.bib.dublin.WorkBibDublinRecordProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to Build WorkBibDublinRecords or documents.
 *
 * @author Rajesh Chowdary K
 */
public class WorkBibDublinDocBuilder extends DocBuilder implements WorkBibDublinFields {

    private static final Logger LOG = LoggerFactory.getLogger(WorkBibDublinDocBuilder.class);
    private static DocumentMetaData dublinCoreMetaData = new DocumentMetaData();

    static {

        List<DocumentCategory> docCategories = DocumentConfig.getInstance().getDocumentCategories();

        for (DocumentCategory cat : docCategories) {

            for (DocumentType type : cat.getDocumentTypes()) {

                for (DocumentFormat format : type.getDocumentFormats()) {

                    if (DocCategory.WORK.isEqualTo(cat.getId()) && DocType.BIB.isEqualTo(type.getId())
                            && DocFormat.DUBLIN_CORE.isEqualTo(format.getId())) {

                        for (org.kuali.ole.docstore.model.xmlpojo.config.Field field : format.getFields()) {

                            Field dublinField = new Field();
                            dublinField.setName(field.getId());

                            String q = null;
                            String el = null;
                            if (field.getMapping().getInclude() != null && field.getMapping().getInclude().length() != 0) {
                                if (field.getMapping().getInclude().contains(";")) {
                                    q = field.getMapping().getInclude().substring(field.getMapping().getInclude().indexOf(';') + 1);
                                    el = field.getMapping().getInclude().substring(0, field.getMapping().getInclude().indexOf(';'));
                                } else {
                                    el = field.getMapping().getInclude();
                                }
                            } else {
                                continue;
                            }

                            dublinField.set("element", el);

                            if (q != null)
                                dublinField.set("qualifier", q);

                            dublinCoreMetaData.getFields().add(dublinField);

                        }

                    }

                }

            }

        }

        // dublinCoreMetaData = DocumentsMetaData.getInstance().getDocumentMetaData(DocCategory.WORK.getCode(), DocType.BIB.getCode(),
        // DocFormat.DUBLIN_CORE.getCode());

    }

    /**
     * Method to build SolrInputDocument for a given WorkBibDublinRecord.
     *
     * @param record
     * @return
     */
    public SolrInputDocument buildSolrInputDocument(WorkBibDublinRecord record) {
        SolrInputDocument solrDoc = new SolrInputDocument();
        solrDoc.setField(DOC_CATEGORY, DocCategory.WORK.getCode());
        solrDoc.addField(DOC_TYPE, DocType.BIB.getDescription());
        solrDoc.addField(DOC_FORMAT, DocFormat.DUBLIN_CORE.getCode());

        for (Field field : dublinCoreMetaData.getFields()) {
            if (field.getName().equals(PUBLICATIONDATE_FACET)) {
                addFieldToSolrDoc(field.getName(),
                        buildPublicationDateFacetValues(buildFieldValue(field.get("element"), field.get("qualifier"), record)), solrDoc);
            } else {
                addFieldToSolrDoc(field.getName(), buildFieldValue(field.get("element"), field.get("qualifier"), record), solrDoc);
            }
        }
        addFieldToSolrDoc(ALL_TEXT, getAllText(record), solrDoc);
        return solrDoc;
    }

    private void addFieldToSolrDoc(String fieldName, Object value, SolrInputDocument solrDoc) {
//        if (value instanceof List) {
//            for (Object obj : (List<Object>) value) {
//                solrDoc.addField(fieldName, obj);
//            }
//        }
//        else {
//            solrDoc.addField(fieldName, value);
//        }

        if (value instanceof List) {
            if (fieldName.toLowerCase().endsWith("_sort")) // Sort fields only the first value to be inserted.
            {
                if (((List) value).size() > 0) {
                    solrDoc.addField(fieldName, ((List) value).get(0));
                }
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
            if (fieldName.toLowerCase().endsWith("_sort")) {
                if (value != null)
                    solrDoc.addField(fieldName, value.toString());
            } else if (fieldName.endsWith("_facet")) {
                if (value != null)
                    solrDoc.addField(fieldName, getSortString(value.toString()));
            } else {
                solrDoc.addField(fieldName, value);
            }
        }
    }

    /**
     * Method to build Field Value.
     *
     * @param element
     * @param qualifier
     * @param record
     * @return
     */
    private List buildFieldValue(String element, String qualifier, WorkBibDublinRecord record) {
        List fieldValues = new ArrayList();
        for (DCValue dcValue : record.getDcValues()) {


            if (qualifier == null) {
                if (element.equals(dcValue.getElement())) {
                    fieldValues.add(dcValue.getValue());
                }
            } else {
                if (element.equals(dcValue.getElement()) && qualifier.equals(dcValue.getQualifier())) {
                    if (element.equals("date") && qualifier.equals("issued")) {
                        String pubDate = "";
                        pubDate = extractPublicationDateWithRegex(dcValue.getValue());
                        fieldValues.add(pubDate);
                    } else if ("language".equals(element) && "iso".equals(qualifier)) {
                        String lang = Languages.getInstance(Languages.ISO_639_1_CC)
                                .getLanguageDescription(dcValue.getValue().replace('_', '-'));
                        fieldValues.add(lang == null ? "Undefined" : lang);
                    } else {
                        fieldValues.add(dcValue.getValue());
                    }
                }
            }
        }
        return fieldValues;
    }

    /**
     * Method to give all_text field to a given WorkBibDublinRecord.
     *
     * @param record
     * @return
     */
    public List getAllText(WorkBibDublinRecord record) {
        List allText = new ArrayList();
        for (DCValue dcValue : record.getDcValues()) {
            allText.add(dcValue.getValue());
        }
        return allText;
    }

    public void buildSolrInputDocument(RequestDocument requestDocument, List<SolrInputDocument> solrInputDocuments) {
        WorkBibDublinRecordProcessor workBibDublinRecordProcessor = new WorkBibDublinRecordProcessor();
        WorkBibDublinRecord workBibDublinRecord = workBibDublinRecordProcessor.fromXML(
                requestDocument.getContent().getContent());
        SolrInputDocument solrInputDocument = buildSolrInputDocument(workBibDublinRecord);
        solrInputDocument.addField(LOCALID_SEARCH, DocumentLocalId.getDocumentId(requestDocument.getUuid()));
        solrInputDocument.addField(LOCALID_DISPLAY, DocumentLocalId.getDocumentIdDisplay(requestDocument.getUuid()));
        solrInputDocument.setField(ID, requestDocument.getUuid());
        solrInputDocument.addField(UNIQUE_ID, requestDocument.getUuid());
        solrInputDocuments.add(solrInputDocument);
    }
}
