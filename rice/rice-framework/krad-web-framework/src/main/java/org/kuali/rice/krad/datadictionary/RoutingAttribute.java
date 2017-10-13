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
package org.kuali.rice.krad.datadictionary;

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;

/**
 * This is a description of what this class does - mpham don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "routingAttribute-bean")
public class RoutingAttribute extends WorkflowAttributeMetadata {
    private static final long serialVersionUID = -8232868861868863394L;

    private String qualificationAttributeName;

    @BeanTagAttribute(name = "qualificationAttributeName")
    public String getQualificationAttributeName() {
        return this.qualificationAttributeName;
    }

    public void setQualificationAttributeName(String qualificationAttributeName) {
        this.qualificationAttributeName = qualificationAttributeName;
    }
}
