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
import org.kuali.rice.krms.api.repository.typerelation.TypeTypeRelation;

/**
 *
 * @author nwright
 */
public class TypeTypeRelationSequenceComparator implements Comparator<TypeTypeRelation> {

    @Override
    public int compare(TypeTypeRelation o1, TypeTypeRelation o2) {
        Integer seq1 = o1.getSequenceNumber();
        if (seq1 == null) {
            seq1 = 0;
        }
        Integer seq2 = o2.getSequenceNumber();
        if (seq2 == null) {
            seq2 = 0;
        }
        return seq1.compareTo(seq2);
    }
}
