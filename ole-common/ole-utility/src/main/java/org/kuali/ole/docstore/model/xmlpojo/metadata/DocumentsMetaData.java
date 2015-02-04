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
package org.kuali.ole.docstore.model.xmlpojo.metadata;

import org.apache.commons.io.IOUtils;
import org.kuali.ole.docstore.model.xstream.metadata.DocStoreMetaDataXmlProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Class to DocumentsMetaData.
 *
 * @author Rajesh Chowdary K
 * @created Jun 14, 2012
 */
@XStreamAlias("documentsMetaData")
public class DocumentsMetaData {

    public static Logger logger = LoggerFactory.getLogger(DocumentsMetaData.class);
    public static DocumentsMetaData docStoreMetaData = null;

    @XStreamImplicit
    @XStreamAlias("documentMetaData")
    private List<DocumentMetaData> documentsMetaData = null;

    public List<DocumentMetaData> getDocumentsMetaData() {
        return documentsMetaData;
    }

    public void setDocumentsMetaData(List<DocumentMetaData> documentsMetaData) {
        this.documentsMetaData = documentsMetaData;
    }

    public DocumentMetaData getDocumentMetaData(String category, String type, String format) {
        if (documentsMetaData != null && documentsMetaData.size() > 0)
            for (DocumentMetaData docMetaData : documentsMetaData)
                if (docMetaData.getCategory().equals(category) && docMetaData.getType().equals(type) && docMetaData.getFormat().equals(format))
                    return docMetaData;
        return null;
    }

    public static DocumentsMetaData getInstance() {
        if (docStoreMetaData == null) {
            try {
                DocStoreMetaDataXmlProcessor parser = new DocStoreMetaDataXmlProcessor();
                String xml = IOUtils.toString(DocumentsMetaData.class.getResourceAsStream("/org/kuali/ole/docstore/documentsMetaData.xml"));
                docStoreMetaData = parser.fromXml(xml);
                logger.info("Loaded Doc Store Meta Data Successfully.");
            } catch (Exception e) {
                logger.error("Failed in Loading Doc Store Meta Data : Cause : " + e.getMessage(), e);
            }
        }
        return docStoreMetaData;
    }

}
