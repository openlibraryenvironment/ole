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

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * For uploading zip files full of xml goodness.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ZipXmlDocCollection extends BaseXmlDocCollection {
    private ZipFile zipFile;
    public ZipXmlDocCollection(File file) throws IOException {
        super(file);
        zipFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> e = zipFile.entries();
        while (e.hasMoreElements()) {
            ZipEntry zipEntry = e.nextElement();
            if (!zipEntry.isDirectory() && zipEntry.getName().toLowerCase().endsWith(".xml")) {
                xmlDocs.add(new ZipXmlDoc(zipFile, zipEntry, this));
            }
        }
    }
    public void close() throws IOException {
        zipFile.close();
    }
}
