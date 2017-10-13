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
package org.kuali.rice.core.impl.config.property;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Param", namespace = "http://rice.kuali.org/core/impl/config",  propOrder = {
    "value"
})
public class Param {
    
    @XmlAttribute(required = true)
    protected String name;
    
    @XmlAttribute
    protected Boolean override;
    
    @XmlAttribute
    protected Boolean random;
    
    @XmlAttribute
    protected Boolean system;
    
    @XmlValue
    protected String value;
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isOverride() {
        if (override == null) {
            return true;
        } else {
            return override;
        }
    }

    public void setOverride(Boolean override) {
        this.override = override;
    }
    
    public Boolean isRandom() {
        if (random == null) {
            return false;
        } else {
            return random;
        }
    }

    public void setRandom(Boolean random) {
        this.random = random;
    }
    
    public Boolean isSystem() {
        if (system == null) {
            return false;
        } else {
            return system;
        }
    }

    public void setSystem(Boolean system) {
        this.system = system;
    }
    
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
