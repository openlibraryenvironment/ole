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
package org.kuali.ole.docstore.common.document.content.bib.dc.xstream;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kuali.ole.docstore.common.document.content.bib.dc.DCValue;

/**
 * Converter for DCValue.
 *
 * @author Rajesh Chowdary K
 */
public class DCValueConverter
        implements Converter {
    @Override
    public void marshal(Object o, HierarchicalStreamWriter streamWriter, MarshallingContext marshallingContext) {
        DCValue dcValue = (DCValue) o;
        streamWriter.addAttribute("element", dcValue.getElement());
        streamWriter.addAttribute("qualifier", dcValue.getQualifier());
        streamWriter.setValue(dcValue.getValue());
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader streamReader, UnmarshallingContext unmarshallingContext) {
        DCValue dcValue = new DCValue();
        dcValue.setElement(streamReader.getAttribute("element"));
        dcValue.setQualifier(streamReader.getAttribute("qualifier"));
        dcValue.setValue(streamReader.getValue());
        return dcValue;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(DCValue.class);
    }
}
