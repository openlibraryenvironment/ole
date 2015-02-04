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

/**
 * Package for Dublin Format documents handling.
 *
 */
package org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.unqualified;

import com.thoughtworks.xstream.annotations.XStreamInclude;

/**
 * Class to represent data entity DC Value of Work Bib Dublin Core Document.
 *
 * @author Rajesh Chowdary K
 */
@XStreamInclude(value = {MetaData.class})
public class Record {

    private MetaData metadata;

    private Header header;

    /**
     * @return the header
     */
    public Header getHeader() {
        return header;
    }

    /**
     * @param header the header to set
     */
    public void setHeader(Header header) {
        this.header = header;
    }

    /**
     * @return the metadata
     */
    public MetaData getMetadata() {
        return metadata;
    }

    /**
     * @param metadata the metadata to set
     */
    public void setMetadata(MetaData metaData) {
        this.metadata = metaData;
    }

    @Override
    public String toString() {
        return "Record[ Header: " + header + ", MetaData: " + metadata + "]";
    }
}
