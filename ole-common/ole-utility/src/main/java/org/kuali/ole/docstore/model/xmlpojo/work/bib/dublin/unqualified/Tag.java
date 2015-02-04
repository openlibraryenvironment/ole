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
package org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin.unqualified;

/**
 * Class Tag.
 *
 * @author Rajesh Chowdary K
 */
public class Tag {
    private String name;
    private String value;

    public Tag(String name) {
        this.name = name;
    }

    public Tag(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tag) {
            return name.equals(((Tag) obj).getName());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "<" + name + ", " + value + ">";
    }
}
