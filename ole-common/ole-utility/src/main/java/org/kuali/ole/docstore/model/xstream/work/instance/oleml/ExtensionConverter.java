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
package org.kuali.ole.docstore.model.xstream.work.instance.oleml;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.WorkBibMarcRecord;
import org.kuali.ole.docstore.model.xmlpojo.work.instance.oleml.Extension;
import org.kuali.ole.docstore.model.xstream.ingest.AdditionalAttributeConverter;
import org.kuali.ole.docstore.model.xstream.work.bib.marc.WorkBibMarcRecordConverter;

import java.util.ArrayList;

/**
 * Class to ExtensionConverter.
 *
 * @author Rajesh Chowdary K
 * @created Mar 1, 2012
 */
public class ExtensionConverter
        implements Converter {
    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext cxt) {
        AdditionalAttributeConverter addConverter = new AdditionalAttributeConverter();
        WorkBibMarcRecordConverter workBibMarcRecordConverter = new WorkBibMarcRecordConverter();
        Extension ext = (Extension) o;

        if (ext.getDisplayLabel() != null) {
            writer.addAttribute("displayLabel", ext.getDisplayLabel());
        }
        for (Object obj : ext.getContent()) {
            if (obj instanceof AdditionalAttributes) {
                cxt.convertAnother(obj, addConverter);
            } else if (obj instanceof WorkBibMarcRecord) {
                cxt.convertAnother(obj, workBibMarcRecordConverter);
            } else {
                cxt.convertAnother(obj);
            }
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext ctx) {
        AdditionalAttributeConverter addConverter = new AdditionalAttributeConverter();
        AdditionalAttributes additionalAttributes = new AdditionalAttributes();

        WorkBibMarcRecordConverter workBibMarcRecordConverter = new WorkBibMarcRecordConverter();
        WorkBibMarcRecord workBibMarcRecord = new WorkBibMarcRecord();

        Extension ext = new Extension();
        String displayLabel = reader.getAttribute("displayLabel");
        if (displayLabel != null) {
            ext.setDisplayLabel(displayLabel);
        }
        ArrayList<Object> objects = new ArrayList<Object>();
        ext.setContent(objects);
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            if (reader.getNodeName().equalsIgnoreCase("additionalAttributes")) {
                additionalAttributes = (AdditionalAttributes) addConverter.unmarshal(reader, ctx);
                objects.add(additionalAttributes);
            } else if (reader.getNodeName().equalsIgnoreCase("collection")) {
                workBibMarcRecord = (WorkBibMarcRecord) workBibMarcRecordConverter.unmarshal(reader, ctx);
                objects.add(workBibMarcRecord);
            }
            reader.moveUp();
        }
        return ext;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(Extension.class);
    }
}
