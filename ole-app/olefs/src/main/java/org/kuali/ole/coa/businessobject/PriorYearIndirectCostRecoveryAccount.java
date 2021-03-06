/*
 * Copyright 2011 The Kuali Foundation
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

package org.kuali.ole.coa.businessobject;

import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

/**
 * IndirectCostRecoveryAccount for A21SubAccount
 */
public class PriorYearIndirectCostRecoveryAccount extends IndirectCostRecoveryAccount {
    private static Logger LOG = Logger.getLogger(PriorYearIndirectCostRecoveryAccount.class);

    private Integer priorYearIndirectCostRecoveryAccountGeneratedIdentifier;
    
    /**
     * Default constructor.
     */
    public PriorYearIndirectCostRecoveryAccount() {
    }
    
    public PriorYearIndirectCostRecoveryAccount(IndirectCostRecoveryAccount icr) {
        BeanUtils.copyProperties(icr,this);
    }

    public Integer getPriorYearIndirectCostRecoveryAccountGeneratedIdentifier() {
        return priorYearIndirectCostRecoveryAccountGeneratedIdentifier;
    }

    public void setPriorYearIndirectCostRecoveryAccountGeneratedIdentifier(Integer priorYearIndirectCostRecoveryAccountGeneratedIdentifier) {
        this.priorYearIndirectCostRecoveryAccountGeneratedIdentifier = priorYearIndirectCostRecoveryAccountGeneratedIdentifier;
    }

    /**
     * @see org.kuali.rice.krad.bo.BusinessObjectBase#toStringMapper()
     */
    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        LinkedHashMap<String, String> m = new LinkedHashMap<String, String>();
        if (this.priorYearIndirectCostRecoveryAccountGeneratedIdentifier != null) {
            m.put("priorYearIndirectCostRecoveryAccountGeneratedIdentifier", this.priorYearIndirectCostRecoveryAccountGeneratedIdentifier.toString());
        }
        return m;
    }

}
