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
package org.kuali.rice.krms.impl.repository;

import java.util.Comparator;
import org.kuali.rice.krms.api.repository.proposition.PropositionDefinition;
import org.kuali.rice.krms.api.repository.typerelation.TypeTypeRelation;

/**
 *
 * @author nwright
 */
public class CompoundPropositionComparator implements Comparator<PropositionDefinition> {
    
    @Override
    public int compare(PropositionDefinition o1, PropositionDefinition o2) {
        Integer seq1 = buildKey (o1);
        Integer seq2 = buildKey (o2);
        return seq1.compareTo(seq2);
    }
    
    private static final Integer ZERO = new Integer (0);
    private Integer buildKey (PropositionDefinition prop) {
        if (prop.getCompoundSequenceNumber() != null) {
            return prop.getCompoundSequenceNumber ();
        }
        return ZERO;
    }
}
