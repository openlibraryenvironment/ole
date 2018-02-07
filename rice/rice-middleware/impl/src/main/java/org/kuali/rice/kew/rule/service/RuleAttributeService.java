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
package org.kuali.rice.kew.rule.service;

import java.util.List;

import org.kuali.rice.core.framework.impex.xml.XmlExporter;
import org.kuali.rice.core.framework.impex.xml.XmlLoader;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.rule.bo.RuleAttribute;
import org.springframework.cache.annotation.CacheEvict;

/**
 * A service which provides data access for {@link org.kuali.rice.kew.rule.bo.RuleAttribute}s.
 * 
 * @see org.kuali.rice.kew.rule.bo.RuleAttribute
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface RuleAttributeService extends XmlLoader, XmlExporter {

    @CacheEvict(value={ExtensionDefinition.Cache.NAME}, allEntries = true)
    public void save(RuleAttribute ruleAttribute);

    @CacheEvict(value={ExtensionDefinition.Cache.NAME}, allEntries = true)
    public void delete(String ruleAttributeId);
    public List<RuleAttribute> findByRuleAttribute(RuleAttribute ruleAttribute);
    public RuleAttribute findByRuleAttributeId(String ruleAttributeId);
    public List<RuleAttribute> findAll();
    public RuleAttribute findByName(String name);
    public List<RuleAttribute> findByClassName(String className);

    public Object loadRuleAttributeService(RuleAttribute ruleAttribute);
    
    public Object loadRuleAttributeService(RuleAttribute ruleAttribute, String defaultApplicationId);
    
}
