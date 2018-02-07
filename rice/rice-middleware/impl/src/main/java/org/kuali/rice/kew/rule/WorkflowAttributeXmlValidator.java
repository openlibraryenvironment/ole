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
package org.kuali.rice.kew.rule;

import org.kuali.rice.core.api.uif.RemotableAttributeErrorContract;

import java.util.List;



/**
 * An interface which can be implemented by a {@link WorkflowRuleAttribute} to allow for
 * validation of client routing data.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface WorkflowAttributeXmlValidator {

    /**
     * called after client has set properties on the attribute for 'xmlizing'.  Returns 
     * @return List<? extends AttributeError> objects
     */
    public List<? extends RemotableAttributeErrorContract> validateClientRoutingData();
    
}
