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
package org.kuali.ole.select.service.impl;

import org.kuali.batchingest.ProcessVendorFile;
import org.kuali.ole.select.service.OleBatchIngestService;
import org.kuali.ole.sys.context.SpringContext;

import java.io.InputStream;
import java.util.List;


public class OleBatchIngestServiceImpl implements OleBatchIngestService {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleBatchIngestServiceImpl.class);


    public String transformRawDataToXml(InputStream inputStream) throws Exception {
        String xml = SpringContext.getBean(ProcessVendorFile.class).transformStreamToRawData(inputStream);
        if (LOG.isDebugEnabled()) {
            LOG.debug("-----------Ole Format Xml Begins------------");
            LOG.debug(xml);
            LOG.debug("-----------Ole Format Xml Ends------------");
        }
        return xml;
    }

    public String getRawXml(InputStream inputStream, List bibinfoFailure) throws Exception {
        String rawData = SpringContext.getBean(ProcessVendorFile.class).getRawXml(inputStream, bibinfoFailure);
        if (LOG.isDebugEnabled()) {
            LOG.debug("-----------Raw Data Begins------------");
            LOG.debug(rawData);
            LOG.debug("-----------Raw Data Ends------------");
        }
        return rawData;
    }

}
