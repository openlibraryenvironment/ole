package org.kuali.ole.docstore.engine.service.index.solr;

import org.apache.solr.common.SolrInputDocument;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.content.bib.dc.BibDcRecord;
import org.kuali.ole.docstore.common.document.content.bib.dc.DCValue;
import org.kuali.ole.docstore.common.document.content.bib.dc.xstream.BibDcRecordProcessor;

import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.discovery.util.*;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentCategory;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentConfig;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentFormat;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentType;
import org.kuali.ole.docstore.model.xmlpojo.metadata.DocumentMetaData;
import org.kuali.ole.docstore.model.xmlpojo.metadata.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/17/13
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class BibDCIndexer extends BibMarcIndexer {


    private static BibDCIndexer bibDCIndexer = null;

    public static BibDCIndexer getInstance() {
        if(bibDCIndexer == null) {
            bibDCIndexer = new BibDCIndexer();
        }
        return bibDCIndexer;
    }

    private BibDcRecordProcessor bibDcRecordProcessor = new BibDcRecordProcessor();

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
    }


    protected void buildSolrInputDocument(Object object, List<SolrInputDocument> solrInputDocuments) {
        Bib bib = (Bib) object;
        BibDcRecord bibMarcRecords = bibDcRecordProcessor.fromXML(bib.getContent());
        SolrInputDocument solrInputDocument = buildSolrInputDocument(bibMarcRecords);

        setCommonFields(bib, solrInputDocument);

        solrInputDocuments.add(solrInputDocument);

    }

    public SolrInputDocument buildSolrInputDocument(BibDcRecord record) {
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

    /**
     * Method to build Field Value.
     *
     * @param element
     * @param qualifier
     * @param record
     * @return
     */
    private List buildFieldValue(String element, String qualifier, BibDcRecord record) {
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
                        String lang = org.kuali.ole.docstore.discovery.util.Languages.getInstance(org.kuali.ole.docstore.discovery.util.Languages.ISO_639_1_CC)
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
    public List getAllText(BibDcRecord record) {
        List allText = new ArrayList();
        for (DCValue dcValue : record.getDcValues()) {
            allText.add(dcValue.getValue());
        }
        return allText;
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

}
