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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * An XmlDoc implementation backed by a physical XML File
 * @see org.kuali.rice.core.api.impex.xml.batch.XmlDoc
 * @see org.kuali.rice.core.api.impex.xml.impl.impex.BaseXmlDoc
 * @see org.kuali.rice.core.api.impex.xml.FileXmlDocCollection
 * @see org.kuali.rice.core.impl.impex.DirectoryXmlDocCollection
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class FileXmlDoc extends BaseXmlDoc {
    private File file;
    private String name;
    public FileXmlDoc(File file, XmlDocCollection collection) {
        this(file, file.getName(), collection);
        this.file = file;
    }
    public FileXmlDoc(File file, String name, XmlDocCollection collection) {
        super(collection);
        this.file = file;
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public InputStream getStream() throws IOException {
        return new FileInputStream(file);
    }
    public int hashCode() {
        return file.hashCode();
    }
    public boolean equals(Object o) {
        if (!(o instanceof FileXmlDoc)) return false;
        return file.equals(((FileXmlDoc) o).file);
    }
}
