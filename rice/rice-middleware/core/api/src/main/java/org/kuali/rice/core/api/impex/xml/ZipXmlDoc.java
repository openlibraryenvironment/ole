/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.core.api.impex.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * An XmlDoc implementation backed by a ZipEntry in a ZipFile
 * @see org.kuali.rice.core.api.impex.xml.batch.XmlDoc
 * @see org.kuali.rice.core.api.impex.xml.impl.impex.BaseXmlDoc
 * @see org.kuali.rice.core.api.impex.xml.ZipXmlDocCollection
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
class ZipXmlDoc extends BaseXmlDoc {
    private ZipFile zipFile;
    private ZipEntry zipEntry;
    public ZipXmlDoc(ZipFile zipFile, ZipEntry zipEntry, XmlDocCollection collection) {
        super(collection);
        this.zipFile = zipFile;
        this.zipEntry = zipEntry;
    }
    public String getName() {
        return zipEntry.getName();
    }
    public InputStream getStream() throws IOException {
        return zipFile.getInputStream(zipEntry);
    }
    public int hashCode() {
        return zipEntry.hashCode();
    }
    public boolean equals(Object o) {
        if (!(o instanceof ZipXmlDoc)) return false;
        return zipEntry.equals(((ZipXmlDoc) o).zipEntry);
    }
}
