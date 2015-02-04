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
package org.kuali.ole.docstore.xstream.work.bib.dublin.unqualified;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.unqualified.WorkBibDublinUnQualifiedRecord;
import org.kuali.ole.docstore.model.xstream.work.bib.dublin.unqualified.WorkBibDublinUnQualifiedRecordProcessor;
import org.kuali.ole.docstore.xstream.BaseTestCase;
import org.kuali.ole.docstore.xstream.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Class to Test WorkBibDublinUnqualifiedRecordProcessor.
 *
 * @author Rajesh Chowdary K
 */
public class WorkBibDublinUnqualifiedRecordProcessor_UT extends BaseTestCase {
    //    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(WorkBibDublinUnqualifiedRecordProcessor_UT.class);
    private static final Logger LOG = LoggerFactory.getLogger(WorkBibDublinUnqualifiedRecordProcessor_UT.class);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testFromXML() {
        try {
            String resFile = "/bib/bib/dublin/unqualified/Bib-Bib-DublinUnQ-Test1.xml";
            URL resource = getClass().getResource(resFile);
            File file = new File(resource.toURI());
            String fileContent;
            fileContent = new FileUtil().readFile(file);
            WorkBibDublinUnQualifiedRecordProcessor processor = new WorkBibDublinUnQualifiedRecordProcessor();
            WorkBibDublinUnQualifiedRecord rec = processor.fromXML(fileContent);
            LOG.info("record-->" + rec);
            String xml = processor.toXml(rec);
            LOG.info("XML-->" + xml);
            LOG.info("record = " + rec);
            assertNotNull(rec);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed Due to: " + e.getMessage());
        }

    }

    @Test
    public void testToXml() {
        try {
            String resFile = "/bib/bib/dublin/unqualified/Bib-Bib-DublinUnQ-Test1.xml";
            URL resource = getClass().getResource(resFile);
            File file = new File(resource.toURI());
            String fileContent;
            fileContent = new FileUtil().readFile(file);
            WorkBibDublinUnQualifiedRecordProcessor processor = new WorkBibDublinUnQualifiedRecordProcessor();
            WorkBibDublinUnQualifiedRecord rec = processor.fromXML(fileContent);
            LOG.info("Output: " + rec);
            assertNotNull(rec);
            String xml = processor.toXml(rec);
            LOG.info(xml);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed Due to: " + e.getMessage());
        }
    }

}
