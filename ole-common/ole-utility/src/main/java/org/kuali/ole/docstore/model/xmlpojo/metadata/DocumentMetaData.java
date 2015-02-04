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
package org.kuali.ole.docstore.model.xmlpojo.metadata;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

/**
 * Class DocumentMetaData to represent entity of type DocumentMetaData.
 *
 * @author Rajesh Chowdary K
 * @created Jun 14, 2012
 */
@XStreamAlias("document")
public class DocumentMetaData {

    @XStreamAsAttribute
    private String category;
    @XStreamAsAttribute
    private String type;
    @XStreamAsAttribute
    private String format;

    @XStreamImplicit
    @XStreamAlias("fields")
    private List<Field> fields = null;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public List<Field> getFields() {
        if (fields == null)
            fields = new ArrayList<Field>();
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

}
