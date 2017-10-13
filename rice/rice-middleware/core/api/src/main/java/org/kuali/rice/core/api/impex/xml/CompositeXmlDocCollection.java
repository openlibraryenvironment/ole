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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


/**
 * In someway allows muliple xml documents to be pushed into the xml loading 
 * 'pipeline'.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CompositeXmlDocCollection implements XmlDocCollection {
    protected Collection<XmlDocCollection> collections;
    public CompositeXmlDocCollection(Collection<XmlDocCollection> xmlDocCollections) {
        collections = xmlDocCollections;
    }

    public File getFile() {
        return null;
    }

    public List<? extends XmlDoc> getXmlDocs() {
        List<XmlDoc> docs = new LinkedList<XmlDoc>();
        for (XmlDocCollection collection : collections)
        {
            docs.addAll(collection.getXmlDocs());
        }
        return docs;
    }

    public void close() throws IOException {
        for (XmlDocCollection collection : collections)
        {
            collection.close();
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("[CompositeXmlDocCollection: ");
        for (XmlDocCollection collection : collections)
        {
            sb.append(collection.toString()).append(", ");
        }
        if (collections.size() > 0) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("]");
        return sb.toString();
    }
}
