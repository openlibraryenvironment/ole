package org.kuali.ole.service;

import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.select.bo.OLEGOKbPlatform;
import org.kuali.ole.select.bo.OLEGOKbTIPP;
import org.kuali.ole.select.gokb.OleGokbPlatform;
import org.kuali.ole.select.gokb.OleGokbTipp;
import org.kuali.ole.select.gokb.OleGokbView;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.*;

/**
 * Created by sambasivam on 23/12/14.
 */
public class OLEGOKBSearchDaoOjb extends PlatformAwareDaoBaseOjb {

    public List<OleGokbTipp> packageSearch(String packageName, String platformName, List<String> platformProviders, String title, List<String> issnList, String titleInstanceType, List<String> platformStatusList, List<String> packageStatusList, List<String> tippStatusList) {

     List<OleGokbTipp> oleGokbTipps ;
    Criteria goKbSearchCriteria = new Criteria();
       if(StringUtils.isNotEmpty(packageName)){
           goKbSearchCriteria.addEqualTo("oleGokbPackage.packageName",packageName);
       }

        if(StringUtils.isNotEmpty(platformName)){
            goKbSearchCriteria.addEqualTo("oleGokbPlatform.platformName",packageName);
        }


        if(StringUtils.isNotEmpty(title)){
            goKbSearchCriteria.addEqualTo("oleGokbTitle.titleName",packageName);
        }


        if(StringUtils.isNotEmpty(titleInstanceType)){
            goKbSearchCriteria.addEqualTo("oleGokbTitle.medium",titleInstanceType);
        }

        if(platformProviders.size()>0 && platformProviders.size()>0){
            goKbSearchCriteria.addIn("oleGokbPlatform.oleGokbOrganization.organizationName", platformProviders);
        }

        if(platformStatusList.size()>0 && platformStatusList.size()>0){
            goKbSearchCriteria.addIn("oleGokbPlatform.status",platformStatusList);
        }

        if(packageStatusList.size()>0 && packageStatusList.size()>0){
            goKbSearchCriteria.addIn("oleGokbPackage.status",packageStatusList);

        }

        if(issnList.size()>0 && issnList.size()>0){
            goKbSearchCriteria.addIn("oleGokbTitle.issnOnline",issnList);
            goKbSearchCriteria.addIn("oleGokbTitle.issnPrint",issnList);
            goKbSearchCriteria.addIn("oleGokbTitle.issnL",issnList);
        }

        if(tippStatusList.size()>0 && tippStatusList.size()>0){
            goKbSearchCriteria.addIn("status",tippStatusList);
        } 

        QueryByCriteria query = QueryFactory.newQuery(OleGokbTipp.class, goKbSearchCriteria);
        Collection results=  getPersistenceBrokerTemplate().getCollectionByQuery(query);
        return (List<OleGokbTipp>)results;
    }



    public List<OleGokbTipp> getTippsByPlatform(Integer platformId) {
        Criteria goKbSearchCriteria = new Criteria();
            goKbSearchCriteria.addEqualTo("gokbPlatformId",platformId);
        QueryByCriteria query = QueryFactory.newQuery(OleGokbTipp.class, goKbSearchCriteria);
        Collection results=  getPersistenceBrokerTemplate().getCollectionByQuery(query);
    return ( List<OleGokbTipp>)results;
    }

}
