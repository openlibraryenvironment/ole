package org.kuali.ole.docstore.engine.service.index.solr;

import org.apache.solr.common.SolrInputDocument;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.content.bib.dc.unqualified.BibDublinUnQualifiedRecord;
import org.kuali.ole.docstore.common.document.content.bib.dc.unqualified.OaiDcDoc;
import org.kuali.ole.docstore.common.document.content.bib.dc.unqualified.Record;
import org.kuali.ole.docstore.common.document.content.bib.dc.unqualified.Tag;
import org.kuali.ole.docstore.common.document.content.bib.dc.unqualified.xstream.BibDublinUnQualifiedRecordProcessor;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
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
 * Date: 1/21/14
 * Time: 2:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class BibDcUnqualifiedIndexer extends BibMarcIndexer {

    private static BibDcUnqualifiedIndexer bibDcUnqualifiedIndexer = null;

    public static BibDcUnqualifiedIndexer getInstance() {
        if(bibDcUnqualifiedIndexer == null) {
            bibDcUnqualifiedIndexer = new BibDcUnqualifiedIndexer();
        }
        return bibDcUnqualifiedIndexer;
    }
    private static DocumentMetaData dublinUnqMetaData = new DocumentMetaData();

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


    private BibDublinUnQualifiedRecordProcessor bibDublinUnQualifiedRecordProcessor = new BibDublinUnQualifiedRecordProcessor();
    protected void buildSolrInputDocument(Object object, List<SolrInputDocument> solrInputDocuments) {
        Bib bib = (Bib) object;

        BibDublinUnQualifiedRecord bibDublinUnQualifiedRecord = bibDublinUnQualifiedRecordProcessor.fromXML(bib.getContent());

       List<SolrInputDocument> solrInputDocumentList = buildSolrInputDocuments(bibDublinUnQualifiedRecord);

        for(SolrInputDocument solrInputDocument : solrInputDocumentList) {
            setCommonFields(bib, solrInputDocument);
        }

        solrInputDocuments.addAll(solrInputDocumentList);

    }

    /**
     * Method to build SolrInputDocument for a given WorkBibDublinRecord.
     *
     * @param doc
     * @return
     */
    public SolrInputDocument buildSolrInputDocument(OaiDcDoc doc) {
        SolrInputDocument solrDoc = new SolrInputDocument();
        solrDoc.addField(DOC_TYPE, DocType.BIB.getCode());
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


    /**
     * Method to build SolrInputDocuments for a given WorkBibDublinRecord.
     *
     * @param
     * @return
     */
    public List<SolrInputDocument> buildSolrInputDocuments(BibDublinUnQualifiedRecord unqRecord) {
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
     * @param name    - Name of the Field defined in {@link }
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

    protected void updateRecordInSolr(Object object, List<SolrInputDocument> solrInputDocuments) {

    }

}
