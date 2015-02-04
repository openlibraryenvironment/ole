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
package org.kuali.ole.docstore.model.xstream.work.bib.dublin;

import org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.DCValue;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.WorkBibDublinRecord;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Class Converter for WorkBibDublinRecord.
 *
 * @author Rajesh Chowdary K
 */
public class WorkBibDublinRecordConverter
        implements Converter {
    @Override
    public void marshal(Object o, HierarchicalStreamWriter streamWriter, MarshallingContext marshallingContext) {
        WorkBibDublinRecord record = (WorkBibDublinRecord) o;
        streamWriter.addAttribute("schema", record.getSchema());
        for (DCValue dcValue : record.getDcValues()) {
            streamWriter.startNode("dcvalue");
            marshallingContext.convertAnother(dcValue, new DCValueConverter());
            streamWriter.endNode();
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader streamReader, UnmarshallingContext unmarshallingContext) {
        WorkBibDublinRecord record = new WorkBibDublinRecord();
        record.setSchema(streamReader.getAttribute("schema"));
        while (streamReader.hasMoreChildren()) {
            streamReader.moveDown();
            record.addDublinDCValue(
                    (DCValue) unmarshallingContext.convertAnother(streamReader, DCValue.class, new DCValueConverter()));
            streamReader.moveUp();
        }
        return record;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(WorkBibDublinRecord.class);
    }
}
