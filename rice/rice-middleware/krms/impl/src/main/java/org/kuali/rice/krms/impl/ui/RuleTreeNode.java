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
package org.kuali.rice.krms.impl.ui;

import org.kuali.rice.krms.impl.repository.PropositionBo;

import java.io.Serializable;

/**
 * abstract data class for the rule tree {@link Node}s
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class RuleTreeNode implements Serializable {

    private static final long serialVersionUID = 8038174553531544943L;
    public static final String COMPOUND_NODE_TYPE = "ruleTreeNode compoundNode";
    protected PropositionBo proposition;

    public RuleTreeNode(){}
    
    public RuleTreeNode(PropositionBo proposition){
        this.proposition = proposition;
    }
    
    public PropositionBo getProposition() {
        return this.proposition;
    }
    
    public void setProposition(PropositionBo proposition) {
        this.proposition = proposition;
    }

}
