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
package org.kuali.rice.kew.rule.xmlrouting;

import java.util.Map;

import org.kuali.rice.kew.rule.WorkflowRuleAttribute;
import org.kuali.rice.kew.rule.XmlConfiguredAttribute;


/**
 * A {@link WorkflowRuleAttribute} which is configured via an XML definition.
 * Since it has no specific getters and setters for it's various
 * properties, it uses a Map of parameters to represent it's
 * attribute properties.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface GenericXMLRuleAttribute extends WorkflowRuleAttribute, XmlConfiguredAttribute {

	public void setParamMap(Map paramMap);
	public Map getParamMap();
}
