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

import java.util.Properties;

/**
 * Class Field to represent entity Field.
 *
 * @author Rajesh Chowdary K
 * @created Jun 14, 2012
 */
@XStreamAlias("field")
public class Field {

    @XStreamAsAttribute
    private String name;
    private Properties properties = new Properties();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void set(String property, String value) {
        properties.put(property, value);
    }

    public String get(String property) {
        return properties.getProperty(property);
    }

    public Properties getProperties() {
        return properties;
    }

}
