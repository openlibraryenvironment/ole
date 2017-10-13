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
package org.kuali.rice.krms.impl.type;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;
import org.kuali.rice.krms.framework.engine.BasicRule;
import org.kuali.rice.krms.framework.engine.Rule;
import org.kuali.rice.krms.framework.type.RuleTypeService;
import org.kuali.rice.krms.impl.provider.repository.RepositoryToEngineTranslator;
import org.kuali.rice.krms.impl.repository.KrmsRepositoryServiceLocator;

/**
 * Base class for {@link org.kuali.rice.krms.framework.type.RuleTypeService} implementations, providing
 * boilerplate for attribute building and merging from various sources.
 */
public class RuleTypeServiceBase extends KrmsTypeServiceBase implements RuleTypeService {

    public static final RuleTypeService defaultRuleTypeService = new RuleTypeServiceBase();

    private RepositoryToEngineTranslator translator;

    @Override
    public Rule loadRule(RuleDefinition ruleDefinition) {
            if (ruleDefinition == null) { throw new RiceIllegalArgumentException("ruleDefinition must not be null"); }
            if (ruleDefinition.getAttributes() == null) { throw new RiceIllegalArgumentException("ruleDefinition must not be null");}

        return new BasicRule(ruleDefinition.getName(),
                getTranslator().translatePropositionDefinition(ruleDefinition.getProposition()),
                getTranslator().translateActionDefinitions(ruleDefinition.getActions()));
    }

    public RepositoryToEngineTranslator getTranslator() {
        if (translator == null) {
            translator = KrmsRepositoryServiceLocator.getKrmsRepositoryToEngineTranslator();
        }
        return translator;
    }

    public void setTranslator(RepositoryToEngineTranslator translator) {
        this.translator = translator;
    }
}
