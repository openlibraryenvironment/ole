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
package org.kuali.ole.docstore.model.xstream.metadata;

import com.thoughtworks.xstream.core.util.Fields;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.docstore.model.xmlpojo.metadata.DocumentMetaData;
import org.kuali.ole.docstore.model.xmlpojo.metadata.DocumentsMetaData;
import org.kuali.ole.docstore.model.xmlpojo.metadata.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import javax.swing.event.ListDataEvent;
import java.util.List;

import java.util.ArrayList;

import static junit.framework.Assert.*;

/**
 * Class to DocStoreMetaDataXmlProcessor_UT.
 *
 * @author Rajesh Chowdary K
 * @created Jun 14, 2012
 */
public class DocStoreMetaDataXmlProcessor_UT {

    public static final Logger LOG = LoggerFactory.getLogger(DocStoreMetaDataXmlProcessor_UT.class);

    /**
     * Method to setUp
     *
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Method to tearDown
     *
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link org.kuali.ole.docstore.model.xstream.metadata.DocStoreMetaDataXmlProcessor#fromXml(java.lang.String)}.
     *
     * @throws Exception
     */
    @Test
    public void testFromXml() throws Exception {
        try {
            DocumentsMetaData metadata = new DocStoreMetaDataXmlProcessor().fromXml(IOUtils.toString(getClass().getResource(
                    "/org/kuali/ole/docstore/documentsMetaData.xml")));
            LOG.info("converted to MetaData : " + metadata);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Test method for {@link org.kuali.ole.docstore.model.xstream.metadata.DocStoreMetaDataXmlProcessor#fromXml(java.lang.String)}.
     *
     * @throws Exception
     */
    @Test
    public void testDocStoreMetaData() throws Exception {
        try {
            DocumentMetaData marcMetaData = DocumentsMetaData.getInstance().getDocumentMetaData("work", "bib", "marc");
            LOG.info("Marc MetaData : " + marcMetaData);
            for (Field field : marcMetaData.getFields())
                LOG.info("Field:\t" + field.getName() + "\t\t:\t\t" + field.getProperties());

            DocumentMetaData dubUnqMetaData = DocumentsMetaData.getInstance().getDocumentMetaData("work", "bib", "dublinunq");
            LOG.info("Dublin Unq MetaData : " + dubUnqMetaData);
            for (Field field : dubUnqMetaData.getFields())
                LOG.info("Field:\t" + field.getName() + "\t\t:\t\t" + field.getProperties());

            DocumentMetaData dubMetaData = DocumentsMetaData.getInstance().getDocumentMetaData("work", "bib", "dublin");
            LOG.info("Dublin MetaData : " + dubMetaData);
            for (Field field : dubMetaData.getFields())
                LOG.info("Field:\t" + field.getName() + "\t\t:\t\t" + field.getProperties());

            DocumentMetaData documentMetaData = new DocumentMetaData();
            Field field = new Field();
            List<Field> fieldList = new ArrayList<Field>();
            documentMetaData.setCategory("category");
            documentMetaData.setFormat("Format");
            documentMetaData.setType("type");
            field.set("property", "value");
            if (field.get("property") != null) {
                LOG.info(field.get("property"));
            }
            fieldList.add(field);
            documentMetaData.setFields(null);
            if (documentMetaData.getFields() != null) {
                for (Field field1 : documentMetaData.getFields()) {
                    LOG.info(field1.toString());
                }
            }
            List<DocumentMetaData> documentsMetaDatas = new ArrayList<DocumentMetaData>();
            documentsMetaDatas.add(documentMetaData);
            DocumentsMetaData documentsMetaData = new DocumentsMetaData();
            documentsMetaData.setDocumentsMetaData(documentsMetaDatas);
            if (documentsMetaData.getDocumentsMetaData() != null) {
                for (DocumentMetaData documentMetaData1 : documentsMetaData.getDocumentsMetaData()) {
                    LOG.info(documentMetaData1.toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
