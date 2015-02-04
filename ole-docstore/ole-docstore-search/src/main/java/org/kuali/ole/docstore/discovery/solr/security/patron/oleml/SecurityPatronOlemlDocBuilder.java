//package org.kuali.ole.docstore.discovery.solr.security.patron.oleml;
//
//import org.apache.solr.common.SolrInputDocument;
//import org.kuali.ole.docstore.model.enums.DocCategory;
//import org.kuali.ole.docstore.model.enums.DocFormat;
//import org.kuali.ole.docstore.model.enums.DocType;
//import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
//import org.kuali.ole.docstore.model.xmlpojo.security.patron.oleml.Identification;
//import org.kuali.ole.docstore.model.xmlpojo.security.patron.oleml.Patron;
//import org.kuali.ole.docstore.model.xmlpojo.security.patron.oleml.PatronGroup;
//import org.kuali.ole.docstore.model.xstream.security.patron.oleml.SecurityPatronOlemlRecordProcessor;
//
//import java.util.List;
//
///**
// * Created by IntelliJ IDEA.
// * User: Pranitha
// * Date: 3/16/12
// * Time: 12:41 PM
// * To change this template use File | Settings | File Templates.
// */
//public class SecurityPatronOlemlDocBuilder
//        implements SecurityPatronCommomFields {
//
//    public void buildSolrInputDocument(RequestDocument requestDocument, List<SolrInputDocument> solrInputDocuments) {
//        SecurityPatronOlemlRecordProcessor patronProcessor = new SecurityPatronOlemlRecordProcessor();
//        PatronGroup patronGroup = patronProcessor.fromXML(requestDocument.getContent().getContent());
//        List<Patron> patronList = patronGroup.getPatron();
//        for (Patron patron : patronList) {
//
//            SolrInputDocument solrInputDocument = new SolrInputDocument();
//            solrInputDocument.addField(DOC_CATEGORY, DocCategory.SECURITY.getCode());
//            solrInputDocument.addField(DOC_TYPE, DocType.PATRON.getCode());
//            solrInputDocument.addField(DOC_FORMAT, DocFormat.OLEML.getCode());
//            solrInputDocument.addField(ID, requestDocument.getUuid());
//            solrInputDocument.addField(UNIQUE_ID, requestDocument.getUuid());
//            solrInputDocument.addField(BORROWER_TYPE_SEARCH, patron.getBorrowerType());
//            solrInputDocument.addField(BORROWER_TYPE_DISPLAY, patron.getBorrowerType());
//            solrInputDocument.addField(RECORD_NUMBER_SEARCH, patron.getRecordNumber());
//            solrInputDocument.addField(RECORD_NUMBER_DISPLAY, patron.getRecordNumber());
//            solrInputDocument.addField(NAME_SEARCH, patron.getName().toString());
//            solrInputDocument.addField(NAME_DISPLAY, patron.getName().toString());
//            solrInputDocument.addField(BEGIN_DATE_SEARCH, patron.getActiveDates().getBeginDate());
//            solrInputDocument.addField(BEGIN_DATE_DISPLAY, patron.getActiveDates().getBeginDate());
//            solrInputDocument.addField(BAECODE_NUMBER_SEARCH, patron.getBarcode().getBarcodeNumber());
//            solrInputDocument.addField(BAECODE_NUMBER_DISPLAY, patron.getBarcode().getBarcodeNumber());
//            solrInputDocument.addField(BARCODE_STATUS_SEARCH, patron.getBarcode().getStatus());
//            solrInputDocument.addField(BARCODE_STATUS_DISPLAY, patron.getBarcode().getStatus());
//            for (Identification id : patron.getIdentifications().getIdentification()) {
//                solrInputDocument.addField(IDENTIFICATION_TYPE_SEARCH, id.getIdentificationType());
//                solrInputDocument.addField(IDENTIFICATION_TYPE_DISPLAY, id.getIdentificationType());
//                solrInputDocument.addField(IDENTIFICATION_SEARCH, id.getIdentifier());
//                solrInputDocument.addField(IDENTIFICATION_DISPLAY, id.getIdentifier());
//            }
//            solrInputDocuments.add(solrInputDocument);
//        }
//    }
//}
