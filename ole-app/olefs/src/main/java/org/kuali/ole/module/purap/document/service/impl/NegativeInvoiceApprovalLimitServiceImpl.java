/*
 * Copyright 2007 The Kuali Foundation
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
package org.kuali.ole.module.purap.document.service.impl;

import org.kuali.ole.module.purap.businessobject.NegativeInvoiceApprovalLimit;
import org.kuali.ole.module.purap.document.dataaccess.NegativeInvoiceApprovalLimitDao;
import org.kuali.ole.module.purap.document.service.NegativeInvoiceApprovalLimitService;
import org.kuali.ole.sys.service.NonTransactional;
import org.kuali.rice.core.api.util.type.KualiDecimal;

import java.util.Collection;

@NonTransactional
public class NegativeInvoiceApprovalLimitServiceImpl implements NegativeInvoiceApprovalLimitService {

    private NegativeInvoiceApprovalLimitDao dao;

    public void setNegativeInvoiceApprovalLimitDao(NegativeInvoiceApprovalLimitDao dao) {
        this.dao = dao;
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.NegativeInvoiceApprovalLimitService#findByChart(String)
     */
    public Collection<NegativeInvoiceApprovalLimit> findByChart(String chartCode) {
        return dao.findByChart(chartCode);
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.NegativeInvoiceApprovalLimitService#findByChartAndAccount(String,
     *      String)
     */
    public Collection<NegativeInvoiceApprovalLimit> findByChartAndAccount(String chartCode, String accountNumber) {
        return dao.findByChartAndAccount(chartCode, accountNumber);
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.NegativeInvoiceApprovalLimitService#findByChartAndOrganization(String,
     *      String)
     */
    public Collection<NegativeInvoiceApprovalLimit> findByChartAndOrganization(String chartCode, String organizationCode) {
        return dao.findByChartAndOrganization(chartCode, organizationCode);
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.NegativeInvoiceApprovalLimitService#findAboveLimit(org.kuali.rice.core.api.util.type.KualiDecimal)
     */
    public Collection<NegativeInvoiceApprovalLimit> findAboveLimit(KualiDecimal limit) {
        return dao.findAboveLimit(limit);
    }

    /**
     * @see org.kuali.ole.module.purap.document.service.NegativeInvoiceApprovalLimitService#findBelowLimit(org.kuali.rice.core.api.util.type.KualiDecimal)
     */
    public Collection<NegativeInvoiceApprovalLimit> findBelowLimit(KualiDecimal limit) {
        return dao.findBelowLimit(limit);
    }

}
