package org.kuali.ole.docstore.model.xmlpojo.config;

import org.junit.Test;
import org.kuali.ole.docstore.xstream.BaseTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: ?
 * Time: ?
 * To change this template use File | Settings | File Templates.
 */
public class ObjectFactory_UT extends BaseTestCase {
    private static final Logger LOG = LoggerFactory.getLogger(ObjectFactory_UT.class);

    @Test
    public void testObjectFactory() throws Exception {
        ObjectFactory objectFactory = new ObjectFactory();
        if (objectFactory.createDocumentConfig() != null) {
            LOG.info(objectFactory.createDocumentConfig().toString());
        }
        Mapping mapping = objectFactory.createMapping();
        DocumentType documentType = objectFactory.createDocumentType();
        Field field = objectFactory.createField();
        DocumentCategory documentCategory = objectFactory.createDocumentCategory();
        DocumentFormat documentFormat = objectFactory.createDocumentFormat();
        mapping.setExclude("exclude");
        mapping.setInclude("include");
        mapping.setType("type");
        if (mapping.getExclude() != null) {
            LOG.info(mapping.getExclude());
        }
        if (mapping.getInclude() != null) {
            LOG.info(mapping.getInclude());
        }
        if (mapping.getType() != null) {
            LOG.info(mapping.getType());
        }
        field.setMapping(mapping);
        field.setId("240");
        field.setName("name");
        field.setType("type");
        if (field.getId() != null) {
            LOG.info(field.getId());
        }
        if (field.getName() != null) {
            LOG.info(field.getName());
        }
        if (field.getType() != null) {
            LOG.info(field.getType());
        }
        if (field.getMapping() != null) {
            LOG.info(field.getMapping().toString());
        }
        documentFormat.setName("name");
        documentFormat.setId("345");
        documentCategory.setName("name");
        documentCategory.setId("id");
        documentType.setId("678");
        documentType.setName("name");
        if (documentType.getDocumentFormats() != null) {
            for (DocumentFormat documentFormat1 : documentType.getDocumentFormats()) {
                LOG.info(documentFormat1.toString());
            }

        }
    }

}
