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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kuali.rice.krms.impl.repository.mock;

import org.kuali.rice.krms.api.repository.RuleManagementService;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;

/**
 *
 * @author nwright
 */
public class KrmsContextLoader {

    private RuleManagementService ruleManagementService = null;

    public RuleManagementService getRuleManagementService() {
        return ruleManagementService;
    }

    public void setRuleManagementService(RuleManagementService ruleManagementService) {
        this.ruleManagementService = ruleManagementService;
    }
    
    public void loadContext(String id, String namespace, String name, String typeId, String description) {
//        CNTXT_ID	NMSPC_CD	NM	TYP_ID	???? What kind of type	ACTV	VER_NBR	DESC_TXT
        ContextDefinition.Builder bldr = ContextDefinition.Builder.create(namespace, name);
        bldr.setId(id);
        bldr.setActive(true);
        bldr.setTypeId(typeId);
        bldr.setDescription(description);
        this.getRuleManagementService().createContext(bldr.build());
    }

    public void load() {
        loadContext("10000", "KS-SYS", "Course Requirements", "T1004", "Course Requirements");
        loadContext("10001", "KS-SYS", "Program Requirements", "T1004", "Program Requirements");
    }

}
