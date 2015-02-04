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
package org.kuali.ole.docstore.model.xstream.ingest;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kuali.ole.docstore.model.xmlpojo.ingest.LinkInfo;


/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 9/9/11
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class LinkInfoConverter implements Converter {
    @Override
    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter, MarshallingContext marshallingContext) {
        LinkInfo linkInfo = (LinkInfo) o;
        hierarchicalStreamWriter.addAttribute("from", linkInfo.getFrom());
        hierarchicalStreamWriter.addAttribute("to", linkInfo.getTo());
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext) {
        LinkInfo linkInfo = new LinkInfo();
        linkInfo.setFrom(hierarchicalStreamReader.getAttribute("from"));
        linkInfo.setTo(hierarchicalStreamReader.getAttribute("to"));
        return linkInfo;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(LinkInfo.class);
    }
}
