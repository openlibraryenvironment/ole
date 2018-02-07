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

import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krms.api.repository.proposition.PropositionParameterType;
import org.kuali.rice.krms.api.repository.proposition.PropositionType;
import org.kuali.rice.krms.impl.repository.PropositionBo;
import org.kuali.rice.krms.impl.repository.PropositionParameterBo;
import org.kuali.rice.krms.impl.repository.TermBo;

import java.util.List;

/**
 * abstract data class for the rule tree {@link Node}s
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class CompoundPropositionEditNode extends RuleTreeNode {

    private static final long serialVersionUID = -5650654824214437325L;

    public static final String NODE_TYPE = "ruleTreeNode compoundPropositionEditNode";

    public CompoundPropositionEditNode(PropositionBo proposition){
        super(proposition);
    }
    

}
