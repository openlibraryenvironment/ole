/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.krad.uif.util;

import org.kuali.rice.core.api.util.AbstractKeyValue;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.parse.BeanTags;

/**
 * KRAD version of KeyValue to enable use in schema
 */
@BeanTags({@BeanTag(name = "keyValuePair-bean", parent = "Uif-KeyLabelPair"),
        @BeanTag(name = "keyLabelPair-bean", parent = "Uif-KeyLabelPair")})
public class UifKeyValue extends AbstractKeyValue {
    private static final long serialVersionUID = 1176799455504861488L;

    public UifKeyValue() {
        super();
    }

    public UifKeyValue(String key, String value) {
        super(key, value);
    }

    @Override
    @BeanTagAttribute(name = "key")
    public String getKey() {
        return super.getKey();
    }

    @Override
    @BeanTagAttribute(name = "value")
    public String getValue() {
        return super.getValue();
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
