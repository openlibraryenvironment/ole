package org.kuali.ole.service;

import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.kuali.ole.select.gokb.OleGokbView;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;

import java.util.List;

/**
 * Created by sambasivam on 23/12/14.
 */
public class OLEGOKBSearchDaoOjb extends PlatformAwareDaoBaseOjb {

    public List<OleGokbView> packageSearch(String packageName, String platformName, List<String> platformProviders, String title, List<String> issnList, String titleInstanceType, List<String> platformStatusList, List<String> packageStatusList, List<String> tippStatusList) {

        Criteria searchCriteria = new Criteria();
        boolean addSearch = false;
        if(StringUtils.isNotEmpty(packageName)) {
            Criteria packageCriteria = new Criteria();
            packageCriteria.addColumnEqualTo("PKG_NAME", packageName);
            searchCriteria.addAndCriteria(packageCriteria);
            addSearch = true;
        }

        if(StringUtils.isNotEmpty(platformName)) {
            Criteria platformCriteria = new Criteria();
            platformCriteria.addColumnEqualTo("PLTFRM_NAME", platformName);
            searchCriteria.addAndCriteria(platformCriteria);
            addSearch = true;

        }

        if(platformProviders != null && platformProviders.size() > 0) {
            Criteria platformProviderCriteria = new Criteria();
            platformProviderCriteria.addColumnIn("ORG_NAME", platformProviders);
            searchCriteria.addAndCriteria(platformProviderCriteria);
            addSearch = true;

        }

        if(packageStatusList != null && packageStatusList.size() > 0) {
            Criteria packageStatusCriteria = new Criteria();
            packageStatusCriteria.addColumnIn("PKG_STATUS", packageStatusList);
            searchCriteria.addAndCriteria(packageStatusCriteria);
            addSearch = true;
        }

        if(platformStatusList != null && platformStatusList.size() > 0) {
            Criteria platformStatusCriteria = new Criteria();
            platformStatusCriteria.addColumnIn("PLTFRM_STATUS", packageStatusList);
            searchCriteria.addAndCriteria(platformStatusCriteria);
            addSearch = true;
        }

        if(tippStatusList != null && tippStatusList.size() > 0) {
            Criteria tippStatusCriteria = new Criteria();
            tippStatusCriteria.addColumnIn("TIPP_STATUS", packageStatusList);
            searchCriteria.addAndCriteria(tippStatusCriteria);
            addSearch = true;

        }

        if(StringUtils.isNotEmpty(title)) {
            Criteria titleCriteria = new Criteria();
            titleCriteria.addColumnEqualTo("TITLE_NAME", title);
            searchCriteria.addAndCriteria(titleCriteria);
            addSearch = true;

        }
        if(StringUtils.isNotEmpty(titleInstanceType)) {
            Criteria titleInstanceTypeCriteria = new Criteria();
            titleInstanceTypeCriteria.addColumnEqualTo("MEDIUM", titleInstanceType);
            searchCriteria.addAndCriteria(titleInstanceTypeCriteria);
            addSearch = true;
        }


        if(issnList != null && issnList.size() > 0) {

            Criteria issnOnline = new Criteria();
            issnOnline.addColumnIn("TI_ISSN_ONLINE", issnList);

            Criteria issnPrint = new Criteria();
            issnPrint.addColumnIn("TI_ISSN_PRNT", issnList);

            Criteria issnL = new Criteria();
            issnL.addColumnIn("TI_ISSN_L", issnList);

            Criteria issnOrClause = new Criteria();

            issnOrClause.addOrCriteria(issnOnline);
            issnOrClause.addOrCriteria(issnPrint);
            issnOrClause.addOrCriteria(issnL);

            if(addSearch) {
                searchCriteria.addAndCriteria(issnOrClause);
            }
            else {
                searchCriteria = issnOrClause;
            }

        }

        List<OleGokbView> oleGokbViews = (List<OleGokbView>) getPersistenceBrokerTemplate().getCollectionByQuery(QueryFactory.newQuery(OleGokbView.class, searchCriteria));

        return oleGokbViews;
    }



}
