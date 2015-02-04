package org.kuali.ole.docstore.repository;

import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.InstanceRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.pojo.OleException;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import javax.jcr.RepositoryException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: SG7940
 * Date: 10/11/12
 * Time: 12:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class RdbmsNodeCountManager {

    private static RdbmsNodeCountManager nodeCountManager;
    private Map<String, Long> nodeCountMap = null;
    private BusinessObjectService businessObjectService;

    private BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    private RdbmsNodeCountManager() {
    }

    public static RdbmsNodeCountManager getNodeCountManager() {
        if (null == nodeCountManager) {
            nodeCountManager = new RdbmsNodeCountManager();
        }
        return nodeCountManager;
    }

    public Map generateNodeCountMap() throws OleException, RepositoryException {
        Map<String, Long> bibMap = new TreeMap<String, Long>();

        Map countMap = new HashMap();
        countMap.put("uniqueIdPrefix" , "wbm");
        long bibSize =  getBusinessObjectService().countMatching(BibRecord.class,countMap);
        countMap.clear();
        countMap.put("uniqueIdPrefix" , "who");
        long holdingsSize = getBusinessObjectService().countMatching(HoldingsRecord.class,countMap);
        countMap.clear();
        countMap.put("uniqueIdPrefix" , "wio");
        long itemSize =  getBusinessObjectService().countMatching(ItemRecord.class,countMap);
        //instanceSize = instanceSize+holdingsSize+itemSize;

        StringBuffer temp = new StringBuffer();
        temp.append("work").append("/").append("").append("/").append("");
        String temp1 = new String();

        bibMap.put("work", bibSize + holdingsSize);
        bibMap.put("bibliographic", bibSize);
//        bibMap.put("marc", bibSize);


        bibMap.put("holdings", holdingsSize);
        bibMap.put("items", itemSize);


        return bibMap;
    }
}
