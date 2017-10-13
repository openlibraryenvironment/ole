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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "config", namespace = "http://rice.kuali.org/core/impl/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Config", namespace = "http://rice.kuali.org/core/impl/config", propOrder = {
    "paramList"
})
public class Config {
    
    @XmlElement(name="param", namespace = "http://rice.kuali.org/core/impl/config")
    private List<Param> paramList;

    public List<Param> getParamList() {
        if(paramList == null) {
            paramList = new ArrayList<Param>();
        }
        return paramList;
    }

}
