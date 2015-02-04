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
package org.kuali.ole.docstore.model.xstream.work.bib.dublin.unqualified;

import org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.unqualified.Header;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.unqualified.Tag;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Converter for Record.
 *
 * @author Rajesh Chowdary K
 */
public class HeaderConverter implements Converter {
    @Override
    public void marshal(Object o, HierarchicalStreamWriter streamWriter, MarshallingContext marshallingContext) {
        Header header = (Header) o;
        for (Tag tag : header.getAllTags()) {
            streamWriter.startNode(tag.getName());
            streamWriter.setValue(tag.getValue());
            streamWriter.endNode();
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader streamReader, UnmarshallingContext unmarshallingContext) {
        Header header = new Header();
        while (streamReader.hasMoreChildren()) {
            streamReader.moveDown();
            header.put(streamReader.getNodeName(), streamReader.getValue());
            streamReader.moveUp();
        }
        return header;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(Header.class);
    }
}
