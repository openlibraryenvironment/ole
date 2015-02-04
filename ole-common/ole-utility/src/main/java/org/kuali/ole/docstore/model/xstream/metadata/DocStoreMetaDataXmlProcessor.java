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
package org.kuali.ole.docstore.model.xstream.metadata;

import org.kuali.ole.docstore.model.xmlpojo.metadata.DocumentsMetaData;
import org.kuali.ole.docstore.model.xmlpojo.metadata.Field;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.io.File;

/**
 * Class DocStoreMetaDataXmlProcessor.
 *
 * @author Rajesh Chowdary K
 * @created Jun 14, 2012
 */
public class DocStoreMetaDataXmlProcessor {

    /**
     * Method to get Objects from Xml.
     *
     * @param xml
     * @return
     */
    public DocumentsMetaData fromXml(String xml) {
        XStream xStream = new XStream();
        xStream.processAnnotations(DocumentsMetaData.class);
        xStream.registerConverter(new FieldConverter());
        return (DocumentsMetaData) xStream.fromXML(xml);

    }

}

/**
 * Class FieldConverter.
 *
 * @author Rajesh Chowdary K
 * @created Jun 14, 2012
 */
@XStreamConverter(FieldConverter.class)
class FieldConverter implements Converter {

    @Override
    public boolean canConvert(Class type) {
        return Field.class.equals(type);
    }

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
    }

    @Override
    public Field unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Field field = new Field();
        field.setName(reader.getAttribute("name"));
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            field.getProperties().setProperty(reader.getNodeName(), reader.getValue());
            reader.moveUp();
        }

        return field;
    }

}
