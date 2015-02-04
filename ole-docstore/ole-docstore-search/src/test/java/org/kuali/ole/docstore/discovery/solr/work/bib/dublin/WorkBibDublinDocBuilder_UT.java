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

import org.apache.commons.io.FileUtils;
import org.apache.solr.common.SolrInputDocument;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.docstore.discovery.BaseTestCase;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.WorkBibDublinRecord;
import org.kuali.ole.docstore.model.xstream.work.bib.dublin.WorkBibDublinRecordProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.fail;

/**
 * Class to Test WorkBibDublinDocBuilder.
 *
 * @author Rajesh Chowdary K
 */
public class WorkBibDublinDocBuilder_UT extends BaseTestCase {
    private static final Logger LOG = LoggerFactory.getLogger(WorkBibDublinDocBuilder_UT.class);

    private WorkBibDublinRecord record = new WorkBibDublinRecord();

    @Before
    public void setUp() throws Exception {
        String resFile = "/bib/bib/dublin/Bib-Bib-DublinQ-Test1.xml";
        URL resource = getClass().getResource(resFile);
        File file = new File(resource.toURI());
        String fileContent;
        fileContent = FileUtils.readFileToString(file);
        WorkBibDublinRecordProcessor processor = new WorkBibDublinRecordProcessor();
        record = processor.fromXML(fileContent);
//		LOG.info(record);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testBuildSolrInputDocument() {
        try {
            WorkBibDublinDocBuilder builder = new WorkBibDublinDocBuilder();
            SolrInputDocument sid = builder.buildSolrInputDocument(record);
            LOG.info(sid.toString());
        } catch (Exception e) {
            LOG.error(e.getMessage() , e);
            fail("Failed due to: " + e);

        }

    }

}
