package org.kuali.ole.docstore.model.jaxb.config;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.kuali.ole.docstore.model.xmlpojo.config.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 6/22/12
 * Time: 1:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentConfigConverter_UT {

    public static final Logger LOG = LoggerFactory.getLogger(DocumentConfigConverter_UT.class);

    @Test
    public void unmarshal() {
        DocumentConfigConverter documentConverter = new DocumentConfigConverter();
        String nullXml = null;

        URL resource = getClass().getResource("/org/kuali/ole/docstore/DocumentConfig.xml");
        try {
            File file = new File(resource.toURI());
            String docXml = FileUtils.readFileToString(file);
            DocumentConfig documentConfig = documentConverter.unmarshal(docXml);
            LOG.info("Types size: " + documentConfig.getDocumentCategories().get(0).getDocumentTypes().size());
            for (DocumentCategory documentCategory : documentConfig.getDocumentCategories()) {
                LOG.info("Category Name:  " + documentCategory.getName());
                LOG.info("Category ID:  " + documentCategory.getId());
                for (DocumentType documentType : documentCategory.getDocumentTypes()) {
                    LOG.info("Type Name:  " + documentType.getName());
                    LOG.info("Type ID:  " + documentType.getId());
                    for (DocumentFormat documentFormat : documentType.getDocumentFormats()) {
                        LOG.info("Format Name:  " + documentFormat.getName());
                        LOG.info("Format ID:  " + documentFormat.getId());
                        for (Field field : documentFormat.getFields()) {
                            LOG.info("Field Name:  " + field.getName());
                            LOG.info("Field ID:  " + field.getId());
                            LOG.info("Field Type:  " + field.getType());
                        }
                    }
                }
            }
            String docConfigXml = documentConverter.marshal(documentConfig);
            LOG.info("Document Config XML" + docConfigXml);
            DocumentConfig documentConfig1 = DocumentConfig.getInstance();
            if (documentConfig1.getDocumentCategories() != null) {
                for (DocumentCategory documentCategory : documentConfig1.getDocumentCategories()) {
                    LOG.info(documentCategory.toString());
                }
            }
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        }
        //
        try {
            documentConverter.unmarshal(nullXml);

        } catch (Exception e) {
            LOG.info("invalid file");
        }

    }
}
