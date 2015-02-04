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
package org.kuali.ole.docstore.xstream.work.bib.dublin;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.QualifiedDublinRecordHandler;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.DCValue;
import org.kuali.ole.docstore.xstream.BaseTestCase;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.WorkBibDublinRecord;
import org.kuali.ole.docstore.xstream.FileUtil;
import org.kuali.ole.docstore.model.xstream.work.bib.dublin.WorkBibDublinRecordProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Class to Test WorkBibDublinRecordProcessor.
 *
 * @author Rajesh Chowdary K
 */
public class WorkBibDublinRecordProcessor_UT extends BaseTestCase {
    //    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(WorkBibDublinRecordProcessor_UT.class);
    private static final Logger LOG = LoggerFactory.getLogger(WorkBibDublinRecordProcessor_UT.class);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testFromXML() {
        try {
            String resFile = "/bib/bib/dublin/Bib-Bib-DublinQ-Test2.xml";
            URL resource = getClass().getResource(resFile);
            File file = new File(resource.toURI());
            String fileContent;
            fileContent = new FileUtil().readFile(file);
            WorkBibDublinRecordProcessor processor = new WorkBibDublinRecordProcessor();
            WorkBibDublinRecord rec = processor.fromXML(fileContent);
            LOG.info("rec content with WorkBibDublinRecordProcessor" + rec);
            DCValue dcValue = new DCValue("element");
            dcValue.setElement("element");
            dcValue.setQualifier("qualifier");
            dcValue.setValue("value");
            DCValue dcValue2 = new DCValue("element");
            dcValue2.setElement("element");
            dcValue2.setQualifier("qualifier");
            dcValue2.setValue("value");
            DCValue dcValue3 = new DCValue("element");
            dcValue3.setElement("elem");
            dcValue3.setQualifier("qual");
            dcValue3.setValue("val");
            List<DCValue> dcValueList = new ArrayList<DCValue>();
            dcValueList.add(dcValue);
            dcValueList.add(dcValue2);
            dcValueList.add(dcValue3);
            rec.setDcValues(dcValueList);
            if (dcValue.equals(dcValue2)) {
                LOG.info("objects are equal");
            }
            if (dcValue.equals(rec)) {
                LOG.info("incompatible objects");
            }
            if (dcValue.equals(dcValue3)) {
                LOG.info("objects are equal");
            }
            QualifiedDublinRecordHandler qualifiedDublinRecordHandler = new QualifiedDublinRecordHandler();
            rec = qualifiedDublinRecordHandler.fromXML(fileContent);
            LOG.info("rec content with QualifiedDublinRecordHandler" + rec);
            assertNotNull(rec);
            assertNotNull(qualifiedDublinRecordHandler.toXml(rec));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed Due to: " + e.getMessage());
        }

    }

    @Test
    public void testToXml() {
        try {
            String resFile = "/bib/bib/dublin/Bib-Bib-DublinQ-Test2.xml";
            URL resource = getClass().getResource(resFile);
            File file = new File(resource.toURI());
            String fileContent;
            fileContent = new FileUtil().readFile(file);
            WorkBibDublinRecordProcessor processor = new WorkBibDublinRecordProcessor();
            WorkBibDublinRecord rec = processor.fromXML(fileContent);
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
