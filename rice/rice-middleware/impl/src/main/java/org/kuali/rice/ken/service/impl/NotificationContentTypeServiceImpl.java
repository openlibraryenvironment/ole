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
package org.kuali.rice.ken.service.impl;

import org.kuali.rice.core.framework.persistence.dao.GenericDao;
import org.kuali.rice.ken.bo.NotificationBo;
import org.kuali.rice.ken.bo.NotificationContentTypeBo;
import org.kuali.rice.ken.service.NotificationContentTypeService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//import org.apache.ojb.broker.query.QueryByCriteria;
//import org.apache.ojb.broker.query.QueryFactory;
//import org.kuali.rice.core.jpa.criteria.Criteria;


/**
 * NotificationContentTypeService implementation - uses the businessObjectDao to get at the underlying data in the stock DBMS.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NotificationContentTypeServiceImpl implements NotificationContentTypeService {
    private GenericDao businessObjectDao;

    /**
     * Constructs a NotificationContentTypeServiceImpl.java.
     * @param businessObjectDao
     */
    public NotificationContentTypeServiceImpl(GenericDao businessObjectDao) {
        this.businessObjectDao = businessObjectDao;
    }

    /**
     * @see org.kuali.rice.ken.service.NotificationContentTypeService#getNotificationContentType(java.lang.String)
     */
    //this is the one need to tweek on criteria
    public NotificationContentTypeBo getNotificationContentType(String name) {
//        Criteria c = new Criteria();
//        c.addEqualTo("name", name);
//        c.addEqualTo("current", true);	
//    	Criteria c = new Criteria(NotificationContentType.class.getName());
//    	c.eq("name", name);
//    	c.eq("current", true);
    	Map<String, Object> c = new HashMap<String, Object>();
    	c.put("name", name);
    	c.put("current", new Boolean(true));
    	
        Collection<NotificationContentTypeBo> coll = businessObjectDao.findMatching(NotificationContentTypeBo.class, c);
        if (coll.size() == 0) {
            return null;
        } else {
            return coll.iterator().next();
        }
    }

    protected int findHighestContentTypeVersion(String name) {
        // there's probably a better way...'report'? or direct SQL
        Map<String, Object> fields = new HashMap<String, Object>(2);
        fields.put("name", name);
        Collection<NotificationContentTypeBo> types = businessObjectDao.findMatchingOrderBy(NotificationContentTypeBo.class, fields, "version", false);
        if (types.size() > 0) {
            return types.iterator().next().getVersion();
        }
        return -1;
    }

    /**
     * @see org.kuali.rice.ken.service.NotificationContentTypeService#saveNotificationContentType(org.kuali.rice.ken.bo.NotificationContentTypeBo)
     */
    public void saveNotificationContentType(NotificationContentTypeBo contentType) {
        NotificationContentTypeBo previous = getNotificationContentType(contentType.getName());
        if (previous != null) {
            previous.setCurrent(false);
            businessObjectDao.save(previous);
        }
        int lastVersion = findHighestContentTypeVersion(contentType.getName());
        NotificationContentTypeBo next;
        if (contentType.getId() == null) {
            next = contentType; 
        } else {
            next = new NotificationContentTypeBo();
            next.setName(contentType.getName());
            next.setDescription(contentType.getDescription());
            next.setNamespace(contentType.getNamespace());
            next.setXsd(contentType.getXsd());
            next.setXsl(contentType.getXsl());
        }

        next.setVersion(lastVersion + 1);
        next.setCurrent(true);
        businessObjectDao.save(next);
        
        // update all the old references
        if (previous != null) {
            Collection<NotificationBo> ns = getNotificationsOfContentType(previous);
            for (NotificationBo n: ns) {
                n.setContentType(next);
                businessObjectDao.save(n);
            }
        }
    }

    protected Collection<NotificationBo> getNotificationsOfContentType(NotificationContentTypeBo ct) {
        Map<String, Object> fields = new HashMap<String, Object>(1);
        fields.put("contentType", ct.getId());
        return businessObjectDao.findMatching(NotificationBo.class, fields);
    }
    /**
     * @see org.kuali.rice.ken.service.NotificationContentTypeService#getAllCurrentContentTypes()
     */
    public Collection<NotificationContentTypeBo> getAllCurrentContentTypes() {
//        Criteria c = new Criteria();
//        c.addEqualTo("current", true);
////    	Criteria c = new Criteria(NotificationContentType.class.getName());
////    	c.eq("current", true);
    	
    	Map<String, Boolean> c = new HashMap<String, Boolean>();
    	c.put("current", new Boolean(true));
   
        return businessObjectDao.findMatching(NotificationContentTypeBo.class, c);
    }
    
    /**
     * @see org.kuali.rice.ken.service.NotificationContentTypeService#getAllContentTypes()
     */
    public Collection<NotificationContentTypeBo> getAllContentTypes() {
        return businessObjectDao.findAll(NotificationContentTypeBo.class);
    }
}
