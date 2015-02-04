package org.kuali.ole.docstore.discovery.solr.work.license.binary;

import org.apache.solr.common.SolrInputDocument;
import org.kuali.ole.docstore.discovery.solr.work.bib.DocBuilder;
import org.kuali.ole.docstore.discovery.solr.work.license.WorkLicenseCommonFields;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentCategory;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentConfig;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentFormat;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.metadata.DocumentMetaData;
import org.kuali.ole.docstore.model.xmlpojo.metadata.Field;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sreekanth
 * Date: 5/29/12
 * Time: 7:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkLicenseBinaryDocBuilder
        extends DocBuilder
        implements WorkLicenseCommonFields {

    private static final DocumentMetaData pdfMetaData = new DocumentMetaData();
    private static final DocumentMetaData docMetaData = new DocumentMetaData();
    private static final DocumentMetaData xsltMetaData = new DocumentMetaData();

    static {

        List<DocumentCategory> docCategories = DocumentConfig.getInstance().getDocumentCategories();

        for (DocumentCategory cat : docCategories) {

            for (DocumentType type : cat.getDocumentTypes()) {

                for (DocumentFormat format : type.getDocumentFormats()) {

                    if (DocCategory.WORK.isEqualTo(cat.getId()) && DocType.LICENSE.isEqualTo(type.getId()) && DocFormat.PDF.isEqualTo(format.getId())) {

                        for (org.kuali.ole.docstore.model.xmlpojo.config.Field field : format.getFields()) {

                            Field licenseField = new Field();
                            licenseField.setName(field.getId());
                            if (field.getMapping().getInclude() != null)
                                licenseField.set("xpath", field.getMapping().getInclude());
                            pdfMetaData.getFields().add(licenseField);

                        }

                    } else if (DocCategory.WORK.isEqualTo(cat.getId()) && DocType.LICENSE.isEqualTo(type.getId()) && DocFormat.DOC.isEqualTo(format.getId())) {

                        for (org.kuali.ole.docstore.model.xmlpojo.config.Field field : format.getFields()) {

                            Field licenseField = new Field();
                            licenseField.setName(field.getId());
                            if (field.getMapping().getInclude() != null)
                                licenseField.set("xpath", field.getMapping().getInclude());
                            docMetaData.getFields().add(licenseField);

                        }

                    } else if (DocCategory.WORK.isEqualTo(cat.getId()) && DocType.LICENSE.isEqualTo(type.getId())
                            && DocFormat.XSLT.isEqualTo(format.getId())) {

                        for (org.kuali.ole.docstore.model.xmlpojo.config.Field field : format.getFields()) {

                            Field licenseField = new Field();
                            licenseField.setName(field.getId());
                            if (field.getMapping().getInclude() != null)
                                licenseField.set("xpath", field.getMapping().getInclude());
                            xsltMetaData.getFields().add(licenseField);

                        }

                    }

                }

            }

        }

    }

    public void buildSolrInputDocument(RequestDocument requestDocument,
                                       List<SolrInputDocument> solrInputDocuments)
            throws IOException, SAXException, XPathExpressionException, ParserConfigurationException {
        AdditionalAttributes additionalAttributes = null;
        if (requestDocument != null && requestDocument.getAdditionalAttributes() != null) {
            additionalAttributes = requestDocument.getAdditionalAttributes();

        }
        String docFormat = requestDocument.getFormat();
        SolrInputDocument solrInputDocument = buildSolrInputDocumentByJxp(additionalAttributes, docFormat);
        solrInputDocument.setField(ID, requestDocument.getUuid());
        solrInputDocument.setField(UNIQUE_ID, requestDocument.getUuid());
        solrInputDocument.setField(DOC_CATEGORY, DocCategory.WORK.getCode());
        solrInputDocuments.add(solrInputDocument);
    }

    public SolrInputDocument buildSolrInputDocumentByJxp(AdditionalAttributes additionalAttributes, String docFormat) {
        SolrInputDocument solrDoc = new SolrInputDocument();
        solrDoc.addField(DOC_TYPE, DocType.LICENSE.getDescription());
        DocumentMetaData docMetaData = null;
        if (docFormat.equalsIgnoreCase(DocFormat.PDF.getCode())) {
            solrDoc.addField(DOC_FORMAT, DocFormat.PDF.getCode());
            docMetaData = pdfMetaData;
        } else if (docFormat.equalsIgnoreCase(DocFormat.DOC.getCode())) {
            solrDoc.addField(DOC_FORMAT, DocFormat.DOC.getCode());
            docMetaData = WorkLicenseBinaryDocBuilder.docMetaData;
        } else if (docFormat.equalsIgnoreCase(DocFormat.XSLT.getCode())) {
            solrDoc.addField(DOC_FORMAT, DocFormat.XSLT.getCode());
            docMetaData = xsltMetaData;
        }

        if (additionalAttributes != null && docMetaData != null) {
            for (Field field : docMetaData.getFields())
                solrDoc.addField(field.getName(), additionalAttributes.getAttribute(field.get("xpath").substring(1)));
            solrDoc.addField(ALL_TEXT, getAllTextForAdditionalAttributes(additionalAttributes));
        }
        return solrDoc;
    }

    public String getAllTextForAdditionalAttributes(AdditionalAttributes additionalAttributes) {
        StringBuilder allText = new StringBuilder();
        allText.append(additionalAttributes.getAttribute(TYPE));
        allText.append(FIELD_SEPERATOR);
        allText.append(additionalAttributes.getAttribute(NAME));
        allText.append(FIELD_SEPERATOR);
        allText.append(additionalAttributes.getAttribute(FILENAME));
        allText.append(FIELD_SEPERATOR);
        allText.append(additionalAttributes.getAttribute(DATEUPLOADED));
        allText.append(FIELD_SEPERATOR);
        allText.append(additionalAttributes.getAttribute(OWNER));
        allText.append(FIELD_SEPERATOR);
        allText.append(additionalAttributes.getAttribute(NOTES));

        return allText.toString();
    }

}
