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

import org.apache.commons.io.FileUtils;
import org.apache.solr.common.SolrInputDocument;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.docstore.discovery.BaseTestCase;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.unqualified.OaiDcDoc;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.unqualified.Record;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.unqualified.WorkBibDublinUnQualifiedRecord;
import org.kuali.ole.docstore.model.xstream.work.bib.dublin.unqualified.WorkBibDublinUnQualifiedRecordProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Class to test WorkBibDublinUnQualifiedDocBuilder.
 *
 * @author Rajesh Chowdary K
 */
public class WorkBibDublinUnQualifiedDocBuilder_UT extends BaseTestCase {

    private static final Logger LOG = LoggerFactory.getLogger(WorkBibDublinUnQualifiedDocBuilder_UT.class);
    private WorkBibDublinUnQualifiedRecord record;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        String resFile = "/bib/bib/dublin/unqualified/Bib-Bib-DublinUnQ-Test1.xml";
        URL resource = getClass().getResource(resFile);
        File file = new File(resource.toURI());
        String fileContent;
        fileContent = FileUtils.readFileToString(file);
        WorkBibDublinUnQualifiedRecordProcessor processor = new WorkBibDublinUnQualifiedRecordProcessor();
        record = processor.fromXML(fileContent);
        LOG.info(record.getRequest());
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link org.kuali.ole.docstore.discovery.solr.work.bib.dublin.unqualified.WorkBibDublinUnQualifiedDocBuilder#buildSolrInputDocument(org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.unqualified.OaiDcDoc)}.
     */
    @Test
    public final void testBuildSolrInputDocument() {
        try {
            WorkBibDublinUnQualifiedDocBuilder builder = new WorkBibDublinUnQualifiedDocBuilder();
            for (Record rec : record.getListRecords().getRecords())
                for (OaiDcDoc doc : rec.getMetadata().getOaiDcDocs()) {
                    SolrInputDocument solrDoc = builder.buildSolrInputDocument(doc);
                    LOG.info(solrDoc.toString());
                    assertNotNull(solrDoc);
                }
        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
            fail("Document Build Failed: " + e.getMessage());
        }
    }

}
