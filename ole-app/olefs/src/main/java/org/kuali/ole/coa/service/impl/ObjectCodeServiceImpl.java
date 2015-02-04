/*
 * Copyright 2005-2006 The Kuali Foundation
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
package org.kuali.ole.coa.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.coa.businessobject.ObjectCode;
import org.kuali.ole.coa.dataaccess.ObjectCodeDao;
import org.kuali.ole.coa.service.ObjectCodeService;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.NonTransactional;
import org.kuali.ole.sys.service.UniversityDateService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.springframework.cache.annotation.Cacheable;

/**
 * This class is the service implementation for the ObjectCode structure. This is the default implementation, that is delivered with
 * Kuali.
 */

@NonTransactional
public class ObjectCodeServiceImpl implements ObjectCodeService {

    protected ObjectCodeDao objectCodeDao;
    protected UniversityDateService universityDateService;

    /**
     * @see org.kuali.ole.coa.service.ObjectCodeService#getByPrimaryId(java.lang.Integer, java.lang.String, java.lang.String)
     */
    @Override
    @Cacheable(value=ObjectCode.CACHE_NAME, key="#p0+'-'+#p1+'-'+#p2")
    public ObjectCode getByPrimaryId(Integer universityFiscalYear, String chartOfAccountsCode, String financialObjectCode) {
        Map<String, Object> keys = new HashMap<String, Object>(3);
        keys.put(OLEPropertyConstants.UNIVERSITY_FISCAL_YEAR, universityFiscalYear);
        keys.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartOfAccountsCode);
        keys.put(OLEPropertyConstants.FINANCIAL_OBJECT_CODE, financialObjectCode);
        return SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(ObjectCode.class, keys);
    }

    /**
     * @see org.kuali.ole.coa.service.ObjectCodeService#getByPrimaryIdWithCaching(java.lang.Integer, java.lang.String,
     *      java.lang.String)
     */
    @Override
    @Cacheable(value=ObjectCode.CACHE_NAME, key="#p0+'-'+#p1+'-'+#p2")
    public ObjectCode getByPrimaryIdWithCaching(Integer universityFiscalYear, String chartOfAccountsCode, String financialObjectCode) {
        ObjectCode objectCode = getByPrimaryId(universityFiscalYear, chartOfAccountsCode, financialObjectCode);
        return objectCode;
    }

    public void setObjectCodeDao(ObjectCodeDao objectCodeDao) {
        this.objectCodeDao = objectCodeDao;
    }
    public void setUniversityDateService(UniversityDateService universityDateService) {
        this.universityDateService = universityDateService;
    }

    /**
     * @see org.kuali.ole.coa.service.ObjectCodeService#getYearList(java.lang.String, java.lang.String)
     */
    @Override
    public List getYearList(String chartOfAccountsCode, String financialObjectCode) {
        return objectCodeDao.getYearList(chartOfAccountsCode, financialObjectCode);
    }

    /**
     * @see org.kuali.ole.coa.service.ObjectCodeService#getObjectCodeNamesByCharts(java.lang.Integer, java.lang.String[],
     *      java.lang.String)
     */
    @Override
    public String getObjectCodeNamesByCharts(Integer universityFiscalYear, String[] chartOfAccountCodes, String financialObjectCode) {
        String onlyObjectCodeName = "";
        SortedSet<String> objectCodeNames = new TreeSet<String>();
        List<String> objectCodeNameList = new ArrayList<String>();
        for (String chartOfAccountsCode : chartOfAccountCodes) {
            ObjectCode objCode = this.getByPrimaryId(universityFiscalYear, chartOfAccountsCode, financialObjectCode);
            if (objCode != null) {
                onlyObjectCodeName = objCode.getFinancialObjectCodeName();
                objectCodeNames.add(objCode.getFinancialObjectCodeName());
                objectCodeNameList.add(chartOfAccountsCode + ": " + objCode.getFinancialObjectCodeName());
            }
            else {
                onlyObjectCodeName = "Not Found";
                objectCodeNameList.add(chartOfAccountsCode + ": Not Found");
            }
        }
        if (objectCodeNames.size() > 1) {
            return StringUtils.join(objectCodeNames.toArray(), ", ");
        }
        else {
            return onlyObjectCodeName;
        }
    }

    /**
     * @see org.kuali.ole.coa.service.ObjectCodeService#getByPrimaryIdForCurrentYear(java.lang.String, java.lang.String)
     */
    @Override
    @Cacheable(value=ObjectCode.CACHE_NAME, key="'CurrentFY'+'-'+#p0+'-'+#p1")
    public ObjectCode getByPrimaryIdForCurrentYear(String chartOfAccountsCode, String financialObjectCode) {
        return getByPrimaryId(universityDateService.getCurrentFiscalYear(), chartOfAccountsCode, financialObjectCode);
    }
}
