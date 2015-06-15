package org.kuali.ole.select.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.coa.businessobject.OleFundCodeAccountingLine;
import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;

import java.util.List;

/**
 * Created by angelind on 6/12/15.
 */
public class OLESelectDaoOjb extends PlatformAwareDaoBaseOjb {
    private static final Logger LOG = Logger.getLogger(OLESelectDaoOjb.class);

    public List<OleFundCodeAccountingLine> getFundCodeList(PurApAccountingLine purApAccountingLine) {
        String dbVendor = ConfigContext.getCurrentContextConfig().getProperty("db.vendor");
        Criteria criteria = new Criteria();
        criteria.addEqualTo(OLEConstants.CHART_CODE, purApAccountingLine.getChartOfAccountsCode());
        criteria.addEqualTo(OLEConstants.ACCOUNT_NUMBER, purApAccountingLine.getAccountNumber());
        criteria.addEqualTo(OLEConstants.OBJECT_CODE, purApAccountingLine.getFinancialObjectCode());
        criteria.addEqualTo(OLEConstants.ACCOUNT_LINE_PERCENT, purApAccountingLine.getAccountLinePercent());
        if(StringUtils.isNotBlank(purApAccountingLine.getSubAccountNumber())){
            criteria.addEqualTo(OLEConstants.SUB_ACCOUNT, purApAccountingLine.getSubAccountNumber());
        } else {
            if(dbVendor.equalsIgnoreCase("oracle")){
                criteria.addIsNull(OLEConstants.SUB_ACCOUNT);
            }else{
                criteria.addEqualTo(OLEConstants.SUB_ACCOUNT, "");
            }
        }
        if(StringUtils.isNotBlank(purApAccountingLine.getFinancialSubObjectCode())){
            criteria.addEqualTo(OLEConstants.SUB_OBJECT, purApAccountingLine.getFinancialSubObjectCode());
        } else {
            if(dbVendor.equalsIgnoreCase("oracle")){
                criteria.addIsNull(OLEConstants.SUB_OBJECT);
            } else {
                criteria.addEqualTo(OLEConstants.SUB_OBJECT, "");
            }
        }
        if(StringUtils.isNotBlank(purApAccountingLine.getProjectCode())){
            criteria.addEqualTo(OLEConstants.PROJECT, purApAccountingLine.getProjectCode());
        } else {
            if(dbVendor.equalsIgnoreCase("oracle")){
                criteria.addIsNull(OLEConstants.PROJECT);
            } else {
                criteria.addEqualTo(OLEConstants.PROJECT,"");
            }
        }
        if(StringUtils.isNotBlank(purApAccountingLine.getOrganizationReferenceId())){
            criteria.addEqualTo(OLEConstants.ORG_REF_ID,purApAccountingLine.getOrganizationReferenceId());
        } else {
            if(dbVendor.equalsIgnoreCase("oracle")){
                criteria.addIsNull(OLEConstants.ORG_REF_ID);
            } else {
                criteria.addEqualTo(OLEConstants.ORG_REF_ID, "");
            }
        }
        QueryByCriteria query = QueryFactory.newQuery(OleFundCodeAccountingLine.class, criteria);
        return (List<OleFundCodeAccountingLine>)getPersistenceBrokerTemplate().getCollectionByQuery
                (query);
    }

}
