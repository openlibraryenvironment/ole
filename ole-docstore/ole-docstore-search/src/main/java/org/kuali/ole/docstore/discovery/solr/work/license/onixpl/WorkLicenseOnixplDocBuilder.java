/*
 * Copyright 2012 The Kuali Foundation.
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
package org.kuali.ole.docstore.discovery.solr.work.license.onixpl;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.solr.common.SolrInputDocument;
import org.kuali.ole.docstore.discovery.solr.work.bib.DocBuilder;
import org.kuali.ole.docstore.discovery.solr.work.license.WorkLicenseCommonFields;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.jaxb.work.license.onixpl.WorkLicenseOnixplRecordConverter;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentCategory;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentConfig;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentFormat;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.metadata.DocumentMetaData;
import org.kuali.ole.docstore.model.xmlpojo.metadata.Field;
import org.kuali.ole.docstore.model.xmlpojo.work.license.onixpl.ONIXPublicationsLicenseMessage;
import org.kuali.ole.docstore.utility.XMLUtility;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Class to WorkLicenseOnixplDocBuilder.
 */
public class WorkLicenseOnixplDocBuilder extends DocBuilder implements WorkLicenseCommonFields {

    private static DocumentMetaData onixPlMetaData = new DocumentMetaData();
    StringBuffer allText = new StringBuffer();
    XMLUtility xmlUtility = new XMLUtility();

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

    public SolrInputDocument buildSolrInputDocument(ONIXPublicationsLicenseMessage license, AdditionalAttributes addAtrbts, String content) {
        SolrInputDocument solrDoc = new SolrInputDocument();
        solrDoc.addField(DOC_TYPE, DocType.LICENSE.getDescription());
        solrDoc.addField(DOC_FORMAT, DocFormat.ONIXPL.getCode());
        solrDoc.setField(DOC_CATEGORY, DocCategory.WORK.getCode());
        JXPathContext message = JXPathContext.newContext(license);
        for (Field field : onixPlMetaData.getFields()) {
            String xp = field.get("xpath");
            if (xp != null && xp.trim().length() != 0) {
                boolean hasValues = false;
                if (!xp.startsWith("/request/requestDocuments/ingestDocument/additionalAttributes/")) {
                    Iterator values = message.iterate(xp);
                    while (values.hasNext()) {
                        hasValues = true;
                        Object value = values.next();
                        solrDoc.addField(field.getName(), value);
                    }
                } else if (addAtrbts != null) {
                    solrDoc.addField(field.getName(), addAtrbts.getAttribute(xp.substring(xp.lastIndexOf('/') + 1)));
                    hasValues = true;
                }
                if (!hasValues)
                    solrDoc.addField(field.getName(), null);
            }
        }
        if (content != null)
            allText.append(xmlUtility.getAllContentText(content));
        if (addAtrbts != null)
            allText.append(getAllTextForAdditionalAttributes(addAtrbts));
        solrDoc.addField(ALL_TEXT, allText.toString());
        return solrDoc;
    }

    public void buildSolrInputDocument(RequestDocument requestDocument, List<SolrInputDocument> solrInputDocuments) throws IOException, SAXException,
            XPathExpressionException, ParserConfigurationException {
        WorkLicenseOnixplRecordConverter licenseOnixplRecordProcessor = new WorkLicenseOnixplRecordConverter();
        ONIXPublicationsLicenseMessage onixPublicationsLicenseMessage = null;
        if (requestDocument.getContent() != null && requestDocument.getContent().getContent() != null) {
            onixPublicationsLicenseMessage = licenseOnixplRecordProcessor
                    .unmarshal(requestDocument.getContent().getContent());
        }
        AdditionalAttributes additionalAttributes = requestDocument.getAdditionalAttributes();
        String content = requestDocument.getContent().getContent();
        SolrInputDocument solrInputDocument = buildSolrInputDocument(onixPublicationsLicenseMessage,
                additionalAttributes, content);
        solrInputDocument.setField(ID, requestDocument.getUuid());
        solrInputDocument.setField(UNIQUE_ID, requestDocument.getUuid());
        solrInputDocuments.add(solrInputDocument);
    }

    public String getAllTextForAdditionalAttributes(AdditionalAttributes additionalAttributes) {
        StringBuilder allText = new StringBuilder();
        allText.append(additionalAttributes.getAttribute(TYPE));
        allText.append(FIELD_SEPERATOR);
        allText.append(additionalAttributes.getAttribute(STATUS));
        allText.append(FIELD_SEPERATOR);
        allText.append(additionalAttributes.getAttribute(METHOD));
        return allText.toString();
    }

}
