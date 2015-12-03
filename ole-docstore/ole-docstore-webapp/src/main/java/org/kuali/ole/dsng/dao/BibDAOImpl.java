package org.kuali.ole.dsng.dao;

import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 * Created by SheikS on 12/3/2015.
 */
@Repository("bibDAO")
@Scope("prototype")
public class BibDAOImpl implements BibDAO {

    private BusinessObjectService businessObjectService;

    public BusinessObjectService getBusinessObjectService() {
        if(null ==  businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public BibRecord retrieveBibById(String id) {
        BibRecord matchedBibRecord = getBusinessObjectService().findBySinglePrimaryKey(BibRecord.class, id);
        return matchedBibRecord;
    }
}
