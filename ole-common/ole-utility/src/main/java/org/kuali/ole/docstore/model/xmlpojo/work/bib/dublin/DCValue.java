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

/**
 * Package for Dublin Format documents handling.
 *
 */
package org.kuali.ole.docstore.model.xmlpojo.work.bib.dublin;


/**
 * Class to represent data entity DC Value of Work Bib Dublin Core Document.
 *
 * @author Rajesh Chowdary K
 */
public class DCValue {
    private String element = null;
    private String qualifier = null;
    private String value = null;

    public DCValue() {
    }

    public DCValue(String element) {
        this.element = element;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "element=" + element + ", qualifier=" + qualifier + ", value=" + value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DCValue) {
            DCValue dcValue = (DCValue) obj;
            if (dcValue.getElement().equals(this.getElement())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
